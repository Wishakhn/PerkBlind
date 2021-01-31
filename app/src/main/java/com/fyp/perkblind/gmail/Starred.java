package com.fyp.perkblind.gmail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fyp.perkblind.R;

public class Starred extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starred);
        initAppBar("STARRED");
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