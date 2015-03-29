package com.twobits.pocketleague.db.tables;

import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.twobits.pocketleague.BuildConfig;

import java.util.HashMap;
import java.util.Map;

public class CouchDocumentBase {
    protected String LOGTAG = getClass().getSimpleName();
    public static final HashMap<String, Object> QUERY_END = new HashMap<>();
    Document document;
    Map<String, Object> content = new HashMap<>();

    public CouchDocumentBase() {}

    public CouchDocumentBase(Document document) {
        this.document = document;
        content.putAll(document.getProperties());

        if (BuildConfig.DEBUG) {
            if (content.containsKey("name")) {
                String name = (String) content.get("name");
                log(String.format("Loaded document: %s (%s)", document.getId(), name));
            } else {
                log(String.format("Loaded document: %s", document.getId()));
            }
        }
    }

    public void createDocument(Database database) {
        if (database != null && document == null) {
            document = database.createDocument();
            log("Created document: " + document.getId());
        }
    }

    public String getId() {
        if (document != null) {
            return document.getId();
        } else {
            return null;
        }
    }

    public Database getDatabase() {
        if (document != null) {
            return document.getDatabase();
        } else {
            return null;
        }
    }

    public void update() {
        if (document != null) {
            try {
                document.putProperties(content);
                logd("updated retrievedDocument=" + String.valueOf(document.getProperties()));
            } catch (CouchbaseLiteException e) {
                loge("Cannot update document", e);
            }
        } else {
            if (BuildConfig.DEBUG) {
                throw new InstantiationError("No document is attached for update.");
            }
        }

        content.clear();
        content.putAll(document.getProperties());
    }

    public void delete() {
        if (document != null) {
            try {
                document.delete();
                logd("Deleted document, deletion status = " + document.isDeleted());
            } catch (CouchbaseLiteException e) {
                loge("Cannot delete document", e);
            }
        } else {
            if (BuildConfig.DEBUG) {
                throw new InstantiationError("No document is attached for delete.");
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CouchDocumentBase that = (CouchDocumentBase) o;
        if (!getId().equals(that.getId())) return false;
        if (!document.getCurrentRevision().equals(that.document.getCurrentRevision())) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = document.getCurrentRevision().hashCode();
        result = 31 * result + getId().hashCode();
        return result;
    }

    public void log(String msg) {
        Log.i(LOGTAG, msg);
    }

    public void logd(String msg) {
        Log.d(LOGTAG, msg);
    }

    public void loge(String msg, Exception e) {
        Log.e(LOGTAG, msg + ": " + e.getMessage());
    }
}
