package com.tcoe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class FingerText extends Activity implements
		ColorPickerDialog.OnColorChangedListener {

	private Paint mPaint;
	private Bitmap mBitmap;
	private boolean inkChosen;
	private int bgColor = 0xFFFFFFFF; // set initial bg color var to white
	private int inkColor = 0xFF000000; // set initial ink color var to black
	File directory;

	MyView v;
	String name;
	File mypath;
	String name1 ;
	static int count = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		v = new MyView(this);
		setContentView(v);
		
		directory = new File("/sdcard/Signatures/");

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		// this is the line that sets the initial pen color
		mPaint.setColor(inkColor);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(5f);
		
		

	}

	public void colorChanged(int color) {
		// This is the implementation of the interface from
		// colorpickerdialog.java

		if (inkChosen) {
			mPaint.setColor(color);
			inkColor = color;
		} else {
			mBitmap.eraseColor(color);
			bgColor = color;
			// set the color to the user's last ink color choice
			mPaint.setColor(inkColor);
		}

	}

	


	private static final int PEN = Menu.FIRST;
	private static final int CLEAR_MENU_ID = Menu.FIRST+1;
	private static final int ERASER_MENU_ID = Menu.FIRST+2;
	private static final int SAVE_MENU_ID = Menu.FIRST+3;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

	
		menu.add(0, PEN, 0, "Pen").setIcon(R.drawable.monotone_pen_write);
		menu.add(0, CLEAR_MENU_ID, 0, "Clear All").setIcon(R.drawable.edit_clear);
		menu.add(0, ERASER_MENU_ID, 0, "Eraser").setIcon(R.drawable.eraser);
		menu.add(0, SAVE_MENU_ID, 0, "Save").setIcon(R.drawable.save);

		
		/****
		 * Is this the mechanism to extend with filter effects? Intent intent =
		 * new Intent(null, getIntent().getData());
		 * intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
		 * menu.addIntentOptions( Menu.ALTERNATIVE, 0, new ComponentName(this,
		 * NotesList.class), null, intent, 0, null);
		 *****/
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		mPaint.setXfermode(null);
		mPaint.setAlpha(0xFF);

		switch (item.getItemId()) {

		case PEN:
			mPaint.setColor(inkColor);
			return true;
		case CLEAR_MENU_ID:
			mBitmap.eraseColor(bgColor);
			return true;
		case ERASER_MENU_ID:
			// set pen color to bg color for 'erasing'
			mPaint.setColor(bgColor);
			return true;
			
		case SAVE_MENU_ID:
			
			prepareDirectory();
			try{
				name1 = getIntent().getExtras().getString("name");
			
			name = name1 +".png";
			mypath= new File(directory,name);
			
			if(mypath.exists()){
				count++;
				name = name1 +" "+count+".png";
				mypath= new File(directory,name);
				
			}
			
			
			FileOutputStream mFileOutStream = new FileOutputStream(mypath);
			mBitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
	        mFileOutStream.flush();
	        mFileOutStream.close();
	        Toast.makeText(this, "Saved in sdcard/Signatures", Toast.LENGTH_SHORT).show();
	        
			}
			
			catch(IOException e1){
				Toast.makeText(this, "IOException "+e1.getMessage(), Toast.LENGTH_SHORT).show();
				Log.d("IOException", e1.getMessage());
			}
			catch(NullPointerException e2){
				Toast.makeText(this, "NullPointerException "+e2.getMessage(), Toast.LENGTH_SHORT).show();
				Log.d("NullPointerException", e2.getMessage());
			}
			catch(Exception e3){
				Toast.makeText(this, "Exception "+e3.getMessage(), Toast.LENGTH_SHORT).show();
				Log.d("Exception",e3.getMessage());
			}
			return true;
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void prepareDirectory() {
		// TODO Auto-generated method stub
		if(!directory.exists()){
			directory.mkdirs();
		}
	}
	
	
	public class MyView extends View {

		
		private Canvas mCanvas;
		private Path mPath;
		private Paint mBitmapPaint;

		public MyView(Context c) {
			super(c);

			mBitmap = Bitmap.createBitmap(1400,1400 , Bitmap.Config.ARGB_8888);

			// this sets the bg color for the bitmap
			mBitmap.eraseColor(bgColor);
			mCanvas = new Canvas(mBitmap);
			mPath = new Path();
			mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// this is the line that changes the bg color in the initial canvas
			canvas.drawColor(bgColor);
			canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

			canvas.drawPath(mPath, mPaint);
		}

		private float mX, mY;
		private static final float TOUCH_TOLERANCE = 4;

		private void touch_start(float x, float y) {
			mPath.reset();
			mPath.moveTo(x, y);
			mX = x;
			mY = y;
		}

		private void touch_move(float x, float y) {
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
				mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;
			}
		}

		private void touch_up() {
			mPath.lineTo(mX, mY);
			// commit the path to our offscreen
			mCanvas.drawPath(mPath, mPaint);
			// kill this so we don't double draw
			mPath.reset();
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX();
			float y = event.getY();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touch_start(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				touch_move(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				touch_up();
				invalidate();
				break;
			}
			return true;
		}
	}
}
