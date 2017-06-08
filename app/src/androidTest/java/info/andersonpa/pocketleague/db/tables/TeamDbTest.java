package info.andersonpa.pocketleague.db.tables;

import android.graphics.Color;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TeamDbTest extends DbBaseTestCase {
    private Player p1;
    private Player p2;
    private Player p3;
    private Player p4;
    private Player p5;
    private Player p6;

    private Team t1;
    private Team t2;
    private Team t3;
    private Team t4;
    private Team t5;
    private Team t6;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        p1 = new Player(database, "Tim");
        p1.setIsActive(false);
        p1.update();
        p2 = new Player(database, "Bob");
        p2.update();
        p3 = new Player(database, "Sue");
        p3.update();
        p4 = new Player(database, "Dick");
        p4.setIsActive(false);
        p4.setIsFavorite(true);
        p4.update();
        p5 = new Player(database, "Alice", "a", "e", true, false, true, false, 44, 12,
                Color.BLACK, true, null);
        p5.update();
        p6 = new Player(database, "Steve");
        p6.setIsFavorite(true);
        p6.update();


        t1 = new Team(database, "Us", Arrays.asList(p1, p2));
        t1.update();
        t2 = new Team(database, "Double Trouble", Arrays.asList(p5, p6), Color.YELLOW, true);
        t2.update();
        t3 = new Team(database, "A Team", Arrays.asList(p5, p1, p3), Color.BLACK, true);
        t3.setIsActive(false);
        t3.update();
        t4 = new Team(database, "Triple Threat", Arrays.asList(p2, p3, p1), Color.BLUE, false);
        t4.update();
        t5 = new Team(database, "Trees Company", Arrays.asList(p2, p6, p4), Color.GREEN, true);
        t5.update();
        t6 = new Team(database, "Bigly", Arrays.asList(p2, p3, p1, p6, p5, p4), Color.RED, true);
        t6.update();
    }

    @Test
    public void testConstructor() throws Exception {
        Team team = new Team("No doc team", null);
        assertNull(team.getId());

        team = new Team(database, "Doc team", null);
        assertNotNull(team.getId());
    }

    @Test
    public void testGetFromId() throws Exception {
        Team t = Team.getFromId(database, t1.getId());

        assertNotNull(t);
        assertEquals(t1.getId(), t.getId());
        assertEquals(t1.document.getCurrentRevisionId(), t.document.getCurrentRevisionId());
    }

    @Test
    public void testFindByName() throws Exception {
        Team t = Team.findByName(database, "Test Other Team");
        assertNull(t);

        t = Team.findByName(database, "A Team");
        assertNotNull(t);
        assertEquals(t3.getId(), t.getId());
        assertEquals(t3.document.getCurrentRevisionId(), t.document.getCurrentRevisionId());
        assertEquals("A Team", t.getName());

        t = Team.findByName(database, "Bigly");
        assertNotNull(t);
        assertEquals(t6.getId(), t.getId());
        assertEquals(t6.document.getCurrentRevisionId(), t.document.getCurrentRevisionId());
        assertEquals("Bigly", t.getName());
    }

    @Test
    public void testGetAllTeams() throws Exception {
        List<Team> all_teams = Team.getAllTeams(database);
        assertEquals(12, all_teams.size());
    }

    @Test
    public void testGetTeams() throws Exception {
        List<Team> all_teams = Team.getTeams(database, 1, false, false);
        assertEquals(2, all_teams.size());
        assertEquals("Dick", all_teams.get(0).getName());
        assertEquals("Tim", all_teams.get(1).getName());

        all_teams = Team.getTeams(database, 2, false, false);
        assertEquals(0, all_teams.size());

        all_teams = Team.getTeams(database, 3, false, false);
        assertEquals(1, all_teams.size());
        assertEquals("A Team", all_teams.get(0).getName());

        all_teams = Team.getTeams(database, 4, false, false);
        assertEquals(0, all_teams.size());

        all_teams = Team.getTeams(database, 6, false, false);
        assertEquals(0, all_teams.size());

    }

    @Test
    public void testGetTeamsActive() throws Exception {
        List<Team> all_teams = Team.getTeams(database, 1, true, false);
        assertEquals(4, all_teams.size());
        assertEquals("Alice", all_teams.get(0).getName());
        assertEquals("Bob", all_teams.get(1).getName());
        assertEquals("Steve", all_teams.get(2).getName());
        assertEquals("Sue", all_teams.get(3).getName());

        all_teams = Team.getTeams(database, 2, true, false);
        assertEquals(2, all_teams.size());
        assertEquals("Double Trouble", all_teams.get(0).getName());
        assertEquals("Us", all_teams.get(1).getName());

        all_teams = Team.getTeams(database, 3, true, false);
        assertEquals(2, all_teams.size());
        assertEquals("Trees Company", all_teams.get(0).getName());
        assertEquals("Triple Threat", all_teams.get(1).getName());

        all_teams = Team.getTeams(database, 4, true, false);
        assertEquals(0, all_teams.size());

        all_teams = Team.getTeams(database, 6, true, false);
        assertEquals(1, all_teams.size());
        assertEquals("Bigly", all_teams.get(0).getName());
    }

    @Test
    public void testGetTeamsFavorite() throws Exception {
        List<Team> all_teams = Team.getTeams(database, 1, false, true);
        assertEquals(1, all_teams.size());
        assertEquals("Dick", all_teams.get(0).getName());

        all_teams = Team.getTeams(database, 2, false, true);
        assertEquals(0, all_teams.size());

        all_teams = Team.getTeams(database, 3, false, true);
        assertEquals(1, all_teams.size());
        assertEquals("A Team", all_teams.get(0).getName());

        all_teams = Team.getTeams(database, 4, false, true);
        assertEquals(0, all_teams.size());

        all_teams = Team.getTeams(database, 6, false, true);
        assertEquals(0, all_teams.size());
    }

    @Test
    public void testGetTeamsActiveFavorite() throws Exception {
        List<Team> all_teams = Team.getTeams(database, 1, true, true);
        assertEquals(2, all_teams.size());
        assertEquals("Alice", all_teams.get(0).getName());
        assertEquals("Steve", all_teams.get(1).getName());

        all_teams = Team.getTeams(database, 2, true, true);
        assertEquals(1, all_teams.size());
        assertEquals("Double Trouble", all_teams.get(0).getName());

        all_teams = Team.getTeams(database, 3, true, true);
        assertEquals(1, all_teams.size());
        assertEquals("Trees Company", all_teams.get(0).getName());

        all_teams = Team.getTeams(database, 4, true, true);
        assertEquals(0, all_teams.size());

        all_teams = Team.getTeams(database, 6, true, true);
        assertEquals(1, all_teams.size());
        assertEquals("Bigly", all_teams.get(0).getName());
    }

    @Test
    public void testGetTeamSizes() throws Exception {
        List<Integer> team_sizes = Team.getTeamSizes(database);
        assertEquals(4, team_sizes.size());
        assertEquals(1, (int) team_sizes.get(0));
        assertEquals(2, (int) team_sizes.get(1));
        assertEquals(3, (int) team_sizes.get(2));
        assertEquals(6, (int) team_sizes.get(3));
    }

    @Test
    public void testGetMembers() throws Exception {
        List<Player> members = t3.getMembers();
        assertEquals(3, members.size());
        assertEquals("Alice", members.get(0).getName());
        assertEquals(p5.getId(), members.get(0).getId());
        assertEquals("Tim", members.get(1).getName());
        assertEquals(p1.getId(), members.get(1).getId());
        assertEquals("Sue", members.get(2).getName());
        assertEquals(p3.getId(), members.get(2).getId());

        members = t6.getMembers();
        assertEquals(6, members.size());
        assertEquals("Bob", members.get(0).getName());
        assertEquals(p2.getId(), members.get(0).getId());
        assertEquals("Sue", members.get(1).getName());
        assertEquals(p3.getId(), members.get(1).getId());
        assertEquals("Tim", members.get(2).getName());
        assertEquals(p1.getId(), members.get(2).getId());
        assertEquals("Steve", members.get(3).getName());
        assertEquals(p6.getId(), members.get(3).getId());
        assertEquals("Alice", members.get(4).getName());
        assertEquals(p5.getId(), members.get(4).getId());
        assertEquals("Dick", members.get(5).getName());
        assertEquals(p4.getId(), members.get(5).getId());

        members = p1.getMembers();
        assertEquals(1, members.size());
        assertEquals("Tim", members.get(0).getName());
    }

    @Test
    public void testGetSize() throws Exception {
        assertEquals(2, t1.getSize());
        assertEquals(2, t2.getSize());
        assertEquals(3, t3.getSize());
        assertEquals(3, t4.getSize());
        assertEquals(3, t5.getSize());
        assertEquals(6, t6.getSize());
        assertEquals(1, p2.getSize());
        assertEquals(1, p5.getSize());
    }

    @Test
    public void testExists() throws Exception {
        assertTrue(t1.exists());
        assertTrue(t2.exists());
        Team t3 = new Team(database, "Test Team C", Arrays.asList(p1, p2));
        assertFalse(t3.exists());

        assertTrue(p1.exists());
        assertTrue(p2.exists());
        Player p4 = new Player(database, "Scooby");
        assertFalse(p4.exists());
    }

    @Test
    public void testStaticExists() throws Exception {
        assertTrue(Team.exists(database, t1.getName()));
        assertTrue(Team.exists(database, t2.getName()));
        assertFalse(Team.exists(database, "Nonexistent Team Name"));
    }
}
