package info.andersonpa.pocketleague.db.tables;

import android.graphics.Color;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TeamTest {
    private Team team;
    private Player player;

    @Before
    public void setUp() throws Exception {
        team = new Team("Team First", null);
        player = new Player("Panzer");
    }

    @Test
    public void testGetSetName() throws Exception {
        assertEquals("Team First", team.getName());

        final String new_name = "Renamed Test Team";
        team.setName(new_name);
        assertEquals(new_name, team.getName());
    }

    @Test
    public void testGetMembers() throws Exception {
        assertEquals(0, team.getMembers().size());
    }

    @Test
    public void testGetSetColor() throws Exception {
        assertNotNull(team.getColor());
        team.setColor(Color.CYAN);
        assertEquals(Color.CYAN, team.getColor());

        assertNotNull(player.getColor());
        player.setColor(Color.BLUE);
        assertEquals(player.getColor(), Color.BLUE);
    }

    @Test
    public void testGetSetIsActive() throws Exception {
        assertEquals(true, team.getIsActive());
        team.setIsActive(false);
        assertEquals(false, team.getIsActive());

        assertTrue(player.getIsActive());
        player.setIsActive(false);
        assertFalse(player.getIsActive());
    }

    @Test
    public void testGetIsFavorite() throws Exception {
        assertEquals(false, team.getIsFavorite());
        team.setIsFavorite(true);
        assertEquals(true, team.getIsFavorite());

        assertFalse(player.getIsFavorite());
        player.setIsFavorite(true);
        assertTrue(player.getIsFavorite());
    }

    @Test
    public void testGetSize() throws Exception {
        assertNotNull(team.getMembers());
        assertEquals(0, team.getSize());
    }
}