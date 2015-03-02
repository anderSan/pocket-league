package com.twobits.pocketleague;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.twobits.pocketleague.AboutPage;
import com.twobits.pocketleague.DbSettings;
import com.twobits.pocketleague.Detail_Player;
import com.twobits.pocketleague.Detail_Team;
import com.twobits.pocketleague.Detail_Venue;
import com.twobits.pocketleague.List_GameTypes;
import com.twobits.pocketleague.List_Players;
import com.twobits.pocketleague.List_Sessions;
import com.twobits.pocketleague.List_Teams;
import com.twobits.pocketleague.List_Venues;
import com.twobits.pocketleague.Modify_Player;
import com.twobits.pocketleague.Modify_Session;
import com.twobits.pocketleague.Modify_Team;
import com.twobits.pocketleague.Modify_Venue;
import com.twobits.pocketleague.Preferences;
import com.twobits.pocketleague.Quick_Game;
import com.twobits.pocketleague.R;
import com.twobits.pocketleague.backend.DataInterface;
import com.twobits.pocketleague.db.DatabaseHelper;
import com.twobits.pocketleague.db.tables.Game;
import com.twobits.pocketleague.db.tables.GameMember;
import com.twobits.pocketleague.db.tables.Player;
import com.twobits.pocketleague.db.tables.Session;
import com.twobits.pocketleague.db.tables.SessionMember;
import com.twobits.pocketleague.db.tables.Team;
import com.twobits.pocketleague.db.tables.TeamMember;
import com.twobits.pocketleague.db.tables.Venue;
import com.twobits.pocketleague.enums.SessionType;
import com.twobits.pocketleague.gameslibrary.GameType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class DataInterfaceActivity extends ActionBarActivity implements DataInterface {
    public static final String APP_PREFS = "PocketLeaguePreferences";
    private SharedPreferences settings;
    private SharedPreferences.Editor prefs_editor;

    private DatabaseHelper databaseHelper = null;
    Dao<Game, Long> gDao;
    Dao<GameMember, Long> gmDao;
    Dao<Player, Long> pDao;
    Dao<Session, Long> sDao;
    Dao<SessionMember, Long> smDao;
    Dao<Team, Long> tDao;
    Dao<TeamMember, Long> tmDao;
    Dao<Venue, Long> vDao;

    public Dao<Game, Long> getGameDao(){
        return gDao;
    }

    public Dao<GameMember, Long> getGameMemberDao(){
         return gmDao;
    }

    public Dao<Player, Long> getPlayerDao(){
        return pDao;
    }

    public Dao<Session, Long> getSessionDao(){
        return sDao;
    }

    public Dao<SessionMember, Long> getSessionMemberDao(){
        return smDao;
    }

    public Dao<Team, Long> getTeamDao(){
        return tDao;
    }

    public Dao<TeamMember, Long> getTeamMemberDao(){
        return tmDao;
    }

    public Dao<Venue, Long> getVenueDao(){
        return vDao;
    }

    public DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            gDao = getHelper().getGameDao();
            gmDao = getHelper().getGameMemberDao();
            pDao = getHelper().getPlayerDao();
            sDao = getHelper().getSessionDao();
            smDao = getHelper().getSessionMemberDao();
            tDao = getHelper().getTeamDao();
            tmDao = getHelper().getTeamMemberDao();
            vDao = getHelper().getVenueDao();
        } catch (SQLException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

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
}