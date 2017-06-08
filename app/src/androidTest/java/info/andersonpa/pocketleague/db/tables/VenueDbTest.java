package info.andersonpa.pocketleague.db.tables;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class VenueDbTest extends DbBaseTestCase {
    private Venue v1;
    private Venue v2;
    private Venue v3;
    private Venue v4;
    private Venue v5;
    private Venue v6;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        v1 = new Venue(database, "Test Venue");
        v1.setIsActive(false);
        v1.update();
        v2 = new Venue(database, "Test Venue 2", true);
        v2.setIsActive(false);
        v2.update();
        v3 = new Venue(database, "Boston", true);
        v3.update();
        v4 = new Venue(database, "Oxford", true);
        v4.update();
        v5 = new Venue(database, "Huntsville", true);
        v5.update();
        v6 = new Venue(database, "Mars");
        v6.update();
    }

    @Test
    public void testConstructor() throws Exception {
        Venue venue = new Venue("No doc venue");
        assertNull(venue.getId());

        venue = new Venue(database, "Doc venue");
        assertNotNull(venue.getId());
    }

    @Test
    public void testGetFromId() throws Exception {
        Venue v = Venue.getFromId(database, v1.getId());

        assertNotNull(v);
        assertEquals(v1.getId(), v.getId());
        assertEquals(v1.document.getCurrentRevisionId(), v.document.getCurrentRevisionId());
    }

    @Test
    public void testFindByName() throws Exception {
        Venue v = Venue.findByName(database, "Test Other Venue");
        assertNull(v);

        v = Venue.findByName(database, "Test Venue");
        assertNotNull(v);
        assertEquals(v1.getId(), v.getId());
        assertEquals(v1.document.getCurrentRevisionId(), v.document.getCurrentRevisionId());
        assertEquals("Test Venue", v.getName());

        v = Venue.findByName(database, "Oxford");
        assertNotNull(v);
        assertEquals(v4.getId(), v.getId());
        assertEquals(v4.document.getCurrentRevisionId(), v.document.getCurrentRevisionId());
        assertEquals("Oxford", v.getName());
    }

    @Test
    public void testGetAllVenues() throws Exception {
        List<Venue> all_venues = Venue.getAllVenues(database);
        assertEquals(6, all_venues.size());
    }

    @Test
    public void testGetVenues() throws Exception {
        List<Venue> all_venues = Venue.getVenues(database, false, false);
        assertEquals(2, all_venues.size());

        assertEquals("Test Venue", all_venues.get(0).getName());
        assertEquals("Test Venue 2", all_venues.get(1).getName());

        // All venues are active
        v1.setIsActive(true);
        v1.update();
        v2.setIsActive(true);
        v2.update();

        all_venues = Venue.getVenues(database, false, false);
        assertEquals(0, all_venues.size());
    }

    @Test
    public void testGetVenuesActive() throws Exception {
        List<Venue> all_venues = Venue.getVenues(database, true, false);
        assertEquals(4, all_venues.size());

        assertEquals("Boston", all_venues.get(0).getName());
        assertEquals("Huntsville", all_venues.get(1).getName());
        assertEquals("Mars", all_venues.get(2).getName());
        assertEquals("Oxford", all_venues.get(3).getName());
    }

    @Test
    public void testGetVenuesFavorite() throws Exception {
        List<Venue> all_venues = Venue.getVenues(database, false, true);
        assertEquals(1, all_venues.size());

        assertEquals("Test Venue 2", all_venues.get(0).getName());
    }

    @Test
    public void testGetVenuesActiveFavorite() throws Exception {
        List<Venue> all_venues = Venue.getVenues(database, true, true);
        assertEquals(3, all_venues.size());

        assertEquals("Boston", all_venues.get(0).getName());
        assertEquals("Huntsville", all_venues.get(1).getName());
        assertEquals("Oxford", all_venues.get(2).getName());
    }
}