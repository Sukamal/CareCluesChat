package careclues.careclueschat.storage.preference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import careclues.careclueschat.util.AppConstant;

public class AppPreference {

    private SharedPreferences preferences;
    private SharedPreferences.Editor appEditor;

    public AppPreference(Context context){
        this.preferences = context.getSharedPreferences(AppConstant.PREFERENCE_NAME, Activity.MODE_PRIVATE);
        this.appEditor = preferences.edit();
    }

    public void clearAllPreferences(){
        appEditor.clear();
        appEditor.commit();
    }

    public void removePreferences(String key) {
        appEditor.remove(key);
        appEditor.commit();
    }

    public void saveBooleanPref(String touple, boolean data) {

        appEditor.putBoolean(touple, data);
        appEditor.commit();
    }

    public boolean getBooleanPref(String touple) {

        return preferences.getBoolean(touple, false);
    }

    public void saveStringPref(String key, String value) {

        appEditor.putString(key, value);
        appEditor.commit();
    }

    public String getStringPref(String Key) {

        return preferences.getString(Key, null);
    }

    public void saveIntPref(String key, int value) {
        appEditor.putInt(key, value);
        appEditor.commit();
    }


}
