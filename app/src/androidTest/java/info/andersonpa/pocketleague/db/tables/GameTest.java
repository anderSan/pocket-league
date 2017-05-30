package info.andersonpa.pocketleague.db.tables;

import android.os.SystemClock;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import info.andersonpa.pocketleague.enums.SessionType;
import info.andersonpa.pocketleague.gameslibrary.GameSubtype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class GameTest {
    private Game game;
    private Session session;
    private Venue venue;
    Date before;

    @Before
    public void setUp() throws Exception {
        before = new Date();
        venue = new Venue("Venue name");
        session = new Session("Session name", SessionType.OPEN, GameSubtype.UNDEFINED, 4, venue);
        List<GameMember> members = new ArrayList<>();


        game = new Game(session, 1, members, venue, false);
    }

    @Test
    public void testGetIdInSession() throws Exception {
        assertEquals(game.getIdInSession(), 1);
    }

    @Test
    public void testGetDatePlayed() throws Exception {
        Date before = new Date();
        SystemClock.sleep(3);
        Game g = new Game(session, 1, new ArrayList<GameMember>(), venue, false);
        SystemClock.sleep(3);
        Date after = new Date();

        assertTrue(before.before(g.getDatePlayed()));
        assertTrue(after.after(g.getDatePlayed()));
    }

    @Test
    public void testGetSetIsComplete() throws Exception {
        assertFalse(game.getIsComplete());
        game.setIsComplete(true);
        assertTrue(game.getIsComplete());
    }

    @Test
    public void testGetIsTracked() throws Exception {
        assertFalse(game.getIsTracked());
    }
}