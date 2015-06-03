package com.example.serverupdate_8k;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Bluetoothapp extends Application {
	boolean bluetoohcheck = true;
	BluetoothSocket btSocket;
	InputStream is;
	OutputStream os;
	String tag = "Telemedi";
	Socket sock;
	OutputStream os1;
	byte[] mybytearray = null;
	byte[] buf1 = null;
	

	public byte SOP_EOP = 0x7E;
	public int reqheader = 5;
	public int ptnamelength = 15;
	private static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	static String address = "00:12:F3:11:A3:70";

	private static final int REQUEST_ENABLE_BT = 1;

	// Creating object for device and adaptor
	BluetoothDevice device;
	BluetoothAdapter btAdapter;

	private static Bluetoothapp singleton;

	public Bluetoothapp getInstance() {

		return singleton;

	}

	@Override
	public void onCreate() {

		super.onCreate();

		singleton = this;
		Bluetoothinitial();
	}

	void Bluetoothinitial() {
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		device = btAdapter.getRemoteDevice(address);

		try {
			btSocket = device
					.createInsecureRfcommSocketToServiceRecord(MY_UUID);
			Log.d(tag, "BtSocket created");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d(tag, "BtSocket Creation failed");
			e.printStackTrace();
		}

		try {

			btAdapter.cancelDiscovery();
			Log.d(tag, "Cancel Discovery success");
		} catch (Exception e) {

			Log.d(tag, "Failed to cancel Discovery");
		}

		try {
			btSocket.connect();
			Log.d(tag, "Socket connection success");
		} catch (Exception e) {

			try {
				btSocket.close();
				Log.d(tag, "Close socket failed");
			} catch (Exception e1) {

				Log.d(tag, "Couldn't close socket");
			}
			Log.d(tag, "Couldn't connect socket");
		}

		try {
			is = btSocket.getInputStream();
			os = btSocket.getOutputStream();

			Log.d(tag, "input and output Streams Created");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d(tag, "input and output Streams failed");
		}
	}

	private void startActivityForResult(Intent intent, int rEQUEST_ENABLE_BT2) {
		// TODO Auto-generated method stub

	}

	void closesock() {
		if (!(btAdapter == null)) {
			try {
				is.close();
				Log.d(tag, "I/p stream closed");
				os.close();
				Log.d(tag, "O/p Stream closed");
				btSocket.close();

				Log.d(tag, "Socket closed");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
