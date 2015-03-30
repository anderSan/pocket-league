package com.twobits.pocketleague.backend;

import junit.framework.Assert;
import junit.framework.TestCase;

public class LeagueTableTest extends TestCase {

    public void testMemberPositionsToMatchId() throws Exception {
        // check upper triangle
        assertEquals(0, LeagueTable.memberPositionsToMatchId(new int[]{0,0}, 5));
        assertEquals(4, LeagueTable.memberPositionsToMatchId(new int[]{0,4}, 5));
        assertEquals(9, LeagueTable.memberPositionsToMatchId(new int[]{1,4}, 5));
        assertEquals(6, LeagueTable.memberPositionsToMatchId(new int[]{1,3}, 4));

        // check lower triangle
        assertEquals(0, LeagueTable.memberPositionsToMatchId(new int[]{0,0}, 5));
        assertEquals(4, LeagueTable.memberPositionsToMatchId(new int[]{0,4}, 5));
        assertEquals(9, LeagueTable.memberPositionsToMatchId(new int[]{1,4}, 5));
        assertEquals(6, LeagueTable.memberPositionsToMatchId(new int[]{1,3}, 4));

        // out of bounds indices
        try {
            LeagueTable.memberPositionsToMatchId(new int[]{5,0}, 5);
            Assert.fail();
        } catch (ArrayIndexOutOfBoundsException e) {
            // success
        }

        try {
            LeagueTable.memberPositionsToMatchId(new int[]{0,5}, 5);
            Assert.fail();
        } catch (ArrayIndexOutOfBoundsException e) {
            // success
        }
    }
}