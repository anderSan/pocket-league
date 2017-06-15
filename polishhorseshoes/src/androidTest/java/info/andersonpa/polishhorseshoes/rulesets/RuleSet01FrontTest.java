package info.andersonpa.polishhorseshoes.rulesets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import info.andersonpa.polishhorseshoes.GameInProgress;
import info.andersonpa.polishhorseshoes.R;
import info.andersonpa.polishhorseshoes.backend.ActiveGame;
import info.andersonpa.polishhorseshoes.db.DatabaseHelper;
import info.andersonpa.polishhorseshoes.db.Throw;
import info.andersonpa.polishhorseshoes.enums.DeadType;
import info.andersonpa.polishhorseshoes.enums.RuleType;
import info.andersonpa.polishhorseshoes.enums.ThrowResult;
import info.andersonpa.polishhorseshoes.enums.ThrowType;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RuleSet01FrontTest {
    private Context targetContext;
    private ActiveGame ag;

    private ImageView vwDeadHigh;
    private ImageView vwDeadRight;
    private ImageView vwDeadLow;
    private ImageView vwDeadLeft;
    private ImageButton btnTrap;

    @Rule
    public ActivityTestRule<GameInProgress> mActivityRule = new ActivityTestRule<GameInProgress>(
            GameInProgress.class) {
        @Override
        protected Intent getActivityIntent() {
            targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent result = new Intent(targetContext, GameInProgress.class);
            result.putExtra("GID", "test_game");
            result.putExtra("RSID", RuleType.rs01);
            return result;
        }
    };

    @BeforeClass
    public static void clearDB() {
        DatabaseHelper db_helper = OpenHelperManager
                .getHelper(InstrumentationRegistry.getTargetContext(), DatabaseHelper.class);
        db_helper.clearAll();
        OpenHelperManager.releaseHelper();
    }

    @Before
    public void setUp() {
        ag = mActivityRule.getActivity().ag;
        ag.setSaveToDB(false);

        vwDeadHigh = (ImageView) mActivityRule.getActivity()
                .findViewById(info.andersonpa.polishhorseshoes.R.id.gip_dead_high);
        vwDeadRight = (ImageView) mActivityRule.getActivity()
                .findViewById(info.andersonpa.polishhorseshoes.R.id.gip_dead_right);
        vwDeadLow = (ImageView) mActivityRule.getActivity()
                .findViewById(info.andersonpa.polishhorseshoes.R.id.gip_dead_low);
        vwDeadLeft = (ImageView) mActivityRule.getActivity()
                .findViewById(info.andersonpa.polishhorseshoes.R.id.gip_dead_left);
        btnTrap = (ImageButton) mActivityRule.getActivity()
                .findViewById(info.andersonpa.polishhorseshoes.R.id.gip_button_trap);
    }

    @Test
    public void testPreconditions() {
        assertEquals("Wrong ruleset.", 1, ag.getRuleSet().getId());
    }

    @Test
    public void testFire_activate01() {
        // remember that fire counts are initial counts
        onView(withId(R.id.gip_button_pole)).perform(click()); // id 0
        checkFireCounts(0, 0, 0);

        onView(withId(R.id.gip_button_low)).perform(click()); // id 1
        checkFireCounts(1, 1, 0);

        onView(withId(R.id.gip_button_bottle)).perform(click());
        checkFireCounts(2, 1, 0);

        onView(withId(R.id.gip_button_strike)).perform(click()); // id 3
        checkFireCounts(3, 2, 0);

        onView(withId(R.id.gip_button_cup)).perform(click()); // id 4
        checkFireCounts(4, 2, 0);

        onView(withId(R.id.gip_button_right)).perform(click()); // id 5
        checkFireCounts(5, 3, 0);
        checkThrow(5, ThrowType.FIRED_ON, ThrowResult.NA, DeadType.ALIVE);

        onView(withId(R.id.gip_button_pole)).perform(click()); // id 6
        checkFireCounts(6, 3, 0);
        checkThrow(6, ThrowType.POLE, ThrowResult.NA, DeadType.ALIVE);
        checkInitScore(6, 0, 0);

        onView(withId(R.id.gip_button_strike)).perform(click()); // id 7
        checkFireCounts(7, 4, 0);
        checkThrow(7, ThrowType.FIRED_ON, ThrowResult.NA, DeadType.ALIVE);
        checkInitScore(7, 2, 0);
    }

    @Test
    public void testFire_quench01() {
        // remember that fire counts are initial counts
        onView(withId(R.id.gip_button_right)).perform(click()); // id 0
        checkFireCounts(0, 0, 0);

        onView(withId(R.id.gip_button_cup)).perform(click()); // id 1
        checkFireCounts(1, 0, 0);

        onView(withId(R.id.gip_button_high)).perform(click()); // id 2
        checkFireCounts(2, 0, 1);

        onView(withId(R.id.gip_button_pole)).perform(click()); // id 3
        checkFireCounts(3, 0, 1);

        onView(withId(R.id.numPicker_catch)).perform(swipeDown());
        onView(withId(R.id.gip_button_pole)).perform(click()); // id 4
        checkFireCounts(4, 0, 2);

        onView(withId(R.id.gip_button_cup)).perform(click()); // id 5
        checkFireCounts(5, 1, 0);

        onView(withId(R.id.gip_button_pole)).perform(click()); // id 6
        checkFireCounts(6, 1, 1);
    }

    @Test
    public void testWideThrows() {
        int activeColor = Color.RED;
        int inactiveColor = Color.LTGRAY;

        assertEquals(inactiveColor, getButtonColor(vwDeadHigh));
        onView(withId(R.id.gip_button_high)).perform(longClick());
        assertEquals(activeColor, getButtonColor(vwDeadHigh));
        onView(withId(R.id.gip_button_high)).perform(click());
        assertEquals(DeadType.HIGH, ag.getThrow(0).deadType);
        assertTrue("Dead high is broken.", ag.getRuleSet().isValid(ag.getThrow(0)));

        onView(withId(R.id.gip_button_right)).perform(longClick());
        assertEquals(activeColor, getButtonColor(vwDeadRight));
        onView(withId(R.id.gip_button_right)).perform(click());
        assertEquals(DeadType.RIGHT, ag.getThrow(1).deadType);
        assertTrue("Dead right is broken.", ag.getRuleSet().isValid(ag.getThrow(1)));

        onView(withId(R.id.gip_button_low)).perform(longClick());
        assertEquals(activeColor, getButtonColor(vwDeadLow));
        onView(withId(R.id.gip_button_low)).perform(click());
        assertEquals(DeadType.LOW, ag.getThrow(2).deadType);
        assertTrue("Dead low is broken.", ag.getRuleSet().isValid(ag.getThrow(2)));

        onView(withId(R.id.gip_button_left)).perform(longClick());
        assertEquals(activeColor, getButtonColor(vwDeadLeft));
        onView(withId(R.id.gip_button_left)).perform(click());
        assertEquals(DeadType.LEFT, ag.getThrow(3).deadType);
        assertTrue("Dead left is broken.", ag.getRuleSet().isValid(ag.getThrow(3)));
    }

    @Test
    public void testTraps() {
        // test a trap throw
        onView(withId(R.id.gip_button_trap)).perform(click());
        assertEquals(2, btnTrap.getDrawable().getLevel());
        onView(withId(R.id.gip_button_strike)).perform(click());
        // TODO: how to go back to a previous throw to check UI?
        assertEquals(ThrowType.TRAP, ag.getThrow(0).throwType);
        assertTrue("Trap is broken.", ag.getRuleSet().isValid(ag.getThrow(0)));

        // test a redeemed trap throw
        onView(withId(R.id.gip_button_trap)).perform(click());
        assertEquals(2, btnTrap.getDrawable().getLevel());
        onView(withId(R.id.gip_button_bottle)).perform(click());
        assertEquals(ThrowType.TRAP_REDEEMED, ag.getThrow(1).throwType);
        assertTrue("Redeemed trap is broken.", ag.getRuleSet().isValid(ag.getThrow(1)));
    }

    public int getButtonColor(View view) {
        return ((ColorDrawable) view.getBackground()).getColor();
    }

    public void checkThrow(int t_idx, int exp_throwtype, int exp_throwresult, int exp_deadtype) {
        Throw t = ag.getThrow(t_idx);
        assertEquals(formatErrMsg(t_idx, "ThrowType"), exp_throwtype, t.throwType);
        assertEquals(formatErrMsg(t_idx, "ThrowResult"), exp_throwresult, t.throwResult);
        assertEquals(formatErrMsg(t_idx, "DeadType"), exp_deadtype, t.deadType);
    }

    public void checkThrowSpecial(int throwIdx, String errMsg) {
        // TODO: add checks for special marks
    }

    public void checkFireCounts(int t_idx, int exp_p1_fire, int exp_p2_fire) {
        int p1_fire;
        int p2_fire;
        if (t_idx % 2 == 0) {
            // P1 is offense for even idx
            p1_fire = ag.getThrow(t_idx).offenseFireCount;
            p2_fire = ag.getThrow(t_idx).defenseFireCount;
        } else {
            // P2 is offense for odd idx
            p1_fire = ag.getThrow(t_idx).defenseFireCount;
            p2_fire = ag.getThrow(t_idx).offenseFireCount;
        }
        assertEquals(formatErrMsg(t_idx, "P1 fire count"), exp_p1_fire, p1_fire);
        assertEquals(formatErrMsg(t_idx, "P1 fire count"), exp_p2_fire, p2_fire);
    }

    public void checkInitScore(int t_idx, int exp_p1_pts, int exp_p2_pts) {
        int p1Score;
        int p2Score;
        if (t_idx % 2 == 0) {
            // P1 is offense for even idx
            p1Score = ag.getThrow(t_idx).initialOffensivePlayerScore;
            p2Score = ag.getThrow(t_idx).initialDefensivePlayerScore;
        } else {
            p1Score = ag.getThrow(t_idx).initialDefensivePlayerScore;
            p2Score = ag.getThrow(t_idx).initialOffensivePlayerScore;
        }
        assertEquals(formatErrMsg(t_idx, "P1 score"), exp_p1_pts, p1Score);
        assertEquals(formatErrMsg(t_idx, "P2 score"), exp_p2_pts, p2Score);
    }

    public String formatErrMsg(int t_idx, String post) {
        return "(Throw " + t_idx + ") Wrong " + post + ".";
    }
}
