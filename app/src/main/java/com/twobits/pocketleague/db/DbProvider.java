package com.twobits.pocketleague.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.twobits.pocketleague.db.tables.Game;
import com.twobits.pocketleague.db.tables.GameMember;
import com.twobits.pocketleague.db.tables.Session;
import com.twobits.pocketleague.db.tables.Team;
import com.twobits.pocketleague.db.tables.Venue;

import java.sql.SQLException;
import java.util.List;

public class DbProvider extends ContentProvider {
    private DatabaseHelper dbhelper;
    private RuntimeExceptionDao<Game, Long> gDao;
    private RuntimeExceptionDao<GameMember, Long> gmDao;
    private RuntimeExceptionDao<Session, Long> sDao;
    private RuntimeExceptionDao<Team, Long> tDao;
    private RuntimeExceptionDao<Venue, Long> vDao;

    private static final String AUTHORITY = DbUris.AUTHORITY;
    public static final int ROUTE_GAME = 1;
    public static final int ROUTE_GAME_MEMBERS = 2;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, "games/*", ROUTE_GAME);
        sUriMatcher.addURI(AUTHORITY, "game_members/*", ROUTE_GAME_MEMBERS);
    }

    @Override
    public boolean onCreate() {
        dbhelper = OpenHelperManager.getHelper(getContext(), DatabaseHelper.class);
        gDao = dbhelper.getRuntimeExceptionDao(Game.class);
        gmDao = dbhelper.getRuntimeExceptionDao(GameMember.class);
        sDao = dbhelper.getRuntimeExceptionDao(Session.class);
        tDao = dbhelper.getRuntimeExceptionDao(Team.class);
        vDao = dbhelper.getRuntimeExceptionDao(Venue.class);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] arg1, String arg2, String[] arg3, String arg4) {
        long id = Long.valueOf(uri.getLastPathSegment());
        String[] columnNames;
        MatrixCursor cursor = null;

        int uriMatch = sUriMatcher.match(uri);
        switch (uriMatch) {
            case ROUTE_GAME:
                // Return values for the game with given id.
                columnNames = new String[]{"id", "id_in_session", "ruleset_id", "session_name",
                        "venue_name", "date_played", "is_complete"};
                cursor = new MatrixCursor(columnNames);
                Game g = gDao.queryForId(id);
                sDao.refresh(g.getSession());
                vDao.refresh(g.getVenue());

                cursor.addRow(new Object[]{g.getId(), g.getIdInSession(),
                        g.getSession().getGameSubtype(), g.getSession().getSessionName(),
                        g.getVenue().getName(), g.getDatePlayed(), g.getIsComplete()});
                break;
            case ROUTE_GAME_MEMBERS:
                // Return values for each game member in game given by id.
                columnNames = new String[] {"id", "team_id", "team_name"};
                cursor = new MatrixCursor(columnNames);
                try {
                    List<GameMember> game_members =
                            gmDao.queryBuilder().where().eq(GameMember.GAME, id).query();

                    for (GameMember gm : game_members) {
                        tDao.refresh(gm.getTeam());
                        cursor.addRow(new Object[]{gm.getId(), gm.getTeam().getId(),
                                gm.getTeam().getTeamName()});
                    }
                } catch (SQLException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(this.getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ROUTE_GAME_MEMBERS:
                return DbUris.CONTENT_GAME_MEMBERS;
            case ROUTE_GAME:
                return DbUris.CONTENT_GAME;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int delete(Uri arg0, String arg1, String[] arg2) {
        return 0;
    }

    @Override
    public Uri insert(Uri arg0, ContentValues arg1) {
        return null;
    }

    @Override
    public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
        return 0;
    }
}
