package com.medapp;

import java.util.ArrayList;

import com.backend.GlobalAppData;
import com.backend.ReceiveResponse;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class StethOptions extends Dialog implements OnItemClickListener{
	private Context context;
	private ListView steth_options;
	private ArrayList<String> options_list;
	private ReceiveResponse receive_response;

	public StethOptions(Context context,ReceiveResponse receive_response) {
		super(context);
		setContentView(R.layout.steth_options);
		setTitle("Stethescope");
		this.context=context;
		steth_options = (ListView)findViewById(R.id.listView1);
		options_list=new ArrayList<String>();
		options_list.add("Heart Sound");
		options_list.add("General");
		ArrayAdapter<String> adaptor = new ArrayAdapter<String>(this.context, android.R.layout.simple_list_item_1, options_list);
		steth_options.setAdapter(adaptor);
		steth_options.setOnItemClickListener(this);
		this.receive_response=receive_response;
		show();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		String option = (String) parent.getItemAtPosition(position);
		if(option.equalsIgnoreCase("Heart Sound")) {
			GlobalAppData.steth_option_index = 1;
			Toast.makeText(context, "Heart Sound", Toast.LENGTH_LONG).show();
		}else if(option.equalsIgnoreCase("General")){
			GlobalAppData.steth_option_index=2;
			Toast.makeText(context, "General", Toast.LENGTH_LONG).show();
		}
		receive_response.execute(1);
		dismiss();
		
	}
}


