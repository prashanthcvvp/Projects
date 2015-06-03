package com.example.screencapture_server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class Screenservice extends Service {
	Process sh;
	Timer timer;
	int count = 0;
	boolean last = false;
	Thread screen_capture_thread;
	Thread upload;
	InputStream is;
	int bytesRead;
	int datatotal = 0;
	byte[] bytearray = null;
	String tag = "screen";
	ServerSocket server;
	OutputStream os1;
	Socket client_accept;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		String path = "/sdcard/ScreenCapture";

		File f1 = new File(path);
		f1.mkdirs();
		screen_capture_thread = new Thread(new Thread1());
		screen_capture_thread.start();
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

	public void screen_capture() {

		try {
			sh = Runtime.getRuntime().exec("su", null, null);
			OutputStream os = sh.getOutputStream();
			// byte[] bytes = new byte[102400];
			os.write(("/system/bin/screencap -p "
					+ "/sdcard/ScreenCapture/img.png").getBytes("ASCII"));
			// os.write(bytes);
			// Log.d(tag, bytes.toString());
			os.flush();
			os.close();
			sh.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
			Socket_open();
			String path_file = "/sdcard/ScreenCapture/img.png";
			File file_ip = new File(path_file);
			int size = (int) file_ip.length();
			Log.d(tag, "Size of file " + String.valueOf(size));
			if (size > 0) {
				BufferedInputStream bis = new BufferedInputStream(
						new FileInputStream(file_ip));
				byte[] file_buffer = new byte[(int) file_ip.length()];
				bis.read(file_buffer, 0, file_buffer.length);
				Log.d(tag, "Finished reading the file");

				os1 = client_accept.getOutputStream();
				os1.write(file_buffer, 0, file_buffer.length);

			}

			// while (true) {
			// if (is.available() > 0) {
			// byte[] bytearray = new byte[is.available() + 1];
			// is.read(bytearray, 0, bytearray.length);
			// baos.write(bytearray, 0, bytearray.length);
			// }
			// else{
			//
			// }
			//
			// }
			Socket_close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public class Thread1 implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				count++;
				screen_capture();
				// try {
				// Thread.sleep(30);
				//
				// } catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				if (last) {
					break;
				}
			}
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
