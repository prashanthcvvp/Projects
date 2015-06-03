package com.example.serverupdate_8k;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Dbhelper extends SQLiteOpenHelper {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "person_name";
	public static final String KEY_AGE = "age";
	public static final String KEY_GENDER = "gender";

	public static final String DATABASE_NAME = "telemedi";
	public static final String DATABASE_TABLE = "patients";
	public static final int DATABASE_VERSION = 1;

	public Dbhelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ROWID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME + ","
				+ KEY_AGE + "," + KEY_GENDER + ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
		onCreate(db);
	}

}
