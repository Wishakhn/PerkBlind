package com.fyp.perkblind;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    EditText NameET;
    TextView Email;
    Button updateBtn;
    Prefrences prefs;
    SpeechTextManager speechManager;
    Handler handler;
    HelperClass helper;
    Boolean takeInputForText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initAppBar("PROFILE");
        initViews();
    }

    private void initViews() {
        NameET = findViewById(R.id.NameET);
        Email = findViewById(R.id.MailET);
        updateBtn = findViewById(R.id.updateBtn);
        prefs = new Prefrences(Profile.this);
        handler = new Handler();
        prefs.initPrefernce();
        helper = new HelperClass(Profile.this);
        speechManager = new SpeechTextManager(Profile.this, false);
        NameET.setText(prefs.fetchUserData().getUsername());
        Email.setText(prefs.fetchUserData().getEmail());
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

    public void ProfileClickHandler(View view) {
        switch (view.getId()) {
            case R.id.updateBtn:
                if (NameET.getText().toString() != null && !NameET.getText().toString().isEmpty() && NameET.isEnabled()) {
                    updateUserProfile();
                }
                break;

            case R.id.viewQR:
                prefs.saveTargetClass(Profile.class);
                speechManager.setTts_str("QR Code Viewer");
                speechManager.showOptionDialog(prefs.getTargetName());
                break;

            case R.id.uNameContainer:
                takeInputForText = false;
                speechManager.setTts_str("You have selected Edit User Name. Please say Next to proceed with editing or command dismiss to abort");
                speechManager.ttsListner();
                speechManager.speak(speechManager.getTts_str());
                break;
            case R.id.icnmail:
                takeInputForText = false;
                speechManager.setTts_str("You have selected Edit User Name. Please say Next to proceed with editing or command dismiss to abort");
                speechManager.ttsListner();
                speechManager.speak(speechManager.getTts_str());
                break;
            case R.id.editName:
                takeInputForText = false;
                speechManager.setTts_str("You have selected Edit User Name. Please say Next to proceed with editing or command dismiss to abort");
                speechManager.ttsListner();
                speechManager.speak(speechManager.getTts_str());
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    startWriting(result.get(0));
                }
                break;
        }

    }

    private void startWriting(String s) {
        if (s.equalsIgnoreCase("next") || s.contains("next")) {
            NameET.setEnabled(true);
            speechManager.ttsListner();
            speechManager.speak("Start speaking your name after beep sound");
            takeInputForText = true;
        } else if (takeInputForText) {
            NameET.setText(s);
            updateBtn.setEnabled(true);
            updateBtn.setBackground(getDrawable(R.drawable.btn1background_normal));
            updateBtn.setTextColor(getColor(R.color.colorBlack));
            takeInputForText = false;
        }

    }

    private void updateUserProfile() {
        HelperClass.displayProgressDialog(Profile.this, "Updating Profile. . . ");
        String fbid = "" + prefs.fetchUserData().getId();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(fbid).child("username");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HelperClass.hideProgressDialog();
                reference.setValue(NameET.getText().toString());
                FetchUpdatedData();
                NameET.setEnabled(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                HelperClass.hideProgressDialog();
                Toast.makeText(Profile.this, "Error !! \n" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void FetchUpdatedData() {
        UserData data = prefs.fetchUserData();
        UserData newData = new UserData(data.getId(), NameET.getText().toString(), data.getEmail(), data.getQrCode(), data.getPassword(), data.getLoggedIn());
//        prefs.removePrefernce(Prefrences.USER_DATA);
        prefs.saveUserData(newData);
    }
}