package info.andersonpa.pocketleague.db.tables;

import info.andersonpa.pocketleague.enums.SessionType;
import info.andersonpa.pocketleague.gameslibrary.GameDescriptor;
import info.andersonpa.pocketleague.gameslibrary.GameSubtype;
import info.andersonpa.pocketleague.gameslibrary.GameType;

import junit.framework.TestCase;

public class SessionTest extends TestCase {
    Session session;

    protected void setUp() throws Exception {
        super.setUp();
        Venue venue = new Venue("Test Venue");
        session = new Session("Session name", SessionType.OPEN, GameSubtype.EIGHTBALL, 3, venue);
    }

    public void testGetSetName() throws Exception {
        assertEquals(session.getName(), "Session name");
        session.setName("A new name");
        assertEquals(session.getName(), "A new name");
    }

    public void testGetSessionType() throws Exception {
        assertEquals(session.getSessionType(), SessionType.OPEN);
    }

    public void testGetGameType() throws Exception {
        assertEquals(session.getGameType(), GameType.BILLIARDS);
    }

    public void testGetGameSubtype() throws Exception {
        assertEquals(session.getGameSubtype(), GameSubtype.EIGHTBALL);
    }

    public void testGetTeamSize() throws Exception {
        assertEquals(session.getTeamSize(), 3);
    }

    public void testGetSetIsActive() throws Exception {
        assertTrue(session.getIsActive());
        session.setIsActive(false);
        assertFalse(session.getIsActive());
    }

    public void testGetSetIsFavorite() throws Exception {
        assertFalse(session.getIsFavorite());
        session.setIsFavorite(true);
        assertTrue(session.getIsFavorite());
    }

    public void testGetDescriptor() throws Exception {
        GameDescriptor gd = session.getDescriptor();
        assertEquals(gd.getName(), "8-ball");
        assertEquals(gd.actionString(), "com.twobits.billiards.eightball.PLAY_GAME");
    }
}