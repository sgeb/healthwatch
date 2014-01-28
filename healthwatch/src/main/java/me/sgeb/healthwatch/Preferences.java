package me.sgeb.healthwatch;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private static final String PREFS_AUTH_FILE = "HealthWatchAuth";
    private static final String PREFS_AUTH_ACCESS_TOKEN = "AccessToken";
    private final Context context;

    public Preferences(Context context) {
        this.context = context;
    }

    public void setAuthAccessToken(String accessToken) {
        getSharedPreferencesAuth().edit().putString(PREFS_AUTH_ACCESS_TOKEN, accessToken).apply();
    }

    public boolean hasAuthAccessToken() {
        return getSharedPreferencesAuth().getString(PREFS_AUTH_ACCESS_TOKEN, null) != null;
    }

    private SharedPreferences getSharedPreferencesAuth() {
        return context.getSharedPreferences(PREFS_AUTH_FILE, Context.MODE_PRIVATE);
    }
}
