package com.fyp.perkblind;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

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

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        final Button regbtn = v.findViewById(R.id.regbtn);
        ConfirmPassET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (validate()) {
                        regbtn.setEnabled(true);
                    }
                }
                return false;
            }
        });
        regbtn.setOnClickListener(clickListner);

        return v;
    }

    View.OnClickListener clickListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (validate()) {
                displayProgressDialog(requireContext(), "Signing you UP..");
                register(name, email, password);
            }
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
    private void register(final String uname, String email, String pass) {
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser fUser = auth.getCurrentUser();
                    assert fUser != null;
                    String userId = fUser.getUid();
                    System.out.println("User ID is "+userId);
                    firebaseRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                    HashMap<String,String> hashmap = new HashMap<String, String>();
                    hashmap.put("id",userId);
                    hashmap.put("username",uname);
                    firebaseRef.setValue(hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                hideProgressDialog();
                                moveToLogging();
                            }
                        }
                    });

                }
                else {
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
}