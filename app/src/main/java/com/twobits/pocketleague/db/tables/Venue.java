package com.twobits.pocketleague.db.tables;

import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryOptions;
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

	public Venue(Database database, String venue_name, boolean is_favorite) {
        super(database);
        // name should be unique
        content.put("type", TYPE);
        content.put(NAME, venue_name);
        content.put(IS_ACTIVE, true);
        content.put(IS_FAVORITE, is_favorite);
	}

    private Venue(Document document) {
        super(document);
    }

    public static Venue getFromId(Database database, String id) {
        Document document = database.getDocument(id);
        return new Venue(document);
    }

    public static Venue findByName(Database database, String name)
            throws CouchbaseLiteException {
        Query query = database.getView("venue-names").createQuery();
        query.setStartKey(Arrays.asList(name));
        query.setEndKey(Arrays.asList(name));
        QueryEnumerator result = query.run();

        assert (result.getCount() <= 1);
        if (result.hasNext()) {
            return Venue.getFromId(database, result.next().getDocumentId());
        } else {
            return null;
        }
    }

    private static List<Venue> getAll(Database database, List<Object> key_filter)
            throws CouchbaseLiteException {
        List<Venue> venues = new ArrayList<>();

        QueryOptions options = new QueryOptions();
        options.setKeys(key_filter);
        List<QueryRow> rows = database.getView("venue-names").queryWithOptions(options);
        for (QueryRow row : rows) {
            venues.add(getFromId(database, row.getDocumentId()));
        }

        return venues;
    }

    public static List<Venue> getAllVenues(Database database) throws CouchbaseLiteException {
        return getAll(database, null);
    }

    public static List<Venue> getVenues(Database database, boolean active, boolean only_favorite)
            throws CouchbaseLiteException {
        List<Object> key_filter = new ArrayList<>();
        List<Venue> venues = new ArrayList<>();

        Query query = database.getView("venue-act.fav").createQuery();
        query.setStartKey(Arrays.asList(active, only_favorite));
        query.setEndKey(Arrays.asList(active, QUERY_END));
        QueryEnumerator filter = query.run();
        for (Iterator<QueryRow> it = filter; it.hasNext(); ) {
            QueryRow row = it.next();
            key_filter.add(row.getDocumentId());

            // temp solution until setKeys is solved...
            venues.add(getFromId(database, row.getDocumentId()));
        }

        return venues;
//        return getAll(database, key_filter);
    }

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
