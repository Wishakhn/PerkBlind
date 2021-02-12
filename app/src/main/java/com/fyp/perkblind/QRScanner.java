package com.fyp.perkblind;

import android.Manifest;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.fyp.perkblind.qr.QRCodeReaderView;

import static com.fyp.perkblind.HelperClass.checkCameraPermissions;

public class QRScanner extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {
    QRCodeReaderView qrCodeReaderView;
    AppCompatImageView ivMyQrCode, ivLibrary;
    HelperClass helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_scanner);
        initViews();
        initAppBar("QR SCANNER");
    }

    private void initViews() {
        helper = new HelperClass(QRScanner.this);
        qrCodeReaderView = findViewById(R.id.qrCodeReaderView);
        ivMyQrCode = findViewById(R.id.ivMyQrCode);
        ivLibrary = findViewById(R.id.ivLibrary);
        if (checkCameraPermissions(QRScanner.this)) {
            initQRCodeReaderView();
        } else {
            requestPermissions(getPermissions(), 1005);
        }
    }

    private String[] getPermissions() {
        return new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
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
    public void onQRCodeRead(String text, PointF[] points) {
        qrCodeReaderView.setQRDecodingEnabled(false);

    }

    private void initQRCodeReaderView() {
        qrCodeReaderView = qrCodeReaderView;
        qrCodeReaderView.setAutofocusInterval(2000L);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setBackCamera();
        qrCodeReaderView.startCamera();
        qrCodeReaderView.setQRDecodingEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.qrCodeReaderView != null)
            qrCodeReaderView.stopCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1005) {
            if (grantResults.length <= 0 || grantResults[0] != 0) {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                initQRCodeReaderView();
            }
        }

    }


}