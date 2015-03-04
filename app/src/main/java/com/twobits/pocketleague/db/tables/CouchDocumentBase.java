package com.twobits.pocketleague.db.tables;

import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;

import java.util.HashMap;
import java.util.Map;

public class CouchDocumentBase {
    protected String LOGTAG = getClass().getSimpleName();
    public static final HashMap<String, Object> QUERY_END = new HashMap<>();
    Document document;
    Map<String, Object> content = new HashMap<>();

    public CouchDocumentBase(Database database) {
        document = database.createDocument();
        log("Created document: " + document.getId());
    }

    public CouchDocumentBase(Document document) {
        this.document = document;
        content.putAll(document.getProperties());
        log("Loaded document: " + document.getId());
    }

    public String getId() {
        return document.getId();
    }

    public Database getDatabase() {
        return document.getDatabase();
    }

    public void update() {
        try {
            document.putProperties(content);
            logd("updated retrievedDocument=" + String.valueOf(document.getProperties()));
        } catch (CouchbaseLiteException e) {
            loge("Cannot update document", e);
        }
    }

    public void delete() {
        try {
            document.delete();
            logd("Deleted document, deletion status = " + document.isDeleted());
        } catch (CouchbaseLiteException e) {
            loge("Cannot delete document", e);
        }
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
