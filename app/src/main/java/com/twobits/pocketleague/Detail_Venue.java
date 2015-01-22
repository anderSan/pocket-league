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
import com.twobits.pocketleague.db.tables.Venue;

import java.sql.SQLException;

public class Detail_Venue extends OrmLiteFragment {
	private static final String LOGTAG = "Detail_Venue";
    private View rootView;
    private Context context;

	Long vId;
	Venue v;
	Dao<Venue, Long> vDao;

	TextView tv_venueName;
	TextView tv_venueId;
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
        rootView = inflater.inflate(R.layout.activity_detail_venue, container, false);

        Bundle args = getArguments();
        vId = args.getLong("VID", -1);

		vDao = Venue.getDao(context);

		tv_venueName = (TextView) rootView.findViewById(R.id.vDet_name);
		tv_venueId = (TextView) rootView.findViewById(R.id.vDet_id);
		cb_isFavorite = (CheckBox) rootView.findViewById(R.id.vDet_isFavorite);
		cb_isFavorite.setOnClickListener(favoriteClicked);
		sw_isActive = (Switch) rootView.findViewById(R.id.vDet_isActive);
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

		Intent intent = new Intent(context, NewVenue.class);
		intent.putExtra("VID", vId);
        fav.setIntent(intent);

        mNav.setTitle(v.getName());
    }

	@Override
	public void onResume() {
		super.onResume();
		refreshDetails();
	}

	public void refreshDetails() {
		if (vId != -1) {
			try {

				v = vDao.queryForId(vId);
			} catch (SQLException e) {
				Toast.makeText(context, e.getMessage(),
						Toast.LENGTH_LONG).show();
			}
		}

		tv_venueName.setText(v.getName());
		tv_venueId.setText(String.valueOf(v.getId()));
		cb_isFavorite.setChecked(v.getIsFavorite());
		sw_isActive.setChecked(v.getIsActive());
	}

	private OnClickListener favoriteClicked = new OnClickListener() {
		@Override
		public void onClick(View view) {
			if (vId != -1) {
				v.setIsFavorite(((CheckBox) view).isChecked());
				updateVenue();
			}
		}
	};

	private OnClickListener activeClicked = new OnClickListener() {
		@Override
		public void onClick(View view) {
			if (vId != -1) {
				v.setIsActive(((Switch) view).isChecked());
				updateVenue();
			}
		}
	};

	private void updateVenue() {
		try {
			vDao.update(v);
		} catch (SQLException e) {
			loge("Could not update venue", e);
			e.printStackTrace();
		}
	}
}
