package com.example.screencapture_server;

import android.app.Activity;
import android.content.Intent;
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

public class Screencapture extends Activity {

	View view;
	TextView status;
	String tag = "screen";
	Button stop_service, start_service;
	DhcpInfo d_info;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screencapture);
		// view = (LinearLayout) findViewById(R.id.screen);
		view = getWindow().getDecorView().findViewById(android.R.id.content);

		status = (TextView) findViewById(R.id.textView1);
		stop_service = (Button) findViewById(R.id.button1);
		start_service = (Button) findViewById(R.id.button2);
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

		// String path = "/sdcard/ScreenCapture";
		//
		// File f1 = new File(path);
		// f1.mkdirs();
		// Process sh;
		// try {
		// sh = Runtime.getRuntime().exec("su", null, null);
		// OutputStream os = sh.getOutputStream();
		// byte[] bytes = new byte[102400];
		// os.write(("/system/bin/screencap -p "
		// + "/sdcard/ScreenCapture/img.png").getBytes("ASCII"));
		// // os.write(bytes);
		// // Log.d(tag, bytes.toString());
		// os.flush();
		// os.close();
		// sh.waitFor();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// view.setDrawingCacheEnabled(true);
		// // this is the important code :)
		// // Without it the view will have a dimension of 0,0 and the bitmap
		// will
		// // be null
		//
		// view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
		// MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		//
		// view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		//
		// view.buildDrawingCache(true);
		// Bitmap b = Bitmap.createBitmap(view.getDrawingCache());
		// view.setDrawingCacheEnabled(false); // clear drawing cache
		//
		// bmImage.setImageBitmap(b);

	}

	public String intToIP(int i) {
		return ((i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + ((i >> 24) & 0xFF));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_screencapture, menu);
		return true;
	}
}
