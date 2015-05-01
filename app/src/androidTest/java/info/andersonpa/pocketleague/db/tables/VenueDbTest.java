package info.andersonpa.pocketleague.db.tables;

import java.util.List;

public class VenueDbTest extends DbBaseTestCase {
    Venue v1;
    Venue v2;

    protected void setUp() throws Exception {
        super.setUp();

        v1 = new Venue(database, "Test Venue");
        v1.update();

        v2 = new Venue(database, "Test Venue2", true);
        v2.update();
    }

    public void testConstructor() throws Exception {
        Venue venue = new Venue("No doc venue");
        assertNull(venue.getId());

        venue = new Venue(database, "Doc venue");
        assertNotNull(venue.getId());
    }

    public void testGetFromId() throws Exception {
        Venue v = Venue.getFromId(database, v1.getId());

        assertNotNull(v);
        assertEquals(v1.getId(), v.getId());
        assertEquals(v1.document.getCurrentRevisionId(), v.document.getCurrentRevisionId());
    }

    public void testFindByName() throws Exception {
        Venue v = Venue.findByName(database, "Test Other Venue");
        assertNull(v);

        v = Venue.findByName(database, "Test Venue");
        assertNotNull(v);
        assertEquals(v1.getId(), v.getId());
        assertEquals(v1.document.getCurrentRevisionId(), v.document.getCurrentRevisionId());
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