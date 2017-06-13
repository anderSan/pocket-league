package info.andersonpa.polishhorseshoes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.andersonpa.polishhorseshoes.backend.ActiveGame;
import info.andersonpa.polishhorseshoes.backend.Activity_Base;
import info.andersonpa.polishhorseshoes.backend.Adapter_Inning;
import info.andersonpa.polishhorseshoes.backend.Item_Inning;
import info.andersonpa.polishhorseshoes.backend.ThrowTableRow;
import info.andersonpa.polishhorseshoes.db.Throw;
import info.andersonpa.polishhorseshoes.enums.DeadType;
import info.andersonpa.polishhorseshoes.enums.ThrowResult;
import info.andersonpa.polishhorseshoes.enums.ThrowType;
import info.andersonpa.polishhorseshoes.rulesets.RuleSet01;

public class GameInProgress extends Activity_Base {
    private RecyclerView rv_throws;

    private View[] deadViews = new View[4];
    private ImageView ivHigh;
    private ImageView ivLow;
    private ImageView ivLeft;
    private ImageView ivRight;
    private ImageView ivTrap;
    private ImageView ivShort;
    private ImageView ivStrike;
    private ImageView ivBottle;
    private ImageView ivCup;
    private ImageView ivPole;
    private TextView tvOwnGoal;
    private TextView tvDefErr;
    private ToggleButton tbFire;
    private View naViewL;
    private View naViewR;
    NumberPicker resultNp;

    private String session_name;
    private String[] team_names = new String[2];
    private String venue_name;

    public ActiveGame ag;
    Throw uiThrow;
    Adapter_Inning inning_adapter;
    List<Item_Inning> innings = new ArrayList<>();

    // LISTENERS ==============================================================
    private OnValueChangeListener resultNPChangeListener = new OnValueChangeListener() {
        public void onValueChange(NumberPicker parent, int oldVal, int newVal) {
            switch (newVal) {
                case 0:
                    uiThrow.throwResult = ThrowResult.DROP;
                    break;
                case 1:
                    uiThrow.throwResult = ThrowResult.CATCH;
                    break;
                case 2:
                    uiThrow.throwResult = ThrowResult.STALWART;
                    break;
            }
            updateActiveThrow();
        }
    };

    private View.OnClickListener onThrowClicked = new View.OnClickListener() {
        public void onClick(View view) {
        }
    };

    private OnLongClickListener mLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            log("mLongClickListener(): " + view.getContentDescription() + " was long pressed");
            int buttonId = view.getId();

            if (uiThrow.throwType == ThrowType.TRAP || uiThrow.throwType == ThrowType
                    .TRAP_REDEEMED) {
                switch (buttonId) {
                    case R.id.gip_button_pole:
                        toggleBroken();
                        ag.getRuleSet().setThrowType(uiThrow, ThrowType.TRAP_REDEEMED);
                        break;
                    case R.id.gip_button_cup:
                        toggleBroken();
                        ag.getRuleSet().setThrowType(uiThrow, ThrowType.TRAP_REDEEMED);
                        break;
                    case R.id.gip_button_bottle:
                        toggleBroken();
                        ag.getRuleSet().setThrowType(uiThrow, ThrowType.TRAP_REDEEMED);
                        break;
                }
            } else {
                switch (buttonId) {
                    case R.id.gip_button_pole:
                        toggleBroken();
                        ag.getRuleSet().setThrowType(uiThrow, ThrowType.POLE);
                        break;
                    case R.id.gip_button_cup:
                        toggleBroken();
                        ag.getRuleSet().setThrowType(uiThrow, ThrowType.CUP);
                        break;
                    case R.id.gip_button_bottle:
                        toggleBroken();
                        ag.getRuleSet().setThrowType(uiThrow, ThrowType.BOTTLE);
                        break;
                }
            }

            switch (buttonId) {
                case R.id.gip_button_strike:
                    ag.getRuleSet().setIsTipped(uiThrow, !uiThrow.isTipped);
                    if (uiThrow.isTipped) {
                        ivStrike.getDrawable().setLevel(2);
                    } else {
                        ivStrike.getDrawable().setLevel(0);
                    }
                    break;
                case R.id.gip_button_high:
                    toggleDeadType(DeadType.HIGH);
                    break;
                case R.id.gip_button_right:
                    toggleDeadType(DeadType.RIGHT);
                    break;
                case R.id.gip_button_low:
                    toggleDeadType(DeadType.LOW);
                    break;
                case R.id.gip_button_left:
                    toggleDeadType(DeadType.LEFT);
                    break;
                default:
                    break;
            }
            if (buttonId == R.id.gip_button_pole || buttonId == R.id.gip_button_cup || buttonId
                    == R.id.gip_button_bottle) {
                confirmThrow();
            } else {
                updateActiveThrow();
            }
            return true;
        }
    };

    public void onThrowClicked(int local_throw_idx) {
//        int global_throw_idx = ThrowTableFragment.localThrowIdxToGlobal(vp.getCurrentItem(),
//                local_throw_idx);
//        if (global_throw_idx > ag.nThrows() - 1) {
//            global_throw_idx = ag.nThrows() - 1;
//        }
//        gotoThrowIdx(global_throw_idx);
    }

    public void throwTypePressed(View view) {
        log("buttonPressed(): " + view.getContentDescription() + " was pressed");
        int buttonId = view.getId();

        if (uiThrow.throwType == ThrowType.TRAP || uiThrow.throwType == ThrowType.TRAP_REDEEMED) {
            switch (buttonId) {
                case R.id.gip_button_trap:
                    ag.getRuleSet().setThrowResult(uiThrow, getThrowResultFromNP());
                    ag.getRuleSet().setThrowType(uiThrow, ThrowType.NOT_THROWN);
                    ((ImageView) view).getDrawable().setLevel(0);
                    break;
                case R.id.gip_button_bottle:
                case R.id.gip_button_pole:
                case R.id.gip_button_cup:
                    ag.getRuleSet().setThrowType(uiThrow, ThrowType.TRAP_REDEEMED);
                    confirmThrow();
                    break;
                default:
                    ag.getRuleSet().setThrowType(uiThrow, ThrowType.TRAP);
                    confirmThrow();
                    break;
            }
        } else {
            switch (buttonId) {
                case R.id.gip_button_high:
                    ag.getRuleSet().setThrowType(uiThrow, ThrowType.BALL_HIGH);
                    break;
                case R.id.gip_button_low:
                    ag.getRuleSet().setThrowType(uiThrow, ThrowType.BALL_LOW);
                    break;
                case R.id.gip_button_left:
                    ag.getRuleSet().setThrowType(uiThrow, ThrowType.BALL_LEFT);
                    break;
                case R.id.gip_button_right:
                    ag.getRuleSet().setThrowType(uiThrow, ThrowType.BALL_RIGHT);
                    break;
                case R.id.gip_button_trap:
                    ag.getRuleSet().setThrowType(uiThrow, ThrowType.TRAP);
                    ((ImageView) view).getDrawable().setLevel(2);
                    break;
                case R.id.gip_button_short:
                    ag.getRuleSet().setThrowType(uiThrow, ThrowType.SHORT);
                    break;
                case R.id.gip_button_strike:
                    ag.getRuleSet().setThrowType(uiThrow, ThrowType.STRIKE);
                    break;
                case R.id.gip_button_bottle:
                    ag.getRuleSet().setThrowType(uiThrow, ThrowType.BOTTLE);
                    break;
                case R.id.gip_button_pole:
                    ag.getRuleSet().setThrowType(uiThrow, ThrowType.POLE);
                    break;
                case R.id.gip_button_cup:
                    ag.getRuleSet().setThrowType(uiThrow, ThrowType.CUP);
                    break;
            }

            if (buttonId != R.id.gip_button_trap) {
                confirmThrow();
            }
        }
    }

    public void fireButtonPressed(View view) {
        boolean isChecked = ((ToggleButton) view).isChecked();

        if (isChecked) {
            uiThrow.offenseFireCount = 3;
            uiThrow.defenseFireCount = 0;
            if (uiThrow.throwResult != ThrowResult.BROKEN) {
                ag.getRuleSet().setThrowResult(uiThrow, ThrowResult.NA);
            }
            if (uiThrow.throwType == ThrowType.FIRED_ON) {
                ag.getRuleSet().setThrowType(uiThrow, ThrowType.NOT_THROWN);
            }
        } else {
            uiThrow.offenseFireCount = 0;
            ag.getRuleSet().setThrowResult(uiThrow, getThrowResultFromNP());
        }
        log("fire checked changed");
        updateActiveThrow();
    }

    public void firedOnPressed(View view) {
        log("buttonPressed(): " + view.getContentDescription() + " was pressed");

        if (uiThrow.defenseFireCount == 0) {
            uiThrow.defenseFireCount = 3;
            uiThrow.offenseFireCount = 0;
            ag.getRuleSet().setThrowType(uiThrow, ThrowType.FIRED_ON);
            confirmThrow();
        } else {
            uiThrow.defenseFireCount = 0;
            ag.getRuleSet().setThrowType(uiThrow, ThrowType.NOT_THROWN);
            ag.getRuleSet().setThrowResult(uiThrow, getThrowResultFromNP());
            updateActiveThrow();
        }
    }

    // INNER CLASSES ==========================================================
    public void OwnGoalDialog(View view) {
        final boolean[] ownGoals = uiThrow.getOwnGoals();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Own Goal").setMultiChoiceItems(R.array.owngoals, ownGoals,
                new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to
                            // the selected items
                            ownGoals[which] = true;
                        } else {
                            ownGoals[which] = false;
                        }
                        uiThrow.setOwnGoals(ownGoals);
                        updateActiveThrow();
                    }
                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void PlayerErrorDialog(View view) {
        final boolean[] defErrors = uiThrow.getDefErrors();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Defensive Error").setMultiChoiceItems(R.array.defErrors, defErrors,
                new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to
                            // the selected items
                            defErrors[which] = true;
                        } else {
                            defErrors[which] = false;
                        }
                        uiThrow.setDefErrors(defErrors);
                        updateActiveThrow();
                    }
                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void InfoDialog() {
        DateFormat df = new SimpleDateFormat("EEE MMM dd, yyyy. HH:mm", Locale.US);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game #" + String.valueOf(ag.getGameId())).setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        LayoutInflater inflater = getLayoutInflater();

        View fView = inflater.inflate(R.layout.dialog_game_information, null);
        TextView tv;

        // players
        tv = (TextView) fView.findViewById(R.id.gInfo_p1);
        tv.setText(team_names[0]);

        tv = (TextView) fView.findViewById(R.id.gInfo_p2);
        tv.setText(team_names[1]);

        // // session
        tv = (TextView) fView.findViewById(R.id.gInfo_session);
        tv.setText(session_name);

        // venue
        tv = (TextView) fView.findViewById(R.id.gInfo_venue);
        tv.setText(venue_name);

        // date
        tv = (TextView) fView.findViewById(R.id.gInfo_date);
        tv.setText(df.format(ag.getGameDate()));

        builder.setView(fView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static class GentlemensDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Time out, Gentlemen!").setPositiveButton("Resume",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    // ANDROID CALLBACKS ======================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        log("onCreate(): creating GIP");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_in_progress);

        rv_throws = (RecyclerView) this.findViewById(R.id.rv_throws_table);
        rv_throws.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        String gId = intent.getStringExtra("GID");
        fetchGameDetails(gId);

        uiThrow = ag.getActiveThrow();
        inning_adapter = new Adapter_Inning(this, innings, new RuleSet01(), onThrowClicked);
        rv_throws.setAdapter(inning_adapter);


        initMetadata();
        initListeners();
    }

    public void fetchGameDetails(String gId) {

        ag = new ActiveGame(this, gId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem fav = menu.add(0, 1, 0, "Game Information");
        fav.setIcon(R.drawable.ic_action_about);
        fav.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case 1:
                InfoDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        log("onResume(): vp's adapter has " + vpAdapter.getCount() + " items");
        gotoThrowIdx(ag.getActiveIdx());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        log("Calling onStop");
        ag.saveAllThrows();
        ag.saveGame();
    }

    // INITIALIZATION =========================================================
    private void initMetadata() {
        TextView tv;

        // table header
        tv = (TextView) findViewById(R.id.header_p1);
        tv.setText(team_names[0]);
        tv.setTextColor(ThrowTableRow.tableTextColor);
        tv.setTextSize(ThrowTableRow.tableTextSize);

        tv = (TextView) findViewById(R.id.header_p2);
        tv.setText(team_names[1]);
        tv.setTextColor(ThrowTableRow.tableTextColor);
        tv.setTextSize(ThrowTableRow.tableTextSize);
    }

    private void initListeners() {
        deadViews[0] = findViewById(R.id.gip_dead_high);
        deadViews[1] = findViewById(R.id.gip_dead_right);
        deadViews[2] = findViewById(R.id.gip_dead_low);
        deadViews[3] = findViewById(R.id.gip_dead_left);

        ivHigh = (ImageView) findViewById(R.id.gip_button_high);
        ivHigh.setOnLongClickListener(mLongClickListener);

        ivLeft = (ImageView) findViewById(R.id.gip_button_left);
        ivLeft.setOnLongClickListener(mLongClickListener);

        ivRight = (ImageView) findViewById(R.id.gip_button_right);
        ivRight.setOnLongClickListener(mLongClickListener);

        ivLow = (ImageView) findViewById(R.id.gip_button_low);
        ivLow.setOnLongClickListener(mLongClickListener);

        ivTrap = (ImageView) findViewById(R.id.gip_button_trap);
        ivTrap.setOnLongClickListener(mLongClickListener);

        ivShort = (ImageView) findViewById(R.id.gip_button_short);
        ivShort.setOnLongClickListener(mLongClickListener);

        ivStrike = (ImageView) findViewById(R.id.gip_button_strike);
        ivStrike.setOnLongClickListener(mLongClickListener);

        ivPole = (ImageView) findViewById(R.id.gip_button_pole);
        ivPole.setOnLongClickListener(mLongClickListener);

        ivCup = (ImageView) findViewById(R.id.gip_button_cup);
        ivCup.setOnLongClickListener(mLongClickListener);

        ivBottle = (ImageView) findViewById(R.id.gip_button_bottle);
        ivBottle.setOnLongClickListener(mLongClickListener);

        tvOwnGoal = (TextView) findViewById(R.id.gip_ownGoal);
        tvDefErr = (TextView) findViewById(R.id.gip_playerError);

        tbFire = (ToggleButton) findViewById(R.id.gip_toggle_fire);

        if (ag.getRuleSet().useAutoFire()) {
            tbFire.setVisibility(View.GONE);
            Button bFiredOn = (Button) findViewById(R.id.gip_button_fired_on);
            bFiredOn.setVisibility(View.GONE);
        }

        naViewL = findViewById(R.id.gip_na_indicatorL);
        naViewR = findViewById(R.id.gip_na_indicatorR);

        resultNp = (NumberPicker) findViewById(R.id.numPicker_catch);
        resultNp.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        String[] catchText = new String[3];
        catchText[0] = getString(R.string.gip_drop);
        catchText[1] = getString(R.string.gip_catch);
        catchText[2] = getString(R.string.gip_stalwart);
        resultNp.setMinValue(0);
        resultNp.setMaxValue(2);
        resultNp.setValue(1);
        resultNp.setDisplayedValues(catchText);
        resultNp.setOnValueChangedListener(resultNPChangeListener);
    }

    // STATE LOGIC AND PROGRAM FLOW ===========================================
    void updateActiveThrow() {
        log("updateThrow(): Updating throw at idx " + ag.getActiveIdx());
        ag.updateActiveThrow(uiThrow);

        renderPage();
        refreshUI();
    }

    void confirmThrow() {
        int activeIdx = ag.getActiveIdx();
        if ((activeIdx + 7) % 70 == 0) {
            Toast.makeText(getApplicationContext(), "GTO in 3 innings", Toast.LENGTH_LONG).show();
        } else if ((activeIdx + 1) % 70 == 0) {
            GentlemensDialogFragment frag = new GentlemensDialogFragment();
            frag.show(getFragmentManager(), "gentlemens");
        }
        gotoThrowIdx(activeIdx + 1);
        ag.updateScoresFrom(activeIdx + 1);
    }

    void gotoThrowIdx(int newActiveIdx) {
        log("gotoThrow() - Going from throw idx " + ag.getActiveIdx() + " to throw idx " +
                newActiveIdx + ".");

        ag.updateActiveThrow(uiThrow); // ui -> ag
        ag.setActiveIdx(newActiveIdx); // change index
        uiThrow = ag.getActiveThrow(); // ag -> ui

        refreshUI();

        int idx = ag.getActiveIdx();

        // try to render the throw table
        renderPage();
        rv_throws.scrollToPosition(newActiveIdx/2);
        ag.saveGame(); // save the game
    }

    // UI =====================================================================
    private void refreshUI() {
        setThrowResultToNP(uiThrow.throwResult);
        setThrowButtonState(ThrowType.BALL_HIGH, ivHigh);
        setThrowButtonState(ThrowType.BALL_LOW, ivLow);
        setThrowButtonState(ThrowType.BALL_LEFT, ivLeft);
        setThrowButtonState(ThrowType.BALL_RIGHT, ivRight);
        setThrowButtonState(ThrowType.TRAP, ivTrap);
        setThrowButtonState(ThrowType.SHORT, ivShort);
        setThrowButtonState(ThrowType.STRIKE, ivStrike);
        setThrowButtonState(ThrowType.BOTTLE, ivBottle);
        setThrowButtonState(ThrowType.POLE, ivPole);
        setThrowButtonState(ThrowType.CUP, ivCup);
        setBrokenButtonState();
        setExtrasButtonState();

        if (uiThrow.isTipped) {
            ivStrike.getDrawable().setLevel(3);
        }

        for (View vw : deadViews) {
            vw.setBackgroundColor(Color.LTGRAY);
        }
        if (uiThrow.deadType > 0) {
//            deadViews[uiThrow.deadType - 1].setBackgroundColor(Color.RED);
        }

        int hp1, hp2;
        hp1 = uiThrow.initialOffensivePlayerHitPoints;
        hp2 = uiThrow.initialDefensivePlayerHitPoints;

        if (!uiThrow.isP1Throw()) {
            int tmp = hp1;
            hp1 = hp2;
            hp2 = tmp;
        }

        inning_adapter.setCurrent_throw(uiThrow);
    }

    private void setThrowButtonState(int throwType, ImageView iv) {
        if (throwType == uiThrow.throwType) {
            iv.getDrawable().setLevel(1);
        } else if (throwType == ThrowType.TRAP && uiThrow.throwType == ThrowType.TRAP_REDEEMED) {
            iv.getDrawable().setLevel(1);
        } else {
            iv.getDrawable().setLevel(0);
        }
    }

    private void setBrokenButtonState() {
        Drawable poleDwl = ivPole.getDrawable();
        Drawable cupDwl = ivCup.getDrawable();
        Drawable bottleDwl = ivBottle.getDrawable();

        if (uiThrow.throwResult == ThrowResult.BROKEN) {
            switch (uiThrow.throwType) {
                case ThrowType.POLE:
                    poleDwl.setLevel(3);
                    cupDwl.setLevel(2);
                    bottleDwl.setLevel(2);
                    break;
                case ThrowType.CUP:
                    poleDwl.setLevel(2);
                    cupDwl.setLevel(3);
                    bottleDwl.setLevel(2);
                    break;
                case ThrowType.BOTTLE:
                    poleDwl.setLevel(2);
                    cupDwl.setLevel(2);
                    bottleDwl.setLevel(3);
                    break;
                case ThrowType.TRAP:
                case ThrowType.TRAP_REDEEMED:
                    poleDwl.setLevel(2);
                    cupDwl.setLevel(2);
                    bottleDwl.setLevel(2);
                    break;
            }
        } else {
            switch (uiThrow.throwType) {
                case ThrowType.POLE:
                    poleDwl.setLevel(1);
                    cupDwl.setLevel(0);
                    bottleDwl.setLevel(0);
                    break;
                case ThrowType.CUP:
                    poleDwl.setLevel(0);
                    cupDwl.setLevel(1);
                    bottleDwl.setLevel(0);
                    break;
                case ThrowType.BOTTLE:
                    poleDwl.setLevel(0);
                    cupDwl.setLevel(0);
                    bottleDwl.setLevel(1);
                    break;
                case ThrowType.TRAP:
                case ThrowType.TRAP_REDEEMED:
                    poleDwl.setLevel(0);
                    cupDwl.setLevel(0);
                    bottleDwl.setLevel(0);
                    break;
            }
        }
    }

    private void setExtrasButtonState() {
        tvOwnGoal.setTextColor(Color.BLACK);
        tvDefErr.setTextColor(Color.BLACK);
        for (boolean og : uiThrow.getOwnGoals()) {
            if (og) {
                tvOwnGoal.setTextColor(Color.RED);
            }
        }
        for (boolean de : uiThrow.getDefErrors()) {
            if (de) {
                tvDefErr.setTextColor(Color.RED);
            }
        }

        if (uiThrow.offenseFireCount >= 3) {
            tbFire.setChecked(true);
        } else {
            tbFire.setChecked(false);
        }
    }

    private void renderPage() {
        innings.clear();
        innings.addAll(ag.getInnings());
        inning_adapter.notifyDataSetChanged();
    }

    public int getThrowResultFromNP() {
        int theResult = 0;
        switch (resultNp.getValue()) {
            case 0:
                theResult = ThrowResult.DROP;
                break;
            case 1:
                theResult = ThrowResult.CATCH;
                break;
            case 2:
                theResult = ThrowResult.STALWART;
                break;
        }
        return theResult;
    }

    public void setThrowResultToNP(int result) {
        naViewL.setBackgroundColor(Color.LTGRAY);
        naViewR.setBackgroundColor(Color.LTGRAY);
        switch (result) {
            case ThrowResult.DROP:
                resultNp.setValue(0);
                break;
            case ThrowResult.CATCH:
                resultNp.setValue(1);
                break;
            case ThrowResult.STALWART:
                resultNp.setValue(2);
                break;
            case ThrowResult.NA:
                naViewL.setBackgroundColor(Color.RED);
                naViewR.setBackgroundColor(Color.RED);
                break;
        }
    }

    public void toggleDeadType(int deadType) {
        if (uiThrow.deadType == deadType) {
            ag.getRuleSet().setDeadType(uiThrow, DeadType.ALIVE);
        } else {
            ag.getRuleSet().setDeadType(uiThrow, deadType);
        }
    }

    public void toggleBroken() {
        if (uiThrow.throwResult == ThrowResult.BROKEN) {
            ag.getRuleSet().setThrowResult(uiThrow, getThrowResultFromNP());
        } else {
            ag.getRuleSet().setThrowResult(uiThrow, ThrowResult.BROKEN);
        }
    }
}
