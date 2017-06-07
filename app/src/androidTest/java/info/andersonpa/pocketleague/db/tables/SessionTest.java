package info.andersonpa.pocketleague.db.tables;

import android.support.test.runner.AndroidJUnit4;

import info.andersonpa.pocketleague.enums.SessionType;
import info.andersonpa.pocketleague.gameslibrary.GameDescriptor;
import info.andersonpa.pocketleague.gameslibrary.GameSubtype;
import info.andersonpa.pocketleague.gameslibrary.GameType;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class SessionTest {
    private Session session;

    @Before
    public void setUp() throws Exception {
        Venue venue = new Venue("Test Venue");
        session = new Session("Session name", SessionType.OPEN, GameSubtype.BILLIARDS_EIGHTBALL, 3, venue);
    }

    @Test
    public void testGetSetName() throws Exception {
        assertEquals(session.getName(), "Session name");
        session.setName("A new name");
        assertEquals(session.getName(), "A new name");
    }

    @Test
    public void testGetSessionType() throws Exception {
        assertEquals(session.getSessionType(), SessionType.OPEN);
    }

    @Test
    public void testGetGameType() throws Exception {
        assertEquals(session.getGameType(), GameType.BILLIARDS);
    }

    @Test
    public void testGetGameSubtype() throws Exception {
        assertEquals(session.getGameSubtype(), GameSubtype.BILLIARDS_EIGHTBALL);
    }

    @Test
    public void testGetTeamSize() throws Exception {
        assertEquals(session.getTeamSize(), 3);
    }

    @Test
    public void testGetSetIsActive() throws Exception {
        assertTrue(session.getIsActive());
        session.setIsActive(false);
        assertFalse(session.getIsActive());
    }

    @Test
    public void testGetSetIsFavorite() throws Exception {
        assertFalse(session.getIsFavorite());
        session.setIsFavorite(true);
        assertTrue(session.getIsFavorite());
    }

    @Test
    public void testGetDescriptor() throws Exception {
        GameDescriptor gd = session.getDescriptor();
        assertEquals(gd.getName(), "8-ball");
        assertEquals(gd.actionString(), "info.andersonpa.billiards.eightball.PLAY_GAME");
    }
}