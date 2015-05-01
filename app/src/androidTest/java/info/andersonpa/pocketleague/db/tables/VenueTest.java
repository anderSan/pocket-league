package info.andersonpa.pocketleague.db.tables;

import junit.framework.TestCase;

public class VenueTest extends TestCase {
    Venue venue;

    protected void setUp() throws Exception {
        super.setUp();
        venue = new Venue("Test Venue");
    }

    public void testGetSetName() throws Exception {
        assertEquals("Test Venue", venue.getName());
        final String new_name = "Renamed Test Venue";
        venue.setName(new_name);
        assertEquals(new_name, venue.getName());
    }

    public void testGetSetIsActive() throws Exception {
        assertEquals(true, venue.getIsActive());
        venue.setIsActive(false);
        assertEquals(false, venue.getIsActive());
    }

    public void testGetIsFavorite() throws Exception {
        assertEquals(false, venue.getIsFavorite());
        venue.setIsFavorite(true);
        assertEquals(true, venue.getIsFavorite());
    }
}