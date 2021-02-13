package com.fyp.perkblind.gmail;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fyp.perkblind.HelperClass;
import com.fyp.perkblind.Prefrences;
import com.fyp.perkblind.R;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import static com.fyp.perkblind.HelperClass.REQUEST_AUTHORIZATION;
import static com.fyp.perkblind.HelperClass.REQUEST_GOOGLE_PLAY_SERVICES;

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
    HelperClass mUtils;
    LinearLayoutCompat lytParent;
    String[] SCOPES = {
            GmailScopes.GMAIL_LABELS,
            GmailScopes.GMAIL_COMPOSE,
            GmailScopes.GMAIL_INSERT,
            GmailScopes.GMAIL_MODIFY,
            GmailScopes.GMAIL_READONLY,
            GmailScopes.MAIL_GOOGLE_COM
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        refreshMessages = findViewById(R.id.refreshMessages);
        listMessages = findViewById(R.id.listMessages);
        lytParent = findViewById(R.id.lytParent);
        sharedPref = new Prefrences(Inbox.this);
        sharedPref.initPrefernce();
        mUtils = new HelperClass(Inbox.this);
        initAppBar("INBOX");
       // initGmailAPI();
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    getMessages();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    void getMessages(){
        Properties props = new Properties();
        //IMAPS protocol
        props.setProperty("mail.store.protocol", "imaps");
        //Set host address
        props.setProperty("mail.imaps.host", "imaps.gmail.com");
        //Set specified port
        props.setProperty("mail.imaps.port", "993");
        //Using SSL
        props.setProperty("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.imaps.socketFactory.fallback", "false");
        Session imapSession = Session.getInstance(props);
        Store store = null;
        try {
            store = imapSession.getStore("imaps");
            store.connect("imap.gmail.com", "wishakhn@gmail.com", "d0ntmesswithme.");
            Folder inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_WRITE);
            javax.mail.Message[] msgs =inbox.getMessages();
            System.out.println("You got your inbox"+inbox.getMessage(4));
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
    private void initGmailAPI() {
        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        mService = null;
        String accountName = "wishakhn";
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
        messageList = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(this, messageList);
        initView();
    }

    private void initView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.action_search);
        searchView.setQueryHint(getString(R.string.search));
        searchView.setSearchableInfo(searchManager != null ? searchManager.getSearchableInfo(getComponentName()) : null);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                messagesAdapter.getFilter().filter(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                messagesAdapter.getFilter().filter(newText);

                return true;
            }
        });

        refreshMessages.setColorSchemeResources(R.color.colorPrimary);
        refreshMessages.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isFetching && mUtils.isDeviceOnline()) {
                    getMessagesFromDB();
                } else
                    mUtils.showSnackbar(lytParent, getString(R.string.device_is_offline));
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listMessages.setLayoutManager(mLayoutManager);
        listMessages.setItemAnimator(new DefaultItemAnimator());
        listMessages.setAdapter(messagesAdapter);
     /*   if (!isFetching && mUtils.isDeviceOnline())
            new GetEmailsTask(false).execute();*/
    }

    private void getMessagesFromDB() {
        refreshMessages.setRefreshing(true);
        messageList.clear();
        messageList.addAll(SQLite.select().from(Message.class).queryList());
        messagesAdapter.notifyDataSetChanged();
        refreshMessages.setRefreshing(false);

        if (mUtils.isDeviceOnline())
            new GetEmailsTask(true).execute();
        else
            mUtils.showSnackbar(lytParent, getString(R.string.device_is_offline));
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

    @SuppressLint("StaticFieldLeak")
    private class GetEmailsTask extends AsyncTask<Void, Void, List<Message>> {

        private int itemCount = 0;
        private boolean clear;
        private Exception mLastError = null;

        GetEmailsTask(boolean clear) {
            this.clear = clear;
        }

        @Override
        protected List<Message> doInBackground(Void... voids) {
            isFetching = true;
            List<Message> messageListReceived = null;

            if (clear) {
                Delete.table(Message.class);
                pageToken = null;
            }

            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshMessages.setRefreshing(true);
                    }
                });
                String user = "me";
                String query = "in:inbox";
                ListMessagesResponse messageResponse = mService.users().messages().list(user).setQ(query).setMaxResults(20L).setPageToken(pageToken).execute();
                pageToken = messageResponse.getNextPageToken();

                messageListReceived = new ArrayList<>();
                List<com.google.api.services.gmail.model.Message> receivedMessages = messageResponse.getMessages();
                for (com.google.api.services.gmail.model.Message message : receivedMessages) {
                    com.google.api.services.gmail.model.Message actualMessage = mService.users().messages().get(user, message.getId()).execute();

                    Map<String, String> headers = new HashMap<>();
                    for (MessagePartHeader messagePartHeader : actualMessage.getPayload().getHeaders())
                        headers.put(
                                messagePartHeader.getName(), messagePartHeader.getValue()
                        );

                    Message newMessage = new Message(
                            actualMessage.getLabelIds(),
                            actualMessage.getSnippet(),
                            actualMessage.getPayload().getMimeType(),
                            headers,
                            actualMessage.getPayload().getParts(),
                            actualMessage.getInternalDate(),
                            mUtils.getRandomMaterialColor(),
                            actualMessage.getPayload()
                    );

                    newMessage.save();
                    messageListReceived.add(newMessage);

                    itemCount++;
                }
            } catch (Exception e) {
                Log.w("TAG_GMAIL", e);
                mLastError = e;
                cancel(true);
            }

            return messageListReceived;
        }

        @Override
        protected void onPostExecute(List<Message> output) {
            isFetching = false;

            if (output != null && output.size() != 0) {
                if (clear) {
                    messageList.clear();
                    messageList.addAll(output);
                    messagesAdapter.notifyDataSetChanged();
                } else {
                    int listSize = messageList.size();
                    messageList.addAll(output);
                    messagesAdapter.notifyItemRangeInserted(listSize, itemCount);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshMessages.setRefreshing(false);
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshMessages.setRefreshing(false);
                    }
                });
                mUtils.showSnackbar(lytParent, getString(R.string.fetch_failed));
            }
        }

        @Override
        protected void onCancelled() {
            isFetching = false;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refreshMessages.setRefreshing(false);
                }
            });
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            REQUEST_AUTHORIZATION);
                } else {
                    mUtils.showSnackbar(lytParent, getString(R.string.an_error_occurred));
                }
            } else {
                mUtils.showSnackbar(lytParent, getString(R.string.an_error_occurred));
            }
        }

    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

}