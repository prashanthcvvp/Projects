package com.example.serverupdate_8k;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.URL;
import java.util.Timer;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Stethtest extends Activity implements Runnable {
	Button steth;
	EditText ptnameedit;
	Thread Bluetooththread;
	static int start = 0;

	/*
	 * BluetoothSocket btSocket; InputStream is; OutputStream os;
	 * 
	 * 
	 * 
	 * 
	 * private static final UUID MY_UUID =
	 * UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); static String
	 * address = "00:E0:00:DE:D8:77";
	 */
	boolean nodata = false;
	static boolean startread = false;
	static boolean checkstart = true;
	String tag = "Telemedi";
	Socket sock;
	OutputStream os1;
	byte[] mybytearray = null;
	byte[] buf1 = null;
	public byte SOP_EOP = 0x7E;
	public int reqheader = 5;
	public int ptnamelength = 15;
	String patient = null;
	boolean startboolean = false;
	boolean startrun = false;
	boolean last = false;
	boolean startexecution = false;
	boolean waitfordata = false;
	boolean endofdata = false;
	boolean datapresent = false;
	int countstart = 0;

	Thread uploadthread;
	boolean startupload = false;
	int uploadcount = 0;
	int time = 20;
	int samples = (8000 * time * 2) + 45;
	int timercount = 0;
	Handler mHandler;
	// Creating object for device and adaptor
	BluetoothDevice device;
	BluetoothAdapter btAdapter;
	Bluetoothapp mBluetooth;
	byte[] dummy;
	HttpURLConnection connection = null;
	DataOutputStream outputStream = null;

	// Required for http connection establishment
	String urlServer = "http://121.242.232.156/upload1.php";
	String lineEnd = "\r\n";
	String twoHyphens = "--";
	String boundary = "*****";
	URL url = null;
	Timer timer;
	static String ptname1;
	Intent intent;
	static String rowid;
	int count = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stethtest);

		startread = false;
		checkstart = true;

		steth = (Button) findViewById(R.id.button1);
		ptnameedit = (EditText) findViewById(R.id.editText1);

		ptnameedit.setEnabled(false);

		intent = getIntent();
		// ptname1 = intent.getExtras().getString("ptname_db").toString();
		ptname1 = intent.getStringExtra("ptname_db").toString();
		ptnameedit.setText(ptname1);
		rowid = intent.getStringExtra("id").toString();
		mBluetooth = (Bluetoothapp) getApplicationContext();
		/*
		 * 
		 * if (start != 0) { mBluetooth.onCreate(); Log.d(tag,
		 * "mBluetooth created"); }
		 */

		Bluetooththread = new Thread(this);
		uploadthread = new Thread(new upload1());
		// Bluetoothinitial();

		steth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// stop.setEnabled(true);
				count++;
				waitfordata = true;
				steth.setEnabled(false);
				startboolean = true;
				patient = ptnameedit.getText().toString();
				Log.d(tag, patient);
				if (countstart == 0) {
					Log.d(tag, "Count " + String.valueOf(countstart)
							+ "First time Starting the thread");
					startrun = true;
					startboolean = true;
					Bluetooththread.start();
					countstart++;

				} else {
					Log.d(tag, "Count " + String.valueOf(countstart)
							+ "First time Starting the thread");
					startrun = true;
					startboolean = true;
					countstart++;

				}
				countstart++;

			}
		});

		/*
		 * stop.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub // Bluetooththread.stop(); // Bluetooththread.run();
		 * 
		 * startboolean = false; startrun = false; // upload.setEnabled(true);
		 * steth.setEnabled(true); stop.setEnabled(false);
		 * 
		 * // stethtest(6, patient); } });
		 */

		/*
		 * upload.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * steth.setEnabled(false); // upload.setEnabled(false); patient =
		 * ptnameedit.getText().toString(); if (uploadcount == 0) {
		 * uploadthread.start(); startupload = true; } else { startupload =
		 * true; } uploadcount++; } });
		 */

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Toast.makeText(getBaseContext(), "On Stop", Toast.LENGTH_SHORT).show();

		super.onBackPressed();
		last = true;

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// mBluetooth.closesock();
		last = true;
		startread = false;
		checkstart = true;
		// timer.cancel();
		start++;
		finish();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// mBluetooth.closesock();
		last = true;
		startread = false;
		checkstart = true;

		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_serverupdate, menu);
		return true;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		while (true) {

			if (startrun) {
				startrun = false;
				Log.d(tag, "Inside the thread");
				stethtest(0, "steth");

			}

			else {
				// Log.d(tag, "Outside run commd");
				continue;
			}
			if (last) {
				break;
			}

		}

	}

	/*
	 * void Bluetoothinitial(){ btAdapter =
	 * BluetoothAdapter.getDefaultAdapter(); device =
	 * btAdapter.getRemoteDevice(address);
	 * 
	 * 
	 * 
	 * try { btSocket =
	 * device.createInsecureRfcommSocketToServiceRecord(MY_UUID); Log.d(tag,
	 * "BtSocket created"); } catch (IOException e) { // TODO Auto-generated
	 * catch block Log.d(tag, "BtSocket Creation failed"); e.printStackTrace();
	 * }
	 * 
	 * try {
	 * 
	 * btAdapter.cancelDiscovery(); Log.d(tag, "Cancel Discovery success"); }
	 * catch (Exception e) {
	 * 
	 * Log.d(tag, "Failed to cancel Discovery"); }
	 * 
	 * 
	 * try { btSocket.connect(); Log.d(tag, "Socket connection success"); }
	 * catch (Exception e) {
	 * 
	 * try { btSocket.close(); Log.d(tag, "Close socket failed"); } catch
	 * (Exception e1) {
	 * 
	 * Log.d(tag, "Couldn't close socket"); } Log.d(tag,
	 * "Couldn't connect socket"); }
	 * 
	 * try { is = btSocket.getInputStream(); os = btSocket.getOutputStream();
	 * 
	 * Log.d(tag, "input and output Streams Created"); } catch (IOException e) {
	 * // TODO Auto-generated catch block Log.d(tag,
	 * "input and output Streams failed"); } }
	 */

	public void stethtest(int test, String ptname) {
		int length = 0;

		String path = "/sdcard/Stethtest/" + patient + " " + count;

		File f1 = new File(path);
		f1.mkdirs();
		startrun = false;
		checkstart = true;
		startread = false;
		while (true) {
			try {

				if (mBluetooth.is.available() > 0) {

					dummy = new byte[mBluetooth.is.available() + 1];

					mBluetooth.is.read(dummy);
					Log.d(tag, "Dummy byte array read = " + dummy.length);
					dummy = null;
				} else {
					break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			byte bufbeforestuffing[] = new byte[25];

			byte bufafterstuffing[] = new byte[50];
			bufbeforestuffing = FormRequestPacket(test, 2, ptname);
			Log.d(tag, "FRP success");
			bufafterstuffing = stuffing(bufbeforestuffing);
			Log.d(tag, "Stuffing success");

			mBluetooth.os.write(bufafterstuffing);
			mBluetooth.os.flush();
			Log.d(tag, "Data Sent");
		} catch (Exception e) {
			// TODO: handle exception
			Log.d(tag, e.getMessage());

		}

		try {
			boolean flag = false;
			boolean end1 = false;
			String pathtemp = "steth.wav";
			File f = new File(f1, pathtemp);
			File f2 = new File(f1, "test1.wav");
			Log.d(tag, path);
			FileOutputStream fOut = null;
			fOut = new FileOutputStream(f);

			try {
				/*
				 * sock = new Socket("121.242.232.156", 6000);
				 * 
				 * Log.d("Server", "Socket opened");
				 * 
				 * os1 = sock.getOutputStream(); Log.d("Server",
				 * "Socket opened");
				 */

			} catch (Exception e) {
				connection.disconnect();
			}
			// time = 120;
			// int overallsize = 8000*time+46;
			// int overallsize = 20000 * 8 + 46;
			// int overallsize = 960000+47; // 10% overhead
			// Log.d("Overall Size", String.valueOf(overallsize));

			int length1 = 0;
			while (length1 < samples) {

				if (mBluetooth.is.available() > 0) {
					Log.d(tag, "inputdata Available");
					int start = 0;
					ByteArrayOutputStream baos = null;
					baos = new ByteArrayOutputStream();
					ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
					Log.d(tag, "Receiving input data");
					// mybytearray = new byte[is.available()];
					// int readAmount = inStream.read(buf1,0,4096);
					buf1 = new byte[mBluetooth.is.available() + 1];
					int readAmount = mBluetooth.is.read(buf1);

					if (length1 == 0) {
						start = 1;
						// start = 0;

					}
					int extra = 0;

					for (int i = start; i < readAmount; i++) {

						if (flag == true) {
							Log.d(tag, "Reading the data");
							if ((buf1[i] == ~0x0A) || (buf1[i] == ~0x7E)) {
								baos1.write(buf1[i]);
								baos.write(~buf1[i]);
								// extra++;
								flag = false;
							} else {
								Log.d(tag, "Last 0x7E");
								baos1.write(0x7E);
								// baos.write(0x7E);
								flag = false;
								// end1 = true;
								// break;
							}
						} else {
							if (buf1[i] == 0x7E) {
								// baos1.write(buf1[i]);
								// baos.write(buf1[i]);
								flag = true;
							} else {
								baos1.write(buf1[i]);
								baos.write(buf1[i]);
							}
						}

					}
					// length = length + readAmount - extra;
					Log.d(tag, "Extra = " + String.valueOf(extra));
					length1 = length1 + (readAmount - extra);
					Log.d(tag, "Length + Readamt " + String.valueOf(length1));

					fOut.write(baos.toByteArray());
					baos.flush();
					baos.reset();
					// os1.write(baos1.toByteArray(), 0,
					// baos1.toByteArray().length);
					// os1.flush();

				}

			}
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(Stethtest.this, "File save locally ",
							Toast.LENGTH_SHORT).show();
				}
			});
			uploadData();

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					steth.setEnabled(true);

				}

			});
			checkstart = true;
			startread = false;
			Log.d(tag, "Check start = " + checkstart + " Start read = "
					+ startread);
			// Log.d(tag, "Outside the while loop");

		} catch (IOException e) {
			Log.d(tag, "Error");
			// mBluetooth.closesock();
			// mBluetooth.onCreate();
		} catch (Exception e1) {
			// TODO: handle exception
		}
		// steth.setEnabled(true);

	}

	public void uploadData() {

		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;

		String path = "/sdcard/Stethtest/" + patient + " " + count
				+ "/steth.wav";
		Log.d(tag, path);
		File file = new File(path);
		int size = (int) file.length();
		byte[] bytesfile = new byte[size];
		Log.d(tag, String.valueOf(size));
		try {
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			bis.read(bytesfile, 0, bytesfile.length);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			url = new URL(urlServer);

		} catch (MalformedURLException e) {

			e.printStackTrace();
		}
		try {

			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			connection.setRequestMethod("POST");
			Log.d(tag, "Connected");

		} catch (ProtocolException e) {

			e.printStackTrace();
			connection.disconnect();

		} catch (IOException e) {

			connection.disconnect();
			e.printStackTrace();
		}

		try {

			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			String pathToOurFile = "Piano.wav";
			String patientname = rowid + patient;
			outputStream
					.writeBytes("Content-Disposition: form-data; name=\"upload_field\";filename=\""
							+ patientname + ".wav" + "\"" + lineEnd);
			String fileformat = "audio/x-wav";
			outputStream.writeBytes("Content-Type: " + fileformat + lineEnd
					+ lineEnd);

			// ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// baos.write(bytesfile);

			outputStream.write(bytesfile, 0, bytesfile.length);

			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens
					+ lineEnd);

			DataInputStream dis = new DataInputStream(
					connection.getInputStream());
			byte[] result = new byte[7];
			dis.read(result);
			String resultString = new String(result);
			String re = resultString.toString();
			Log.d("Result message", re);

			dis.close();
			outputStream.flush();
			outputStream.close();
			connection.disconnect();

			// startupload = false;

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(Stethtest.this, "uploaded",
							Toast.LENGTH_SHORT).show();

				}
			});

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			connection.disconnect();
		}

	}

	public byte[] FormRequestPacket(int cmd, int start_end, String name) {
		int index = 0;
		Log.d(tag, "1");
		byte command[] = { 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37 };
		int length = ptnamelength + reqheader;
		byte sendtoBTbuf[] = new byte[length + 1];
		sendtoBTbuf[index++] = SOP_EOP;
		Log.d(tag, "11");
		sendtoBTbuf[index++] = (byte) length;
		sendtoBTbuf[index++] = command[cmd];
		for (int pname = 0; pname < name.length(); pname++) {
			sendtoBTbuf[index++] = (byte) name.charAt(pname);
		}
		Log.d(tag, "111");
		if (start_end == 1) {
			sendtoBTbuf[length - 2] = 0x01;
		}

		else {
			sendtoBTbuf[length - 2] = 0x02;
		}
		Log.d(tag, "1111");

		sendtoBTbuf[length - 1] = SOP_EOP;
		Log.d(tag, "1111");
		Log.d(tag, "RETURNED SUCCESS");
		return sendtoBTbuf;
	}

	public byte[] stuffing(byte[] tobeStuffed) {

		int j = 1;
		int length = tobeStuffed.length;
		int pktlength = 0;
		for (int pos = 1; pos < length; pos++) {
			if (tobeStuffed[pos] == 0x7E)
				pktlength = pos;
		}
		byte stuffed[] = new byte[50];
		stuffed[0] = tobeStuffed[0];
		for (int k = 1; k < pktlength; k++) {
			if (tobeStuffed[k] == 0x7E) {
				stuffed[j++] = (byte) 0x7E;
				stuffed[j++] = (byte) ~0x7E;
			} else if (tobeStuffed[k] == 0x0A) {
				stuffed[j++] = (byte) 0x7E;
				stuffed[j++] = (byte) ~0x0A;
			} else {
				stuffed[j++] = tobeStuffed[k];
			}
		}
		stuffed[j++] = tobeStuffed[pktlength];
		String check = new String(stuffed);
		Log.d(tag, "Stuffed data " + check);
		return stuffed;
	}

	public class upload1 implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				if (startupload) {
					uploadData();
				} else {
					continue;
				}

				if (last) {
					break;
				}

			}

		}

	}

}
