package com.example.perkblind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import static com.example.perkblind.Prefrences.TTS_SPEAKER;

public class IntroSlider extends AppCompatActivity {
RadioButton Rbtn1;
RadioButton Rbtn2;
Button startbTN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slider);
        Rbtn1 = findViewById(R.id.Rbtn1);
        Rbtn2 = findViewById(R.id.Rbtn2);
        startbTN = findViewById(R.id.startBtn);
        final Prefrences prefs = new Prefrences(this);
        prefs.initPrefernce();


        Rbtn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Rbtn2.setChecked(false);
                prefs.saveIntegerPreference(TTS_SPEAKER, 1);
                startbTN.setEnabled(true);
                startbTN.setTextColor(getResources().getColor(R.color.colorBlack));
                startbTN.setBackground(getResources().getDrawable(R.drawable.btn2background_normal));

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
            }
        });
        startbTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tent = new Intent(IntroSlider.this, Register_Login.class);
                startActivity(tent);
                finish();
            }
        });
    }
}