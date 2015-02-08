package com.twobits.pocketleague;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.twobits.pocketleague.backend.Fragment_TopList;
import com.twobits.pocketleague.backend.Item_Venue;
import com.twobits.pocketleague.backend.ListAdapter_Venue;
import com.twobits.pocketleague.db.tables.Venue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class List_Venues extends Fragment_TopList {
    private ListView lv;
    private ListAdapter_Venue venue_adapter;
    private List<Item_Venue> venue_list = new ArrayList<>();
    private Dao<Venue, Long> vDao;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			                 Bundle savedInstanceState) {
        setAddClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav.modifyVenue(null);
            }
        });
        mNav.setTitle("Venues");
        mNav.setDrawerItemChecked(5);
		rootView = inflater.inflate(R.layout.activity_view_listing, container, false);

        lv = (ListView) rootView.findViewById(R.id.dbListing);
        venue_adapter = new ListAdapter_Venue(context, R.layout.list_item_venue, venue_list, cbClicked);
        lv.setAdapter(venue_adapter);
        lv.setOnItemClickListener(lvItemClicked);

        setupBarButtons(getString(R.string.open), getString(R.string.closed));

		return rootView;
	}

    @Override
	public void refreshListing() {
        venue_adapter.clear();

		try {
            List<Venue> venues;
            vDao = mData.getVenueDao();

            if (show_favorites) {
                venues = vDao.queryBuilder().where().eq(Venue.IS_FAVORITE, show_favorites)
                        .and().eq(Venue.IS_ACTIVE, show_actives).query();
            } else {
                venues = vDao.queryBuilder().where().eq(Venue.IS_ACTIVE, show_actives).query();
            }
			for (Venue v : venues) {
                venue_adapter.add(new Item_Venue(v.getId(), v.getName(), v.getIsFavorite()));
			}
		} catch (SQLException e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			loge("Retrieval of venues failed", e);
		}

//		venue_adapter.notifyDataSetChanged(); // required in case the list has changed
	}

    private AdapterView.OnItemClickListener lvItemClicked = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Long vId = venue_list.get(position).getId();
            mNav.viewVenueDetails(vId);
        }
    };

    private View.OnClickListener cbClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            long vId = (long) view.getTag();
            try {
                Venue v = vDao.queryForId(vId);
                v.setIsFavorite(((CheckBox) view).isChecked());
                vDao.update(v);
            } catch (SQLException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                loge("Retrieval of venue failed", e);
            }
        }
    };
}
