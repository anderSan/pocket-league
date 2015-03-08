package com.twobits.pocketleague.db.tables;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeamDbTest extends DbBaseTestCase {
    Player p1;
    Player p2;
    Player p3;

    Team t1;
    Team t2;

    protected void setUp() throws Exception {
        super.setUp();

        p1 = new Player(database, "Bob");
        p1.update();
        p2 = new Player(database, "Sue");
        p2.update();
        p3 = new Player(database, "Tom");
        p3.update();

        t1 = new Team(database, "Test Team A", Arrays.asList(p1, p2));
        t1.update();

        t2 = new Team(database, "Test Team B", Arrays.asList(p2, p3, p1), Color.GREEN, true);
        t2.update();
    }

    public void testConstructor() throws Exception {
        Team team = new Team("No doc team", null);
        assertNull(team.getId());

        team = new Team(database, "Doc venue", null);
        assertNotNull(team.getId());
    }

    public void testGetFromId() throws Exception {
        Team t = Team.getFromId(database, t1.getId());

        assertNotNull(t);
        assertEquals(t1.getId(), t.getId());
        assertEquals(t1.document.getCurrentRevisionId(), t.document.getCurrentRevisionId());
    }

    public void testFindByName() throws Exception {
        Team t = Team.findByName(database, "Test Other Venue");
        assertNull(t);

        t = Team.findByName(database, "Test Team A");
        assertNotNull(t);
        assertEquals(t1.getId(), t.getId());
        assertEquals(t1.document.getCurrentRevisionId(), t.document.getCurrentRevisionId());
    }

    public void testGetAllTeams() throws Exception {
        List<Team> all_teams = Team.getAllTeams(database);
        assertEquals(2, all_teams.size());
    }

    public void testGetTeams() throws Exception {
        List<Team> all_teams = Team.getTeams(database, true, true);
        assertEquals(1, all_teams.size());
    }

    public void testGetMembers() throws Exception {
        List<Player> members = t1.getMembers();
        assertEquals(2, members.size());
        assertEquals("Bob", members.get(0).getName());
        assertEquals(p1.getId(), members.get(0).getId());
        assertEquals("Sue", members.get(1).getName());
        assertEquals(p2.getId(), members.get(1).getId());

        members = t2.getMembers();
        assertEquals(3, members.size());
        assertEquals("Sue", members.get(0).getName());
        assertEquals(p2.getId(), members.get(0).getId());
        assertEquals("Tom", members.get(1).getName());
        assertEquals(p3.getId(), members.get(1).getId());
        assertEquals("Bob", members.get(2).getName());
        assertEquals(p1.getId(), members.get(2).getId());
    }

    public void testGetSize() throws Exception {
        assertEquals(2, t1.getSize());
        assertEquals(3, t2.getSize());
    }

    public void testExists() throws Exception {
        assertTrue(t1.exists());
        assertTrue(t2.exists());
        Team t3 = new Team(database, "Test Team C", Arrays.asList(p1, p2));
        assertFalse(t3.exists());
    }

    public void testStaticExists() throws Exception {
        assertTrue(Team.exists(database, t1.getName()));
        assertTrue(Team.exists(database, t2.getName()));
        assertFalse(Team.exists(database, "Nonexistent Team Name"));
    }
}
