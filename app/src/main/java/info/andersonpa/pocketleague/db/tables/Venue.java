package info.andersonpa.pocketleague.db.tables;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Venue extends CouchDocumentBase {
    public static final String TYPE = "venue";
    public static final String NAME = "name";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String ZIP_CODE = "zip_code";
    public static final String IS_ACTIVE = "is_active";
    public static final String IS_FAVORITE = "is_favorite";

    // Constructors
    public Venue(String venue_name) {
        // name should be unique
        content.put("type", TYPE);
        setName(venue_name);
        setIsActive(true);
        setIsFavorite(false);
    }

    public Venue(Database database, String venue_name) {
        this(venue_name);
        createDocument(database);
    }

    public Venue(Database database, String venue_name, boolean is_favorite) {
        this(database, venue_name);
        setIsFavorite(is_favorite);
    }

    private Venue(Document document) {
        super(document);
    }

    // Static database methods
    public static Venue getFromId(Database database, String id) {
        Document document = database.getDocument(id);
        return new Venue(document);
    }

    public static Venue findByName(Database database, String name) throws CouchbaseLiteException {
        List<Object> key_filter = new ArrayList<>();
        key_filter.add(Arrays.asList(true, name));
        key_filter.add(Arrays.asList(false, name));
        List<Venue> result = getAll(database, key_filter);

        if (result.size() == 1) {
            return result.get(0);
        } else {
            return null;
        }
    }

    private static List<Venue> getAll(Database database, List<Object> key_filter) throws
            CouchbaseLiteException {
        List<Venue> venues = new ArrayList<>();

        Query query = database.getView("venues").createQuery();
        query.setKeys(key_filter);
        QueryEnumerator rows = query.run();
        for (; rows.hasNext();) {
            QueryRow row = rows.next();
            venues.add(getFromId(database, row.getDocumentId()));
        }
        return venues;
    }

    public static List<Venue> getAllVenues(Database database) throws CouchbaseLiteException {
        return getAll(database, null);
    }

    public static List<Venue> getVenues(Database database, boolean active, boolean only_favorite)
            throws CouchbaseLiteException {
        List<Venue> venues = new ArrayList<>();

        Query query = database.getView("venues").createQuery();
        query.setStartKey(Arrays.asList(active, ""));
        query.setEndKey(Arrays.asList(active, QUERY_END));
        QueryEnumerator rows = query.run();
        for (; rows.hasNext(); ) {
            QueryRow row = rows.next();
            if ((boolean) row.getValue() || !only_favorite) {
                venues.add(getFromId(database, row.getDocumentId()));
            }
        }
        return venues;
    }

    // Other methods
    public String getName() {
        return (String) content.get(NAME);
    }

    public void setName(String venue_name) {
        content.put(NAME, venue_name);
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
}
