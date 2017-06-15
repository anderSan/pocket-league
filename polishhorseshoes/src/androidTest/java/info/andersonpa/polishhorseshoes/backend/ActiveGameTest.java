package info.andersonpa.polishhorseshoes.backend;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import info.andersonpa.polishhorseshoes.db.DatabaseHelper;
import info.andersonpa.polishhorseshoes.db.Game;
import info.andersonpa.polishhorseshoes.db.Throw;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ActiveGameTest {
    ActiveGame ag;
    private Dao<Game, Long> gDao;
    private Dao<Throw, Long> tDao;
    String gId = "test_game";

    @BeforeClass
    public static void clearDB() {
        DatabaseHelper db_helper = OpenHelperManager
                .getHelper(InstrumentationRegistry.getTargetContext(), DatabaseHelper.class);
        db_helper.clearAll();
        OpenHelperManager.releaseHelper();
    }

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        ag = new ActiveGame(context, gId);
        ag.setSaveToDB(false);
        gDao = Game.getDao(context);
        tDao = Throw.getDao(context);
    }

    @Test
    public void testInitialState() throws Exception {
        Game g = gDao.queryBuilder().where().eq(Game.POCKETLEAGUE_ID, gId).queryForFirst();
        assertEquals(g.getId(), ag.getGame().getId());
        assertEquals(0, ag.getActiveIdx());
        assertEquals("No session", ag.getSessionName());
        assertEquals("No venue", ag.getVenueName());
        assertEquals("Team 1", ag.getTeamNames()[0]);
        assertEquals("Team 2", ag.getTeamNames()[1]);
        assertEquals(1, ag.getRuleSet().getId());
    }
}