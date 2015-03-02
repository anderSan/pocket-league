package com.twobits.pocketleague.db.tables;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.twobits.pocketleague.db.DatabaseHelper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CouchDocumentBase {
    protected String LOGTAG = getClass().getSimpleName();
    public static final String ID = "_id";
    Map<String, Object> content = new HashMap<>();

    public CouchDocumentBase() {}

    public CouchDocumentBase(Database database, String id) {
        Document document = database.getDocument(id);
        content = document.getProperties();
    }

    public String getId() {
        return (String) content.get(ID);
    }

    public void update(Database database) {
        Document document = database.getDocument(ID);

        if (document == null) {
            document = database.createDocument();
        }

        try {
            document.putProperties(content);
            logd("updated retrievedDocument=" + String.valueOf(document.getProperties()));
        } catch (CouchbaseLiteException e) {
            loge("Cannot update document", e);
        }
    }

    public void delete(Database database) {
        Document document = database.getDocument(ID);
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
