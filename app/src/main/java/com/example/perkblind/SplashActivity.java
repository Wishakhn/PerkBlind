package com.example.perkblind;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.perkblind.Prefrences.FIRST_RUN;

public class SplashActivity extends AppCompatActivity {
    boolean isFirstrun = false;
    Prefrences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prefs = new Prefrences(this);
        prefs.initPrefernce();
        isFirstrun = prefs.loadBooleanPrefernce(FIRST_RUN);
        Handler hndler = new Handler();
        hndler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startSplash();
            }
        }, 1700);
    }

    private void startSplash() {
        if (isFirstrun) {
            Intent tent = new Intent(this, IntroSlider.class);
            startActivity(tent);
            prefs.saveBooleanPrefernce(FIRST_RUN, false);
            finish();
        } else {
            if (prefs.loadBooleanPrefernce(prefs.IS_LOGGED_IN)) {
                Intent tent = new Intent(this, MainActivity.class);
                startActivity(tent);
                finish();
            } else {
                Intent tent = new Intent(this, Register_Login.class);
                startActivity(tent);
                finish();
            }
        }
    }
}