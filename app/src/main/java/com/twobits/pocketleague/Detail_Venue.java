package com.twobits.pocketleague;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.twobits.pocketleague.backend.Fragment_Detail;
import com.twobits.pocketleague.db.tables.Venue;

public class Detail_Venue extends Fragment_Detail {
    String vId;
	Venue v;

	TextView tv_venueName;
	TextView tv_venueId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setModifyClicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav.modifyVenue(vId);
            }
        });

        setFavoriteClicked(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vId != null) {
                    v.setIsFavorite(((ToggleButton) view).isChecked());
                    v.update();
                }
            }
        });

        setActiveClicked(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vId != null) {
                    v.setIsActive(((ToggleButton) view).isChecked());
                    v.update();
                }
            }
        });

        rootView = inflater.inflate(R.layout.activity_detail_venue, container, false);

        Bundle args = getArguments();
        vId = args.getString("VID");

        tv_venueName = (TextView) rootView.findViewById(R.id.vDet_name);
        tv_venueId = (TextView) rootView.findViewById(R.id.vDet_id);

        setupBarButtons();
        bar_isActive.setTextOn(getString(R.string.open));
        bar_isActive.setTextOff(getString(R.string.closed));

        return rootView;
      }

	public void refreshDetails() {
        if (vId != null) {
            v = Venue.getFromId(database, vId);
        }

        mNav.setTitle(v.getName(), "Venue Details");

        tv_venueName.setText(v.getName());
        tv_venueId.setText(String.valueOf(v.getId()));

        bar_isFavorite.setChecked(v.getIsFavorite());
        bar_isActive.setChecked(v.getIsActive());
    }
}
