package com.twobits.pocketleague;

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
import com.twobits.pocketleague.db.tables.Venue;

import java.sql.SQLException;

public class Detail_Venue extends Fragment_Detail {
	Long vId;
	Venue v;
	Dao<Venue, Long> vDao;

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
                if (vId != -1) {
                    v.setIsFavorite(((ToggleButton) view).isChecked());
                    updateVenue();
                }
            }
        });

        setActiveClicked(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vId != -1) {
                    v.setIsActive(((ToggleButton) view).isChecked());
                    updateVenue();
                }
            }
        });

        rootView = inflater.inflate(R.layout.activity_detail_venue, container, false);

        Bundle args = getArguments();
        vId = args.getLong("VID", -1);

        vDao = mData.getVenueDao();

        tv_venueName = (TextView) rootView.findViewById(R.id.vDet_name);
        tv_venueId = (TextView) rootView.findViewById(R.id.vDet_id);

        setupBarButtons();
        bar_isActive.setTextOn(getString(R.string.open));
        bar_isActive.setTextOff(getString(R.string.closed));

        return rootView;
      }

	public void refreshDetails() {
        if (vId != -1) {
            try {

                v = vDao.queryForId(vId);
            } catch (SQLException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        mNav.setTitle(v.getName(), "Venue Details");

        tv_venueName.setText(v.getName());
        tv_venueId.setText(String.valueOf(v.getId()));

        bar_isFavorite.setChecked(v.getIsFavorite());
        bar_isActive.setChecked(v.getIsActive());
    }

	private void updateVenue() {
		try {
			vDao.update(v);
		} catch (SQLException e) {
			loge("Could not update venue", e);
			e.printStackTrace();
		}
	}
}
