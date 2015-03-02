package com.twobits.pocketleague.db.tables;

import com.couchbase.lite.Database;
import com.twobits.pocketleague.BuildConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Game extends CouchDocumentBase {
    static final String TYPE = "game";
	public static final String ID_IN_SESSION = "id_in_session";
	public static final String SESSION = "session_id";
	public static final String VENUE = "venue_id";
	public static final String DATE_PLAYED = "date_played";
	public static final String IS_COMPLETE = "is_complete";
	public static final String IS_TRACKED = "is_tracked";
    public static final String MEMBERS = "member_ids";

	public Game(Session session, long id_in_session, List<Team> members, Venue venue,
                Date date_played, boolean is_tracked) {
        // session and id_in_session combination should be unique
        content.put("type", TYPE);
        content.put(SESSION, session);
        content.put(ID_IN_SESSION, id_in_session);
        content.put(MEMBERS, members);
        content.put(VENUE, venue);
        content.put(DATE_PLAYED, date_played);
        content.put(IS_COMPLETE, true);
        content.put(IS_TRACKED, is_tracked);
	}

    public Game(Session session, long id_in_session, List<Team> members, Venue venue,
                boolean is_tracked) {
        content.put("type", TYPE);
        content.put(SESSION, session);
        content.put(ID_IN_SESSION, id_in_session);
        content.put(MEMBERS, members);
        content.put(VENUE, venue);
        content.put(DATE_PLAYED, new Date());
        content.put(IS_COMPLETE, true);
        content.put(IS_TRACKED, is_tracked);
    }

    public Game(Database database, String id){
        super(database, id);
    }

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
            members.add(new Team(database, member_id));
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
//        if (BuildConfig.DEBUG && game_members.get(0).getScore() <= game_members.get(1).getScore()) {
//            throw new AssertionError("Called getWinner but winner is not certain.");
//        }
//        return game_members.get(0).getTeam();
//	}
}
