package com.twobits.pocketleague.db.tables;

import android.test.AndroidTestCase;

import com.couchbase.lite.Database;
import com.twobits.pocketleague.SandboxContext;
import com.twobits.pocketleague.db.DatabaseHelper;

import java.util.List;

public class VenueDbTest extends AndroidTestCase {
    DatabaseHelper databaseHelper;
    Database database;
    Venue v1;
    Venue v2;

    protected void setUp() throws Exception {
        super.setUp();
        SandboxContext mock_context = new SandboxContext();
        databaseHelper = new DatabaseHelper(mock_context);
        database = databaseHelper.getDatabase();

        v1 = new Venue(database, "Test Venue", false);
        v1.update();

        v2 = new Venue(database, "Test Venue2", true);
        v2.update();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        database.delete();
        database.close();
        database = null;
        databaseHelper.close();
        databaseHelper = null;
    }

    public void testGetFromId() throws Exception {
        Venue v = Venue.getFromId(database, v1.getId());

        assertNotNull(v);
        assertEquals(v.getId(), v1.getId());
        assertEquals(v.document.getCurrentRevisionId(), v1.document.getCurrentRevisionId());
    }

    public void testFindByName() throws Exception {
        Venue v = Venue.findByName(database, "Test Other Venue");
        assertNull(v);

        v = Venue.findByName(database, "Test Venue");
        assertNotNull(v);
        assertEquals(v.getId(), v1.getId());
        assertEquals(v.document.getCurrentRevisionId(), v1.document.getCurrentRevisionId());
    }

    public void testGetAllVenues() throws Exception {
            List<Venue> all_venues = Venue.getAllVenues(database);
            assertEquals(2, all_venues.size());
    }

    public void testGetVenues() throws Exception {
        List<Venue> all_venues = Venue.getVenues(database, true, true);
        assertEquals(1, all_venues.size());
    }
}