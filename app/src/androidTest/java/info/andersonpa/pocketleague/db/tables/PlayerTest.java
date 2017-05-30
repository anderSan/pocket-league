package info.andersonpa.pocketleague.db.tables;

import android.graphics.Color;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PlayerTest {
    private Player player;

    @Before
    public void setUp() throws Exception {
        player = new Player(null, "PlayerNickname", "First", "Last", true, false, true, false, 101,
                65, Color.GREEN, false, null);
    }

    @Test
    public void testGetSetFirstName() throws Exception {
        assertEquals(player.getFirstName(), "First");
        player.setFirstName("Firstly");
        assertEquals(player.getFirstName(), "Firstly");
    }

    @Test
    public void testGetSetLastName() throws Exception {
        assertEquals(player.getLastName(), "Last");
        player.setLastName("Lastly");
        assertEquals(player.getLastName(), "Lastly");
    }

    @Test
    public void testGetSetIsLeftHanded() throws Exception {
        assertTrue(player.getIsLeftHanded());
        player.setIsLeftHanded(false);
        assertFalse(player.getIsLeftHanded());
    }

    @Test
    public void testGetSetIsRightHanded() throws Exception {
        assertFalse(player.getIsRightHanded());
        player.setIsRightHanded(true);
        assertTrue(player.getIsRightHanded());
    }

    @Test
    public void testGetSetIsLeftFooted() throws Exception {
        assertTrue(player.getIsLeftFooted());
        player.setIsLeftFooted(false);
        assertFalse(player.getIsLeftFooted());
    }

    @Test
    public void testGetSetIsRightFooted() throws Exception {
        assertFalse(player.getIsRightFooted());
        player.setIsRightFooted(true);
        assertTrue(player.getIsRightFooted());
    }

    @Test
    public void testGetSetHeight_cm() throws Exception {
        assertEquals(player.getHeight(), 101);
        player.setHeight_cm(92);
        assertEquals(player.getHeight(), 92);
    }

    @Test
    public void testGetSetWeight_kg() throws Exception {
        assertEquals(player.getWeight(), 65);
        player.setWeight_kg(71);
        assertEquals(player.getWeight(), 71);
    }

    @Test
    public void testGetSetContactUri() throws Exception {
        throw new UnsupportedOperationException("This test is not yet implemented.");
    }

    @Test
    public void testGetDisplayName() throws Exception {
        assertEquals(player.getDisplayName(), "First \"PlayerNickname\" Last");
    }

    @Test
    public void testGetFullName() throws Exception {
        assertEquals(player.getFullName(), "First Last");
    }
}