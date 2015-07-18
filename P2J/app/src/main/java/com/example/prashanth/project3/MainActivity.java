package com.example.prashanth.project3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.file.access.FileList;
import com.file.access.ServerConnection;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.ui.Rowelements;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ListView directory_list;
    private FileList file_list;
    private String path = "", path_old = "";
    //private ArrayAdapter<String> adapter;
    private TextView status;
    private ImageButton convert_btn;
    private Button up_btn;
    private Handler handler;
    private Runnable runnable;
    private String[] array_file_names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    /**
     * ******************************************************************************************************
     */

    public void initialize() {
        directory_list = (ListView) findViewById(R.id.listView);
        status = (TextView) findViewById(R.id.textView);
        convert_btn = (ImageButton) findViewById(R.id.convert_btn);
        up_btn = (Button) findViewById(R.id.up_btn);

        file_list = new FileList();
        path = Environment.getExternalStorageDirectory().toString();
        ArrayList<String> arrayList = file_list.getFiles(path);

        //adapter = new ArrayAdapter<String>();
        array_file_names = file_list.toArray(arrayList);
        Rowelements rows = new Rowelements(MainActivity.this,R.layout.custom_row,array_file_names);
        directory_list.setAdapter(rows);
        directory_list.setOnItemClickListener(this);

        convert_btn.setOnClickListener(this);
        up_btn.setOnClickListener(this);
        timerTask();

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Toolbar title_bar = (Toolbar)findViewById(R.id.toolbar);
        title_bar.setTitle(R.string.app_name);
        title_bar.setTitleTextColor(Color.rgb(185, 211, 238));
        title_bar.setSubtitle("Welcome");
        title_bar.setSubtitleTextColor(Color.rgb(185, 211, 238));


        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getRealSize(point);
        directory_list.setBottom(point.y-mAdView.getHeight());
    }

    /**
     * ******************************************************************************************************
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * ******************************************************************************************************
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ******************************************************************************************************
     */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int id_ = parent.getId();
        switch (id_) {
            case R.id.listView: {
                parent.getChildAt(position).setBackgroundColor(Color.argb(100,57,73,171));
                path_old = path;
                path = path + "/" + array_file_names[position];
                ArrayList<String> list = file_list.getFiles(path);

                if (list != null) {
                    array_file_names=file_list.toArray(list);
                    Rowelements rows = new Rowelements(MainActivity.this,R.layout.custom_row,array_file_names);
                    directory_list.setAdapter(rows);
                } else {
                    if (array_file_names[position].endsWith(".pdf")) {
                        status.setText(path);
                        path = path_old;
                    } else {
                        if (array_file_names[position].endsWith(".jpg")) {
                            Intent intent_image = new Intent();
                            intent_image.setAction(Intent.ACTION_VIEW);
                            intent_image.setDataAndType(Uri.fromFile(new File(path)), "image/*");
                            startActivity(intent_image);
                        }
                    }
                }
                break;
            }
        }
    }

    /**
     * ******************************************************************************************************
     */

    @Override
    public void onClick(View v) {
        int selected = v.getId();
        switch (selected) {
            case R.id.convert_btn:
                if (!status.getText().equals("")) {
                    handler.postDelayed(runnable, 0);
                    String[] str_array = status.getText().toString().split(".pdf");
                    Log.d("p3","str --- "+str_array[0]);
                    ServerConnection connection = new ServerConnection(path,str_array[0],MainActivity.this,handler,runnable);
                    connection.execute();
                    path = path_old;
                } else {
                    Toast.makeText(getApplicationContext(), "Select a File", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.up_btn:
                status.setText("");
                path = path_old;
                updatePath(path);
                break;
        }

    }

    /**
     * ******************************************************************************************************
     */
    public void updatePath(String path_temp) {
        ArrayList<String> list = file_list.getFiles(path_temp);
        if (list != null) {
            array_file_names=file_list.toArray(list);
            directory_list.setAdapter(new Rowelements(MainActivity.this,R.layout.custom_row,array_file_names));
        }
    }

    /**
     * ******************************************************************************************************
     */
    public void timerTask() {
        handler = new Handler();
         runnable= new Runnable() {
            @Override
            public void run() {
                updatePath(path);
                handler.postDelayed(this, 2000);
                Log.d("p3","loop");
            }
        };
    }

    /**
     * ******************************************************************************************************
     */

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(runnable);
        finish();
    }
}
