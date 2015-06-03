package com.example.screencapture_client;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;

import android.app.Service;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

public class Screenservice extends Service {
	Process sh;
	Timer timer;
	int count = 0;
	boolean last = false;

	Thread upload;
	InputStream is;
	int bytesRead;
	int datatotal = 0;
	byte[] bytearray = null;
	String tag = "screen";
	ServerSocket server;
	OutputStream os1;
	Socket client_accept;

	DhcpInfo d_info;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		String path = "/sdcard/ScreenCapture";

		File f1 = new File(path);
		f1.mkdirs();

		upload = new Thread(new Thread2());
		upload.start();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		last = true;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void Socket_open() {
		try {
			server = new ServerSocket(5000);
			Log.d(tag, "WAiting for client");
			client_accept = server.accept();
			Log.d(tag, "Connected");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void Socket_connect() {
		try {

			WifiManager wifi_manager = (WifiManager) getSystemService(Screenservice.this.WIFI_SERVICE);
			WifiInfo wifi_info = wifi_manager.getConnectionInfo();
			// Log.e(tag, wifi_info.getIpAddress() + "");
			Log.e(tag, intToIP(wifi_info.getIpAddress()) + "");

			d_info = wifi_manager.getDhcpInfo();
			// Log.e(tag, intToIP(d_info.gateway).toString());
			String ipaddr = intToIP(d_info.gateway).toString();

			Socket client = new Socket(ipaddr, 5000);
			Log.d(tag, "Connected");
			is = client.getInputStream();
			if (is.available() > 0) {
				String path = "/sdcard/sample.png";

				File f_client = new File(path);
				// f_client.mkdirs();
				FileOutputStream fos = new FileOutputStream(f_client);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bytearray = new byte[30000];

				while ((bytesRead = is.read(bytearray, 0, bytearray.length)) != -1) {

					Log.d(tag, "Receiving data " + bytesRead);
					baos.write(bytearray, 0, bytesRead);
					datatotal = bytesRead + datatotal;
					Log.d(tag, "Receiving data " + datatotal);

				}
				fos.write(baos.toByteArray());
				Log.d(tag, "Completed");
				baos.close();
				fos.close();
			}

			is.close();
			client.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String intToIP(int i) {
		return ((i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + ((i >> 24) & 0xFF));
	}

	public void Socket_close() {

		try {
			os1.close();
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class Thread2 implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.d(tag, "Thread started");
			while (true) {
				Socket_connect();

				if (last) {
					break;
				}
			}
		}

	}
}
