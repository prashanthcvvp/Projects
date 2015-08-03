package com.example.prashanth.projectk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.backend.GetDataFromServer;

/**
 * Created by prashanth on 6/27/15.
 */
public class Parameter extends Activity implements View.OnClickListener{
    Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parameter_activity);
        start = (Button)findViewById(R.id.button);
        start.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        int select = v.getId();
        switch(select){
            case R.id.button:
               startActivity(new Intent(Parameter.this,google_map.class));
                break;
        }
    }
}
