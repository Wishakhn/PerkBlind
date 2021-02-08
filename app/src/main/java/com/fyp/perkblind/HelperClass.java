package com.fyp.perkblind;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.api.services.gmail.GmailScopes;

import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

public class HelperClass {
    static ProgressDialog pDialog;
    public static final int REQUEST_PERMISSION_CODE = 1002;
    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1004;
    public static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    public static final int REQUEST_SPEECH_INPUT = 10;

    public static final String TAG = "PerkBlindApp";
    public static final String[] SCOPES = {GmailScopes.MAIL_GOOGLE_COM};
    public static final String PREF_ACCOUNT_NAME = "accountName";

    private Context mContext;

    public HelperClass(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public boolean isDeviceOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public int getRandomMaterialColor() {
        int returnColor = Color.GRAY;
        int arrayId = mContext.getResources().getIdentifier("material_colors", "array", mContext.getPackageName());

        if (arrayId != 0) {
            TypedArray colors = mContext.getResources().obtainTypedArray(arrayId);

            Random random = new Random();
            int index = random.nextInt(colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }

    public String timestampToDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        return DateFormat.format("dd/MM/yyyy", cal).toString();
    }

    public boolean isValidEmail(String email) {
        final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }

    public static void displayProgressDialog(Context context, String msg) {
        pDialog = new ProgressDialog(context, R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage(msg);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public static void hideProgressDialog() {
        pDialog.dismiss();
    }

    public static void makeAlert(Context context, String title, String message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton(context.getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                }
            });
            builder.show();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkCameraPermissions(Context context) {
        int writeExternalResults = ContextCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE");
        int checkSelfPermission = ContextCompat.checkSelfPermission(context, "android.permission.READ_EXTERNAL_STORAGE");
        int checkCameraPermission = ContextCompat.checkSelfPermission(context, "android.permission.CAMERA");
        return writeExternalResults == 0 && checkCameraPermission == 0 && checkSelfPermission == 0;
    }

    public static void startEditing(EditText inputEditText, Context context) {
        inputEditText.requestFocus();
        inputEditText.setFocusableInTouchMode(true);
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(inputEditText, InputMethodManager.SHOW_FORCED);
    }

    public static void stopEditing(EditText stopEdittext, Context contxt) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                contxt.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(stopEdittext.getWindowToken(), 0);
    }

    public static String  composeTextToEmail(String email){
        String spacedEmail= email.replace(" ","");
        String composedEmail ="";
        String keyOne ="attherateof";
        String keyTwo ="attherate";
        String keyThree ="at";
        if (spacedEmail.contains(keyOne)){
            composedEmail = spacedEmail.replace(keyOne,"@");
        }
        else if (spacedEmail.contains(keyTwo)){
            composedEmail = spacedEmail.replace(keyTwo,"@");
        }
        else if(spacedEmail.contains(keyThree)){
            composedEmail = spacedEmail.replace(keyThree,"@");
        }
        composedEmail = composedEmail.replace("dot",".");
        return composedEmail;

    }
}
