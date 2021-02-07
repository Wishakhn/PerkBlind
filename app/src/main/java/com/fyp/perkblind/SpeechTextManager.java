package com.fyp.perkblind;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class SpeechTextManager implements ISpeechTextManager {
    TextToSpeech tts;
    TextToSpeech tts_alert;
    String tts_str = "";
    Handler handler;
    Context context;
    Activity activity;
    Boolean showDialog = false;
    Boolean userSpeech = false;
    Boolean DismissIO = false;
    AlertDialog dialog;
    AlertDialog dialog2;

    public SpeechTextManager(Context activity, Boolean showDialog) {
        this.context = activity;
        this.showDialog = showDialog;
        this.activity = (Activity) activity;
        handler = new Handler();
        setTts_str(  context.getResources().getString(R.string.welcome_note));
        initSpeechTextManager(showDialog);
    }


    @Override
    public void initSpeechTextManager(final Boolean showDialog) {
        tts_alert = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = tts_alert.setLanguage(Locale.getDefault());
                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                        if (showDialog) {
                                   showWelcomeDialog();
                        }
                    }
                    Log.i("TTS", "Initialization success.");
                } else {
                    Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = tts.setLanguage(Locale.getDefault());
                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                } else {
                    Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void showWelcomeDialog() {
        showDialog = true;
        AlertDialog.Builder welcomenote = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View v = inflater.inflate(R.layout.welcomedialog, null);
        welcomenote.setView(v);
        dialog = welcomenote.create();
        dialog.show();
        dialog.setCancelable(false);
        TextView notetext = v.findViewById(R.id.notetext);
        notetext.setText(getTts_str());
        setSpeechListener(dialog);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                speakAlert(getTts_str());
            }
        }, 1000);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void StopTTS() {
        tts.stop();
        tts.shutdown();
        tts_alert.stop();
        tts_alert.shutdown();
    }

    @Override
    public void pauseTTS() {
        if (tts.isSpeaking()) tts.stop();
    if (tts_alert.isSpeaking())tts_alert.stop();
    }

    public void showOptionDialog(String text) {
        showDialog = true;
        AlertDialog.Builder welcomenote = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View v = inflater.inflate(R.layout.welcomedialog, null);
        welcomenote.setView(v);
        dialog2 = welcomenote.create();
        dialog2.show();
        dialog2.setCancelable(false);
        TextView notetext = v.findViewById(R.id.notetext);
        setTts_str("You have selected "+text+" option to proceed please speak Next or to decline please speak Dismiss. Thank you.");
        notetext.setText(getTts_str());
        ttsListner();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                speak(getTts_str());
            }
        }, 1000);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void ttsListner(){
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

            }

            @Override
            public void onDone(String s) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (showDialog) dialog2.dismiss();
                        getSpeechInput();
                    }
                });
            }

            @Override
            public void onError(String s) {

            }
        });
    }
    @Override
    public void setSpeechListener(final AlertDialog dialog) {
        tts_alert.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

            }

            @Override
            public void onDone(String s) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onError(String s) {

            }
        });
    }


    @Override
    public void getSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            activity.startActivityForResult(intent, 10);
        } else {
            Toast.makeText(context, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void speak(String text) {
        float pitch = (float) 0.0f;
        if (pitch < 0.1) pitch = 1.0f;
        float speed = (float) 0.0f;
        if (speed < 0.1) speed = 1.0f;

        tts.setPitch(pitch);
        tts.setSpeechRate(speed);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    public void speakAlert(String text) {
        float pitch = (float) 0.0f;
        if (pitch < 0.1) pitch = 1.0f;
        float speed = (float) 0.0f;
        if (speed < 0.1) speed = 1.0f;

        tts_alert.setPitch(pitch);
        tts_alert.setSpeechRate(speed);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts_alert.speak(text, TextToSpeech.QUEUE_FLUSH, null, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
        } else {
            tts_alert.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void actOnCommand(String s, Class NextActivity) {
        if (s.equalsIgnoreCase("next") || s.contains("next")) {
            Toast.makeText(context, "You have commanded to move to "+NextActivity, Toast.LENGTH_SHORT).show();
            Intent startnext = new Intent(context, NextActivity);
            context.startActivity(startnext);
        } else {
            if (showDialog) {
                dialog2.dismiss();
                if (tts.isSpeaking()) {
                    tts.stop();
                }
            }
        }
    }

    @Override
    public void moveToScreen(Class<?> target_class) {
        Intent intent = new Intent(context, target_class);
        context.startActivity(intent);
    }

    public String getTts_str() {
        return tts_str;
    }

    public void setTts_str(String tts_str) {
        this.tts_str = tts_str;
    }

    public Boolean getUserSpeech() {
        return userSpeech;
    }

    public void setUserSpeech(Boolean userSpeech) {
        this.userSpeech = userSpeech;
    }

    public Boolean getDismissIO() {
        return DismissIO;
    }

    public void setDismissIO(Boolean dismissIO) {
        DismissIO = dismissIO;
    }
}
