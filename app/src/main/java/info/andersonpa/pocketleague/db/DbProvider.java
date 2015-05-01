package info.andersonpa.pocketleague.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import com.couchbase.lite.Database;
import info.andersonpa.pocketleague.PocketLeagueApp;
import info.andersonpa.pocketleague.db.tables.Game;
import info.andersonpa.pocketleague.db.tables.GameMember;

import java.util.List;

public class DbProvider extends ContentProvider {
    Database database;

    private static final String AUTHORITY = DbUris.AUTHORITY;
    public static final int ROUTE_GAME = 1;
    public static final int ROUTE_GAME_MEMBER = 2;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, "game/*", ROUTE_GAME);
        sUriMatcher.addURI(AUTHORITY, "game_member/*", ROUTE_GAME_MEMBER);
    }

    @Override
    public boolean onCreate() {
        database = ((PocketLeagueApp) getContext().getApplicationContext()).getDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] arg1, String arg2, String[] arg3, String arg4) {
        String id = uri.getLastPathSegment();
        String[] columnNames;
        MatrixCursor cursor;

        int uriMatch = sUriMatcher.match(uri);
        switch (uriMatch) {
            case ROUTE_GAME:
                // Return values for the game with given id.
                columnNames = new String[]{"id", "ruleset_id", "session_name", "venue_name",
                        "date_played"};
                cursor = new MatrixCursor(columnNames);
                Game g = Game.getFromId(database, id);

                cursor.addRow(new Object[]{g.getId(), g.getSession().getGameSubtype(),
                        g.getSession().getName(), g.getVenue().getName(), g.getDatePlayed()});
                break;
            case ROUTE_GAME_MEMBER:
                // Return values for each game member in game given by id.
                columnNames = new String[] {"team_id", "team_name"};
                cursor = new MatrixCursor(columnNames);
                g = Game.getFromId(database, id);

                List<GameMember> game_members = g.getMembers();
                for (GameMember gm : game_members) {
                    cursor.addRow(new Object[]{gm.getTeam().getId(), gm.getTeam().getName()});
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(this.getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String arg2, String[] arg3) {
        String id = uri.getLastPathSegment();

        int uriMatch = sUriMatcher.match(uri);
        switch (uriMatch) {
            case ROUTE_GAME:
                // Update values for the game given by id.
                Game g = Game.getFromId(database, id);
                g.setIsComplete(values.getAsBoolean("is_complete"));

                String key = "t%s_score";
                List<GameMember> members = g.getMembers();
                for (int ii = 0; ii < members.size(); ii++) {
                    String new_key = String.format(key, String.valueOf(ii+1));
                    members.get(ii).setScore(values.getAsInteger(new_key));
                }
                g.update();

                break;
            case ROUTE_GAME_MEMBER:
                // Update values for the game member given by id.
//                GameMember gm = gmDao.queryForId(id);
//                gm.setScore(values.getAsInteger("score"));
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return 1; // number of affected rows
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ROUTE_GAME_MEMBER:
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
}
