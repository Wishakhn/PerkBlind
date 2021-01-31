package com.fyp.perkblind.gmail;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fyp.perkblind.HelperClass;
import com.fyp.perkblind.Prefrences;
import com.fyp.perkblind.R;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.Gmail;

import java.util.Arrays;

import static com.fyp.perkblind.HelperClass.PREF_ACCOUNT_NAME;
import static com.fyp.perkblind.HelperClass.SCOPES;

public class SendEmail extends AppCompatActivity {

    GoogleAccountCredential mCredential;
    Gmail mService;
    Prefrences sharedPref;
    HelperClass mUtils;
    TextView RecieverName, senderCCText, senderSubjText, senderContentText;
    Button send, reset;
    boolean isSending = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);
        initViews();
        initAppBar("COMPOSE EMAIL");
    }

    private void initViews() {
        sharedPref = new Prefrences(SendEmail.this);
        sharedPref.initPrefernce();
        mUtils = new HelperClass(SendEmail.this);
        iniGmail();
        RecieverName = findViewById(R.id.RecieverName);
        senderCCText = findViewById(R.id.senderCCText);
        senderSubjText = findViewById(R.id.senderSubjText);
        senderContentText = findViewById(R.id.senderContentText);
        send = findViewById(R.id.send);
        reset = findViewById(R.id.reset);
    }

    private void iniGmail() {
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        mService = null;
        String accountName = sharedPref.loadStringPreference(PREF_ACCOUNT_NAME);
        if (accountName != null) {
            mCredential.setSelectedAccountName(accountName);

            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.gmail.Gmail.Builder(
                    transport, jsonFactory, mCredential)
                    .setApplicationName("MailBox App")
                    .build();
        } else {
            finish();
        }
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
}