package com.twobits.pocketleague.db.tables;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;

import java.io.IOException;

public class VenueTest extends AndroidTestCase {
    Venue venue;
    protected void setUp() throws Exception {
        super.setUp();
//        setContext(new RenamingDelegatingContext(getContext(), "test_"));
//
//        Manager manager = null;
//        Database database = null;
//
//        try {
//            manager = new Manager(new AndroidContext(getContext()), Manager.DEFAULT_OPTIONS);
//        } catch (IOException e) {
//            Log.e("Test", "Failed to create manager.", e);
//        }
//
//        try {
//            if (manager != null) {
//                database = manager.getDatabase("test_db");
//            }
//        } catch (CouchbaseLiteException e) {
//            Log.e("Test", "Failed to create database.", e);
//        }

        venue = new Venue("Test Venue", false);
    }

    public void testGetName() throws Exception {
        assertEquals(venue.getName(), "Test Venue");
    }

    public void testSetName() throws Exception {
        final String new_name = "Renamed Test Venue";
        venue.setName(new_name);
        assertEquals(venue.getName(), new_name);
    }

    public void testGetIsActive() throws Exception {
        assertEquals(venue.getIsActive(), true);
    }

    public void testSetIsActive() throws Exception {
        venue.setIsActive(false);
        assertEquals(venue.getIsActive(), false);
    }

    public void testGetIsFavorite() throws Exception {
        assertEquals(venue.getIsFavorite(), false);
    }

    public void testSetIsFavorite() throws Exception {
        venue.setIsFavorite(true);
        assertEquals(venue.getIsFavorite(), true);
    }
}