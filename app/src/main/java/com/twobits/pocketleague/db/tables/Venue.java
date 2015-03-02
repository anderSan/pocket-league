package com.twobits.pocketleague.db.tables;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;

import java.util.HashMap;
import java.util.Map;

public class Venue {
    static final String TYPE = "venue";
    public static final String ID = "_id";
	public static final String NAME = "name";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String ZIP_CODE = "zip_code";
	public static final String IS_ACTIVE = "is_active";
	public static final String IS_FAVORITE = "is_favorite";

    Map<String, Object> content = new HashMap<>();

	public Venue(String name, boolean is_favorite) {
        content.put("type", TYPE);
        content.put(NAME, name);
        content.put(IS_ACTIVE, true);
        content.put(IS_FAVORITE, is_favorite);
	}

    public Venue(Database database, long id) {
        Document document = database.getDocument(ID);
        content = document.getProperties();
    }

	public long getId() {
		return (long) content.get(ID);
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

    public void update(Database database) {
        Document document = database.getDocument(ID);

        if (document == null) {
            document = database.createDocument();
        }

        try {
            document.putProperties(content);
//            logd("updated retrievedDocument=" + String.valueOf(document.getProperties()));
        } catch (CouchbaseLiteException e) {
//            loge("Cannot update document", e);
        }
    }

    public void delete(Database database) {
        Document document = database.getDocument(ID);
        try {
            document.delete();
//            logd("Deleted document, deletion status = " + document.isDeleted());
        } catch (CouchbaseLiteException e) {
//            loge("Cannot delete document", e);
        }
    }
}
