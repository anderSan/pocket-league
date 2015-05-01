package info.andersonpa.pocketleague.backend;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import info.andersonpa.pocketleague.BuildConfig;
import info.andersonpa.pocketleague.db.tables.Game;
import info.andersonpa.pocketleague.db.tables.Session;
import info.andersonpa.pocketleague.db.tables.SessionMember;
import info.andersonpa.pocketleague.enums.BrNodeType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LadderTable implements View.OnClickListener {
    public static String LOGTAG = "LadderTable";
    public Context context;
    private Session s;
    private ListView lv;
    public List<SessionMember> members = new ArrayList<>();
    private Map<Integer, Item_Match> matches = new TreeMap<>();

    public LadderTable(FrameLayout fl, Session s) {
        this.s = s;
        lv = new ListView(fl.getContext());
        FrameLayout.LayoutParams lp;
        lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        lv.setLayoutParams(lp);
        context = lv.getContext();

        members = s.getMembers();
        Collections.sort(members, SessionMember.SEED_ORDER);
        buildTable(this);
        fl.addView(lv);
    }

    public void refreshTable() {
        matchMatches();
        refreshViews();
    }

    private void matchMatches() {
        Item_Match match;
        List<Game> games_list = s.getGames();
        List<Game> matched = new ArrayList<>();

        for (Game g : games_list) {
            int match_id = g.getIdInSession();
            if (matches.containsKey(match_id)) {
                match = matches.get(match_id);
                matched.add(g);

                if (match.getGameId().equals("")) {
                    match.setGameId(g.getId());
                } else if (BuildConfig.DEBUG && !match.getGameId().equals(g.getId())) {
                    throw new AssertionError("Match id mismatch.");
                }

                if (g.getIsComplete()) {
                    if (g.getWinner().equals(match.getUpperTeam())) {
                        match.setWinner(true);
                    } else if (g.getWinner().equals(match.getLowerTeam())) {
                        match.setWinner(false);
                    }
                }
            }
        }
        games_list.removeAll(matched);
        if (BuildConfig.DEBUG && !games_list.isEmpty()) {
            throw new AssertionError("Orphaned games found in session.");
        }
    }

    private void refreshViews() {
        TextView tv;
        int drwColor;

        for (Item_Match match : matches.values()) {
            drwColor = Color.LTGRAY;
            if (match.getUpperNodeType() == BrNodeType.WIN) {
                drwColor = Color.GREEN;
            } else if (match.getUpperNodeType() == BrNodeType.LOSS) {
                drwColor = Color.RED;
            }
            tv = (TextView) match.getUpperView();
            tv.setBackgroundColor(drwColor);

            drwColor = Color.LTGRAY;
            if (match.getLowerNodeType() == BrNodeType.WIN) {
                drwColor = Color.GREEN;
            } else if (match.getLowerNodeType() == BrNodeType.LOSS) {
                drwColor = Color.RED;
            }
            tv = (TextView) match.getLowerView();
            tv.setBackgroundColor(drwColor);
        }
    }

    public Item_Match getMatch(int match_id) {
        return matches.get(match_id);
    }

    private void buildTable(View.OnClickListener mListener) {

    }

    private int memberPositionsToMatchId(int[] indices) {
        return memberPositionsToMatchId(indices, members.size());
    }

    public static int memberPositionsToMatchId(int[] indices, int n_members) {
        int row = indices[0];
        int col = indices[1];

        if (BuildConfig.DEBUG && (row > n_members - 1 || col > n_members - 1)) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (row < col) return n_members * row + col;
        else return n_members * col + row;
    }

    @Override
    public void onClick(View v) {
        Log.i(LOGTAG, "View " + v.getId() + " was clicked");
    }
}
