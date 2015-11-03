package net.as93.treasurehunt.models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public abstract class Settings {

    // The package name of this application
    String PACKAGE_NAME = "net.as93.treasurehunt";

    // Uninitialised instance of SharedPreferences
    SharedPreferences prefs;


    /**
     * Initializer
     * Sets the value of prefs, from the reference to an Activity passed as param
     * @param activity the reference to calling activity
     */
    public Settings(Activity activity){
        prefs = activity.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
    }


    /**
     * Returns an instance of SharedPreferences
     * @return SharedPreferences reference
     */
    public SharedPreferences getPrefsObject(){
        return this.prefs;
    }

}
