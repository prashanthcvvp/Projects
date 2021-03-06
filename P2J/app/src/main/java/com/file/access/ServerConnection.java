package com.file.access;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.prashanth.project3.R;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by prashanth on 6/27/15.
 */
public class ServerConnection extends AsyncTask<Void, Void, Boolean> {
    private static String path = "",parent;
    private static String folder_path="";
    private static Context context;
    private ProgressDialog pg;
    private Handler handler;
    private Runnable runnable;
    private boolean is_complete =true;

    /**************************************************************************************************************/
    public ServerConnection(String path,String folder_path, Context context,Handler handler,Runnable runnable) {
        this.path = path;
        this.folder_path=folder_path;
        this.context = context;
        this.handler=handler;
        this.runnable=runnable;
        Log.d("p3",this.folder_path);
    }
    /**************************************************************************************************************/
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pg = new ProgressDialog(this.context);
        pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pg.setTitle("P2J");
        pg.setMessage("Converting...");
        pg.setIcon(R.drawable.p2j);
        pg.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pg.dismiss();
                cancel(true);
            }
        });
        pg.show();
    }

    /**************************************************************************************************************/
    @Override
    protected Boolean doInBackground(Void... params) {
        receiveFileFromServer();
        unzipFolder();
        return true;
    }
    /**************************************************************************************************************/
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(!is_complete){
            is_complete=true;
            Toast.makeText(this.context.getApplicationContext(),"Could not connect to the server",Toast.LENGTH_LONG).show();
        }
        this.handler.removeCallbacks(this.runnable);
        pg.dismiss();

    }

    /**************************************************************************************************************/

    public void receiveFileFromServer() {
        //String url_str = "http://10.0.2.2:8080/PDFToJPEG/PDFServlet";
        String url_str = "http://sample-search1.rhcloud.com/p2j/PDFServlet";

        Log.d("p3", url_str);
        try {

            URL url = new URL(url_str);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            // output folder /////////////////////////////////////////////////////////
            File parent_folder = new File(folder_path);
            if(!parent_folder.isDirectory()) {
                parent_folder.mkdirs();
            }
            File zip_file = new File(parent_folder,"sample.zip");
            BufferedOutputStream zip_file_output_stream = new BufferedOutputStream(new FileOutputStream(zip_file));
            int size = 0;
            //////////////////////////////////////////////////////////////////////////

            File myfile = new File(this.path);

            BufferedInputStream b_fis = new BufferedInputStream(new FileInputStream(myfile));
            byte[] buffer = new byte[(int) myfile.length()];
            b_fis.read(buffer, 0, buffer.length);

            BufferedOutputStream bos = new BufferedOutputStream(conn.getOutputStream());
            bos.write(buffer, 0, buffer.length);
            bos.flush();

            InputStream b_is = conn.getInputStream();
            int n_read = 0;
            ByteArrayOutputStream baos_zip = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            n_read = 0;
            while ((n_read = b_is.read(buf)) != -1) {
                baos_zip.write(buf, 0, n_read);
                size = size + n_read;
            }
            zip_file_output_stream.write(baos_zip.toByteArray());

            bos.close();
            b_is.close();
            zip_file_output_stream.close();
            conn.disconnect();
            is_complete=true;
        } catch (IOException e) {
            is_complete=false;
            e.printStackTrace();
        }
    }
    /**************************************************************************************************************/

    public void unzipFolder() {
        File zip_file = new File(folder_path + "/sample.zip");
        try {
            FileInputStream fis = new FileInputStream(zip_file);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry zip_file_entry;
            byte[] buffer = new byte[1024];
            while ((zip_file_entry = zis.getNextEntry()) != null) {
                String file_name = zip_file_entry.getName();
                File file = new File(folder_path + "/" + file_name);
                FileOutputStream fos = new FileOutputStream(file);
                int n_read = 0;
                while ((n_read = zis.read(buffer)) != -1) {
                    fos.write(buffer, 0, n_read);
                }
                fos.close();
                zis.closeEntry();
            }
            zis.close();
            zip_file.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**************************************************************************************************************/
}
