package com.twobits.pocketleague;

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
import android.widget.ToggleButton;

import com.j256.ormlite.dao.Dao;
import com.twobits.pocketleague.backend.Fragment_Detail;
import com.twobits.pocketleague.db.OrmLiteFragment;
import com.twobits.pocketleague.db.tables.Venue;

import java.sql.SQLException;

public class Detail_Venue extends Fragment_Detail {
	static final String LOGTAG = "Detail_Venue";

	Long vId;
	Venue v;
	Dao<Venue, Long> vDao;

	TextView tv_venueName;
	TextView tv_venueId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_detail_venue, container, false);

        Bundle args = getArguments();
        vId = args.getLong("VID", -1);

		vDao = Venue.getDao(context);

		tv_venueName = (TextView) rootView.findViewById(R.id.vDet_name);
		tv_venueId = (TextView) rootView.findViewById(R.id.vDet_id);

        setModifyClicked(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(context, NewVenue.class);
                intent.putExtra("VID", vId);
                startActivity(intent);
                return false;
            }
        });

        setFavoriteClicked(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vId != -1) {
                    v.setIsFavorite(((CheckBox) view).isChecked());
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

        mNav.setTitle(v.getName());

        tv_venueName.setText(v.getName());
        tv_venueId.setText(String.valueOf(v.getId()));

        mi_isFavorite.setChecked(v.getIsFavorite());
        mi_isActive.setChecked(v.getIsActive());
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
