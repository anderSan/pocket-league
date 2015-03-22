package com.twobits.pocketleague.db.tables;

import junit.framework.TestCase;

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
        t1 = new TeamStub("Team1", null);
        t2 = new TeamStub("Team2", null);
        sm1 = new SessionMember(t1, 2, 3);
        sm2 = new SessionMember(t2, 3);
        sm3 = new SessionMember();
    }

    public void testGetTeam() throws Exception {
        Team t = sm1.getTeam();
        assertEquals(t1.getName(), t.getName());
        assertEquals(t1.getColor(), t.getColor());
        assertEquals(t1.getIsActive(), t.getIsActive());

        assertNull(sm3.getTeam());
    }

    public void testGetSeed() throws Exception {
        assertEquals(2, sm1.getSeed());
        assertEquals(3, sm2.getSeed());
        assertEquals(0, sm3.getSeed());
    }

    public void testGetSetRank() throws Exception {
        assertEquals(3, sm1.getRank());
        assertEquals(0, sm2.getRank());
        assertEquals(0, sm3.getRank());

        sm1.setRank(2);
        assertEquals(2, sm1.getRank());
    }

    public void testSwapRank() throws Exception {
        assertEquals(3, sm1.getRank());
        assertEquals(0, sm2.getRank());

        sm1.swapRank(sm2);
        assertEquals(0, sm1.getRank());
        assertEquals(3, sm2.getRank());

        SessionMember.swapRank(sm1, sm2);
        assertEquals(3, sm1.getRank());
        assertEquals(0, sm2.getRank());
    }

    public void testToMap() throws Exception {
        Map<String, Object> content = sm1.toMap();
        assertTrue(content.containsKey(SessionMember.TEAM_ID));
        assertTrue(content.containsKey(SessionMember.TEAM_SEED));
        assertTrue(content.containsKey(SessionMember.TEAM_RANK));

        assertEquals(TeamStub.ID, content.get(SessionMember.TEAM_ID));
        assertEquals(2, content.get(SessionMember.TEAM_SEED));
        assertEquals(3, content.get(SessionMember.TEAM_RANK));
    }

    public void testCompareTo() throws Exception {
        assertEquals(1, sm1.compareTo(sm2));
        assertEquals(0, sm1.compareTo(sm1));
        assertEquals(-1, sm3.compareTo(sm1));
    }

    public void testCompareToSeed() throws Exception {
        assertEquals(1, SessionMember.SEED_ORDER.compare(sm1, sm3));
        assertEquals(0, SessionMember.SEED_ORDER.compare(sm1, sm1));
        assertEquals(-1, SessionMember.SEED_ORDER.compare(sm3, sm1));
    }
}