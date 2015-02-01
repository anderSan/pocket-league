package com.twobits.pocketleague.backend;

import com.j256.ormlite.dao.Dao;
import com.twobits.pocketleague.db.tables.Game;
import com.twobits.pocketleague.db.tables.GameMember;
import com.twobits.pocketleague.db.tables.Player;
import com.twobits.pocketleague.db.tables.Session;
import com.twobits.pocketleague.db.tables.SessionMember;
import com.twobits.pocketleague.db.tables.Team;
import com.twobits.pocketleague.db.tables.TeamMember;
import com.twobits.pocketleague.db.tables.Venue;

public interface DaoInterface {
    public Dao<Game, Long> getGameDao();

    public Dao<GameMember, Long> getGameMemberDao();

    public Dao<Player, Long> getPlayerDao();

    public Dao<Session, Long> getSessionDao();

    public Dao<SessionMember, Long> getSessionMemberDao();

    public Dao<Team, Long> getTeamDao();

    public Dao<TeamMember, Long> getTeamMemberDao();

    public Dao<Venue, Long> getVenueDao();
}
