package com.example.serverupdate_8k;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Serverupdate extends Activity {
	Button nextentry, finish;
	EditText name, age;
	RadioGroup gender;
	int checked;
	String genderchosen = "";
	JSONArray jarray;
	TextView status;
	static int startapp = 0;
	String line;
	boolean start = false;
	int count;
	Thread updatedb1;
	public static int entrycount;
	String tag = "Telemedi";

	private Dbhelper ourhelper;
	private Context ourcontext = null;
	private SQLiteDatabase ourdatabase;
	boolean worked = false;
	String values;
	String id;
	Bluetoothapp mBluetooth;

	boolean startbluetoothcon = false;
	Thread bluetoothstart = null;

	boolean last = false;

	Runtime r;
	Process p;
	String ip = "121.242.232.156";
	String pingresult = "";
	String pngcmd = "ping -c 4 " + ip;
	String lineping;
	int blue_count = 0;
	public byte SOP_EOP = 0x7E;
	public int reqheader = 5;
	public int ptnamelength = 15;
	byte[] dummy;
	byte[] buf1;
	Timer timer;
	int counttimer = 0;
	boolean checkstart = false;

	ConnectivityManager conn_mgr;
	NetworkInfo wifi_con;
	WifiManager wifi_mgr;
	private static final int REQUEST_ENABLE_BT = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		wifi_mgr = (WifiManager) Serverupdate.this
				.getSystemService(Serverupdate.this.WIFI_SERVICE);
		if (!(wifi_mgr.isWifiEnabled())) {
			wifi_mgr.setWifiEnabled(true);
		}
		conn_mgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		wifi_con = conn_mgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		Initialize();
		mBluetooth = (Bluetoothapp) getApplicationContext();

		gender.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				checked = gender.getCheckedRadioButtonId();
				switch (checked) {
				case R.id.radio0: {
					// Toast.makeText(Serverupdate.this, "Male",
					// Toast.LENGTH_SHORT).show();
					genderchosen = "Male";
					break;
				}

				case R.id.radio1: {
					// Toast.makeText(Serverupdate.this, "Female",
					// Toast.LENGTH_SHORT).show();
					genderchosen = "Female";
					break;
				}

				}
			}
		});

		nextentry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (name.getText().toString().equalsIgnoreCase("")
						|| age.getText().toString().equalsIgnoreCase("")
						|| genderchosen.equalsIgnoreCase("")) {
					entrycheck();
				} else {

					updatedb();
					finish.setEnabled(true);
				}

			}
		});

		finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (count == 0) {
					Log.d(tag, "Sent to server");
					updatedb1.start();
					start = true;
					count++;
				} else {
					start = true;
				}

			}
		});

	}

	void Initialize() {
		ourcontext = Serverupdate.this;
		setContentView(R.layout.activity_serverupdate);
		count = 0;
		updatedb1 = new Thread(new updatedatabase());
		jarray = new JSONArray();
		status = (TextView) findViewById(R.id.textView4);
		name = (EditText) findViewById(R.id.editText1);
		age = (EditText) findViewById(R.id.editText2);

		gender = (RadioGroup) findViewById(R.id.radioGroup1);
		gender.clearCheck();

		nextentry = (Button) findViewById(R.id.button1);

		finish = (Button) findViewById(R.id.button2);
		finish.setEnabled(false);

		ourhelper = new Dbhelper(ourcontext);
		ourdatabase = ourhelper.getWritableDatabase();

		bluetoothstart = new Thread(new bluetoothinitial());
		if (blue_count == 0) {

			bluetoothstart.start();
			startbluetoothcon = true;
			blue_count++;
		} else {
			startbluetoothcon = true;
		}
	}

	public void updatedb() {

		JSONObject jobj = new JSONObject();
		String patient_name = name.getText().toString();
		String patient_age = age.getText().toString();
		String patient_gender = genderchosen;
		createEntry(patient_name, patient_age, patient_gender);
		id = getdata();
		Log.d(tag, "id " + id);
		try {
			jobj.put("updatedb", false);
			jobj.put("id", id);
			jobj.put("name", patient_name);
			jobj.put("age", patient_age);
			jobj.put("gender", patient_gender);
			jarray.put(jobj);
			status.append("\n" + jarray.toString());

			// close();
			worked = true;
			name.setText("");
			age.setText("");
			gender.clearCheck();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			worked = false;
			e.printStackTrace();
		} finally {
			if (worked) {
				Toast.makeText(Serverupdate.this, "Successfully updated",
						Toast.LENGTH_SHORT).show();
				/*
				 * Dialog d = new Dialog(Serverupdate.this);
				 * d.setTitle("DBmanagement"); TextView tv = new
				 * TextView(Serverupdate.this); tv.setText("Updated");
				 * d.setContentView(tv); d.show();
				 */
			}
		}
	}

	public void entrycheck() {
		if (name.getText().toString().equalsIgnoreCase("")) {
			Toast.makeText(Serverupdate.this, "Please enter the name",
					Toast.LENGTH_SHORT).show();

		}

		if (age.getText().toString().equalsIgnoreCase("")) {
			Toast.makeText(Serverupdate.this, "Please enter the age",
					Toast.LENGTH_SHORT).show();
		}
		if (genderchosen.equals("")) {
			Toast.makeText(Serverupdate.this, "Please choose the gender",
					Toast.LENGTH_SHORT).show();

		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Log.d(tag, String.valueOf(mBluetooth.bluetoohcheck));
		if (mBluetooth.bluetoohcheck) {
			mBluetooth.closesock();
		}
		start = false;
		last = true;
		close();

		Toast.makeText(Serverupdate.this, "On Backpress", Toast.LENGTH_SHORT)
				.show();
		this.finish();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		startapp++;
		super.onPause();
		start = false;
		last = true;

		Toast.makeText(Serverupdate.this, "On pause", Toast.LENGTH_SHORT)
				.show();
		this.finish();
	}

	protected long createEntry(String ptname, String age, String gender) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(ourhelper.KEY_NAME, ptname);
		cv.put(ourhelper.KEY_AGE, age);
		cv.put(ourhelper.KEY_GENDER, gender);
		return ourdatabase.insert(ourhelper.DATABASE_TABLE, null, cv);
	}

	public void close() {
		ourhelper.close();
		ourdatabase.close();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_serverupdate, menu);
		return true;
	}

	public class updatedatabase implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {

				if (start) {
					Log.d(tag, "Sent to server");
					start = false;
					update();
					Intent intent = new Intent(Serverupdate.this,
							Sqliteview.class);

					startActivity(intent);

				}
				if (last) {
					break;
				}

			}

		}

	}

	public void update() {
		try {
			URL url = new URL(
					"http://121.242.232.156:8080/telemed/telemed/dbinsert");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			BasicNameValuePair bvnp = new BasicNameValuePair("name1",
					jarray.toString());

			DataOutputStream wr = new DataOutputStream(con.getOutputStream());

			wr.write(bvnp.toString().getBytes());
			wr.flush();
			wr.close();
			con.connect();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));

			if ((line = reader.readLine()) != null) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(Serverupdate.this,
								"Patient list sent to server",
								Toast.LENGTH_SHORT).show();
					}

				});
				Intent intent = new Intent(Serverupdate.this, Sqliteview.class);

				startActivity(intent);

			} else {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(Serverupdate.this,
								"Could not upload the list to server",
								Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(Serverupdate.this,
								Sqliteview.class);

						startActivity(intent);
					}
				});
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String getdata() {
		// TODO Auto-generated method stub
		String[] columns = new String[] { ourhelper.KEY_ROWID,
				ourhelper.KEY_NAME, ourhelper.KEY_AGE, ourhelper.KEY_GENDER };
		Cursor c = ourdatabase.query(ourhelper.DATABASE_TABLE, columns, null,
				null, null, null, null);
		String result = "";

		int irow = c.getColumnIndex(ourhelper.KEY_ROWID);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			values = c.getString(irow);

		}
		return values;

	}

	public class bluetoothinitial implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				if (startbluetoothcon) {
					startbluetoothcon = false;
					// Intent enableBtIntent = new Intent(
					// BluetoothAdapter.ACTION_REQUEST_ENABLE);
					// startActivityForResult(enableBtIntent,
					// REQUEST_ENABLE_BT);
					bluetoothinitiate();
					mBluetooth.bluetoohcheck = true;
					pingcmd();
					// kitcheck(5, "test");
				} else {
					continue;
				}
				if (last) {
					break;
				}

			}
		}

		private void bluetoothinitiate() {
			// TODO Auto-generated method stub
			if (startapp != 0) {
				mBluetooth.onCreate();
				Log.d(tag, "mBluetooth created");
			}

		}

		private void pingcmd() {
			if (wifi_con.isConnected()) {
				r = Runtime.getRuntime();
				try {
					p = r.exec(pngcmd);
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(p.getInputStream()));
					while ((lineping = reader.readLine()) != null) {
						pingresult = pingresult + lineping + "\n";
						Log.d(tag, pingresult);
					}
					String[] serverresult = pingresult.split(", ");
					// Log.d(tag, "Array length " + serverresult[0]);
					String[] serverping = serverresult[1].split(" ");
					Log.d(tag, "Ping Result = " + serverping[0]);
					if (serverping[0].equals("0")) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								status.append("\nServer is Unreachable network problem");

								Toast.makeText(
										Serverupdate.this,
										"Server is Unreachable network problem",
										Toast.LENGTH_SHORT).show();
							}
						});
					} else {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								status.append("\nPing successfull");
								Toast.makeText(Serverupdate.this,
										"Ping successfull", Toast.LENGTH_SHORT)
										.show();
							}
						});

					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						startActivityForResult(
								new Intent(
										android.provider.Settings.ACTION_WIFI_SETTINGS),
								0);

						status.setText("Not Connected to wifi Network please restart the application");
						finish.setEnabled(false);
						Toast.makeText(
								Serverupdate.this,
								"Could not connect to a Wifi network please restart the application",
								Toast.LENGTH_SHORT).show();

					}
				});
			}
		}

		private void kitcheck(int test, String ptname) {
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
			while (true) {
				checkstart = false;
				try {
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

						int extra = 0;

						for (int i = start; i < readAmount; i++) {

							if (buf1[i] == 0x7E) {
								break;
							}
						}

					} else {
						timer = new Timer();
						timer.schedule(new TimerTask() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									if (mBluetooth.is.available() > 0) {
										timer.cancel();
										Log.d(tag, "i/p available");
									}
									counttimer++;
									if (counttimer == 5) {
										Log.d(tag,
												"Timer canceled no data available");
										timer.cancel();
										checkstart = true;

									}
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}, 0, 1000);
						while (true) {
							if (checkstart) {
								break;
							}
						}

					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (checkstart) {
					Log.d(tag, "Outside while loop");
					break;
				}

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

			return stuffed;
		}

	}

}
