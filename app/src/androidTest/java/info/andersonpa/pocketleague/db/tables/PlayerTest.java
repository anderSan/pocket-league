package info.andersonpa.pocketleague.db.tables;

import android.graphics.Color;

import junit.framework.TestCase;

public class PlayerTest extends TestCase {
    Player player;

    protected void setUp() throws Exception {
        super.setUp();
        player = new Player(null, "PlayerNickname", "First", "Last", true, false, true, false, 101,
                65, Color.GREEN, false, null);
    }

    public void testGetSetFirstName() throws Exception {
        assertEquals(player.getFirstName(), "First");
        player.setFirstName("Firstly");
        assertEquals(player.getFirstName(), "Firstly");
    }

    public void testGetSetLastName() throws Exception {
        assertEquals(player.getLastName(), "Last");
        player.setLastName("Lastly");
        assertEquals(player.getLastName(), "Lastly");
    }

    public void testGetSetIsLeftHanded() throws Exception {
        assertTrue(player.getIsLeftHanded());
        player.setIsLeftHanded(false);
        assertFalse(player.getIsLeftHanded());
    }

    public void testGetSetIsRightHanded() throws Exception {
        assertFalse(player.getIsRightHanded());
        player.setIsRightHanded(true);
        assertTrue(player.getIsRightHanded());
    }

    public void testGetSetIsLeftFooted() throws Exception {
        assertTrue(player.getIsLeftFooted());
        player.setIsLeftFooted(false);
        assertFalse(player.getIsLeftFooted());
    }

    public void testGetSetIsRightFooted() throws Exception {
        assertFalse(player.getIsRightFooted());
        player.setIsRightFooted(true);
        assertTrue(player.getIsRightFooted());
    }

    public void testGetSetHeight_cm() throws Exception {
        assertEquals(player.getHeight(), 101);
        player.setHeight_cm(92);
        assertEquals(player.getHeight(), 92);
    }

    public void testGetSetWeight_kg() throws Exception {
        assertEquals(player.getWeight(), 65);
        player.setWeight_kg(71);
        assertEquals(player.getWeight(), 71);
    }

    public void testGetSetContactUri() throws Exception {
        throw new UnsupportedOperationException("This test is not yet implemented.");
    }

    public void testGetDisplayName() throws Exception {
        assertEquals(player.getDisplayName(), "First \"PlayerNickname\" Last");
    }

    public void testGetFullName() throws Exception {
        assertEquals(player.getFullName(), "First Last");
    }
}