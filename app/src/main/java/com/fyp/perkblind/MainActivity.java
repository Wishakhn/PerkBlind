package com.fyp.perkblind;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout profileBtn;
    LinearLayout inboxBtn;
    LinearLayout draftBtn;
    LinearLayout composeBtn;
    LinearLayout gmailBtn;
    LinearLayout impBtn;
    LinearLayout guideBtn;
    LinearLayout settingBtn;
    SpeechTextManager speechManager;
    Prefrences prefrences;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        profileBtn = findViewById(R.id.profileBtn);
        inboxBtn = findViewById(R.id.inboxBtn);
        draftBtn = findViewById(R.id.draftBtn);
        composeBtn = findViewById(R.id.composeBtn);
        gmailBtn = findViewById(R.id.gmailBtn);
        impBtn = findViewById(R.id.impBtn);
        guideBtn = findViewById(R.id.guideBtn);
        settingBtn = findViewById(R.id.settingBtn);
        prefrences = new Prefrences(MainActivity.this);
        initAppBar("PERK BLIND");
        speechManager = new SpeechTextManager(MainActivity.this, true);
        setListeners();
    }

    private void setListeners() {
        profileBtn.setOnClickListener(this);
        inboxBtn.setOnClickListener(this);
        draftBtn.setOnClickListener(this);
        composeBtn.setOnClickListener(this);
        gmailBtn.setOnClickListener(this);
        impBtn.setOnClickListener(this);
        guideBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    try {
                        speechManager.actOnCommand(result.get(0), Class.forName(prefrences.getTargetClassName()));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (speechManager.tts != null) {
            speechManager.tts.stop();
            speechManager.tts.shutdown();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profileBtn:
                prefrences.saveTargetClass(Profile.class);
                speechManager.setTts_str(prefrences.getTargetClassName());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        speechManager.speak(speechManager.getTts_str());
                    }
                }, 1000);
                break;
            case R.id.inboxBtn:
                prefrences.saveTargetClass(Inbox.class);
                speechManager.setTts_str(prefrences.getTargetClassName());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        speechManager.speak(speechManager.getTts_str());
                    }
                }, 1000);

                break;
            case R.id.composeBtn:
                prefrences.saveTargetClass(SendEmail.class);
                speechManager.setTts_str(prefrences.getTargetClassName());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        speechManager.speak(speechManager.getTts_str());
                    }
                }, 1000);
                break;
            case R.id.draftBtn:
                prefrences.saveTargetClass(ReadMail.class);
                speechManager.setTts_str(prefrences.getTargetClassName());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        speechManager.speak(speechManager.getTts_str());
                    }
                }, 1000);
                break;
            case R.id.gmailBtn:
                break;
            case R.id.impBtn:
                prefrences.saveTargetClass(Starred.class);
                speechManager.setTts_str(prefrences.getTargetClassName());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        speechManager.speak(speechManager.getTts_str());
                    }
                }, 1000);
                break;
            case R.id.guideBtn:
                prefrences.saveTargetClass(PerkGuide.class);
                speechManager.setTts_str(prefrences.getTargetClassName());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        speechManager.speak(speechManager.getTts_str());
                    }
                }, 1000);
                break;
            case R.id.settingBtn:
                prefrences.saveTargetClass(Settings.class);
                speechManager.setTts_str(prefrences.getTargetClassName());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        speechManager.speak(speechManager.getTts_str());
                    }
                }, 1000);
                break;

        }
    }

    private void initAppBar(String txt) {
        ImageView back = findViewById(R.id.backarrow);
        TextView title = findViewById(R.id.title);
        title.setText(txt);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}