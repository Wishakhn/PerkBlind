package com.example.perkblind;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginFragment extends Fragment {

String email;
String pass;
EditText MailET;
EditText PassET;
Button login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        MailET = v.findViewById(R.id.MailET);
        PassET = v.findViewById(R.id.PassET);
        login = v.findViewById(R.id.login);
        PassET.setOnEditorActionListener(editorLisnter);
        return v;
    }
    TextView.OnEditorActionListener editorLisnter = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_DONE){
                if (validate()){
                    login.setEnabled(true);
                }
            }
            return false;
        }
    };

    private boolean validate() {
        boolean bool = false;
        email = MailET.getText().toString();
        pass = PassET.getText().toString();
        if(email != null && pass != null & !email.isEmpty() && !pass.isEmpty()){
            bool = true;
        }
        return bool;
    }
}