package info.andersonpa.pocketleague;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.ToggleButton;

import info.andersonpa.pocketleague.backend.Fragment_Detail;
import info.andersonpa.pocketleague.db.tables.Player;
import info.andersonpa.pocketleague.db.tables.Team;

public class Detail_Player extends Fragment_Detail {
    String pId;
	Player p;
	Team t;

	TextView tv_playerName;
	TextView tv_playerId;
	TextView tv_height;
	TextView tv_weight;
	TextView tv_handed;
	TextView tv_footed;
	QuickContactBadge qcb_badge;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setModifyClicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav.modifyPlayer(pId);
            }
        });

        setFavoriteClicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pId != null) {
                    boolean is_favorite = ((ToggleButton) v).isChecked();
                    p.setIsFavorite(is_favorite);
                    p.update();
                }
            }
        });

        setActiveClicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pId != null) {
                    boolean is_active = ((ToggleButton) v).isChecked();
                    p.setIsActive(is_active);
                    p.update();
                }
            }
        });

        rootView = inflater.inflate(R.layout.activity_detail_player, container, false);

        Bundle args = getArguments();
        pId = args.getString("PID");

		tv_playerName = (TextView) rootView.findViewById(R.id.pDet_name);
		tv_playerId = (TextView) rootView.findViewById(R.id.pDet_id);
		tv_height = (TextView) rootView.findViewById(R.id.pDet_height);
		tv_weight = (TextView) rootView.findViewById(R.id.pDet_weight);
		tv_handed = (TextView) rootView.findViewById(R.id.pDet_handed);
		tv_footed = (TextView) rootView.findViewById(R.id.pDet_footed);
        qcb_badge = (QuickContactBadge) rootView.findViewById(R.id.quickbadge);

        setupBarButtons();

		if (!mData.getIsDevMode()) {
            tv_playerId.setVisibility(View.GONE);
        }
        qcb_badge.setImageToDefault();

        return rootView;
	}

    @Override
	public void refreshDetails() {

		if (pId != null) {
            p = Player.getFromId(database(), pId);
		}

        mNav.setTitle(p.getName(), "Player Details");

		tv_playerName.setText(p.getDisplayName());
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

		if (p.getContactUri() != null) {
			qcb_badge.assignContactUri(p.getContactUri());
			qcb_badge.setImageURI(p.getThumbnailUri());
			if(qcb_badge.getDrawable() == null) {
				qcb_badge.setImageToDefault();
                qcb_badge.setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
			}
            log("player uri set.");
		}

		bar_isFavorite.setChecked(p.getIsFavorite());
		bar_isActive.setChecked(p.getIsActive());
	}
}
