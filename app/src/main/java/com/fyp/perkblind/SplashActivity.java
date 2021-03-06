package com.fyp.perkblind;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import static com.fyp.perkblind.Prefrences.FIRST_RUN;

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
        if (!isFirstrun) {
            Intent tent = new Intent(this, Register_Login.class);
            startActivity(tent);
            prefs.saveBooleanPrefernce(FIRST_RUN, true);
            finish();
        } else {
            if (prefs.loadBooleanPrefernce(Prefrences.IS_LOGGED_IN)) {
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