package com.fyp.perkblind;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class QrCodeGenerater extends AppCompatActivity {
    HelperClass helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_generater);
        initAppBar("QR CODE GENERATOR");
        helper = new HelperClass(QrCodeGenerater.this);


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