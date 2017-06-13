package info.andersonpa.polishhorseshoes.rulesets;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import info.andersonpa.polishhorseshoes.GameInProgress;
import info.andersonpa.polishhorseshoes.R;
import info.andersonpa.polishhorseshoes.backend.ActiveGame;
import info.andersonpa.polishhorseshoes.enums.DeadType;
import info.andersonpa.polishhorseshoes.enums.RuleType;
import info.andersonpa.polishhorseshoes.enums.ThrowResult;
import info.andersonpa.polishhorseshoes.enums.ThrowType;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RuleSet01FrontTest {
    private ActiveGame ag;

    private NumberPicker mPicker;

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
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            Intent result = new Intent(targetContext, GameInProgress.class);
            result.putExtra("GID", "testgame");
            return result;
        }
    };

    @Before
    public void setUp() throws Exception {
        Intent intent = new Intent();
        // Long gId = (long) 1;
        // intent.putExtra("GID", gId);
        int rsId = RuleType.rs01;
        intent.putExtra("RSID", rsId);


        mPicker = (NumberPicker) mActivityRule.getActivity()
                .findViewById(info.andersonpa.polishhorseshoes.R.id.numPicker_catch);
//        mActivityRule.getActivity().ag.getRuleSet() = RuleType.RS01;
        ag = mActivityRule.getActivity().ag;

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
        assertEquals("Wrong ruleset", 1, ag.getRuleSet().getId());
        assertTrue(mPicker.getValue() == 1);
    }

    @Test
    public void testFire_activate01() {
        // remember that fire counts are initial counts
        onView(withId(R.id.gip_button_pole)).perform(click()); // id 0
        checkFireCounts(0, 0, 0, "");

        onView(withId(R.id.gip_button_low)).perform(click()); // id 1
        checkFireCounts(1, 1, 0, "");

        onView(withId(R.id.gip_button_bottle)).perform(click());
        checkFireCounts(2, 1, 0, "");

        onView(withId(R.id.gip_button_strike)).perform(click()); // id 3
        checkFireCounts(3, 2, 0, "");

        onView(withId(R.id.gip_button_cup)).perform(click()); // id 4
        checkFireCounts(4, 2, 0, "");

        onView(withId(R.id.gip_button_right)).perform(click()); // id 5
        checkFireCounts(5, 3, 0, "");
        checkThrow(5, ThrowType.FIRED_ON, ThrowResult.NA, DeadType.ALIVE, "");

        onView(withId(R.id.gip_button_pole)).perform(click()); // id 6
        checkFireCounts(6, 3, 0, "");
        checkThrow(6, ThrowType.POLE, ThrowResult.NA, DeadType.ALIVE, "");
        checkInitScore(6, 0, 0, "");

        onView(withId(R.id.gip_button_strike)).perform(click()); // id 7
        checkFireCounts(7, 4, 0, "");
        checkThrow(7, ThrowType.FIRED_ON, ThrowResult.NA, DeadType.ALIVE, "");
        checkInitScore(7, 2, 0, "");
    }

//    @Test
//    public void testFire_quench01() {
//        // remember that fire counts are initial counts
//        onView(withId(R.id.gip_button_right)).perform(click()); // id 0
//        checkFireCounts(0, 0, 0, "");
//
//        onView(withId(R.id.gip_button_cup)).perform(click()); // id 1
//        checkFireCounts(1, 0, 0, "");
//
//        onView(withId(R.id.gip_button_high)).perform(click()); // id 2
//        checkFireCounts(2, 0, 1, "");
//
//        onView(withId(R.id.gip_button_pole)).perform(click()); // id 3
//        checkFireCounts(3, 0, 1, "");
//
//        onView(withId(R.id.gip_button_pole)).perform(swipeDown());
////        TouchUtils.dragViewToBottom(this, mActivity, mPicker);
//        onView(withId(R.id.gip_button_pole)).perform(click()); // id 4
//        checkFireCounts(4, 0, 2, "");
//
//        onView(withId(R.id.gip_button_cup)).perform(click()); // id 5
//        checkFireCounts(5, 1, 0, "");
//
//        onView(withId(R.id.gip_button_pole)).perform(click()); // id 6
//        checkFireCounts(6, 1, 1, "");
//    }

//    @Test
//    public void testWideThrows() {
//        int activeColor = Color.RED;
//        int inactiveColor = Color.LTGRAY;
//
//        assertEquals(inactiveColor, getButtonColor(vwDeadHigh));
//        onView(withId(R.id.gip_button_high)).perform(click());
//        assertEquals(activeColor, getButtonColor(vwDeadHigh));
//        onView(withId(R.id.gip_button_high)).perform(click());
//        assertEquals(DeadType.HIGH, ag.getThrow(0).deadType);
//        assertTrue("dead high invalid", ag.getRuleSet().isValid(ag.getThrow(0)));
//
//        onView(withId(R.id.gip_button_right)).perform(click());
//        assertEquals(activeColor, getButtonColor(vwDeadRight));
//        onView(withId(R.id.gip_button_right)).perform(click());
//        assertEquals(DeadType.RIGHT, ag.getThrow(1).deadType);
//        assertTrue(ag.getRuleSet().isValid(ag.getThrow(2)));
//
//        onView(withId(R.id.gip_button_low)).perform(click());
//        assertEquals(activeColor, getButtonColor(vwDeadLow));
//        onView(withId(R.id.gip_button_low)).perform(click());
//        assertEquals(DeadType.LOW, ag.getThrow(2).deadType);
//        assertTrue(ag.getRuleSet().isValid(ag.getThrow(3)));
//
//        onView(withId(R.id.gip_button_left)).perform(click());
//        assertEquals(activeColor, getButtonColor(vwDeadLeft));
//        onView(withId(R.id.gip_button_left)).perform(click());
//        assertEquals(DeadType.LEFT, ag.getThrow(3).deadType);
//        assertTrue(ag.getRuleSet().isValid(ag.getThrow(4)));
//    }

    @Test
    public void testTraps() {
        // test a trap throw
        onView(withId(R.id.gip_button_trap)).perform(click());
        assertEquals(2, btnTrap.getDrawable().getLevel());
        onView(withId(R.id.gip_button_strike)).perform(click());
        // TODO: how to go back to a previous throw to check UI?
        assertEquals(ThrowType.TRAP, ag.getThrow(0).throwType);
        assertTrue("trap invalid", ag.getRuleSet().isValid(ag.getThrow(0)));

        // test a redeemed trap throw
        onView(withId(R.id.gip_button_trap)).perform(click());
        assertEquals(2, btnTrap.getDrawable().getLevel());
        onView(withId(R.id.gip_button_bottle)).perform(click());
        assertEquals(ThrowType.TRAP_REDEEMED, ag.getThrow(1).throwType);
        assertTrue("redeemed trap invalid", ag.getRuleSet().isValid(ag.getThrow(1)));
    }

    public int getButtonColor(View view) {
        return ((ColorDrawable) view.getBackground()).getColor();
    }

    public void checkThrow(int throwIdx, int expThrowType, int expThrowResult,
                           int expDeadType, String errMsg) {
        assertEquals(errMsg + " (" + throwIdx + "), wrong throwType",
                expThrowType, ag.getThrow(throwIdx).throwType);
        assertEquals(errMsg + " (" + throwIdx + "), wrong throwResult",
                expThrowResult, ag.getThrow(throwIdx).throwResult);
        assertEquals(errMsg + " (" + throwIdx + "), wrong deadType",
                expDeadType, ag.getThrow(throwIdx).deadType);
    }

    public void checkThrowSpecial(int throwIdx, String errMsg) {
        // TODO: add checks for special marks
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
        assertEquals(errMsg + "(" + throwIdx + "), wrong P1 fire count",
                expP1Cnt, p1Cnt);
        assertEquals(errMsg + "(" + throwIdx + "), wrong P2 fire count",
                expP2Cnt, p2Cnt);
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
        assertEquals(errMsg + "(" + throwIdx + "), wrong P1 score", expP1Score,
                p1Score);
        assertEquals(errMsg + "(" + throwIdx + "), wrong P2 score", expP2Score,
                p2Score);
    }
}
