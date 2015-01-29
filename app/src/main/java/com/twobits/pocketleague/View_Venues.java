package com.twobits.pocketleague;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.twobits.pocketleague.backend.ListAdapter_Venue;
import com.twobits.pocketleague.backend.Item_Venue;
import com.twobits.pocketleague.db.OrmLiteFragment;
import com.twobits.pocketleague.db.tables.Team;
import com.twobits.pocketleague.db.tables.Venue;

public class View_Venues extends OrmLiteFragment {
	static final String LOGTAG = "View_Venues";

    private ListView lv;
    private ListAdapter_Venue venue_adapter;
    private List<Item_Venue> venue_list = new ArrayList<>();
    private Dao<Venue, Long> vDao = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			                 Bundle savedInstanceState) {
        mNav.setTitle("Venues");
        mNav.setDrawerItemChecked(5);
		rootView = inflater.inflate(R.layout.activity_view_listing, container, false);

        lv = (ListView) rootView.findViewById(R.id.dbListing);
        venue_adapter = new ListAdapter_Venue(context, R.layout.list_item_venue, venue_list, cbClicked);
        lv.setAdapter(venue_adapter);
        lv.setOnItemClickListener(lvItemClicked);

		return rootView;
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem fav = menu.add("New Venue");
		fav.setIcon(R.drawable.ic_menu_add);
		fav.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		fav.setIntent(new Intent(context, NewVenue.class));
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshListing();
	}

	protected void refreshListing() {
        venue_adapter.clear();

		try {
            vDao = getHelper().getVenueDao();
			for (Venue v : vDao) {
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
            Long vId = Long.valueOf(venue_list.get(position).getId());
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
