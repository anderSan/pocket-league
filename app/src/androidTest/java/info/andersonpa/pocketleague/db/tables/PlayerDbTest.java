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
    private Player p3;
    private Player p4;
    private Player p5;
    private Player p6;


    @Before
    public void setUp() throws Exception {
        super.setUp();

        p1 = new Player(database, "Bob", "b", "o", true, false, true, false, 70, 80, Color.BLACK, false, null);
        p1.update();
        p2 = new Player(database, "Sue", "s", "u", true, false, true, false, 72, 81, Color.RED, true, null);
        p2.update();
        p3 = new Player(database, "Alice");
        p3.update();
        p4 = new Player(database, "Tim");
        p4.setIsActive(false);
        p4.update();
        p5 = new Player(database, "Steve");
        p5.setIsFavorite(true);
        p5.update();
        p6 = new Player(database, "Dick");
        p6.setIsActive(false);
        p6.setIsFavorite(true);
        p6.update();
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

        p = Player.findByName(database, "Dick");
        assertNotNull(p);
        assertEquals(p6.getId(), p.getId());
        assertEquals(p6.document.getCurrentRevisionId(), p.document.getCurrentRevisionId());
    }

    @Test
    public void testGetAllPlayers() throws Exception {
        List<Player> all_players = Player.getAllPlayers(database);
        assertEquals(6, all_players.size());
    }

    @Test
    public void testGetPlayers() throws Exception {
        List<Player> all_players = Player.getPlayers(database, false, false);
        assertEquals(2, all_players.size());

        assertEquals("Dick", all_players.get(0).getName());
        assertEquals("Tim", all_players.get(1).getName());

        // All players are active
        p4.setIsActive(true);
        p4.update();
        p6.setIsActive(true);
        p6.update();

        all_players = Player.getPlayers(database, false, false);
        assertEquals(0, all_players.size());
    }

    @Test
    public void testGetPlayersActive() throws Exception {
        List<Player> all_players = Player.getPlayers(database, true, false);
        assertEquals(4, all_players.size());

        assertEquals("Alice", all_players.get(0).getName());
        assertEquals("Bob", all_players.get(1).getName());
        assertEquals("Steve", all_players.get(2).getName());
        assertEquals("Sue", all_players.get(3).getName());
    }

    @Test
    public void testGetPlayersFavorite() throws Exception {
        List<Player> all_players = Player.getPlayers(database, false, true);
        assertEquals(1, all_players.size());

        assertEquals("Dick", all_players.get(0).getName());
    }

    @Test
    public void testGetPlayersActiveFavorite() throws Exception {
        List<Player> all_players = Player.getPlayers(database, true, true);
        assertEquals(2, all_players.size());

        assertEquals("Steve", all_players.get(0).getName());
        assertEquals("Sue", all_players.get(1).getName());
    }

    @Test
    public void testStaticExists() throws Exception {
        assertTrue(Player.exists(database, p1.getName()));
        assertTrue(Player.exists(database, p2.getName()));
        assertFalse(Player.exists(database, "Nonexistent Team Name"));
    }
}
