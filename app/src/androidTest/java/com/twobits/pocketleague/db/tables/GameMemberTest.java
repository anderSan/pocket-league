package com.twobits.pocketleague.db.tables;

import junit.framework.TestCase;

import java.util.Map;

public class GameMemberTest extends TestCase {
    GameMember gm1;
    Team t1;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        t1 = new TeamStub("Team1", null);
        gm1 = new GameMember(t1);
    }

    public void testGetTeam() throws Exception {
        Team t = gm1.getTeam();
        assertEquals(t1.getName(), t.getName());
        assertEquals(t1.getColor(), t.getColor());
        assertEquals(t1.getIsActive(), t.getIsActive());
    }

    public void testGetSetScore() throws Exception {
        assertEquals(gm1.getScore(), 0);
        gm1.setScore(3);
        assertEquals(gm1.getScore(), 3);
    }

    public void testGetSetPlayOrder() throws Exception {
        assertEquals(gm1.play_order, -1);
        gm1.setPlayOrder(3);
        assertEquals(gm1.play_order, 3);
    }

    public void testToMap() throws Exception {
        Map<String, Object> content = gm1.toMap();
        assertTrue(content.containsKey(GameMember.TEAM_ID));
        assertTrue(content.containsKey(GameMember.SCORE));
        assertTrue(content.containsKey(GameMember.PLAY_ORDER));

        assertEquals(TeamStub.ID, content.get(GameMember.TEAM_ID));
        assertEquals(0, content.get(GameMember.SCORE));
        assertEquals(-1, content.get(GameMember.PLAY_ORDER));
    }

    public void testCompareTo() throws Exception {
        gm1.setPlayOrder(1);
        GameMember gm2 = new GameMember(t1);
        gm2.setPlayOrder(2);

        assertEquals(-1, gm1.compareTo(gm2));
        assertEquals(0, gm1.compareTo(gm1));
        assertEquals(1, gm2.compareTo(gm1));
    }

    public void testCompareToScore() throws Exception {
        gm1.setScore(1);
        GameMember gm2 = new GameMember(t1);
        gm2.setScore(2);

        assertEquals(1, GameMember.SCORE_ORDER.compare(gm1, gm2));
        assertEquals(0, GameMember.SCORE_ORDER.compare(gm1, gm1));
        assertEquals(-1, GameMember.SCORE_ORDER.compare(gm2, gm1));
    }
}
