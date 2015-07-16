package com.example.prashanth.project4;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by prashanth on 7/14/15.
 */
public class StartActivity extends Activity implements View.OnClickListener {
    Button start_game;
    TextView score_view;

    SoundPool soundPool;
    int sound=-1;
    AssetManager assetManager;
    AssetFileDescriptor descriptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_start);
        start_game=(Button)findViewById(R.id.button);
        start_game.setOnClickListener(this);
        score_view=(TextView)findViewById(R.id.score);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(GlobalApplicationData.isIf_show_score()){
            score_view.setText("Score "+String.valueOf(GlobalApplicationData.getScore()));
        }
    }

    @Override
    public void onClick(View v) {
        int select_view = v.getId();
        switch(select_view){
            case R.id.button:
                startActivity(new Intent(StartActivity.this,MainGame.class));
                break;
        }
    }
}
