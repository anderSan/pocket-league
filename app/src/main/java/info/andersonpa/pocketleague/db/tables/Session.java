package info.andersonpa.pocketleague.db.tables;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import info.andersonpa.pocketleague.enums.SessionType;
import info.andersonpa.pocketleague.gameslibrary.GameDescriptor;
import info.andersonpa.pocketleague.gameslibrary.GameSubtype;
import info.andersonpa.pocketleague.gameslibrary.GameType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Session extends CouchDocumentBase {
    public static final String TYPE = "session";
    public static final String NAME = "name";
    public static final String SESSION_TYPE = "session_type";
    public static final String GAME_TYPE = "game_type";
    public static final String GAME_SUBTYPE = "game_subtype";
    public static final String TEAM_SIZE = "team_size";
    public static final String IS_ACTIVE = "is_active";
    public static final String IS_FAVORITE = "is_favorite";
    public static final String CURRENT_VENUE_ID = "current_venue_id";
    public static final String GAME_IDS = "game_ids";
    public static final String STORED_MEMBERS = "stored_members";

    private List<SessionMember> members = new ArrayList<>();

    // Constructors
    public Session(String session_name, SessionType session_type, GameSubtype game_subtype,
                   int team_size, Venue current_venue) {
        // name should be unique
        content.put("type", TYPE);
        setName(session_name);
        content.put(SESSION_TYPE, session_type.name());
        content.put(GAME_SUBTYPE, game_subtype.name());
        content.put(TEAM_SIZE, team_size);
        setIsActive(true);
        setIsFavorite(false);
        setCurrentVenue(current_venue);
        content.put(GAME_IDS, new ArrayList<String>());
    }

    public Session(Database database, String session_name, SessionType session_type,
                   GameSubtype game_subtype, int team_size, Venue current_venue) {
        this(session_name, session_type, game_subtype, team_size, current_venue);
        createDocument(database);
    }

    public Session(Database database, String session_name, SessionType session_type,
                   GameSubtype game_subtype, int team_size, Venue current_venue,
                   List<SessionMember> members, boolean is_favorite) {
        this(database, session_name, session_type, game_subtype, team_size, current_venue);
        this.members = members;
        setIsFavorite(is_favorite);
    }

    private Session(Document document) {
        super(document);
    }

    // Static database methods
    public static Session getFromId(Database database, String id) {
        Document document = database.getDocument(id);
        return new Session(document);
    }

    public static Session findByName(Database database, String name) throws CouchbaseLiteException {
        Query query = database.getView("session-names").createQuery();
        query.setStartKey(name);
        query.setEndKey(name);
        QueryEnumerator result = query.run();

        if (result.hasNext()) {
            return Session.getFromId(database, result.next().getDocumentId());
        } else {
            return null;
        }
    }

    private static List<Session> getAll(Database database, List<Object> key_filter) throws
            CouchbaseLiteException {
        List<Session> sessions = new ArrayList<>();

        Query query = database.getView("session-names").createQuery();
        query.setKeys(key_filter);
        QueryEnumerator rows = query.run();
        for (; rows.hasNext();) {
            QueryRow row = rows.next();
            sessions.add(getFromId(database, row.getDocumentId()));
        }
        return sessions;
    }

    public static List<Session> getAllSessions(Database database) throws CouchbaseLiteException {
        return getAll(database, null);
    }

    public static List<Session> getSessions(Database database, GameType current_game_type,
                                            boolean active, boolean only_favorite)
            throws CouchbaseLiteException {
        List<Object> key_filter = new ArrayList<>();

        Query query = database.getView("session-act.fav").createQuery();
        query.setStartKey(Arrays.asList(current_game_type.name(), active, only_favorite));
        query.setEndKey(Arrays.asList(current_game_type.name(), active, QUERY_END));
        QueryEnumerator filter = query.run();
        for (; filter.hasNext(); ) {
            QueryRow row = filter.next();
            // key_filter.add(row.getDocumentId());
            String key_id = row.getDocumentId();
            key_filter.add(getFromId(database, key_id).getName());
        }
        return getAll(database, key_filter);
    }

    // Other methods
    public String getName() {
        return (String) content.get(NAME);
    }

    public void setName(String session_name) {
        content.put(NAME, session_name);
    }

    public SessionType getSessionType() {
        return SessionType.valueOf((String) content.get(SESSION_TYPE));
    }

    public GameType getGameType() {
        return getGameSubtype().toGameType();
    }

    public GameSubtype getGameSubtype() {
        return GameSubtype.valueOf((String) content.get(GAME_SUBTYPE));
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

    public Venue getCurrentVenue() {
        String venue_id = (String) content.get(CURRENT_VENUE_ID);
        return Venue.getFromId(getDatabase(), venue_id);
    }

    public void setCurrentVenue(Venue current_venue) {
        content.put(CURRENT_VENUE_ID, current_venue.getId());
    }

    @SuppressWarnings("unchecked")
    public void addGame(Game g) {
        List<String> game_ids = (List<String>) content.get(GAME_IDS);
        if (g.getId() != null) {
            game_ids.add(g.getId());
            content.put(GAME_IDS, game_ids);
        } else {
            throw new NullPointerException("Game ID is null.");
        }
    }

    @SuppressWarnings("unchecked")
    public List<Game> getGames() {
        List<Game> games = new ArrayList<>();
        for (String game_id : (List<String>) content.get(GAME_IDS)) {
            games.add(Game.getFromId(getDatabase(), game_id));
        }
        return games;
    }

    public void addMembers(List<SessionMember> new_members) {
        if (members.size() == 0) {
            members = new_members;
        }
    }

    @SuppressWarnings("unchecked")
    public List<SessionMember> getMembers() {
        if (members.size() == 0) {
            List<Map<String, Object>> stored_members;
            if (content.containsKey(STORED_MEMBERS)) {
                stored_members = (List<Map<String, Object>>) content.get(STORED_MEMBERS);
            } else {
                stored_members = new ArrayList<>();
            }
            Team team;
            int team_seed;
            int team_rank;

            for (Map<String, Object> sm : stored_members) {
                team = Team.getFromId(getDatabase(), (String) sm.get(SessionMember.TEAM_ID));
                team_seed = (int) sm.get(SessionMember.TEAM_SEED);
                team_rank = (int) sm.get(SessionMember.TEAM_RANK);
                members.add(new SessionMember(team, team_seed, team_rank));
            }
        }
        return members;
    }

    public void updateMembers(List<SessionMember> members) {
        if (this.members.size() == members.size()) {
            this.members = members;
        } else {
            throw new InternalError("Size of member list does not match.");
        }
    }

    @Override
    public void update() {
        List<Map<String, Object>> stored_members = new ArrayList<>();
        for (SessionMember sm : getMembers()) {
            if (sm.getTeam() != null) {
                stored_members.add(sm.toMap());
            }
        }
        content.put(STORED_MEMBERS, stored_members);

        super.update();
    }

    // =========================================================================
    // Additional methods
    // =========================================================================

    public GameDescriptor getDescriptor() {
        return getGameSubtype().toDescriptor();
    }
}