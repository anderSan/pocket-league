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
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.twobits.pocketleague.backend.ListAdapter_Venue;
import com.twobits.pocketleague.backend.Item_Venue;
import com.twobits.pocketleague.db.OrmLiteFragment;
import com.twobits.pocketleague.db.tables.Venue;

public class View_Venues extends OrmLiteFragment {
	private static final String LOGTAG = "View_Venues";
    private View rootView;
    private Context context;

    private ListView lv;
    private ListAdapter_Venue venue_adapter;
    private List<Item_Venue> venue_list = new ArrayList<>();


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			                 Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_view_listing, container, false);

        lv = (ListView) rootView.findViewById(R.id.dbListing);
        venue_adapter = new ListAdapter_Venue(context, R.layout.list_item_venue, venue_list);
        lv.setAdapter(venue_adapter);
        lv.setOnItemClickListener(lvItemClicked);

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = getActivity();
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
		refreshVenuesListing();
	}

	protected void refreshVenuesListing() {
        venue_adapter.clear();

		try {
            Dao<Venue, Long> vDao = getHelper().getVenueDao();
			for (Venue v : vDao) {
                venue_adapter.add(new Item_Venue(String.valueOf(v.getId()), v.getName()));
			}
		} catch (SQLException e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			loge("Retrieval of venues failed", e);
		}

		venue_adapter.notifyDataSetChanged(); // required in case the list has changed
	}

    private AdapterView.OnItemClickListener lvItemClicked = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Long vId = Long.valueOf(venue_list.get(position).getId());

            String name = venue_list.get(position).getName();
            Toast.makeText(context, "Selected " + name, Toast.LENGTH_SHORT).show();
            mNav.viewVenueDetails(vId);
        }
    };
}
