package info.andersonpa.pocketleague.db.tables;

import android.graphics.Color;
import android.support.test.runner.AndroidJUnit4;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PlayerDbTest extends DbBaseTestCase {
    private Player p1;
    private Player p2;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        p1 = new Player(database, "Bob");
        p1.update();
        p2 = new Player(database, "Sue", "Susan", "Susarilla", true, false, true, false, 72, 81,
                Color.RED, true, null);
        p2.update();
    }

    @Test
    public void testConstructor() throws Exception {
        Player player = new Player("No doc player");
        assertNull(player.getId());

        player = new Player(database, "Doc player");
        assertNotNull(player.getId());
    }

    @Test
    public void testGetFromId() throws Exception {
        Player p = Player.getFromId(database, p1.getId());

        assertNotNull(p);
        assertEquals(p1.getId(), p.getId());
        assertEquals(p1.document.getCurrentRevisionId(), p.document.getCurrentRevisionId());
    }

    @Test
    public void testFindByName() throws Exception {
        Player p = Player.findByName(database, "Test Other Player");
        assertNull(p);

        p = Player.findByName(database, "Bob");
        assertNotNull(p);
        assertEquals(p1.getId(), p.getId());
        assertEquals(p1.document.getCurrentRevisionId(), p.document.getCurrentRevisionId());
    }

    @Test
    public void testGetAllPlayers() throws Exception {
        List<Player> all_players = Player.getAllPlayers(database);
        assertEquals(2, all_players.size());
    }

    @Test
    public void testGetPlayers() throws Exception {
        List<Player> all_players = Player.getPlayers(database, true, true);
        assertEquals(1, all_players.size());
    }

    @Test
    public void testStaticExists() throws Exception {
        assertTrue(Player.exists(database, p1.getName()));
        assertTrue(Player.exists(database, p2.getName()));
        assertFalse(Player.exists(database, "Nonexistent Team Name"));
    }
}
