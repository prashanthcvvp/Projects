package com.backend;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.media.AudioRecord;
import android.os.AsyncTask;
import android.widget.TextView;

public class ReceiveResponse extends AsyncTask<Integer, Void, StringBuilder> implements OnClickListener{
	private AudioRecord recorder;
	private BasicTesting basic_testing;
	private ProgressDialog dialog_loading;
	private TextView status_view;
	private Context context;
	
	StringBuilder builder;
	public ReceiveResponse(AudioRecord recorder,Context context,TextView status_view) {
		this.recorder=recorder;
		basic_testing = new BasicTesting(this.recorder);
		this.context=context;
		this.status_view=status_view;
		
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog_loading = new ProgressDialog(this.context);
		dialog_loading.setTitle("Please wait...");
		dialog_loading.setCancelable(false);
		dialog_loading.setMessage("Reading...");
		
		dialog_loading.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", this);
		dialog_loading.show();
	}

	@Override
	protected StringBuilder doInBackground(Integer... value) {
		testing(value[0]);
		return null;
		
	}
	
	@Override
	protected void onPostExecute(StringBuilder result) {
		super.onPostExecute(result);
		dialog_loading.dismiss();		
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case DialogInterface.BUTTON_NEGATIVE:{
			GlobalAppData.cancel=true;
			cancel(true);
			break;
			}

		}
		
	}
	
	public StringBuilder testing(int test_id){
		switch(test_id){
		case 2:{
			builder = basic_testing.pulseOximeter();
			break;
			
		}
		case 1:{
			builder=basic_testing.stethoscopeStreaming();
			break;
		}
		}
		
		return null;
	}

}
