package com.twobits.pocketleague.backend;

import com.couchbase.lite.Database;
import com.twobits.pocketleague.gameslibrary.GameSubtype;
import com.twobits.pocketleague.gameslibrary.GameType;

public interface DataInterface {
    public Database getDatabase();

    public String getPreference(String pref_name, String pref_default);

    public void setPreference(String pref_name, String pref_value);

    public GameType getCurrentGameType();

    public GameSubtype getCurrentGameSubtype();

    public void setCurrentGameSubtype(GameSubtype gamesubtype);
}
