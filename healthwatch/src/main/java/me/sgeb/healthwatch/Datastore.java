package me.sgeb.healthwatch;

import android.content.Context;
import android.content.SharedPreferences;

import me.sgeb.healthwatch.hgclient.model.WeightSetFeed;

public class Datastore {
    private static final String PREFS_DATASTORE_FILE = "entities.datastore";
    private final SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;

    public Datastore(Context context) {
        prefs = context.getSharedPreferences(PREFS_DATASTORE_FILE, Context.MODE_PRIVATE);
    }

    public Datastore updateWithRemoteWeightSetFeed(WeightSetFeed weightSetFeed) {
        edit();
        return this;
    }

    private void edit() {
        if (prefsEditor == null) {
            prefsEditor = prefs.edit();
        }
    }

    public void apply() {
        if (prefsEditor != null) {
            prefsEditor.apply();
            prefsEditor = null;
        }
    }
}
