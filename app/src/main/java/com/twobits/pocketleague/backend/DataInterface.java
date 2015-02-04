package com.twobits.pocketleague.backend;

import com.j256.ormlite.dao.Dao;
import com.twobits.pocketleague.db.DatabaseHelper;
import com.twobits.pocketleague.db.tables.Game;
import com.twobits.pocketleague.db.tables.GameMember;
import com.twobits.pocketleague.db.tables.Player;
import com.twobits.pocketleague.db.tables.Session;
import com.twobits.pocketleague.db.tables.SessionMember;
import com.twobits.pocketleague.db.tables.Team;
import com.twobits.pocketleague.db.tables.TeamMember;
import com.twobits.pocketleague.db.tables.Venue;
import com.twobits.pocketleague.gameslibrary.GameType;

public interface DataInterface {
    public DatabaseHelper getHelper();

    public Dao<Game, Long> getGameDao();

    public Dao<GameMember, Long> getGameMemberDao();

    public Dao<Player, Long> getPlayerDao();

    public Dao<Session, Long> getSessionDao();

    public Dao<SessionMember, Long> getSessionMemberDao();

    public Dao<Team, Long> getTeamDao();

    public Dao<TeamMember, Long> getTeamMemberDao();

    public Dao<Venue, Long> getVenueDao();

    public String getPreference(String pref_name, String pref_default);

    public void setPreference(String pref_name, String pref_value);

    public GameType getCurrentGameType();

    public void setCurrentGameType(GameType gametype);
}
