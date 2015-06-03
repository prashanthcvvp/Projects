package com.medapp;

import android.app.Activity;
import android.media.AudioRecord;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.backend.GlobalAppData;
import com.backend.ReceiveResponse;

public class MainActivity extends Activity implements OnClickListener {
	
	Button pulseOx,steth;
	TextView status_view;
	GlobalAppData global_app_data;
	AudioRecord recorder;	
	/******************************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		pulseOx = (Button)findViewById(R.id.pulseox);
		steth=(Button)findViewById(R.id.steth);
		status_view =(TextView)findViewById(R.id.textView1);		
		global_app_data = (GlobalAppData)getApplication();
		recorder = global_app_data.findAudioRecord();
		
	}
	/******************************************************************************/
	@Override
	protected void onResume() {
		super.onResume();
		pulseOx.setOnClickListener(this);
		steth.setOnClickListener(this);
	}
	/******************************************************************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/******************************************************************************/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/******************************************************************************/
	@Override
	public void onClick(View v) {
		ReceiveResponse receive_response = new ReceiveResponse(recorder, MainActivity.this,status_view);
		int id = v.getId();
		
		switch (id) {
		case R.id.pulseox:{
			try {
				receive_response.execute(2);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		case R.id.steth:{
			try {
				StethOptions steth = new StethOptions(MainActivity.this,receive_response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		}
	}
	/******************************************************************************/
	public void releaseRecorder(){
		recorder.stop();
		recorder.release();
		
	}
	/******************************************************************************/
	@Override
	protected void onPause() {
		super.onPause();
		releaseRecorder();
	}
	/******************************************************************************/
}
