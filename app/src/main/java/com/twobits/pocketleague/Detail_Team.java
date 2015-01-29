package com.twobits.pocketleague;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.j256.ormlite.dao.Dao;
import com.twobits.pocketleague.backend.Fragment_Detail;
import com.twobits.pocketleague.db.tables.Player;
import com.twobits.pocketleague.db.tables.Team;
import com.twobits.pocketleague.db.tables.TeamMember;

import java.sql.SQLException;
import java.util.List;

public class Detail_Team extends Fragment_Detail {
	static final String LOGTAG = "Detail_Team";

	Long tId;
	Team t;
	Dao<Team, Long> tDao;
	Dao<TeamMember, Long> tmDao;
	Dao<Player, Long> pDao;

	TextView tv_teamName;
	TextView tv_teamId;
	TextView tv_members;

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

        setModifyClicked(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(context, NewTeam.class);
                intent.putExtra("TID", tId);
                startActivity(intent);
                return false;
            }
        });

        setFavoriteClicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tId != -1) {
                    t.setIsFavorite(((CheckBox) v).isChecked());
                    updateTeam();
                }
            }
        });

        setActiveClicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tId != -1) {
                    t.setIsActive(((ToggleButton) v).isChecked());
                    updateTeam();
                }
            }
        });

        return rootView;
	}

    @Override
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

        mNav.setTitle(t.getTeamName());

		tv_teamName.setText(t.getTeamName());
		tv_teamId.setText(String.valueOf(t.getId()));
		tv_members.setText(memberNicks);

		mi_isFavorite.setChecked(t.getIsFavorite());
		mi_isActive.setChecked(t.getIsActive());
	}

	private void updateTeam() {
		try {
			tDao.update(t);
		} catch (SQLException e) {
			loge("Could not update team", e);
			e.printStackTrace();
		}
	}
}
