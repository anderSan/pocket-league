package com.twobits.pocketleague.db.tables;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.ArrayMap;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;

import junit.framework.Assert;

import java.io.IOException;
import java.util.Map;

public class CouchDocumentBaseTest extends AndroidTestCase {
    Manager manager;
    Database database;
    CouchDocumentBase with_empty;
    CouchDocumentBase with_document;

    protected void setUp() throws Exception {
        super.setUp();
        setContext(new RenamingDelegatingContext(getContext(), "test_"));

        manager = null;
        database = null;

        try {
            manager = new Manager(new AndroidContext(getContext()), Manager.DEFAULT_OPTIONS);
        } catch (IOException e) {
            Log.e("Test", "Failed to create manager.", e);
        }

        try {
            if (manager != null) {
                database = manager.getDatabase("test_db");
            }
        } catch (CouchbaseLiteException e) {
            Log.e("Test", "Failed to create database.", e);
        }

        Document document = database.createDocument();
        // Due to a bug in Couchbase, documents will raise nullpointerexception if not saved...
        // So add some data and save to the database.
        Map<String, Object> content = new ArrayMap<>();
        content.put("key1", "value1");
        content.put("key2", 2);
        document.putProperties(content);

        with_empty = new CouchDocumentBase();
        with_document = new CouchDocumentBase(document);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        manager.close();
    }

    public void testCreateDocument() {
        assertNull(with_empty.document);
        with_empty.createDocument(database);
        assertNotNull(with_empty.document);

        String id = with_document.getId();
        with_document.createDocument(database);
        assertEquals(with_document.getId(), id);
    }

    public void testGetId() {
        assertNull(with_empty.getId());
        assertNotNull(with_document.getId());
    }

    public void testGetDatabase() {
        assertNull(with_empty.getDatabase());
        assertNotNull(with_document.getDatabase());
    }

    public void testUpdate() {
        try {
            with_empty.update();
            Assert.fail();
        } catch (InstantiationError e) {
            //success
        }

        String revision_id = with_document.document.getCurrentRevisionId();
        with_document.update();
        assertNotSame(with_document.document.getCurrentRevisionId(), revision_id);
    }

    public void testDelete() {
        try {
            with_empty.delete();
            Assert.fail();
        } catch (InstantiationError e) {
            //success
        }

        assertFalse(with_document.document.isDeleted());
        with_document.delete();
        assertTrue(with_document.document.isDeleted());
    }
}
