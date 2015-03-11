package com.twobits.pocketleague.db.tables;

import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.twobits.pocketleague.BuildConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Game extends CouchDocumentBase {
    public static final String TYPE = "game";
    public static final String ID_IN_SESSION = "id_in_session";
    public static final String SESSION_ID = "session_id";
    public static final String VENUE_ID = "venue_id";
    public static final String DATE_PLAYED = "date_played";
    public static final String IS_COMPLETE = "is_complete";
    public static final String IS_TRACKED = "is_tracked";
    public static final String STORED_MEMBERS = "stored_members";

    private List<GameMember> members = new ArrayList<>();

    // Constructors
    public Game(Session session, int id_in_session, List<GameMember> members, Venue venue,
                boolean is_tracked) {
        content.put("type", TYPE);
        content.put(SESSION_ID, session.getId());
        content.put(ID_IN_SESSION, id_in_session);
        for (int ii = 0 ; ii < members.size() ; ii++) {
            members.get(ii).setPlayOrder(ii);
        }
        this.members = members;
        setVenue(venue);
        content.put(DATE_PLAYED, new Date());
        setIsComplete(false);
        content.put(IS_TRACKED, is_tracked);
    }

    public Game(Database database, Session session, int id_in_session, List<GameMember> members,
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
        return (int) content.get(ID_IN_SESSION);
    }

    public Session getSession() {
        String session_id = (String) content.get(SESSION_ID);
        return Session.getFromId(getDatabase(), session_id);
    }

    public Venue getVenue() {
        String venue_id = (String) content.get(VENUE_ID);
        return Venue.getFromId(getDatabase(), venue_id);
    }

    public void setVenue(Venue venue) {
        content.put(VENUE_ID, venue.getId());
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

    public List<GameMember> getMembers() {
        if (members.size() == 0) {
            List<Map<String, Object>> stored_members = (List<Map<String, Object>>) content.get(STORED_MEMBERS);
            Team team;
            int score;
            int play_order;

            for (Map<String, Object> gm : stored_members) {
                team = Team.getFromId(getDatabase(), (String) gm.get(GameMember.TEAM_ID));
                score = (int) gm.get(GameMember.SCORE);
                play_order = (int) gm.get(GameMember.PLAY_ORDER);
                members.add(new GameMember(team, score, play_order));
            }
        }
        Collections.sort(members);
        return members;
    }

    public void updateMembers(List<GameMember> members) {
        if (this.members.size() == members.size()) {
            this.members = members;
        } else {
            throw new InternalError("Size of member list does not match.");
        }
    }

    @Override
    public void update() {
        List<Map<String, Object>> stored_members = new ArrayList<>();
        for (GameMember gm : members) {
            stored_members.add(gm.toMap());
        }
        content.put(STORED_MEMBERS, stored_members);

        super.update();
    }

    // =========================================================================
    // Additional methods
    // =========================================================================

    public Team getWinner() {
        if (getIsComplete()) {
            Collections.sort(members, GameMember.SCORE_ORDER);

            if (BuildConfig.DEBUG && members.get(0).getScore() <= members.get(1).getScore()) {
                throw new AssertionError("Called getWinner but winner is not certain.");
            }
            return members.get(0).getTeam();
        } else {
            return null;
        }
    }
}
