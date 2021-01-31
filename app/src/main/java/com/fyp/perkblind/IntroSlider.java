package com.fyp.perkblind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import static com.fyp.perkblind.Prefrences.TTS_SPEAKER;

public class IntroSlider extends AppCompatActivity {
    RadioButton Rbtn1;
    RadioButton Rbtn2;
    Button startbTN;
    SpeechTextManager speechManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slider);
        Rbtn1 = findViewById(R.id.Rbtn1);
        Rbtn2 = findViewById(R.id.Rbtn2);
        startbTN = findViewById(R.id.startBtn);
        final Prefrences prefs = new Prefrences(this);
        prefs.initPrefernce();
        speechManager = new SpeechTextManager(IntroSlider.this, false);

        Rbtn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Rbtn2.setChecked(false);
                prefs.saveIntegerPreference(TTS_SPEAKER, 1);
                startbTN.setEnabled(true);
                startbTN.setTextColor(getResources().getColor(R.color.colorBlack));
                startbTN.setBackground(getResources().getDrawable(R.drawable.btn2background_normal));
                speechManager.speak("You have selected the option of "+Rbtn1.getText().toString());

            }
        });
        Rbtn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Rbtn1.setChecked(false);
                prefs.saveIntegerPreference(TTS_SPEAKER, 2);
                startbTN.setEnabled(true);
                startbTN.setTextColor(getResources().getColor(R.color.colorBlack));
                startbTN.setBackground(getResources().getDrawable(R.drawable.btn2background_normal));
                speechManager.speak("You have selected the option of "+Rbtn2.getText().toString());
            }
        });
        startbTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tent = new Intent(IntroSlider.this, MainActivity.class);
                startActivity(tent);
                finish();
            }
        });
    }
}