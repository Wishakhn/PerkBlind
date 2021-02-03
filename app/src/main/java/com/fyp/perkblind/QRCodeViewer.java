package com.fyp.perkblind;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class QRCodeViewer extends AppCompatActivity {
    Prefrences prefrences;
    ImageView viewQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_code_viewer);
        initAppBar("QR CODE VIEWER");
        prefrences = new Prefrences(QRCodeViewer.this);
        prefrences.initPrefernce();
        viewQR = findViewById(R.id.viewQR);
        Glide.with(this).load(prefrences.fetchUserData().getQrCode()).into(viewQR);
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