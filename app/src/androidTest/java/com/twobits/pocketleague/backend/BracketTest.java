package com.twobits.pocketleague.backend;

import android.test.AndroidTestCase;
import android.widget.RelativeLayout;

import com.twobits.pocketleague.db.tables.Player;
import com.twobits.pocketleague.db.tables.SessionMember;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BracketTest extends AndroidTestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {

    }

    public void testGetTier() throws Exception {
        assertEquals(0, Bracket.getTier(0, 0));
        assertEquals(0, Bracket.getTier(0, 1));
        assertEquals(1, Bracket.getTier(1, 2));
        assertEquals(0, Bracket.getTier(1, 4));
        assertEquals(1, Bracket.getTier(2, 4));
        assertEquals(2, Bracket.getTier(3, 4));
        assertEquals(0, Bracket.getTier(2, 8));
        assertEquals(1, Bracket.getTier(4, 8));
        assertEquals(2, Bracket.getTier(6, 8));
        assertEquals(3, Bracket.getTier(7, 8));
        assertEquals(0, Bracket.getTier(6, 16));
        assertEquals(1, Bracket.getTier(9, 16));
        assertEquals(2, Bracket.getTier(12, 16));
        assertEquals(3, Bracket.getTier(14, 16));
        assertEquals(4, Bracket.getTier(15, 16));
    }

    public void testFindViewAboveId() throws Exception {
        Bracket br;
        SessionMember sm1 = new SessionMember(new Player("p1"), 1);
        SessionMember sm2 = new SessionMember();
        br = new Bracket(Arrays.asList(sm1, sm1, sm1, sm1), new RelativeLayout(getContext()));
        assertEquals(3, br.findViewAboveId(1000));
        assertEquals(1000, br.findViewAboveId(2000));
        assertEquals(2000, br.findViewAboveId(1001));
        assertEquals(1001, br.findViewAboveId(2001));
        assertEquals(1000, br.findViewAboveId(1002));
        assertEquals(1002, br.findViewAboveId(2002));

        br = new Bracket(Arrays.asList(sm1, sm2, sm1, sm1), new RelativeLayout(getContext()));
        assertEquals(3, br.findViewAboveId(1002));
        assertEquals(1002, br.findViewAboveId(1001));
        assertEquals(1001, br.findViewAboveId(2001));
        assertEquals(1002, br.findViewAboveId(2002));

        br = new Bracket(Arrays.asList(sm1, sm2, sm1, sm1), new RelativeLayout(getContext()));
        br.setMatchOffset(5);
        assertEquals(3, br.findViewAboveId(1007));
        assertEquals(1007, br.findViewAboveId(1006));
        assertEquals(1006, br.findViewAboveId(2006));
        assertEquals(1007, br.findViewAboveId(2007));
    }

    public void testGenerateReseedMatchIds() throws Exception {
        LinkedList<Integer> match_ids = Bracket.generateReseedMatchIds(8);
        List<Integer> expected = Arrays.asList(2, 3, 5, 6, 7);
        assertEquals(5, match_ids.size());
        for (Integer ii : expected) {
            assertEquals(ii, match_ids.pop());
        }

        match_ids = Bracket.generateReseedMatchIds(32);
        expected = Arrays.asList(10, 11, 14, 15, 21, 23, 26, 27, 29, 30, 31);
        assertEquals(11, match_ids.size());
        for (Integer ii : expected) {
            assertEquals(ii, match_ids.pop());
        }

        match_ids = Bracket.generateReseedMatchIds(128);
        assertEquals(23, match_ids.size());

        match_ids = Bracket.generateReseedMatchIds(512);
        assertEquals(47, match_ids.size());
    }

    public void testGenerateRespawnIds() throws Exception {
        LinkedList<Integer> respawn_ids = Bracket.generateRespawnIds(8);
        List<Integer> expected = Arrays.asList(0, 1, 2, 3, 4);
        assertEquals(5, respawn_ids.size());
        for (Integer ii : expected) {
            assertEquals(ii, respawn_ids.pop());
        }

        respawn_ids = Bracket.generateRespawnIds(32);
        expected = Arrays.asList(0, 1, 2, 4, 3, 5, 6, 7, 8, 9, 10);
        assertEquals(11, respawn_ids.size());
        for (Integer ii : expected) {
            assertEquals(ii, respawn_ids.pop());
        }
    }

    public void testIsUpperView() throws Exception {
        assertTrue(Bracket.isUpperView(1001));
        assertFalse(Bracket.isUpperView(2011));
    }

    public void testFactorTwos() throws Exception {
        assertEquals(0, Bracket.factorTwos(1));
        assertEquals(1, Bracket.factorTwos(2));
        assertEquals(2, Bracket.factorTwos(3));
        assertEquals(2, Bracket.factorTwos(4));
        assertEquals(3, Bracket.factorTwos(7));
        assertEquals(4, Bracket.factorTwos(9));
        assertEquals(5, Bracket.factorTwos(19));
    }
}