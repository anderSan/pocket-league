package info.andersonpa.polishhorseshoes.rulesets;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import info.andersonpa.polishhorseshoes.backend.ActiveGame;
import info.andersonpa.polishhorseshoes.db.DatabaseHelper;
import info.andersonpa.polishhorseshoes.db.Throw;
import info.andersonpa.polishhorseshoes.enums.DeadType;
import info.andersonpa.polishhorseshoes.enums.ThrowResult;
import info.andersonpa.polishhorseshoes.enums.ThrowType;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class RuleSet01BackTest {
	private ActiveGame ag;
    private RuleSet rs = new RuleSet01();
	private String gId = "test_game";
    private Throw ui_throw;

	@BeforeClass
	public static void clearDB() {
		DatabaseHelper db_helper = OpenHelperManager
                .getHelper(InstrumentationRegistry.getTargetContext(), DatabaseHelper.class);
        db_helper.clearAll();
        OpenHelperManager.releaseHelper();
	}

	@Before
	public void setUp() {
		Context context = InstrumentationRegistry.getTargetContext();
		ag = new ActiveGame(context, gId);
        ag.setSaveToDB(false);
        ag.setRuleSet(new RuleSet01());
        ui_throw = ag.getActiveThrow();
	}

	@Test
	public void testPreconditions() {
		assertEquals("Wrong ruleset", 1, ag.getRuleSet().getId());
	}

	@Test
	public void testFire_activate01() {
		// remember that fire counts are initial counts
        rs.setThrowType(ui_throw, ThrowType.POLE);
        ui_throw = ag.updateActiveThrowGetNext(ui_throw);
		checkFireCounts(0, 0, 0, "");

        rs.setThrowType(ui_throw, ThrowType.BALL_LOW);
        ui_throw = ag.updateActiveThrowGetNext(ui_throw);
        checkFireCounts(1, 1, 0, "");

        rs.setThrowType(ui_throw, ThrowType.BOTTLE);
        ui_throw = ag.updateActiveThrowGetNext(ui_throw);
        checkFireCounts(2, 1, 0, "");

        rs.setThrowType(ui_throw, ThrowType.STRIKE);
        ui_throw = ag.updateActiveThrowGetNext(ui_throw);
		checkFireCounts(3, 2, 0, "");

        rs.setThrowType(ui_throw, ThrowType.CUP);
        ui_throw = ag.updateActiveThrowGetNext(ui_throw);
        checkFireCounts(4, 2, 0, "");

        rs.setThrowType(ui_throw, ThrowType.BALL_RIGHT);
        ui_throw = ag.updateActiveThrowGetNext(ui_throw);
		checkFireCounts(5, 3, 0, "");
		checkThrow(5, ThrowType.FIRED_ON, ThrowResult.NA, DeadType.ALIVE, "");

        rs.setThrowType(ui_throw, ThrowType.POLE);
        ui_throw = ag.updateActiveThrowGetNext(ui_throw);
		checkFireCounts(6, 3, 0, "");
		checkThrow(6, ThrowType.POLE, ThrowResult.NA, DeadType.ALIVE, "");
		checkInitScore(6, 0, 0, "");

        rs.setThrowType(ui_throw, ThrowType.STRIKE);
        ui_throw = ag.updateActiveThrowGetNext(ui_throw);
		checkFireCounts(7, 4, 0, "");
		checkThrow(7, ThrowType.FIRED_ON, ThrowResult.NA, DeadType.ALIVE, "");
		checkInitScore(7, 2, 0, "");
	}

	@Test
	public void testFire_quench01() {
		// remember that fire counts are initial counts
        rs.setThrowType(ui_throw, ThrowType.BALL_RIGHT);
        ui_throw = ag.updateActiveThrowGetNext(ui_throw);
		checkFireCounts(0, 0, 0, "");

        rs.setThrowType(ui_throw, ThrowType.CUP);
        ui_throw = ag.updateActiveThrowGetNext(ui_throw);
		checkFireCounts(1, 0, 0, "");

        rs.setThrowType(ui_throw, ThrowType.BALL_HIGH);
        ui_throw = ag.updateActiveThrowGetNext(ui_throw);
		checkFireCounts(2, 0, 1, "");

        rs.setThrowType(ui_throw, ThrowType.POLE);
        ui_throw = ag.updateActiveThrowGetNext(ui_throw);
		checkFireCounts(3, 0, 1, "");


        rs.setThrowType(ui_throw, ThrowType.POLE);
        rs.setThrowResult(ui_throw, ThrowResult.DROP);
        ui_throw = ag.updateActiveThrowGetNext(ui_throw);
		checkFireCounts(4, 0, 2, "");

        rs.setThrowType(ui_throw, ThrowType.CUP);
        ui_throw = ag.updateActiveThrowGetNext(ui_throw);
		checkFireCounts(5, 1, 0, "");

        rs.setThrowType(ui_throw, ThrowType.POLE);
        ui_throw = ag.updateActiveThrowGetNext(ui_throw);
		checkFireCounts(6, 1, 1, "");
	}

	public void checkThrow(int throwIdx, int expThrowType, int expThrowResult,
			int expDeadType, String errMsg) {
		assertEquals(errMsg + " (" + throwIdx + ") Wrong throwType", expThrowType, ag.getThrow(throwIdx).throwType);
		assertEquals(errMsg + " (" + throwIdx + ") Wrong throwResult", expThrowResult, ag.getThrow(throwIdx).throwResult);
		assertEquals(errMsg + " (" + throwIdx + ") Wrong deadType", expDeadType, ag.getThrow(throwIdx).deadType);
	}

	public void checkFireCounts(int throwIdx, int expP1Cnt, int expP2Cnt,
			String errMsg) {
		int p1Cnt;
		int p2Cnt;
		if (throwIdx % 2 == 0) {
			// P1 is offense for even idx
			p1Cnt = ag.getThrow(throwIdx).offenseFireCount;
			p2Cnt = ag.getThrow(throwIdx).defenseFireCount;
		} else {
			// P2 is offense for odd idx
			p1Cnt = ag.getThrow(throwIdx).defenseFireCount;
			p2Cnt = ag.getThrow(throwIdx).offenseFireCount;
		}
		assertEquals(errMsg + "(" + throwIdx + ") Wrong P1 fire count", expP1Cnt, p1Cnt);
		assertEquals(errMsg + "(" + throwIdx + ") Wrong P2 fire count", expP2Cnt, p2Cnt);
	}

	public void checkInitScore(int throwIdx, int expP1Score, int expP2Score,
			String errMsg) {
		int p1Score;
		int p2Score;
		if (throwIdx % 2 == 0) {
			// P1 is offense for even idx
			p1Score = ag.getThrow(throwIdx).initialOffensivePlayerScore;
			p2Score = ag.getThrow(throwIdx).initialDefensivePlayerScore;
		} else {
			p1Score = ag.getThrow(throwIdx).initialDefensivePlayerScore;
			p2Score = ag.getThrow(throwIdx).initialOffensivePlayerScore;
		}
		assertEquals(errMsg + "(" + throwIdx + ") Wrong P1 score", expP1Score, p1Score);
		assertEquals(errMsg + "(" + throwIdx + ") Wrong P2 score", expP2Score, p2Score);
	}
}
