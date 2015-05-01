package info.andersonpa.pocketleague.backend;

import com.couchbase.lite.Database;
import info.andersonpa.pocketleague.gameslibrary.GameSubtype;
import info.andersonpa.pocketleague.gameslibrary.GameType;

public interface DataInterface {
    Database getDatabase();

    void deleteDatabase();

    String getPreference(String pref_name, String pref_default);

    void setPreference(String pref_name, String pref_value);

    GameType getCurrentGameType();

    GameSubtype getCurrentGameSubtype();

    void setCurrentGameSubtype(GameSubtype gamesubtype);
}
