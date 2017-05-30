package info.andersonpa.pocketleague.db.tables;

import android.os.SystemClock;
import android.support.test.runner.AndroidJUnit4;

import info.andersonpa.pocketleague.enums.SessionType;
import info.andersonpa.pocketleague.gameslibrary.GameSubtype;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class GameDbTest extends DbBaseTestCase {
    private Game game;
    private String game_id;
    private Session session;
    private Team t1;
    private Team t2;
    private GameMember gm1;
    private GameMember gm2;
    private Venue venue;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        venue = new Venue(database, "Venue name");
        venue.update();
        session = new Session(database, "Session name", SessionType.OPEN, GameSubtype.UNDEFINED, 4, venue);
        session.update();

        t1 = new Team(database, "Team First", null);
        t1.update();
        t2 = new Team(database, "Team Second", null);
        t2.update();

        gm1 = new GameMember(t1);
        gm1.setScore(8);
        gm2 = new GameMember(t2);

        game = new Game(database, session, 1, Arrays.asList(gm1, gm2), venue, false);
        game.update();
        game_id = game.getId();
    }

    @Test
    public void testConstructor() throws Exception {
        Game g = new Game(session, 2, Arrays.asList(gm1), venue, true);
        assertNull(g.getId());

        g = new Game(database, session, 4, Arrays.asList(gm2), venue, false);
        assertNotNull(g.getId());
    }

    @Test
    public void testGetFromId() throws Exception {
        Game g = Game.getFromId(database, game.getId());

        assertNotNull(g);
        assertEquals(g.getId(), game.getId());
        assertEquals(g.document.getCurrentRevisionId(), game.document.getCurrentRevisionId());
        List<GameMember> members = game.getMembers();
        assertEquals(2, members.size());
        assertEquals(t1, members.get(0).getTeam());
        assertEquals(t2, members.get(1).getTeam());
    }

    @Test
    public void testGetSession() throws Exception {
        Session s = game.getSession();
        assertEquals(session.getName(), s.getName());
        assertEquals(session.getIsFavorite(), s.getIsFavorite());
        assertEquals(session.getTeamSize(), s.getTeamSize());
    }

    @Test
    public void testGetSetVenue() throws Exception {
        Venue v = game.getVenue();
        assertEquals(venue.getName(), v.getName());
        assertEquals(venue.getIsFavorite(), v.getIsFavorite());

        Venue v2 = new Venue(database, "Another venue name");
        v2.update();
        game.setVenue(v2);

        v = game.getVenue();
        assertEquals(v2.getName(), v.getName());
        assertEquals(v2.getIsFavorite(), v.getIsFavorite());
    }

    @Test
    public void testGetDatePlayed() throws Exception {
        Date before = new Date();
        SystemClock.sleep(3);
        Game g = new Game(database, session, 1, new ArrayList<GameMember>(), venue, false);
        g.update();
        SystemClock.sleep(3);
        Date after = new Date();

        g = Game.getFromId(database, g.getId());

        assertTrue(before.before(g.getDatePlayed()));
        assertTrue(after.after(g.getDatePlayed()));
    }

    @Test
    public void testGetMembers() throws Exception {
        List<GameMember> members = game.getMembers();
        assertEquals(2, members.size());
        assertEquals(t1, members.get(0).getTeam());
        assertEquals(t2, members.get(1).getTeam());

        Game g = Game.getFromId(database, game.getId());
        members = g.getMembers();
        assertEquals(2, members.size());
        assertEquals(t1.getId(), members.get(0).getTeam().getId());
        assertEquals(t2.getId(), members.get(1).getTeam().getId());
        assertEquals(8, members.get(0).getScore());
        assertEquals(0, members.get(1).getScore());
    }

    @Test
    public void testUpdateMembers() throws Exception {
        try {
            game.updateMembers(Arrays.asList(gm1));
            Assert.fail();
        } catch (InternalError e) {
            // success
        }

        game.updateMembers(Arrays.asList(gm1, gm2));
        gm1.setScore(5);
        gm2.setScore(3);
        game.updateMembers(Arrays.asList(gm1, gm2));
        List<GameMember> members = game.getMembers();
        assertEquals(2, members.size());
        assertEquals(5, members.get(0).getScore());
        assertEquals(3, members.get(1).getScore());
    }

    @Test
    public void testGetWinner() {
        Team t = game.getWinner();
        assertNull(t);

        game.setIsComplete(true);
        t = game.getWinner();
        assertNotNull(t);
        assertEquals(t1, t);

        List<GameMember> members = game.getMembers();
        assertEquals(8, members.get(0).getScore());
        assertEquals(0, members.get(1).getScore());

        game = Game.getFromId(database, game_id);
    }

    @Test
    public void testGetWinner2() {
        game = Game.getFromId(database, game_id);
        Team t = game.getWinner();
        assertNull(t);

        game.setIsComplete(true);
        t = game.getWinner();
        assertNotNull(t);
        assertEquals(t1, t);

        List<GameMember> members = game.getMembers();
        assertEquals(8, members.get(0).getScore());
        assertEquals(0, members.get(1).getScore());
    }
}