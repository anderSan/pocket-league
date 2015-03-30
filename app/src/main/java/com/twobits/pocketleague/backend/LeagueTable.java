package com.twobits.pocketleague.backend;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.twobits.pocketleague.BuildConfig;
import com.twobits.pocketleague.R;
import com.twobits.pocketleague.db.tables.Session;
import com.twobits.pocketleague.db.tables.SessionMember;

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
    private Map<Integer, Integer> view_ids = new HashMap<>();

    public LeagueTable(ScrollView sv, Session s) {
        this.s = s;
        tl = new TableLayout(sv.getContext());
        ScrollView.LayoutParams lp;
        lp = new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT,
                ScrollView.LayoutParams.WRAP_CONTENT);
        lp.setMargins(20, 20, 10, 10);
        tl.setLayoutParams(lp);
        this.context = tl.getContext();

        members = s.getMembers();
        Collections.sort(members, SessionMember.SEED_ORDER);
        seed(members);
        buildTable(this);
        sv.addView(tl);
    }

    public void refreshTable() {

    }

    public Item_Match getMatch(int viewId) {
//        Item_Match mInfo = wBr.getMatch(viewId);
//
//        if (isDoubleElim) {
//            if (lBr.hasView(viewId)) {
//                mInfo = lBr.getMatch(viewId);
//            } else if (fBr.hasView(viewId)) {
//                mInfo = fBr.getMatch(viewId);
//            }
//        }

        return null;
    }

    private void seed(List<SessionMember> sMembers) {
        Item_Match new_match;
        int id_in_session;

        for (Integer ii = 0; ii < sMembers.size() - 1; ii++) {
            for (Integer jj = ii + 1; jj < sMembers.size(); jj++) {
                id_in_session = memberPositionsToMatchId(new int[]{ii, jj});

                new_match = new Item_Match(id_in_session);
                new_match.setUpperMember(sMembers.get(ii));
                if (jj == 0) new_match.setUpperIsLabelled(true);

                new_match.setLowerMember(sMembers.get(jj));
                if (ii == 0) new_match.setLowerIsLabelled(true);

                matches.put(id_in_session, new_match);
            }
        }
    }

    private void buildTable(View.OnClickListener mListener) {
        TextView tv;
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

        for (int ii = 0; ii < members.size(); ii++) {
            tableRow = new TableRow(context);
            tableRow.setLayoutParams(tp);
            tv = new TextView(context);
            tv.setGravity(Gravity.RIGHT);
            tv.setText(members.get(ii).getTeam().getName());
            tv.setTextAppearance(context, android.R.style.TextAppearance_Medium);
            tv.setLayoutParams(rp1);
            tableRow.addView(tv);

            for (int jj = 0; jj < members.size(); jj++) {
                tv = new TextView(context);
                tv.setLayoutParams(rp2);
                tv.setWidth(96);
                tv.setHeight(96);
                tv.setBackgroundResource(R.drawable.league_table_match);
                tv.setGravity(Gravity.CENTER);
                tableRow.addView(tv);
            }
            tl.addView(tableRow);
        }

        for (Item_Match match : matches.values()) {
            makeMatchViews(match);
//            match.setOnClickListener(mListener);
            addViewsToLayout(match);
        }
    }

    private void makeMatchViews(Item_Match match) {

    }

    private void addViewsToLayout(Item_Match match) {

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

        return n_members * row + col;
    }

    @Override
    public void onClick(View v) {
        Log.i(LOGTAG, "View " + v.getId() + " was clicked");
    }
}
