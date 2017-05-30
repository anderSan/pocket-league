package info.andersonpa.pocketleague.db.tables;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class VenueTest {
    private Venue venue;

    @Before
    public void setUp() throws Exception {
        venue = new Venue("Test Venue");
    }

    @Test
    public void testGetSetName() throws Exception {
        assertEquals("Test Venue", venue.getName());
        final String new_name = "Renamed Test Venue";
        venue.setName(new_name);
        assertEquals(new_name, venue.getName());
    }

    @Test
    public void testGetSetIsActive() throws Exception {
        assertEquals(true, venue.getIsActive());
        venue.setIsActive(false);
        assertEquals(false, venue.getIsActive());
    }

    @Test
    public void testGetIsFavorite() throws Exception {
        assertEquals(false, venue.getIsFavorite());
        venue.setIsFavorite(true);
        assertEquals(true, venue.getIsFavorite());
    }
}