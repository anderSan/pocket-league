package com.twobits.pocketleague.db.tables;

import android.graphics.Color;

import com.twobits.pocketleague.enums.SessionType;
import com.twobits.pocketleague.gameslibrary.GameSubtype;

import java.util.ArrayList;
import java.util.List;

public class SessionDbTest extends DbBaseTestCase {
    Session s1;
    Session s2;
    Venue v1;
    Venue v2;
    List<SessionMember> members;

    protected void setUp() throws Exception {
        super.setUp();

        v1 = new Venue(database, "Test Venue");
        v1.update();
        v2 = new Venue(database, "Test Other Venue");
        v2.update();

        Team t1 = new Team("Team First", null);

//        SessionMember sm = new SessionMember();

        s1 = new Session(database, "Session name", SessionType.OPEN, GameSubtype.EIGHTBALL, 0, 3, v1);
        s1.setIsFavorite(true);
        s1.update();
        s2 = new Session(database, "Other session name", SessionType.LADDER, GameSubtype.GOLF, 1, 2, v1);
        s2.update();
    }

    public void testGetFromId() throws Exception {
        Session s = Session.getFromId(database, s1.getId());

        assertNotNull(s);
        assertEquals(s.getId(), s1.getId());
        assertEquals(s.document.getCurrentRevisionId(), s1.document.getCurrentRevisionId());
    }

    public void testFindByName() throws Exception {
        Session s = Session.findByName(database, "Bogus session name");
        assertNull(s);

        s = Session.findByName(database, "Session name");
        assertNotNull(s);
        assertEquals(s.getId(), s1.getId());
        assertEquals(s.document.getCurrentRevisionId(), s1.document.getCurrentRevisionId());
    }

    public void testGetAllSessions() throws Exception {
        List<Session> all_sessions = Session.getAllSessions(database);
        assertEquals(2, all_sessions.size());
    }

    public void testGetSessions() throws Exception {
        List<Session> all_sessions = Session.getSessions(database, true, true);
        assertEquals(1, all_sessions.size());
    }

    public void testGetSetCurrentVenue() throws Exception {
        Venue v = s1.getCurrentVenue(database);
        assertEquals(v1.getName(), v.getName());
        assertEquals(v1.getIsFavorite(), v.getIsFavorite());

        s1.setCurrentVenue(v2);
        v = s1.getCurrentVenue(database);
        assertEquals(v2.getName(), v.getName());
        assertEquals(v2.getIsFavorite(), v.getIsFavorite());
    }

    public void testGetGames() throws Exception {

    }

    public void testAddMembers() throws Exception {

    }

    public void testGetMembers() throws Exception {

    }

    public void testUpdateMembers() throws Exception {
        s1.content.get("");

    }

    public void testUpdate() throws Exception {

    }
}