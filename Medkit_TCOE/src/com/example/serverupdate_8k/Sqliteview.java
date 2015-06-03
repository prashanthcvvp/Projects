package com.example.serverupdate_8k;

import com.example.serverupdate_8k.Dbhelper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Sqliteview extends Activity {

	Dbhelper dbview;
	SQLiteDatabase database;
	ListView lv;
	ArrayAdapter<String> adapter;
	String values;
	String[] entries;
	String tag = "Patients";
	Bluetoothapp mBluetooth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.sqliteview);
		mBluetooth = (Bluetoothapp) getApplicationContext();
		lv = (ListView) findViewById(R.id.listview);

		adapter = new ArrayAdapter<String>(Sqliteview.this,
				android.R.layout.simple_list_item_1, android.R.id.text1);

		dbview = new Dbhelper(Sqliteview.this);
		database = dbview.getWritableDatabase();
		Log.d(tag, "database created");

		getdata();
		// close();
		// viewdata.setText(data);
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				String selected = (String) (lv.getItemAtPosition(position));
				String[] rowid = selected.split(" ");
				Toast.makeText(Sqliteview.this, rowid[0] + " " + rowid[1],
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(Sqliteview.this, Stethtest.class);
				intent.putExtra("ptname_db", rowid[1]);
				intent.putExtra("id", rowid[0]);
				startActivity(intent);
			}
		});

	}

	private void getdata() {
		// TODO Auto-generated method stub
		String[] columns = new String[] { dbview.KEY_ROWID, dbview.KEY_NAME,
				dbview.KEY_AGE, dbview.KEY_GENDER };
		Cursor c = database.query(dbview.DATABASE_TABLE, columns, null, null,
				null, null, null);
		String result = "";

		int irow = c.getColumnIndex(dbview.KEY_ROWID);
		int iname = c.getColumnIndex(dbview.KEY_NAME);
		int iage = c.getColumnIndex(dbview.KEY_AGE);
		int igender = c.getColumnIndex(dbview.KEY_GENDER);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			values = c.getString(irow) + " " + c.getString(iname) + "\n";
			// values = c.getString(iname);

			adapter.add(values);

		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		database.delete(dbview.DATABASE_TABLE, null, null);
		close();
		mBluetooth.closesock();
		mBluetooth.bluetoohcheck = false;
		Intent intent = new Intent(Sqliteview.this, Serverupdate.class);
		startActivity(intent);

		this.finish();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	public void close() {
		dbview.close();
		database.close();

	}

}
