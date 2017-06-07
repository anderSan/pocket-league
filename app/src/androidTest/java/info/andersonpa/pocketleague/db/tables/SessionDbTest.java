package info.andersonpa.pocketleague.db.tables;

import android.support.test.runner.AndroidJUnit4;

import info.andersonpa.pocketleague.enums.SessionType;
import info.andersonpa.pocketleague.gameslibrary.GameSubtype;
import info.andersonpa.pocketleague.gameslibrary.GameType;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class SessionDbTest extends DbBaseTestCase {
    private Session s1;
    private Session s2;
    private SessionMember sm1;
    private SessionMember sm2;
    private Team t1;
    private Team t2;
    private Venue v1;
    private Venue v2;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        v1 = new Venue(database, "Test Venue");
        v1.update();
        v2 = new Venue(database, "Test Other Venue");
        v2.update();

        t1 = new Team(database, "Team First", null);
        t1.update();
        t2 = new Team(database, "Team Second", null);
        t2.update();

        sm1 = new SessionMember(t1, 1, 2);
        sm2 = new SessionMember(t2, 2, 1);

        s1 = new Session(database, "Session name", SessionType.OPEN, GameSubtype.BILLIARDS_EIGHTBALL, 3, v1);
        s1.update();
        s2 = new Session(database, "Other session name", SessionType.LADDER, GameSubtype.GOLF, 2,
                v1, Arrays.asList(sm1, sm2), true);
        s2.update();
    }

    @Test
    public void testConstructor() throws Exception {
        Session session = new Session("No doc session", SessionType.OPEN, GameSubtype.GOLF, 1, v1);
        assertNull(session.getId());

        session = new Session(database, "Doc session", SessionType.OPEN, GameSubtype.GOLF, 1, v1);
        assertNotNull(session.getId());
    }

    @Test
    public void testGetFromId() throws Exception {
        Session s = Session.getFromId(database, s2.getId());

        assertNotNull(s);
        assertEquals(s.getId(), s2.getId());
        assertEquals(s.document.getCurrentRevisionId(), s2.document.getCurrentRevisionId());
        List<SessionMember> members = s2.getMembers();
        assertEquals(2, members.size());
        assertEquals(t1, members.get(0).getTeam());
        assertEquals(t2, members.get(1).getTeam());
    }

    @Test
    public void testFindByName() throws Exception {
        Session s = Session.findByName(database, "Bogus session name");
        assertNull(s);

        s = Session.findByName(database, "Session name");
        assertNotNull(s);
        assertEquals(s.getId(), s1.getId());
        assertEquals(s.document.getCurrentRevisionId(), s1.document.getCurrentRevisionId());
    }

    @Test
    public void testGetAllSessions() throws Exception {
        List<Session> all_sessions = Session.getAllSessions(database);
        assertEquals(2, all_sessions.size());
    }

    @Test
    public void testGetSessions() throws Exception {
        List<Session> all_sessions = Session.getSessions(database, GameType.GOLF, true, true);
        assertEquals(1, all_sessions.size());
        assertEquals(GameType.GOLF, all_sessions.get(0).getGameType());
        assertTrue(all_sessions.get(0).getIsActive());
        assertTrue(all_sessions.get(0).getIsFavorite());

        all_sessions = Session.getSessions(database, GameType.BILLIARDS, true, true);
        assertEquals(0, all_sessions.size());

        all_sessions = Session.getSessions(database, GameType.BILLIARDS, true, false);
        assertEquals(1, all_sessions.size());
        assertEquals(GameType.BILLIARDS, all_sessions.get(0).getGameType());
        assertTrue(all_sessions.get(0).getIsActive());
        assertFalse(all_sessions.get(0).getIsFavorite());
    }

    @Test
    public void testGetSetCurrentVenue() throws Exception {
        Venue v = s1.getCurrentVenue();
        assertEquals(v1.getName(), v.getName());
        assertEquals(v1.getIsFavorite(), v.getIsFavorite());

        s1.setCurrentVenue(v2);
        v = s1.getCurrentVenue();
        assertEquals(v2.getName(), v.getName());
        assertEquals(v2.getIsFavorite(), v.getIsFavorite());
    }

    @Test
    public void testAddGetGame() throws Exception {
        assertEquals(0, s1.getGames().size());
        Game g1 = new Game(s1, 2, new ArrayList<GameMember>(), v2, true);
        Game g2 = new Game(database, s1, 1, new ArrayList<GameMember>(), v1, true);
        g2.update();

        try {
            s2.addGame(g1);
            Assert.fail();
        } catch (NullPointerException e) {
            // success
        }

        s1.addGame(g2);
        assertEquals(1, s1.getGames().size());
        List<Game> games = s1.getGames();
        assertEquals(g2.getIdInSession(), games.get(0).getIdInSession());
        assertEquals(g2.getIsTracked(), games.get(0).getIsTracked());
    }

    @Test
    public void testAddMembers() throws Exception {
        assertEquals(0, s1.getMembers().size());
        s1.addMembers(Arrays.asList(sm2, sm1));
        List<SessionMember> members = s1.getMembers();
        assertEquals(2, members.size());
        assertEquals(t2, members.get(0).getTeam());
        assertEquals(t1, members.get(1).getTeam());

        assertEquals(2, s2.getMembers().size());
        s2.addMembers(Arrays.asList(sm2, sm1));
        members = s2.getMembers();
        assertEquals(2, members.size());
        assertEquals(t1, members.get(0).getTeam());
        assertEquals(t2, members.get(1).getTeam());
    }

    @Test
    public void testGetMembers() throws Exception {
        assertEquals(0, s1.getMembers().size());

        List<SessionMember> members = s2.getMembers();
        assertEquals(2, members.size());
        assertEquals(t1, members.get(0).getTeam());
        assertEquals(t2, members.get(1).getTeam());

        Session s = Session.getFromId(database, s2.getId());
        members = s.getMembers();
        assertEquals(2, members.size());
        assertEquals(t1.getId(), members.get(0).getTeam().getId());
        assertEquals(t2.getId(), members.get(1).getTeam().getId());
    }

    @Test
    public void testUpdateMembers() throws Exception {
        try {
            s1.updateMembers(Arrays.asList(sm1));
            Assert.fail();
        } catch (InternalError e) {
            // success
        }

        try {
            s2.updateMembers(Arrays.asList(sm1));
            Assert.fail();
        } catch (InternalError e) {
            // success
        }

        s2.updateMembers(Arrays.asList(sm1, sm2));
        sm1.swapRank(sm2);
        s2.updateMembers(Arrays.asList(sm1, sm2));
        List<SessionMember> members = s2.getMembers();
        assertEquals(2, members.size());
        assertEquals(1, members.get(0).getRank());
        assertEquals(2, members.get(1).getRank());
    }

    @Test
    public void testUpdate() throws Exception {
        Session s = Session.getFromId(database, s2.getId());
        assertEquals(2, s2.getMembers().size());
        s.update();
        assertEquals(2, s.getMembers().size());
    }
}