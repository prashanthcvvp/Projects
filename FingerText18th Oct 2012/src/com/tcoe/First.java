package com.tcoe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class First extends Activity {

	Button b;
	EditText ed;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first);
		
		b = (Button) findViewById(R.id.button1);
		ed = (EditText) findViewById(R.id.editText1);
		b.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name = ed.getEditableText().toString();
				Intent intent = new Intent(getBaseContext(),FingerText.class);
				intent.putExtra("name", name);
				startActivity(intent);
			}
		});
	}
}
