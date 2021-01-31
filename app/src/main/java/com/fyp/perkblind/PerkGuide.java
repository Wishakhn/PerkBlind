package com.fyp.perkblind;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PerkGuide extends AppCompatActivity {
    SpeechTextManager speechManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perk_guide);
        speechManager = new SpeechTextManager(PerkGuide.this, false);
        initAppBar("PERK GUIDE");
        startSpeaking();
    }

    private void startSpeaking() {
        TextView title, description;
        title = findViewById(R.id.ptitle);
        description = findViewById(R.id.pdescription);
        String str = composeStringToSpeak(title, description);
        speechManager.setTts_str(str);
        speechManager.speak(speechManager.getTts_str());
    }

    private String composeStringToSpeak(TextView title, TextView description) {
        String str1 = title.getText().toString();
        String str2 = description.getText().toString();
        return str1 + str2;
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

    @Override
    protected void onPause() {
        super.onPause();
        speechManager.tts.stop();
        speechManager.tts.shutdown();
    }
}