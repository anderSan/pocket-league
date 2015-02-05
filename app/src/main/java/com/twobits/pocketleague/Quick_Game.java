package com.twobits.pocketleague;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.twobits.pocketleague.backend.Fragment_Base;
import com.twobits.pocketleague.backend.Item_GameScore;
import com.twobits.pocketleague.backend.ListAdapter_GameScore;
import com.twobits.pocketleague.db.tables.Game;
import com.twobits.pocketleague.db.tables.GameMember;
import com.twobits.pocketleague.db.tables.Session;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Quick_Game extends Fragment_Base {
	Long gId;
	Game g;
	Session s;
	List<GameMember> g_members;
	Dao<Game, Long> gDao;
	Dao<GameMember, Long> gmDao;
	Dao<Session, Long> sDao;

	private ListView lv;
	private ListAdapter_GameScore scoreAdapter;
	List<Item_GameScore> game_scores = new ArrayList<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_quick_game, container, false);

        Bundle args = getArguments();
        gId = args.getLong("GID", -1);

		lv = (ListView) rootView.findViewById(R.id.lv_gameMembers);

        gDao = mData.getGameDao();
        gmDao = mData.getGameMemberDao();
        sDao = mData.getSessionDao();

        Button btn_done = (Button) rootView.findViewById(R.id.button_finished);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneButtonPushed();
            }
        });

		if (gId != -1) {
		    loadGame();
		}

        return rootView;
	}

    private void loadGame() {
        try {
            g = gDao.queryForId(gId);
            gDao.refresh(g);
            s = g.getSession();
            sDao.refresh(s);
        } catch (SQLException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        scoreAdapter = new ListAdapter_GameScore(context,
                R.layout.list_item_gamescore, game_scores, s.getRuleSet().getScoreType());
        lv.setAdapter(scoreAdapter);
    }

	@Override
	public void onResume() {
		super.onResume();
		refreshMemberListing();
	}

	private void refreshMemberListing() {
		Item_GameScore gs;
		for (GameMember gm : g.getGameMembers()) {
            gs = new Item_GameScore(gm.getTeam().getTeamName(), gm.getScore());
            game_scores.add(gs);
		}
		scoreAdapter.notifyDataSetChanged();
	}

	public void doneButtonPushed() {
        ListAdapter_GameScore scoreAdapter = (ListAdapter_GameScore) lv.getAdapter();
        int ii = 0;
        for (GameMember gm : g.getGameMembers()) {
            int score = scoreAdapter.getItem(ii).getMemberScore();
            ii++;
            gm.setScore(score);
            log(gm.getTeam().getTeamName() + "'s score is " + Integer.toString(score));
            try {
                gmDao.update(gm);
            } catch (SQLException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        try {
            g.setIsComplete(true);
            gDao.update(g);
        } catch (SQLException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
	}
}
