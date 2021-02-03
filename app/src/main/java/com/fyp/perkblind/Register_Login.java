package com.fyp.perkblind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class Register_Login extends AppCompatActivity {
    ImageView back;
    TextView title;
    FrameLayout replacer;
    FragmentManager fm;
    FragmentTransaction ft;
    final static String MAIN_TAG="PERK BLIND";
    SpeechTextManager speechManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__login);
        back = findViewById(R.id.backarrow);
        title = findViewById(R.id.title);
        replacer = findViewById(R.id.replacer);
        fm = getSupportFragmentManager();
        speechManager = new SpeechTextManager(Register_Login.this,false);
        initAppBar(MAIN_TAG);

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
                LoginFragment frag = new LoginFragment();
                transectFragment(frag, "Login");
                break;
            case R.id.reg:
                RegisterFragment rfrag = new RegisterFragment();
                transectFragment(rfrag, "Register");
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
}