package com.fyp.perkblind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.Gmail;

import java.util.Arrays;
import java.util.List;

import static com.fyp.perkblind.HelperClass.PREF_ACCOUNT_NAME;
import static com.fyp.perkblind.HelperClass.SCOPES;

public class Inbox extends AppCompatActivity {
    SwipeRefreshLayout refreshMessages;
    RecyclerView listMessages;
    List<Message> messageList;
    MessagesAdapter messagesAdapter;
    String pageToken = null;
    boolean isFetching = false;
    GoogleAccountCredential mCredential;
    Gmail mService;
    Prefrences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        refreshMessages = findViewById(R.id.refreshMessages);
        listMessages = findViewById(R.id.listMessages);
        sharedPref = new Prefrences(Inbox.this);
        sharedPref.initPrefernce();
        initAppBar("INBOX");
        initGmailInbox();
    }

    private void initGmailInbox() {
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
                    .setApplicationName("PerkBlind App")
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