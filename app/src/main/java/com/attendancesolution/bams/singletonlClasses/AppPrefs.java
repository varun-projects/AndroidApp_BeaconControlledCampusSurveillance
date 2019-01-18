package com.attendancesolution.bams.singletonlClasses;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Akshay on 11-May-16.
 */
public class AppPrefs {

    public static final String STATE_PREFS = "STATE_PREFS";

    /*/ State Pref Variables /*/
    private String isServiceStarted = "isServiceStarted";
    private String isNewLink = "isNewLink";
    private String isNewMessage = "isNewMessage";

    /*/ Preferences Variables /*/
    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;
    Context context;

    /*/ Costructor /*/
    public AppPrefs(Context context) {
        this.context = context;
    }

    /*/ isNewLink /*/

    public void setIsNewLink(boolean isNewLink) {
        this.appSharedPrefs = context.getSharedPreferences(STATE_PREFS, Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putBoolean(this.isNewLink, isNewLink).commit();
    }

    public boolean getIsNewLink() {
        this.appSharedPrefs = context.getSharedPreferences(STATE_PREFS, Activity.MODE_PRIVATE);
        return appSharedPrefs.getBoolean(this.isNewLink, false);
    }

    /*/ isNewMessage /*/

    public void setIsNewMessage(boolean isNewMessage) {
        this.appSharedPrefs = context.getSharedPreferences(STATE_PREFS, Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putBoolean(this.isNewMessage, isNewMessage).commit();
    }

    public boolean getIsNewMessage() {
        this.appSharedPrefs = context.getSharedPreferences(STATE_PREFS, Activity.MODE_PRIVATE);
        return appSharedPrefs.getBoolean(this.isNewMessage, false);
    }

    /*/ isNewMessage /*/

    public void setIsServiceStarted(boolean isServiceStarted) {
        this.appSharedPrefs = context.getSharedPreferences(STATE_PREFS, Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putBoolean(this.isServiceStarted, isServiceStarted).commit();
    }

    public boolean getIsServiceStarted() {
        this.appSharedPrefs = context.getSharedPreferences(STATE_PREFS, Activity.MODE_PRIVATE);
        return appSharedPrefs.getBoolean(this.isServiceStarted, false);
    }


}
