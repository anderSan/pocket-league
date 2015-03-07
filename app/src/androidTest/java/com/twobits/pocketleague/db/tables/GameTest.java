package com.twobits.pocketleague.db.tables;

import com.twobits.pocketleague.enums.SessionType;
import com.twobits.pocketleague.gameslibrary.GameSubtype;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class GameTest extends TestCase {
    Game game;
    Session session;
    Venue venue;

    protected void setUp() throws Exception {
        super.setUp();
        venue = new Venue("Venue name", true);
        session = new Session("Session name", SessionType.OPEN, GameSubtype.UNDEFINED, 4, venue);
        List<Team> members = new ArrayList<>();
        game = new Game(session, 1, members, venue, false);
    }

    public void testGetIdInSession() throws Exception {
        assertEquals(game.getIdInSession(), 1);
    }

    public void testGetSession() throws Exception {
        Session s = game.getSession();
        assertEquals(session.getName(), s.getName());
        assertEquals(session.getIsFavorite(), s.getIsFavorite());
        assertEquals(session.getTeamSize(), s.getTeamSize());
    }

    public void testGetVenue() throws Exception {
        Venue v = game.getVenue();
        assertEquals(venue.getName(), v.getName());
        assertEquals(venue.getIsFavorite(), v.getIsFavorite());
    }

    public void testGetDatePlayed() throws Exception {

    }

    public void testGetIsComplete() throws Exception {
        assertFalse(game.getIsComplete());
    }

    public void testSetIsComplete() throws Exception {
        assertFalse(game.getIsComplete());
        game.setIsComplete(true);
        assertTrue(game.getIsComplete());
    }

    public void testGetIsTracked() throws Exception {
        assertFalse(game.getIsTracked());
    }

    public void testGetMembers() throws Exception {

    }
}