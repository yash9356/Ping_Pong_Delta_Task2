package com.annhilator.PingPongLite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameOver extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    boolean won;
    TextView tvHighest;
    TextView tvPoints;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView((int) R.layout.activity_game_over);
        tvPoints = (TextView) findViewById(R.id.tvPoints);
        tvHighest = (TextView) findViewById(R.id.tvHighest);
        int i = getIntent().getExtras().getInt("points");
        won =getIntent().getBooleanExtra("won",false);
        if(won){
            Toast.makeText(GameOver.this,"You Won!",Toast.LENGTH_SHORT).show();
        }
        TextView textView = tvPoints;
        textView.setText(Integer.toString(i) );
        SharedPreferences sharedPreferences2 = getSharedPreferences("my_pref", 0);
        sharedPreferences = sharedPreferences2;
        int i2 = sharedPreferences2.getInt("highest", 0);
        if (i > i2) {
            tvPoints.setText(Integer.toString(i));
            SharedPreferences.Editor edit = this.sharedPreferences.edit();
            edit.putInt("highest", i);
            edit.commit();
        } else {
            i = i2;
        }
        TextView textView2 = tvHighest;
        textView2.setText("High Score :"+Integer.toString(i));
    }

    public void restart(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void exit(View view) {
        finish();
    }
}
