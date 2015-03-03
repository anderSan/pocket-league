package com.twobits.pocketleague.db.tables;

import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.twobits.pocketleague.enums.SessionType;
import com.twobits.pocketleague.gameslibrary.GameDescriptor;
import com.twobits.pocketleague.gameslibrary.GameSubtype;
import com.twobits.pocketleague.gameslibrary.GameType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Session extends CouchDocumentBase {
    public static final String TYPE = "session";
	public static final String NAME = "session_name";
    public static final String SESSION_TYPE = "session_type";
    public static final String GAME_TYPE = "game_type";
	public static final String GAME_SUBTYPE = "game_subtype";
    public static final String RULESET_ID = "ruleset_id";
	public static final String TEAM_SIZE = "team_size";
	public static final String IS_ACTIVE = "is_active";
	public static final String IS_FAVORITE = "is_favorite";
	public static final String CURRENT_VENUE = "current_venue_id";
    public static final String GAMES = "game_ids";
    public static final String MEMBERS = "member_ids";

	public Session(Database database, String session_name, SessionType session_type, GameSubtype game_subtype,
                   int team_size, Venue current_venue) {
        super(database, null);
        // name should be unique
        content.put("type", TYPE);
        content.put(NAME, session_name);
        content.put(SESSION_TYPE, session_type);
        content.put(GAME_SUBTYPE, game_subtype);
//        content.put(RULESET_ID, ruleset_id);
        content.put(TEAM_SIZE, team_size);
        content.put(IS_ACTIVE, true);
        content.put(IS_FAVORITE, false);
        content.put(CURRENT_VENUE, current_venue);
	}

    private Session(Database database, Map<String, Object> content) {
        super(database, content);
    }

    public static Session getFromId(Database database, String id) {
        Document document = database.getDocument(id);
        return new Session(database, document.getProperties());
    }

    public String getName() {
        return (String) content.get(NAME);
    }

    public void setName(String session_name) {
        content.put(NAME, session_name);
    }

    public SessionType getSessionType() {
        return (SessionType) content.get(SESSION_TYPE);
    }

	public GameType getGameType() {
		GameSubtype gst = (GameSubtype) content.get(GAME_SUBTYPE);
        return gst.toGameType();
	}

	public GameSubtype getGameSubtype() {
		return (GameSubtype) content.get(GAME_SUBTYPE);
	}

	public int getTeamSize() {
		return (int) content.get(TEAM_SIZE);
	}

    public boolean getIsActive() {
        return (boolean) content.get(IS_ACTIVE);
    }

    public void setIsActive(boolean is_active) {
        content.put(IS_ACTIVE, is_active);
    }

    public boolean getIsFavorite() {
        return (boolean) content.get(IS_FAVORITE);
    }

    public void setIsFavorite(boolean is_favorite) {
        content.put(IS_FAVORITE, is_favorite);
    }

	public Venue getCurrentVenue(Database database) {
		String venue_id =  (String) content.get(CURRENT_VENUE);
        return Venue.getFromId(database, venue_id);
	}

	public void setCurrentVenue(Venue current_venue) {
		content.put(CURRENT_VENUE, current_venue.getId());
	}

    public List<Game> getGames(Database database) {
        List<Game> games = new ArrayList<>();
        for (String game_id : (List<String>) content.get(GAMES)) {
            games.add(Game.getFromId(database, game_id));
        }
        return games;
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

	public GameDescriptor getDescriptor() {
		return ((GameSubtype) content.get(GAME_SUBTYPE)).toDescriptor();
	}
}
