package info.andersonpa.pocketleague.db.tables;

import android.net.Uri;
import android.provider.ContactsContract;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player extends Team { //implements Comparable<Player> {
    public static final String TYPE = "player";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String IS_LEFT_HANDED = "is_left_handed";
    private static final String IS_RIGHT_HANDED = "is_right_handed";
    private static final String IS_LEFT_FOOTED = "is_left_footed";
    private static final String IS_RIGHT_FOOTED = "is_right_footed";
    private static final String HEIGHT = "height_cm";
    private static final String WEIGHT = "weight_kg";
    private static final String CONTACT_URI = "contact_uri";

    // Constructors
    public Player(String nickname) {
        super(nickname, null);
        content.put("type", TYPE);
        setFirstName("");
        setLastName("");
        setIsLeftHanded(false);
        setIsRightHanded(false);
        setIsLeftFooted(false);
        setIsRightFooted(false);
        setHeight_cm(0);
        setWeight_kg(0);
        setContactUri(null);
    }

    public Player(Database database, String nickname) {
        this(nickname);
        createDocument(database);
    }

    public Player(Database database, String nickname, String first_name, String last_name,
                  boolean is_left_handed, boolean is_right_handed, boolean is_left_footed,
                  boolean is_right_footed, int height_cm, int weight_kg, int color,
                  boolean is_favorite, Uri contact_uri) {
        this(database, nickname);
        setFirstName(first_name);
        setLastName(last_name);
        setIsLeftHanded(is_left_handed);
        setIsRightHanded(is_right_handed);
        setIsLeftFooted(is_left_footed);
        setIsRightFooted(is_right_footed);
        setHeight_cm(height_cm);
        setWeight_kg(weight_kg);
        setColor(color);
        setIsFavorite(is_favorite);
        setContactUri(contact_uri);
    }

    private Player(Document document) {
        super(document);
    }

    @Override
    public void update() {
        if (getSize() == 0 && document != null) {
            content.put(MEMBER_IDS, Arrays.asList(getId()));
        }
        super.update();
    }

    // Static database methods
    public static Player getFromId(Database database, String id) {
        Document document = database.getDocument(id);
        return new Player(document);
    }

    public static Player findByName(Database database, String name) throws CouchbaseLiteException {
        List<Object> key_filter = new ArrayList<>();
        key_filter.add(Arrays.asList(true, name));
        key_filter.add(Arrays.asList(false, name));
        List<Player> result = getAll(database, key_filter);

        if (result.size() == 1) {
            return result.get(0);
        } else {
            return null;
        }
    }

    private static List<Player> getAll(Database database, List<Object> key_filter)
            throws CouchbaseLiteException {
        List<Player> players = new ArrayList<>();

        Query query = database.getView("players").createQuery();
        query.setKeys(key_filter);
        QueryEnumerator rows = query.run();
        for (; rows.hasNext();) {
            QueryRow row = rows.next();
            players.add(getFromId(database, row.getDocumentId()));
        }
        return players;
    }

    static List<Player> getAllPlayers(Database database) throws CouchbaseLiteException {
        return getAll(database, null);
    }

    public static List<Player> getPlayers(Database database, boolean active, boolean only_favorite)
            throws CouchbaseLiteException {
        List<Player> players = new ArrayList<>();

        Query query = database.getView("players").createQuery();
        query.setStartKey(Arrays.asList(active, ""));
        query.setEndKey(Arrays.asList(active, QUERY_END));
        QueryEnumerator rows = query.run();
        for (; rows.hasNext(); ) {
            QueryRow row = rows.next();
            if ((boolean) row.getValue() || !only_favorite) {
                players.add(getFromId(database, row.getDocumentId()));
            }
        }
        return players;
    }

    // Other methods
    @Override
    public List<Player> getMembers() {
        return Arrays.asList(this);
    }

    public String getFirstName() {
        return (String) content.get(FIRST_NAME);
    }

    public void setFirstName(String first_name) {
        content.put(FIRST_NAME, first_name);
    }

    public String getLastName() {
        return (String) content.get(LAST_NAME);
    }

    public void setLastName(String last_name) {
        content.put(LAST_NAME, last_name);
    }

    public boolean getIsLeftHanded() {
        return (boolean) content.get(IS_LEFT_HANDED);
    }

    public void setIsLeftHanded(boolean is_left_handed) {
        content.put(IS_LEFT_HANDED, is_left_handed);
    }

    public boolean getIsRightHanded() {
        return (boolean) content.get(IS_RIGHT_HANDED);
    }

    public void setIsRightHanded(boolean is_right_handed) {
        content.put(IS_RIGHT_HANDED, is_right_handed);
    }

    public boolean getIsLeftFooted() {
        return (boolean) content.get(IS_LEFT_FOOTED);
    }

    public void setIsLeftFooted(boolean is_left_footed) {
        content.put(IS_LEFT_FOOTED, is_left_footed);
    }

    public boolean getIsRightFooted() {
        return (boolean) content.get(IS_RIGHT_FOOTED);
    }

    public void setIsRightFooted(boolean is_right_footed) {
        content.put(IS_RIGHT_FOOTED, is_right_footed);
    }

    public int getHeight() {
        return (int) content.get(HEIGHT);
    }

    public void setHeight_cm(int height_cm) {
        content.put(HEIGHT, height_cm);
    }

    public int getWeight() {
        return (int) content.get(WEIGHT);
    }

    public void setWeight_kg(int weight_kg) {
        content.put(WEIGHT, weight_kg);
    }

    public Uri getContactUri() {
        String uri_string = (String) content.get(CONTACT_URI);
        if (uri_string != null) {
            return Uri.parse(uri_string);
        } else {
            return null;
        }
    }

    public void setContactUri(Uri contact_uri) {
        if (contact_uri != null) {
            content.put(CONTACT_URI, contact_uri.toString());
        }
    }

    public Uri getThumbnailUri() {
        Uri contactUri = getContactUri();
        if (contactUri != null) {
            Uri thumbnail = Uri.withAppendedPath(contactUri,
                    ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
            return thumbnail;
        } else {
            return null;
        }
    }

    // =========================================================================
    // Additional methods
    // =========================================================================

    public String getDisplayName() {
        return getFirstName() + " \"" + getName() + "\" " + getLastName();
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

//    public int compareTo(Player another) {
//        if (id < another.id) {
//            return -1;
//        } else if (id == another.id) {
//            return 0;
//        } else {
//            return 1;
//        }
//    }
//
//    public boolean equals(Object o) {
//        if (!(o instanceof Player)) return false;
//        Player another = (Player) o;
//        return id == another.id;
//    }
//
    public static boolean exists(Database database, String name) throws CouchbaseLiteException {
        return (name != null) && (findByName(database, name) != null);
    }
}
