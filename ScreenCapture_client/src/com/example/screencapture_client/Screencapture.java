package com.example.screencapture_client;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.util.Timer;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug.IntToString;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Screencapture extends Activity {
	ImageView bmImage;
	View view;
	TextView status;
	String tag = "screen";
	Button stop_service, start_service, refresh;
	boolean start = false;
	boolean last = false;
	boolean start_thread = true;

	Bitmap image;
	File imgfile;
	int count = 0;

	Process sh;
	Timer timer;

	Thread screen_capture_thread;
	Thread upload;
	InputStream is;
	int bytesRead;
	int datatotal = 0;
	byte[] bytearray = null;
	boolean disp_image = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screencapture);
		start_thread = true;
		// view = (LinearLayout) findViewById(R.id.screen);
		view = getWindow().getDecorView().findViewById(android.R.id.content);
		bmImage = (ImageView) findViewById(R.id.image);
		status = (TextView) findViewById(R.id.textView1);
		stop_service = (Button) findViewById(R.id.button1);
		start_service = (Button) findViewById(R.id.button2);
		refresh = (Button) findViewById(R.id.button3);

		// startService(new Intent(Screencapture.this, Screenservice.class));

		stop_service.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				stopService(new Intent(Screencapture.this, Screenservice.class));
			}
		});
		start_service.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startService(new Intent(Screencapture.this, Screenservice.class));
			}
		});
		refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				display_image();
			}
		});

	}

	public void display_image() {
		String path_image = "/sdcard/sample.png";
		imgfile = new File(path_image);
		image = BitmapFactory.decodeFile(imgfile.getAbsolutePath());
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				bmImage.setImageBitmap(image);
			}
		});
		count++;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_screencapture, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		last = true;
	}

}
