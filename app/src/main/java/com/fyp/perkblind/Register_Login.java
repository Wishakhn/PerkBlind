package com.fyp.perkblind;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import static com.fyp.perkblind.HelperClass.REQUEST_SPEECH_INPUT;

public class Register_Login extends AppCompatActivity {
    ImageView back;
    TextView title;
    FrameLayout replacer;
    FragmentManager fm;
    FragmentTransaction ft;
    final static String MAIN_TAG = "PERK BLIND";
    SpeechTextManager speechManager;
    Handler handlr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__login);
        back = findViewById(R.id.backarrow);
        title = findViewById(R.id.title);
        replacer = findViewById(R.id.replacer);
        fm = getSupportFragmentManager();
        handlr = new Handler();
        speechManager = new SpeechTextManager(Register_Login.this, false);
        initAppBar(MAIN_TAG);
        speechManager.speak("Welcome To Perk Blind Authentication Option screen please speak Login to move to login screen or speak Register to move register screen after you hear the beep sound");
        speechManager.ttsListner();

    }


    private void initAppBar(String txt) {
        title.setText(txt);
        back.setVisibility(View.INVISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void authEventListner(View view) {
        switch (view.getId()) {
            case R.id.login:
                speechManager.speak("You have clicked login button");
                handlr.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LoginFragment frag = new LoginFragment();
                        transectFragment(frag, "Login");
                    }
                },1500);
                break;
            case R.id.reg:
                speechManager.speak("You have clicked Register button");

                handlr.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RegisterFragment rfrag = new RegisterFragment();
                        transectFragment(rfrag, "Register");
                    }
                },1500);

                break;
        }
    }

    private void transectFragment(Fragment frag, String tag) {
        title.setText(tag);
        ft = fm.beginTransaction();
        ft.add(R.id.replacer, frag, tag);
        ft.addToBackStack(tag);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        title.setText(MAIN_TAG);
        if (fm.getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            fm.popBackStack();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SPEECH_INPUT:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    moveScreen(result.get(0));
                }
                break;
        }
    }

    private void moveScreen(String s) {
        if (s.contains("login")){
            LoginFragment frag = new LoginFragment();
            transectFragment(frag, "Login");
        }
        else {
            RegisterFragment rfrag = new RegisterFragment();
            transectFragment(rfrag, "Register");
        }
    }
}