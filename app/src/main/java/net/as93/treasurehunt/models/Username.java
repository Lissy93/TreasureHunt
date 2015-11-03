package net.as93.treasurehunt.models;

import android.app.Activity;

/**
 * Class manages the username stored in SharedPreferences
 * Inherits SharedPreference settings from Settings class
 */
public class Username extends Settings {

    String userNameKey = PACKAGE_NAME +".username"; // The name of the sharedPrefs key for username
    String defaultUsername = "Guest";               // The default username for first-time use


    /**
     * Initializer
     * Sets the value of prefs, from the reference to an Activity passed as param
     * @param activity the reference to calling activity
     */
    public Username(Activity activity) {
        super(activity);
    }


    /**
     * Fetches the current username from the shared preferences
     * If not available will return a static value
     * @return String value for username
     */
    public String fetchUsername(){
        return prefs.getString(userNameKey, defaultUsername);
    }


    /**
     * Updates the username in the SharedPreferences
     * Validates the username prior to the update
     * Only makes update if username satisfies validation criteria
     * @param username String value of potential username
     * @return Boolean success status (true if insertion success, else false)
     */
    public Boolean updateUsername(String username) {
        if(checkUsernameIsValid(username)) {
            prefs.edit().putString(userNameKey, username).apply();
            return true;
        }
        else{
            return false;
        }
    }


    /**
     * Determines wheather of not the user is registered
     * @return Boolean true if registered, false if not
     */
    public Boolean isUserRegistered(){
        return !fetchUsername().equals(defaultUsername);
    }


    /**
     * Checks if the potential username meets the validation criteria
     * @param username String value of suggested new username
     * @return Boolean (true if valid, false if invalid)
     */
    private Boolean checkUsernameIsValid(String username){
        if(username.length() == 0) { return false; }    // Username is too short
        if(username.length() > 50) { return false; }    // Username is too long
        return true; // Username seems legit
    }

}
