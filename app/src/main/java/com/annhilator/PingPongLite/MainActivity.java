package com.annhilator.PingPongLite;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    Boolean audioState;
    ImageButton ibAudio,playnor,playhack;
    Button easy1,hard1,easy2,hard2;
    SharedPreferences sharedPreferences;
    Reset1 reset1 = new Reset1(1,1);

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView((int) R.layout.activity_main);

        getWindow().addFlags(128);
        easy1=findViewById(R.id.easy1);
        easy2=findViewById(R.id.easy2);
        hard1=findViewById(R.id.hard1);
        hard2=findViewById(R.id.hard2);
        easy1.setVisibility(View.INVISIBLE);
        hard1.setVisibility(View.INVISIBLE);
        easy2.setVisibility(View.INVISIBLE);
        hard2.setVisibility(View.INVISIBLE);
        ibAudio = (ImageButton) findViewById(R.id.ibAudio1);
        playnor=findViewById(R.id.play_nor);
        playhack=findViewById(R.id.play_hacker);
        SharedPreferences sharedPreferences2 = getSharedPreferences("my_pref", 0);
        sharedPreferences = sharedPreferences2;
        Boolean valueOf = Boolean.valueOf(sharedPreferences2.getBoolean("audioState", true));
        audioState = valueOf;
        if (valueOf.booleanValue()) {
            ibAudio.setImageResource(R.drawable.ic_volume_up);
        } else {
            ibAudio.setImageResource(R.drawable.ic_volume_off1);
        }
    }

    public void startGame(View view) {
        int j=reset1.getF1();
        if(j%2!=0){
            easy1.setVisibility(View.VISIBLE);
            hard1.setVisibility(View.VISIBLE);
        }
        else {
            easy1.setVisibility(View.INVISIBLE);
            hard1.setVisibility(View.INVISIBLE);
        }
        j++;
        reset1.setF1(j);

    }
    public void startGame1(View view){
        setContentView((View) new GameView(this,1));
    }
    public void startGame2(View view){
        setContentView((View) new GameView(this,2));
    }


    public void audioPref(View view) {
        if (audioState.booleanValue()) {
            audioState = false;
            ibAudio.setImageResource(R.drawable.ic_volume_off1);
        } else {
            audioState = true;
            ibAudio.setImageResource(R.drawable.ic_volume_up);
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean("audioState", audioState.booleanValue());
        edit.commit();
    }

    public void startGameHack(View view){
        int j=reset1.getF2();
        if(j%2!=0){
            easy2.setVisibility(View.VISIBLE);
            hard2.setVisibility(View.VISIBLE);
        }
        else {
            easy2.setVisibility(View.INVISIBLE);
            hard2.setVisibility(View.INVISIBLE);
        }
        j++;
        reset1.setF2(j);
    }

    public void startGameH1(View view){
        setContentView((View) new GameViewH1(this));
    }
    public void startGameH2(View view){
        setContentView((View) new GameViewH(this));
    }


}
