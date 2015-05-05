package info.andersonpa.pocketleague;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.couchbase.lite.Database;
import info.andersonpa.pocketleague.backend.DataInterface;
import info.andersonpa.pocketleague.gameslibrary.GameSubtype;
import info.andersonpa.pocketleague.gameslibrary.GameType;

public abstract class DataInterfaceActivity extends ActionBarActivity implements DataInterface {
    protected String LOGTAG = getClass().getSimpleName();

    private SharedPreferences settings;

    public Database getDatabase() {
        return ((PocketLeagueApp) getApplication()).getDatabase();
    }

    public void deleteDatabase() {
        ((PocketLeagueApp) getApplication()).deleteDatabase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = PreferenceManager.getDefaultSharedPreferences(this);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    public GameType getCurrentGameType() {
        return getCurrentGameSubtype().toGameType();
    }

    public GameSubtype getCurrentGameSubtype() {
        return GameSubtype.valueOf(settings.getString("currentGameSubtype",
                GameSubtype.UNDEFINED.name()));
    }

    public void setCurrentGameSubtype(GameSubtype gamesubtype) {
        settings.edit().putString("currentGameSubtype", gamesubtype.name()).commit();
    }

    public boolean getIsDevMode() {
        return settings.getBoolean("dev_mode", false);
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