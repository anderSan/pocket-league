package com.twobits.pocketleague;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.couchbase.lite.Database;
import com.twobits.pocketleague.backend.DataInterface;
import com.twobits.pocketleague.db.DatabaseHelper;
import com.twobits.pocketleague.gameslibrary.GameType;

public abstract class DataInterfaceActivity extends ActionBarActivity implements DataInterface {
    protected String LOGTAG = getClass().getSimpleName();

    public static final String APP_PREFS = "PocketLeaguePreferences";
    private SharedPreferences settings;
    private SharedPreferences.Editor prefs_editor;

    Database database;
    private DatabaseHelper databaseHelper = null;


    public Database getDatabase() {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(this);
            database = databaseHelper.getDatabase();
        }
        return database;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
            databaseHelper = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = this.getSharedPreferences(APP_PREFS, 0);
        prefs_editor = settings.edit();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    public String getPreference(String pref_name, String pref_default) {
        return settings.getString(pref_name, pref_default);
    }

    public void setPreference(String pref_name, String pref_value) {
        prefs_editor.putString(pref_name, pref_value);
        prefs_editor.commit();
    }

    public GameType getCurrentGameType() {
        return GameType.valueOf(getPreference("currentGameType", GameType.UNDEFINED.name()));
    }

    public void setCurrentGameType(GameType gametype) {
        setPreference("currentGameType", gametype.name());
    }

    public void log(String msg) {
        Log.i(LOGTAG, msg);
    }

    public void logd(String msg) {
        Log.d(LOGTAG, msg);
    }

    public void loge(String msg, Exception e) {
        Log.e(LOGTAG, msg + ": " + e.getMessage());
    }
}