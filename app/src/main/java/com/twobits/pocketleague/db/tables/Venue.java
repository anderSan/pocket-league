package com.twobits.pocketleague.db.tables;

import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Venue extends CouchDocumentBase {
    public static final String TYPE = "venue";
	public static final String NAME = "venue_name";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String ZIP_CODE = "zip_code";
	public static final String IS_ACTIVE = "is_active";
	public static final String IS_FAVORITE = "is_favorite";

	public Venue(Database database, String venue_name, boolean is_favorite) {
        super(database, null);
        // name should be unique
        content.put("type", TYPE);
        content.put(NAME, venue_name);
        content.put(IS_ACTIVE, true);
        content.put(IS_FAVORITE, is_favorite);
	}

    private Venue(Database database, Map<String, Object> content) {
        super(database, content);
    }

    public static Venue getFromId(Database database, String id) {
        Document document = database.getDocument(id);
        return new Venue(database, document.getProperties());
    }

    public static QueryEnumerator getAll(Database database, boolean active, boolean only_favorite)
            throws CouchbaseLiteException {
        Query query = database.getView("all-venues").createQuery();
        query.setStartKey(Arrays.asList(active, only_favorite));
        query.setEndKey(Arrays.asList(active, new HashMap<String, Object>()));
        QueryEnumerator result = query.run();

        return result;
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
