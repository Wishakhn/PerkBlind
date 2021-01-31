package com.fyp.perkblind;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.fyp.perkblind.HelperClass.displayProgressDialog;
import static com.fyp.perkblind.HelperClass.hideProgressDialog;
import static com.fyp.perkblind.HelperClass.makeAlert;


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
    DatabaseReference firebaseRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        prefrences = new Prefrences(requireContext());
        prefrences.initPrefernce();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        MailET = v.findViewById(R.id.MailET);
        PassET = v.findViewById(R.id.PassET);
        login = v.findViewById(R.id.login);
        loginwithQr = v.findViewById(R.id.loginwithQr);
        login.setOnClickListener(onClickListner);
        PassET.setOnEditorActionListener(editorLisnter);
        speechManager = new SpeechTextManager(requireActivity(), false);
        return v;
    }

    View.OnClickListener onClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            loginUser(email, pass);

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
        displayProgressDialog(requireContext(), "Logging In....");
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser fUser = auth.getCurrentUser();
                    assert fUser != null;
                    final String userId = fUser.getUid();
                    System.out.println("User ID is " + userId);
                    firebaseRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("isLoggedIn");
                    firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            firebaseRef.setValue("true");
                            loadUserData(userId);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    hideProgressDialog();
                    makeAlert(getContext(), "SORRY", "" + task.getException().getMessage());

                }
            }
        });
    }

    private void loadUserData(String userId) {
        firebaseRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hideProgressDialog();
                UserData modelUser = dataSnapshot.getValue(UserData.class);
                String userId = modelUser.getId();
                String name = modelUser.getUsername();
                String email = modelUser.getEmail();
                String status = modelUser.getLoggedIn();
                String qr = modelUser.getQrCode();
                String password = modelUser.getPassword();
                UserData data = new UserData(userId, name, email, qr, password, status);
                prefrences.saveUserData(data);
                prefrences.saveBooleanPrefernce(Prefrences.IS_LOGGED_IN, true);
                Intent intent = new Intent(requireContext(), LoginToGmail.class);
                requireActivity().startActivity(intent);
                requireActivity().finish();
                System.out.println("Database is :" + dataSnapshot.getChildren());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}