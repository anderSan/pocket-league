package com.twobits.pocketleague.db.tables;

import android.os.SystemClock;

import com.twobits.pocketleague.enums.SessionType;
import com.twobits.pocketleague.gameslibrary.GameSubtype;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameTest extends TestCase {
    Game game;
    Session session;
    Venue venue;
    Date before;

    protected void setUp() throws Exception {
        before = new Date();
        super.setUp();
        venue = new Venue("Venue name");
        session = new Session("Session name", SessionType.OPEN, GameSubtype.UNDEFINED, 0, 4, venue);
        List<GameMember> members = new ArrayList<>();


        game = new Game(session, 1, members, venue, false);
    }

    public void testGetIdInSession() throws Exception {
        assertEquals(game.getIdInSession(), 1);
    }

    public void testGetDatePlayed() throws Exception {
        Date before = new Date();
        SystemClock.sleep(2);
        Game g = new Game(session, 1, new ArrayList<GameMember>(), venue, false);
        SystemClock.sleep(2);
        Date after = new Date();

        assertTrue(before.before(g.getDatePlayed()));
        assertTrue(after.after(g.getDatePlayed()));
    }

    public void testGetSetIsComplete() throws Exception {
        assertFalse(game.getIsComplete());
        game.setIsComplete(true);
        assertTrue(game.getIsComplete());
    }

    public void testGetIsTracked() throws Exception {
        assertFalse(game.getIsTracked());
    }
}