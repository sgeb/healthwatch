package me.sgeb.healthwatch;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {
    private static final String PREFS_AUTH_ACCESS_TOKEN = "auth_access_token";
    private SharedPreferences defaultSharedPreferences;

    public Preferences(Context context) {
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setAuthAccessToken(String accessToken) {
        defaultSharedPreferences.edit().putString(PREFS_AUTH_ACCESS_TOKEN, accessToken).apply();
    }

    public boolean hasAuthAccessToken() {
        return getAuthAccessToken() != null;
    }

    public String getAuthAccessToken() {
        return defaultSharedPreferences.getString(PREFS_AUTH_ACCESS_TOKEN, null);
    }

}
