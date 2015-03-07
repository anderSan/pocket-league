package com.twobits.pocketleague;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import com.twobits.pocketleague.db.tables.Venue;

import java.io.IOException;

public class CouchTests extends AndroidTestCase {
    Venue venue;
    Database database;

    protected void setUp() throws Exception {
        super.setUp();
            setContext(new RenamingDelegatingContext(getContext(), "test_"));

            Manager manager = null;
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
    }

    public void testGetId() {

    }

    public void testVenueConstructor() {
        venue = new Venue("Test Venue", false);
        assertNull(venue.getId());

        venue = new Venue(database, "Test Venue 2", false);
        assertNotNull(venue.getId());
    }
}
