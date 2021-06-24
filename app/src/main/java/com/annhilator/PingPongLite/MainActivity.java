package com.annhilator.PingPongLite;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    Boolean audioState;
    ImageButton ibAudio;
    SharedPreferences sharedPreferences;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView((int) R.layout.activity_main);

        getWindow().addFlags(128);
        ibAudio = (ImageButton) findViewById(R.id.ibAudio1);
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
        setContentView((View) new GameView(this));
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

}
