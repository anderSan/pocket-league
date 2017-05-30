package info.andersonpa.pocketleague.db.tables;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class SessionMemberTest {
    private SessionMember sm1;
    private SessionMember sm2;
    private SessionMember sm3;
    private Team t1;
    private Team t2;

    @Before
    public void setUp() throws Exception {
        t1 = new TeamStub("Team1", null);
        t2 = new TeamStub("Team2", null);
        sm1 = new SessionMember(t1, 2, 3);
        sm2 = new SessionMember(t2, 3);
        sm3 = new SessionMember();
    }

    @Test
    public void testGetTeam() throws Exception {
        Team t = sm1.getTeam();
        assertEquals(t1.getName(), t.getName());
        assertEquals(t1.getColor(), t.getColor());
        assertEquals(t1.getIsActive(), t.getIsActive());

        assertNull(sm3.getTeam());
    }

    @Test
    public void testGetSeed() throws Exception {
        assertEquals(2, sm1.getSeed());
        assertEquals(3, sm2.getSeed());
        assertEquals(0, sm3.getSeed());
    }

    @Test
    public void testGetSetRank() throws Exception {
        assertEquals(3, sm1.getRank());
        assertEquals(0, sm2.getRank());
        assertEquals(0, sm3.getRank());

        sm1.setRank(2);
        assertEquals(2, sm1.getRank());
    }

    @Test
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

    @Test
    public void testToMap() throws Exception {
        Map<String, Object> content = sm1.toMap();
        assertTrue(content.containsKey(SessionMember.TEAM_ID));
        assertTrue(content.containsKey(SessionMember.TEAM_SEED));
        assertTrue(content.containsKey(SessionMember.TEAM_RANK));

        assertEquals(TeamStub.ID, content.get(SessionMember.TEAM_ID));
        assertEquals(2, content.get(SessionMember.TEAM_SEED));
        assertEquals(3, content.get(SessionMember.TEAM_RANK));
    }

    @Test
    public void testCompareTo() throws Exception {
        assertEquals(1, sm1.compareTo(sm2));
        assertEquals(0, sm1.compareTo(sm1));
        assertEquals(-1, sm3.compareTo(sm1));
    }

    @Test
    public void testCompareToSeed() throws Exception {
        assertEquals(1, SessionMember.SEED_ORDER.compare(sm1, sm3));
        assertEquals(0, SessionMember.SEED_ORDER.compare(sm1, sm1));
        assertEquals(-1, SessionMember.SEED_ORDER.compare(sm3, sm1));
    }
}