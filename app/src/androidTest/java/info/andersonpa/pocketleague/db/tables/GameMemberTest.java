package info.andersonpa.pocketleague.db.tables;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class GameMemberTest {
    private GameMember gm1;
    private Team t1;

    @Before
    public void setUp() throws Exception {
        t1 = new TeamStub("Team1", null);
        gm1 = new GameMember(t1);
    }

    @Test
    public void testGetTeam() throws Exception {
        Team t = gm1.getTeam();
        assertEquals(t1.getName(), t.getName());
        assertEquals(t1.getColor(), t.getColor());
        assertEquals(t1.getIsActive(), t.getIsActive());
    }

    @Test
    public void testGetSetScore() throws Exception {
        assertEquals(gm1.getScore(), 0);
        gm1.setScore(3);
        assertEquals(gm1.getScore(), 3);
    }

    @Test
    public void testGetSetPlayOrder() throws Exception {
        assertEquals(gm1.play_order, -1);
        gm1.setPlayOrder(3);
        assertEquals(gm1.play_order, 3);
    }

    @Test
    public void testToMap() throws Exception {
        Map<String, Object> content = gm1.toMap();
        assertTrue(content.containsKey(GameMember.TEAM_ID));
        assertTrue(content.containsKey(GameMember.SCORE));
        assertTrue(content.containsKey(GameMember.PLAY_ORDER));

        assertEquals(TeamStub.ID, content.get(GameMember.TEAM_ID));
        assertEquals(0, content.get(GameMember.SCORE));
        assertEquals(-1, content.get(GameMember.PLAY_ORDER));
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("Team1", gm1.toString());
    }

    @Test
    public void testCompareTo() throws Exception {
        gm1.setPlayOrder(1);
        GameMember gm2 = new GameMember(t1);
        gm2.setPlayOrder(2);

        assertEquals(-1, gm1.compareTo(gm2));
        assertEquals(0, gm1.compareTo(gm1));
        assertEquals(1, gm2.compareTo(gm1));
    }

    @Test
    public void testCompareToScore() throws Exception {
        gm1.setScore(1);
        GameMember gm2 = new GameMember(t1);
        gm2.setScore(2);

        assertEquals(1, GameMember.SCORE_ORDER.compare(gm1, gm2));
        assertEquals(0, GameMember.SCORE_ORDER.compare(gm1, gm1));
        assertEquals(-1, GameMember.SCORE_ORDER.compare(gm2, gm1));
    }
}
