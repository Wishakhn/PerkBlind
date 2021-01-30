package com.example.perkblind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void HandleSettings(View view) {
        switch (view.getId()) {
            case R.id.changPass:
                break;
            case R.id.setGuide:
                moveSimpleIntent(PerkGuide.class);
                break;
            case R.id.genQR:

                break;
            case R.id.logout:
                break;
        }
    }

    private void moveSimpleIntent(Class<?> target) {
        Intent intent = new Intent(Settings.this, target);
        startActivity(intent);
    }
    private void initAppBar( String txt) {
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