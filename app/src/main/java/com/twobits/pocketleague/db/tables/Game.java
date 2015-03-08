package com.twobits.pocketleague.db.tables;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Game extends CouchDocumentBase {
    public static final String TYPE = "game";
    public static final String ID_IN_SESSION = "id_in_session";
    public static final String SESSION = "session_id";
    public static final String VENUE = "venue_id";
    public static final String DATE_PLAYED = "date_played";
    public static final String IS_COMPLETE = "is_complete";
    public static final String IS_TRACKED = "is_tracked";
    public static final String MEMBERS = "member_ids";

    // Constructors
    public Game(Session session, long id_in_session, List<Team> members, Venue venue,
                boolean is_tracked) {
        content.put("type", TYPE);
        content.put(SESSION, session);
        content.put(ID_IN_SESSION, id_in_session);
        content.put(MEMBERS, members);
        content.put(VENUE, venue);
        content.put(DATE_PLAYED, new Date());
        content.put(IS_COMPLETE, false);
        content.put(IS_TRACKED, is_tracked);
    }

    public Game(Database database, Session session, long id_in_session, List<Team> members,
                Venue venue, boolean is_tracked) {
        this(session, id_in_session, members, venue, is_tracked);
        createDocument(database);
    }

    private Game(Document document) {
        super(document);
    }

    // Static database methods
    public static Game getFromId(Database database, String id) {
        Document document = database.getDocument(id);
        return new Game(document);
    }

    // Other methods
    public long getIdInSession() {
        return (long) content.get(ID_IN_SESSION);
    }

    public Session getSession() {
        return (Session) content.get(SESSION);
    }

    public Venue getVenue() {
        return (Venue) content.get(VENUE);
    }

    public Date getDatePlayed() {
        return (Date) content.get(DATE_PLAYED);
    }

    public boolean getIsComplete() {
        return (boolean) content.get(IS_COMPLETE);
    }

    public void setIsComplete(boolean is_complete) {
        content.put(IS_COMPLETE, is_complete);
    }

    public boolean getIsTracked() {
        return (boolean) content.get(IS_TRACKED);
    }

    public List<Team> getMembers(Database database) {
        List<Team> members = new ArrayList<>();
        for (String member_id : (List<String>) content.get(MEMBERS)) {
            members.add(Team.getFromId(database, member_id));
        }
        return members;
    }

    // =========================================================================
    // Additional methods
    // =========================================================================

    //	public Team getWinner() {
    //        List<Team> game_members = new ArrayList<>(this.game_members);
    //        Collections.sort(game_members);
    //
    //        if (BuildConfig.DEBUG && game_members.get(0).getScore() <= game_members.get(1)
    // .getScore()) {
    //            throw new AssertionError("Called getWinner but winner is not certain.");
    //        }
    //        return game_members.get(0).getTeam();
    //	}
}
