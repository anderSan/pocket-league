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

    @Before
    public void setUp() throws Exception {
        super.setUp();

        v1 = new Venue(database, "Test Venue");
        v1.update();

        v2 = new Venue(database, "Test Venue2", true);
        v2.update();
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
    }

    @Test
    public void testGetAllVenues() throws Exception {
        List<Venue> all_venues = Venue.getAllVenues(database);
        assertEquals(2, all_venues.size());
    }

    @Test
    public void testGetVenues() throws Exception {
        List<Venue> all_venues = Venue.getVenues(database, true, true);
        assertEquals(1, all_venues.size());
    }
}