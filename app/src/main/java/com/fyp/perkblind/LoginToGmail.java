package com.fyp.perkblind;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.bumptech.glide.Glide;
import com.fyp.perkblind.gmail.GmailData;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import static com.fyp.perkblind.HelperClass.displayProgressDialog;
import static com.fyp.perkblind.HelperClass.hideProgressDialog;

public class LoginToGmail extends AppCompatActivity {

    private static final int RC_SIGN_IN = 191;
    private static final String TAG = "GooleSignTAG";

    Button button;
    GoogleSignInOptions gso;
    GoogleSignInClient googleSignInClient;
    ImageView profileImage;
    TextView PassEnt, MailEnt, NameDisp, MailDisp;
    LinearLayoutCompat signin_container, signinscreen;
    String usename = "";
    String mail = "";
    String pic = "";
    Uri pic_uri;
    Prefrences prefrences;
    SpeechTextManager speechManager;
    Handler handler;
    Boolean takeInputForText;
    TextView textv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_to_gmail);
        initViews();
    }

    private void initViews() {
        prefrences = new Prefrences(LoginToGmail.this);
        prefrences.initPrefernce();
        handler = new Handler();
        speechManager = new SpeechTextManager(LoginToGmail.this,false);
        profileImage = findViewById(R.id.profileImage);
        PassEnt = findViewById(R.id.PassEnt);
        MailEnt = findViewById(R.id.MailEnt);
        NameDisp = findViewById(R.id.NameDisp);
        MailDisp = findViewById(R.id.MailDisp);
        button = findViewById(R.id.sign_in_button);
        signin_container = findViewById(R.id.name_container);
        signinscreen = findViewById(R.id.signinscreen);
        gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        System.out.println("Google Signin Option::" + gso.toString());
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        System.out.println("Google Client::" + googleSignInClient.toString());
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        System.out.println("INTENT SEND SUCCESSFULLY");
    }

    private void HandleSigninGoogle(Task<GoogleSignInAccount> task) {
        displayProgressDialog(LoginToGmail.this, "Signing in to gmail. . . . ");
        System.out.println("response::" + task.getResult());
        /* if (task.isSuccessful()){*/
//        hideProgressDialog();
        GoogleSignInAccount googleAccount = null;
        try {
            googleAccount = task.getResult(ApiException.class);
            System.out.println("Google Account Results::" + googleAccount.toString());
        } catch (ApiException e) {
            e.printStackTrace();
            Log.d(TAG, "Errror ::" + e.toString());
            System.out.println("Exception1111 ::" + e.toString());
        }
        if (googleAccount != null) {
            if (signin_container.getVisibility() == View.VISIBLE) {
                signin_container.setVisibility(View.GONE);
            }
            if (signinscreen.getVisibility() == View.GONE) {
                signinscreen.setVisibility(View.VISIBLE);
            }
            button.setText("PROCEED");
            button.setBackground(getDrawable(R.drawable.btn1background_normal));
            button.setTextColor(getColor(R.color.colorBlack));
            System.out.println("Google USERNAME::" + googleAccount.getDisplayName());
            System.out.println("Google Email::" + googleAccount.getEmail());
            System.out.println("Google Photo::" + googleAccount.getPhotoUrl());
            System.out.println("Google ID::" + googleAccount.getId());
            String id = googleAccount.getId();
            usename = googleAccount.getDisplayName();
            mail = googleAccount.getEmail();
            pic_uri = googleAccount.getPhotoUrl();
            GmailData data = new GmailData(id, mail, usename, pic_uri);
            prefrences.saveGmailData(data);
            Glide.with(this).load(pic_uri).into(profileImage);
            NameDisp.setText(usename);
            MailDisp.setText(mail);

        }
        //}
        else {
            hideProgressDialog();
            Toast.makeText(this, "Sorry the Logging in Failed", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    startWriting(result.get(0),textv);
                }
                break;

            case RC_SIGN_IN:
                System.out.println("Data ::" + data.toString());
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                HandleSigninGoogle(task);
                break;

        }
    }

    public void SignInGmailClickHandler(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                if (button.getText().toString().equalsIgnoreCase("SIGN IN")) {
                    System.out.println("Button Clicked Successfully");
                    signIn();
                } else {
                    Intent tent = new Intent(LoginToGmail.this, MainActivity.class);
                    tent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(tent);
                }
                break;
            case R.id.mailSign_container:
                takeInputForText = false;
                textv = MailEnt;
                speechManager.setTts_str("You have selected Email Field. Please say Next to enter your email or command dismiss to abort");
                speechManager.ttsListner();
                speechManager.speak(speechManager.getTts_str());

                break;
            case R.id.passSign_container:
                takeInputForText = false;
                textv = PassEnt;
                speechManager.setTts_str("You have selected Password Field. Please say Next to enter your email or command dismiss to abort");
                speechManager.ttsListner();
                speechManager.speak(speechManager.getTts_str());
                break;
        }

    }

    private void startWriting(String s, TextView textview) {
        if (s.equalsIgnoreCase("next") || s.contains("next")) {
            speechManager.ttsListner();
            speechManager.speak("Start speaking your name after beep sound");
            takeInputForText = true;
        }
        else if (takeInputForText) {
            textview.setText(s);
            takeInputForText = false;
        }

    }
}