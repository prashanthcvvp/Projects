package com.file.access;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

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
    private static Context context;
    private ProgressDialog pg;
    /**************************************************************************************************************/
    public ServerConnection(String path, Context context) {
        this.path = path;
        this.context = context;
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
        Log.d("p3", "Success");
        return true;
    }
    /**************************************************************************************************************/
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        pg.dismiss();
    }

    /**************************************************************************************************************/

    public void receiveFileFromServer() {
        //String url_str = "http://10.0.2.2:8080/PDFToJPEG/PDFServlet";
        String url_str = "http://sample-search1.rhcloud.com/p2j/PDFServlet";

        Log.d("p3", url_str);
        try {
            File zip_file = new File(Environment.getExternalStorageDirectory().toString() + "/sample.zip");
            BufferedOutputStream zip_file_output_stream = new BufferedOutputStream(new FileOutputStream(zip_file));
            int size = 0;

            URL url = new URL(url_str);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();

            File myfile = new File(this.path);
            BufferedInputStream b_fis = new BufferedInputStream(new FileInputStream(myfile));
            byte[] buffer = new byte[(int) myfile.length()];
            Log.d("p3", String.valueOf(buffer.length));
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
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("p3", e.getMessage());
            e.printStackTrace();
        }
    }
    /**************************************************************************************************************/

    public void unzipFolder() {
        File zip_file = new File(Environment.getExternalStorageDirectory().toString() + "/sample.zip");
        try {
            FileInputStream fis = new FileInputStream(zip_file);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry zip_file_entry;
            byte[] buffer = new byte[1024];
            while ((zip_file_entry = zis.getNextEntry()) != null) {
                String file_name = zip_file_entry.getName();
                File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + file_name);
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
