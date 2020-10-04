package com.example.perkblind;

import android.app.FragmentManager;
import android.os.Bundle;

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


public class RegisterFragment extends Fragment {
    String name;
    String email;
    String password;
    String confirmPass;
    EditText NameET;
    EditText MailET;
    EditText PassET;
    EditText CnfrmPassET;

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

        NameET = v.findViewById(R.id.NameET);
        MailET = v.findViewById(R.id.MailET);
        PassET = v.findViewById(R.id.PassET);
        CnfrmPassET = v.findViewById(R.id.CnfrmPassET);
        final Button regbtn = v.findViewById(R.id.regbtn);
        CnfrmPassET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
            LoginFragment frag = new LoginFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.replacer, frag);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    };

    private boolean validate() {
        boolean bool = false;
        name = NameET.getText().toString();
        password = PassET.getText().toString();
        confirmPass = CnfrmPassET.getText().toString();
        email = MailET.getText().toString();
        if (name != null && email != null && !name.isEmpty() && !email.isEmpty() && password != null && confirmPass != null && !password.isEmpty() && !confirmPass.isEmpty() && password.equalsIgnoreCase(confirmPass)) {
            bool = true;
        }
        return bool;
    }
}