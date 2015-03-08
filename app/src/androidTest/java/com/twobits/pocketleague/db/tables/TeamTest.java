package com.twobits.pocketleague.db.tables;

import android.graphics.Color;

import junit.framework.TestCase;

public class TeamTest extends TestCase {
    Team team;

    public void setUp() throws Exception {
        super.setUp();
        team = new Team("Team First", null);
    }

    public void testGetSetName() throws Exception {
        assertEquals("Team First", team.getName());

        final String new_name = "Renamed Test Team";
        team.setName(new_name);
        assertEquals(new_name, team.getName());
    }

    public void testGetMembers() throws Exception {
        assertEquals(0, team.getMembers().size());
    }

    public void testGetSetColor() throws Exception {
        assertNotNull(team.getColor());
        team.setColor(Color.CYAN);
        assertEquals(Color.CYAN, team.getColor());
    }

    public void testGetSetIsActive() throws Exception {
        assertEquals(true, team.getIsActive());
        team.setIsActive(false);
        assertEquals(false, team.getIsActive());
    }

    public void testGetIsFavorite() throws Exception {
        assertEquals(false, team.getIsFavorite());
        team.setIsFavorite(true);
        assertEquals(true, team.getIsFavorite());
    }

    public void testGetSize() throws Exception {
        assertNotNull(team.getMembers());
        assertEquals(0, team.getSize());
    }
}