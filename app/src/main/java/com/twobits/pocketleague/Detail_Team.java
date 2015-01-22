package com.twobits.pocketleague;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.twobits.pocketleague.backend.MenuContainerActivity;
import com.twobits.pocketleague.db.OrmLiteFragment;
import com.twobits.pocketleague.db.tables.Player;
import com.twobits.pocketleague.db.tables.Team;
import com.twobits.pocketleague.db.tables.TeamMember;

import java.sql.SQLException;
import java.util.List;

public class Detail_Team extends OrmLiteFragment {
	private static final String LOGTAG = "Detail_Team";
    private View rootView;
    private Context context;

	Long tId;
	Team t;
	Dao<Team, Long> tDao;
	Dao<TeamMember, Long> tmDao;
	Dao<Player, Long> pDao;

	TextView tv_teamName;
	TextView tv_teamId;
	TextView tv_members;
	CheckBox cb_isFavorite;
	Switch sw_isActive;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_detail_team, container, false);

        Bundle args = getArguments();
        tId = args.getLong("TID", -1);

		tDao = Team.getDao(context);
		pDao = Player.getDao(context);
		tmDao = TeamMember.getDao(context);

		tv_teamName = (TextView) rootView.findViewById(R.id.tDet_name);
		tv_teamId = (TextView) rootView.findViewById(R.id.tDet_id);
		tv_members = (TextView) rootView.findViewById(R.id.tDet_members);
		cb_isFavorite = (CheckBox) rootView.findViewById(R.id.tDet_isFavorite);
		cb_isFavorite.setOnClickListener(favoriteClicked);
		sw_isActive = (Switch) rootView.findViewById(R.id.tDet_isActive);
		sw_isActive.setOnClickListener(activeClicked);

        return rootView;
	}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = getActivity();
    }

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem fav = menu.add(R.string.menu_modify);
		fav.setIcon(R.drawable.ic_action_edit);
		fav.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		Intent intent = new Intent(context, NewTeam.class);
		intent.putExtra("TID", tId);
		fav.setIntent(intent);

        mNav.setTitle(t.getTeamName());
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshDetails();
	}

	public void refreshDetails() {
		String memberNicks = "";
		if (tId != -1) {
			try {
				t = tDao.queryForId(tId);
				List<TeamMember> memberList = tmDao.queryBuilder().where()
						.eq(TeamMember.TEAM, tId).query();

				for (TeamMember tm : memberList) {
					pDao.refresh(tm.getPlayer());
					memberNicks = memberNicks.concat(tm.getPlayer()
							.getNickName() + ", ");
				}
				if (memberNicks.length() == 0) {
					memberNicks = "Anonymous team (no members).";
				} else {
					memberNicks = memberNicks.substring(0,
							memberNicks.length() - 2) + ".";
				}
			} catch (SQLException e) {
				Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}

		tv_teamName.setText(t.getTeamName());
		tv_teamId.setText(String.valueOf(t.getId()));
		tv_members.setText(memberNicks);
		cb_isFavorite.setChecked(t.getIsFavorite());
		sw_isActive.setChecked(t.getIsActive());
	}

	private OnClickListener favoriteClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (tId != -1) {
				t.setIsFavorite(((CheckBox) v).isChecked());
				updateTeam();
			}
		}
	};

	private OnClickListener activeClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (tId != -1) {
				t.setIsActive(((Switch) v).isChecked());
				updateTeam();
			}
		}
	};

	private void updateTeam() {
		try {
			tDao.update(t);
		} catch (SQLException e) {
			loge("Could not update team", e);
			e.printStackTrace();
		}
	}
}
