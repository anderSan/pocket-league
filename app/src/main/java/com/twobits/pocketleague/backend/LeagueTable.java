package com.twobits.pocketleague.backend;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.twobits.pocketleague.BuildConfig;
import com.twobits.pocketleague.R;
import com.twobits.pocketleague.db.tables.Game;
import com.twobits.pocketleague.db.tables.Session;
import com.twobits.pocketleague.db.tables.SessionMember;
import com.twobits.pocketleague.enums.BrNodeType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LeagueTable implements View.OnClickListener {
    public static String LOGTAG = "LeagueTable";
    public Context context;
    private Session s;
    private TableLayout tl;
    public List<SessionMember> members = new ArrayList<>();
    private Map<Integer, Item_Match> matches = new TreeMap<>();

    public LeagueTable(ScrollView sv, Session s) {
        this.s = s;
        tl = new TableLayout(sv.getContext());
        ScrollView.LayoutParams lp;
        lp = new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT,
                ScrollView.LayoutParams.WRAP_CONTENT);
        lp.setMargins(20, 20, 10, 10);
        tl.setLayoutParams(lp);
        context = tl.getContext();

        members = s.getMembers();
        Collections.sort(members, SessionMember.SEED_ORDER);
        seed(members);
        buildTable(this);
        sv.addView(tl);
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

    private void seed(List<SessionMember> sMembers) {
        Item_Match new_match;
        int id_in_session;

        for (Integer ii = 0; ii < sMembers.size() - 1; ii++) {
            for (Integer jj = ii + 1; jj < sMembers.size(); jj++) {
                id_in_session = memberPositionsToMatchId(new int[]{ii, jj});

                new_match = new Item_Match(id_in_session);
                new_match.setUpperMember(sMembers.get(ii));
                new_match.setLowerMember(sMembers.get(jj));
                new_match.setUpperNodeType(BrNodeType.TIP);
                new_match.setLowerNodeType(BrNodeType.TIP);

                matches.put(id_in_session, new_match);
            }
        }
    }

    private void buildTable(View.OnClickListener mListener) {
        TextView tv;
        Item_Match match;
        VerticalTextView vtv;
        TableLayout.LayoutParams tp;
        TableRow.LayoutParams rp1, rp2;
        TableRow tableRow;

        tp = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        rp1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT, 1f);
        rp1.setMargins(0, 0, 10, 0);
        rp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT, 0.5f);
        rp2.setMargins(0, 0, 10, 10);

        tableRow = new TableRow(context);
        tableRow.setLayoutParams(tp);
        tv = new TextView(context);
        tv.setLayoutParams(rp1);
        tableRow.addView(tv);

        for (int ii = 0; ii < members.size(); ii++) {
            vtv = new VerticalTextView(context, null, false);
            vtv.setLayoutParams(rp2);
            vtv.setText(members.get(ii).getTeam().getName());
            vtv.setTextAppearance(context, android.R.style.TextAppearance_Medium);
            vtv.setPadding(2, 2, 2, 2);
            vtv.setGravity(Gravity.BOTTOM);
            tableRow.addView(vtv);
        }
        tl.addView(tableRow);

        int match_id;
        for (int ii = 0; ii < members.size(); ii++) {
            tableRow = new TableRow(context);
            tableRow.setLayoutParams(tp);
            tv = new TextView(context);
            tv.setGravity(Gravity.RIGHT);
            tv.setMinWidth(300);
            tv.setText(members.get(ii).getTeam().getName());
            tv.setTextAppearance(context, android.R.style.TextAppearance_Medium);
            tv.setLayoutParams(rp1);
            tableRow.addView(tv);

            for (int jj = 0; jj < members.size(); jj++) {
                tv = new TextView(context);
                tv.setLayoutParams(rp2);
                tv.setWidth(96);
                tv.setHeight(96);
                if (ii != jj) {
                    tv.setBackgroundResource(R.drawable.league_table_match);
                    tv.setOnClickListener(mListener);
                    match_id = memberPositionsToMatchId(new int[]{ii, jj});
                    tv.setTag(match_id);
                    match = matches.get(match_id);
                    if (ii < jj) {
                        match.setUpperView(tv);
                    } else {
                        match.setLowerView(tv);
                    }
                }
                tv.setGravity(Gravity.CENTER);
                tableRow.addView(tv);
            }
            tl.addView(tableRow);
        }
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
