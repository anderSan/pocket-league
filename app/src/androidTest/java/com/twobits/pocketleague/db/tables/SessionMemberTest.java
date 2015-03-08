package com.twobits.pocketleague.db.tables;

import android.graphics.Color;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SessionMemberTest extends TestCase {
    SessionMember sm1;
    SessionMember sm2;
    SessionMember sm3;
    Team t1;
    Team t2;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        t1 = new TeamStub("Team1", new ArrayList<String>(), Color.CYAN, false);
        t2 = new TeamStub("Team2", new ArrayList<String>(), Color.RED, true);
        sm1 = new SessionMember(t1, 2, 3);
        sm2 = new SessionMember(t2, 3);
        sm3 = new SessionMember(1, 1);
    }

    public void testGetTeam() throws Exception {
        Team t = sm1.getTeam();
        assertEquals(t1.getName(), t.getName());
        assertEquals(t1.getColor(), t.getColor());
        assertEquals(t1.getIsActive(), t.getIsActive());

        assertNull(sm3.getTeam());
    }

    public void testGetSeed() throws Exception {
        assertEquals(sm1.getSeed(), 2);
        assertEquals(sm2.getSeed(), 3);
        assertEquals(sm3.getSeed(), 1);
    }

    public void testGetSetRank() throws Exception {
        assertEquals(sm1.getRank(), 3);
        assertEquals(sm2.getRank(), 0);
        assertEquals(sm3.getRank(), 1);

        sm1.setRank(2);
        assertEquals(sm1.getRank(), 2);
    }

    public void testSwapRank() throws Exception {
        assertEquals(sm1.getRank(), 3);
        assertEquals(sm2.getRank(), 0);

        sm1.swapRank(sm2);
        assertEquals(sm1.getRank(), 0);
        assertEquals(sm2.getRank(), 3);

        SessionMember.swapRank(sm1, sm2);
        assertEquals(sm1.getRank(), 3);
        assertEquals(sm2.getRank(), 0);
    }

    public void testToMap() throws Exception {
        Map<String, Object> content = sm1.toMap();
        assertTrue(content.containsKey(SessionMember.TEAM_ID));
        assertTrue(content.containsKey(SessionMember.TEAM_SEED));
        assertTrue(content.containsKey(SessionMember.TEAM_RANK));

        assertEquals(content.get(SessionMember.TEAM_ID), TeamStub.ID);
        assertEquals(content.get(SessionMember.TEAM_SEED), 2);
        assertEquals(content.get(SessionMember.TEAM_RANK), 3);
    }

    public void testCompareTo() throws Exception {
        assertEquals(sm1.compareTo(sm2), 1);
        assertEquals(sm1.compareTo(sm1), 0);
        assertEquals(sm3.compareTo(sm1), -1);
    }
}

class TeamStub extends Team {
    public static String ID = "stub_ID";

    public TeamStub(String team_name, List<String> member_ids, int color, boolean is_favorite) {
        super(team_name, member_ids, color, is_favorite);
    }

    @Override
    public String getId() {
        return ID;
    }
}