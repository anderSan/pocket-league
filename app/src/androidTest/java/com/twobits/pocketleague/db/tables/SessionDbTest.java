package com.twobits.pocketleague.db.tables;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import com.twobits.pocketleague.enums.SessionType;
import com.twobits.pocketleague.gameslibrary.GameSubtype;

import java.io.IOException;

public class SessionDbTest extends AndroidTestCase {
    Manager manager;
    Database database;
    Session session;
    Venue venue;

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

        venue = new Venue(database, "Test Venue", false);
        venue.update();
        session = new Session("Session name", SessionType.OPEN, GameSubtype.EIGHTBALL, 3, venue);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        manager.close();
    }

    public void testGetSetCurrentVenue() throws Exception {
        Venue v = session.getCurrentVenue(database);
        assertEquals(venue.getName(), v.getName());
        assertEquals(venue.getIsFavorite(), v.getIsFavorite());
    }

    public void testGetGames() throws Exception {

    }

    public void testAddMembers() throws Exception {

    }

    public void testGetMembers() throws Exception {

    }

    public void testUpdateMembers() throws Exception {

    }

    public void testGetDescriptor() throws Exception {

    }
}