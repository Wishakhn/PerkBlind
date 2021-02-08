package com.fyp.perkblind;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.fyp.perkblind.HelperClass.REQUEST_SPEECH_INPUT;
import static com.fyp.perkblind.HelperClass.composeTextToEmail;
import static com.fyp.perkblind.HelperClass.displayProgressDialog;
import static com.fyp.perkblind.HelperClass.hideProgressDialog;

public class RegisterFragment extends Fragment {
    String name;
    String email;
    String password;
    String confirmPass;
    EditText NameET;
    EditText MailET;
    EditText PassET;
    EditText ConfirmPassET;
    FirebaseAuth auth;
    DatabaseReference firebaseRef;
    SpeechTextManager speechManager;
    Button regbtn;
    EditText textv;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        auth = FirebaseAuth.getInstance();
        NameET = v.findViewById(R.id.NameET);
        MailET = v.findViewById(R.id.MailET);
        PassET = v.findViewById(R.id.PassET);
        ConfirmPassET = v.findViewById(R.id.CnfrmPassET);
        regbtn = v.findViewById(R.id.regbtn);
        speechManager = new SpeechTextManager(requireContext(), false);
        NameET.setOnClickListener(clickListner1);
        MailET.setOnClickListener(clickListner2);
        PassET.setOnClickListener(clickListner3);
        ConfirmPassET.setOnClickListener(clickListner4);
        regbtn.setOnClickListener(clickListner);

        return v;
    }

    View.OnClickListener clickListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (validate()) {
                displayProgressDialog(requireContext(), "Signing you UP..");
                register(name, email, password, "false", "null");
            }
        }
    };
    View.OnClickListener clickListner1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            textv = NameET;
            speechManager.setTts_str("Welcome to registration screen, you have started to register a new account. please speak out your user name after a beep sound.");
            setRegisterData();

        }
    };

    private void setRegisterData() {
        speechManager.speak(speechManager.getTts_str());
        speechManager.ttsListner();
    }

    View.OnClickListener clickListner2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            textv = MailET;
            speechManager.setTts_str("please speak out your email address after a beep sound.");
            setRegisterData();
        }
    };
    View.OnClickListener clickListner3 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            textv = PassET;
            speechManager.setTts_str(" please speak out your password after a beep sound. Note that your password should contains at least 9 characters.");
            setRegisterData();
        }
    };
    View.OnClickListener clickListner4 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            textv = ConfirmPassET;
            speechManager.setTts_str("please speak your password again to reconfirm after a beep sound.");
            setRegisterData();
        }
    };

    private boolean validate() {
        boolean bool = false;
        name = NameET.getText().toString();
        password = PassET.getText().toString();
        confirmPass = ConfirmPassET.getText().toString();
        email = MailET.getText().toString();
        if (name != null && email != null && !name.isEmpty() && !email.isEmpty() && password != null && confirmPass != null && !password.isEmpty() && !confirmPass.isEmpty() && password.equalsIgnoreCase(confirmPass)) {
            bool = true;
        }
        return bool;
    }

    private void register(final String uname, final String email, final String pass, final String LoggedIn, final String QR) {
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser fUser = auth.getCurrentUser();
                    assert fUser != null;
                    String userId = fUser.getUid();
                    System.out.println("User ID is " + userId);
                    firebaseRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                    HashMap<String, String> hashmap = new HashMap<String, String>();
                    hashmap.put("id", userId);
                    hashmap.put("username", uname);
                    hashmap.put("password", pass);
                    hashmap.put("email", email);
                    hashmap.put("isLoggedIn", LoggedIn);
                    hashmap.put("qrCode", QR);
                    firebaseRef.setValue(hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                hideProgressDialog();
                                moveToLogging();
                            }
                        }
                    });

                } else {
                    hideProgressDialog();
                    Toast.makeText(getActivity(), "Sorry!! Unable to register you with provided credentials", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void moveToLogging() {
        LoginFragment frag = new LoginFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager() != null ? getFragmentManager().beginTransaction() : null;
        fragmentTransaction.replace(R.id.replacer, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SPEECH_INPUT:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    fillTheField(result.get(0),textv);
                }
                break;
        }
    }

    private void fillTheField(String s, EditText t) {
        if (t == MailET){
            t.setText(composeTextToEmail(s));
        }
        else {
            t.setText(s);
        }
        if (validate()) {
            regbtn.setEnabled(true);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        speechManager.StopTTS();
    }
}