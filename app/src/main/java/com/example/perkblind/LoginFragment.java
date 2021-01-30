package com.example.perkblind;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.perkblind.HelperClass.hideProgressDialog;
import static com.example.perkblind.HelperClass.makeAlert;


public class LoginFragment extends Fragment {

    String email;
    String pass;
    EditText MailET;
    EditText PassET;
    Button login;
    Button loginwithQr;
    FirebaseAuth auth;
    Prefrences prefrences;
    SpeechTextManager speechManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        MailET = v.findViewById(R.id.MailET);
        PassET = v.findViewById(R.id.PassET);
        login = v.findViewById(R.id.login);
        loginwithQr = v.findViewById(R.id.loginwithQr);
        loginwithQr.setOnClickListener(onClickListner);
        PassET.setOnEditorActionListener(editorLisnter);
        auth = FirebaseAuth.getInstance();
        prefrences = new Prefrences(requireContext());
        speechManager = new SpeechTextManager(requireActivity(),false);
        return v;
    }

    View.OnClickListener onClickListner = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
loginUser(email,pass);

        }
    };
    TextView.OnEditorActionListener editorLisnter = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_DONE) {
                if (validate()) {
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
        if (email != null && !email.isEmpty() && !pass.isEmpty()) {
            bool = true;
        }
        return bool;
    }
    private void loginUser(String email, final String pass) {
        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    UserData data = new UserData();
                    prefrences.saveUserData(data);
                    prefrences.saveBooleanPrefernce(prefrences.IS_LOGGED_IN,data.getLoggedIn());
                    hideProgressDialog();
                  if (prefrences.loadBooleanPrefernce(prefrences.FIRST_RUN)){
                      speechManager.moveToScreen(IntroSlider.class);
                  }
                  else {
                      speechManager.moveToScreen(MainActivity.class);
                      requireActivity().finish();
                  }
                }
                else {
                    hideProgressDialog();
                    makeAlert(requireContext(),"Sorry!!", "unable to login user..");
                }
            }
        });
    }
}