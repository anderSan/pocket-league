package com.twobits.pocketleague;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.j256.ormlite.dao.Dao;
import com.twobits.pocketleague.backend.Fragment_Detail;
import com.twobits.pocketleague.db.DatabaseCommonQueue;
import com.twobits.pocketleague.db.tables.Player;
import com.twobits.pocketleague.db.tables.Team;

import java.sql.SQLException;

public class Detail_Player extends Fragment_Detail {
	Long pId;
	Player p;
	Team t;
	Dao<Player, Long> pDao;
	Dao<Team, Long> tDao;

	TextView tv_playerName;
	TextView tv_playerId;
	TextView tv_height;
	TextView tv_weight;
	TextView tv_handed;
	TextView tv_footed;

    public Detail_Player() {
        LOGTAG = "Detail_Player";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setModifyClicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewPlayer.class);
                intent.putExtra("PID", pId);
                startActivity(intent);
            }
        });

        setFavoriteClicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pId != -1) {
                    boolean is_favorite = ((ToggleButton) v).isChecked();
                    p.setIsFavorite(is_favorite);
                    t.setIsFavorite(is_favorite);
                    updatePlayer();
                }
            }
        });

        setActiveClicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pId != -1) {
                    boolean is_active = ((ToggleButton) v).isChecked();
                    p.setIsActive(is_active);
                    t.setIsActive(is_active);
                    updatePlayer();
                }
            }
        });

        rootView = inflater.inflate(R.layout.activity_detail_player, container, false);

        Bundle args = getArguments();
        pId = args.getLong("PID", -1);

		pDao = Player.getDao(context);
		tDao = Team.getDao(context);

		tv_playerName = (TextView) rootView.findViewById(R.id.pDet_name);
		tv_playerId = (TextView) rootView.findViewById(R.id.pDet_id);
		tv_height = (TextView) rootView.findViewById(R.id.pDet_height);
		tv_weight = (TextView) rootView.findViewById(R.id.pDet_weight);
		tv_handed = (TextView) rootView.findViewById(R.id.pDet_handed);
		tv_footed = (TextView) rootView.findViewById(R.id.pDet_footed);

        setupBarButtons();

        return rootView;
	}

    @Override
	public void refreshDetails() {

		if (pId != -1) {
			try {
				p = pDao.queryForId(pId);
				t = DatabaseCommonQueue.findPlayerSoloTeam(context, p);
			} catch (SQLException e) {
				Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}

        mNav.setTitle(p.getNickName());

		tv_playerName.setText(p.getNickName() + " (" + p.getFirstName() + ' '
				+ p.getLastName() + ")");
		tv_playerId.setText(String.valueOf(p.getId()));
		tv_height.setText("Height: " + String.valueOf(p.getHeight()) + " cm");
		tv_weight.setText("Weight: " + String.valueOf(p.getWeight()) + " kg");
		if (p.getIsLeftHanded()) {
			if (p.getIsRightHanded()) {
				tv_handed.setText("L + R");
			} else {
				tv_handed.setText("L");
			}
		} else {
			tv_handed.setText("R");
		}

		if (p.getIsLeftFooted()) {
			if (p.getIsRightFooted()) {
				tv_footed.setText("L + R");
			} else {
				tv_footed.setText("L");
			}
		} else {
			tv_footed.setText("R");
		}

		bar_isFavorite.setChecked(p.getIsFavorite());
		bar_isActive.setChecked(p.getIsActive());
	}

	private void updatePlayer() {
		try {
			pDao.update(p);
			tDao.update(t);
		} catch (SQLException e) {
			loge("Could not update player", e);
			e.printStackTrace();
		}
	}
}
