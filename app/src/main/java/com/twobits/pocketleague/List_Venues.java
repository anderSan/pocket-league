package com.twobits.pocketleague;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.twobits.pocketleague.backend.Fragment_TopList;
import com.twobits.pocketleague.backend.Item_Venue;
import com.twobits.pocketleague.backend.ListAdapter_Venue;
import com.twobits.pocketleague.db.tables.Venue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class List_Venues extends Fragment_TopList {
    private ListView lv;
    private ListAdapter_Venue venue_adapter;
    private List<Item_Venue> venue_list = new ArrayList<>();

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
	public void refreshDetails() {
        venue_adapter.clear();
        List<Venue> venues = getVenues();

        for (Venue v : venues) {
            venue_adapter.add(new Item_Venue(v.getId(), v.getName(), v.getIsFavorite()));
        }
	}

    private AdapterView.OnItemClickListener lvItemClicked = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String vId = venue_list.get(position).getId();
            mNav.viewVenueDetails(vId);
        }
    };

    private View.OnClickListener cbClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String vId = (String) view.getTag();

            Venue v = Venue.getFromId(database, vId);
            v.setIsFavorite(((CheckBox) view).isChecked());
            v.update();
        }
    };

    private List<Venue> getVenues() {
        List<Venue> venues = new ArrayList<>();
        try {
            venues = Venue.getVenues(database, show_actives, show_favorites);
        } catch (CouchbaseLiteException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            loge("Retrieval of venues failed. ", e);
        }
        return venues;
    }
}
