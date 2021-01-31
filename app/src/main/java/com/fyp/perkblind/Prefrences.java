package com.fyp.perkblind;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class Prefrences {

    private SharedPreferences sherdPrefs;
    private Context context;
    private final String SHARED_PREFERENCE_NAME = "PERK_PREFERNCES";
    private final int PREF_MODE = Context.MODE_PRIVATE;
    SharedPreferences.Editor editor;
    public static final String USER_ONLINE_KEY = "isUserOnline";
    public static final String LAUNCH_KEY = "serviceisRunning";
    public static final String FIRST_RUN = "isFirstRun";
    public static final String TTS_SPEAKER = "SpeakAllInstructions";
    public static final String IS_LOGGED_IN = "IsUserLogin";
    public static final String USER_DATA = "SaveUserData";
    static final String KEY_PREFERNCE_CLASS="KIsUserLoginEY_TARGET_PREFERENCE";


    public Prefrences(Context context) {
        this.context = context;
    }

    public void initPrefernce(){
        sherdPrefs = context.getSharedPreferences(SHARED_PREFERENCE_NAME,PREF_MODE);
    }

    public  void saveUserState(boolean state){
        editor  = sherdPrefs.edit();
        editor.putBoolean(USER_ONLINE_KEY,state);
        editor.apply();
    }
    public void saveBooleanPrefernce(String key, boolean value){
        editor  = sherdPrefs.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }
    public void saveIntegerPreference(String key, int value){
        editor  = sherdPrefs.edit();
        editor.putInt(key,value);
        editor.apply();
    }
    public boolean loadBooleanPrefernce(String key){
        return sherdPrefs.getBoolean(key,false);
    }
    public String loadStringPreference(String key){
        return  sherdPrefs.getString(key," ");
    }
    public int loadIntegerPrefernce(String key){
        return  sherdPrefs.getInt(key,0);
    }

    public void saveTargetClass( Class target){
        editor = sherdPrefs.edit();
        editor.putString(KEY_PREFERNCE_CLASS,target.toString());
        editor.apply();
    }
    public String getTargetClassName(){
        String class_str = sherdPrefs.getString(KEY_PREFERNCE_CLASS," ");
        String arr[] = class_str.split(" ");
        class_str = arr[1];
        return class_str;
    }

    public String getTargetName(){
        String class_str = sherdPrefs.getString(KEY_PREFERNCE_CLASS," ");
        String arr[] = class_str.split(" ");
        class_str = getClassName(arr[1]);
        return class_str;
    }
    private String getClassName(String s) {
        String arr_new[] = s.split("\\.");
        String className = arr_new[arr_new.length-1];
        return className;
    }

    public void saveUserData(UserData data){
        editor = sherdPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString(USER_DATA, json);
        editor.apply();
    }
    public UserData fetchUserData(){
        Gson gson = new Gson();
        String json = sherdPrefs.getString(USER_DATA, "");
        UserData user = gson.fromJson(json, UserData.class);
        return user;
    }
    public  void  removePrefernce(String key){
        editor.remove(key);
        editor.commit();
    }

}
