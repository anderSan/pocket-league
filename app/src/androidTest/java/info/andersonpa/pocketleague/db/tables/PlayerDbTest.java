package info.andersonpa.pocketleague.db.tables;

import android.graphics.Color;

import java.util.List;

public class PlayerDbTest extends DbBaseTestCase {
    Player p1;
    Player p2;

    protected void setUp() throws Exception {
        super.setUp();

        p1 = new Player(database, "Bob");
        p1.update();
        p2 = new Player(database, "Sue", "Susan", "Susarilla", true, false, true, false, 72, 81,
                Color.RED, true);
        p2.update();
    }

    public void testConstructor() throws Exception {
        Player player = new Player("No doc player");
        assertNull(player.getId());

        player = new Player(database, "Doc player");
        assertNotNull(player.getId());
    }

    public void testGetFromId() throws Exception {
        Player p = Player.getFromId(database, p1.getId());

        assertNotNull(p);
        assertEquals(p1.getId(), p.getId());
        assertEquals(p1.document.getCurrentRevisionId(), p.document.getCurrentRevisionId());
    }

    public void testFindByName() throws Exception {
        Player p = Player.findByName(database, "Test Other Player");
        assertNull(p);

        p = Player.findByName(database, "Bob");
        assertNotNull(p);
        assertEquals(p1.getId(), p.getId());
        assertEquals(p1.document.getCurrentRevisionId(), p.document.getCurrentRevisionId());
    }

    public void testGetAllPlayers() throws Exception {
        List<Player> all_players = Player.getAllPlayers(database);
        assertEquals(2, all_players.size());
    }

    public void testGetPlayers() throws Exception {
        List<Player> all_players = Player.getPlayers(database, true, true);
        assertEquals(1, all_players.size());
    }

    public void testStaticExists() throws Exception {
        assertTrue(Player.exists(database, p1.getName()));
        assertTrue(Player.exists(database, p2.getName()));
        assertFalse(Player.exists(database, "Nonexistent Team Name"));
    }
}
