package com.example.perkblind;

import android.app.AlertDialog;
import android.view.View;

public interface ISpeechTextManager {
    void  initSpeechTextManager(Boolean showDialog);
    void  setSpeechListener(final AlertDialog dialog);
    void  getSpeechInput();
    void  speak(String text);
    void actOnCommand(String s,Class NextActivity);
    void moveToScreen(Class<?> target_class);
    void showWelcomeDialog();
}
