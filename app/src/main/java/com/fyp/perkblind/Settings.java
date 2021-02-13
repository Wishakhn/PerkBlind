package com.fyp.perkblind;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Set;

public class Settings extends AppCompatActivity {
    Prefrences prefs;
    SpeechTextManager speechManager;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        prefs = new Prefrences(Settings.this);
        prefs.initPrefernce();
        handler = new Handler();
        speechManager = new SpeechTextManager(Settings.this, false);
        initAppBar("SETTINGS");
    }

    public void HandleSettings(View view) {
        switch (view.getId()) {
            case R.id.changPass:
                break;
            case R.id.setGuide:
                navigateTo(PerkGuide.class,"You have selected PERK Guide option now we are navigating to to Perk Guide screen ");
                break;
            case R.id.genQR:
                navigateTo(QrCodeGenerater.class,"You have selected QR COde generator now we are navigating to to QR Code Generator screen");
                break;
            case R.id.logout:
                logoutUser();
                break;
        }
    }

    private void navigateTo(Class<?> target, String tsr) {
        prefs.saveTargetClass(target);
                speechManager.setTts_str(tsr);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        speechManager.speak(speechManager.getTts_str());
                    }
                }, 1000);
                speechManager.showOptionDialog(prefs.getTargetName());
    }

    private void moveSimpleIntent(Class<?> target) {
        Intent intent = new Intent(Settings.this, target);
        startActivity(intent);
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
    private void logoutUser(){
        String fbid = ""+ prefs.fetchUserData().getId();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(fbid).child("isLoggedIn");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reference.setValue("false");
                FirebaseAuth.getInstance().signOut();
                prefs.saveBooleanPrefernce(Prefrences.IS_LOGGED_IN,false);
                Intent goback = new Intent(Settings.this, Register_Login.class);
                goback.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(goback);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Settings.this, "Error !! \n" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}