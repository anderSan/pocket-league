package info.andersonpa.pocketleague;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import info.andersonpa.pocketleague.backend.Fragment_Detail;
import info.andersonpa.pocketleague.db.tables.Player;
import info.andersonpa.pocketleague.db.tables.Team;

public class Detail_Team extends Fragment_Detail {
	String tId;
	Team t;

	TextView tv_teamName;
	TextView tv_teamId;
	TextView tv_members;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setModifyClicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav.modifyTeam(tId);
            }
        });

        setFavoriteClicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tId != null) {
                    t.setIsFavorite(((ToggleButton) v).isChecked());
                    t.update();
                }
            }
        });

        setActiveClicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tId != null) {
                    t.setIsActive(((ToggleButton) v).isChecked());
                    t.update();
                }
            }
        });

		rootView = inflater.inflate(R.layout.activity_detail_team, container, false);

        Bundle args = getArguments();
        tId = args.getString("TID");

		tv_teamName = (TextView) rootView.findViewById(R.id.tDet_name);
		tv_teamId = (TextView) rootView.findViewById(R.id.tDet_id);
		tv_members = (TextView) rootView.findViewById(R.id.tDet_members);

        setupBarButtons();

        if (!mData.getIsDevMode()) {
            tv_teamId.setVisibility(View.GONE);
        }

        return rootView;
	}

    @Override
	public void refreshDetails() {
		String member_names = "";
		if (tId != null) {
            t = Team.getFromId(database(), tId);
            for (Player p : t.getMembers()) {
                member_names = member_names.concat(p.getName() + ", ");
            }
            if (member_names.length() == 0) {
                member_names = "Anonymous team (no members).";
            } else {
                member_names = member_names.substring(0, member_names.length() - 2) + ".";
            }
		}

        mNav.setTitle(t.getName(), "Team Details");

		tv_teamName.setText(t.getName());
		tv_teamId.setText(String.valueOf(t.getId()));
		tv_members.setText(member_names);

		bar_isFavorite.setChecked(t.getIsFavorite());
		bar_isActive.setChecked(t.getIsActive());
	}
}
