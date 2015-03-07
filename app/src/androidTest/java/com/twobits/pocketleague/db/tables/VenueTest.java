package com.twobits.pocketleague.db.tables;

import junit.framework.TestCase;

public class VenueTest extends TestCase {
    Venue venue;
    protected void setUp() throws Exception {
        super.setUp();
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