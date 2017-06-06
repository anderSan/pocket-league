package info.andersonpa.pocketleague;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;

import java.util.ArrayList;
import java.util.List;

import info.andersonpa.pocketleague.backend.Fragment_TopList;
import info.andersonpa.pocketleague.backend.Item_Venue;
import info.andersonpa.pocketleague.backend.ListAdapter_Venue;
import info.andersonpa.pocketleague.db.tables.Venue;

public class List_Venues extends Fragment_TopList {
    private RecyclerView rv;
    private ListAdapter_Venue venue_adapter;
    private List<Item_Venue> venue_list = new ArrayList<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			                 Bundle savedInstanceState) {
        setAddClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFABMenu();
                mNav.modifyVenue(null);
            }
        });

        mNav.setTitle("Venues");
        mNav.setDrawerItemChecked(5);
		rootView = inflater.inflate(R.layout.activity_view_listing, container, false);

        rv = (RecyclerView) rootView.findViewById(R.id.dbListing);
        rv.setLayoutManager(new LinearLayoutManager(context));

        venue_adapter = new ListAdapter_Venue(context, venue_list, lvItemClicked, cbClicked);
        rv.setAdapter(venue_adapter);

        setupFabButtons("Add new venue", "Show closed venues", "Favorites only");

		return rootView;
	}

    @Override
	public void refreshDetails() {
        String title = "";
        if (show_favorites) {
            title += "Favorite ";
            changeFabTitle(3, "Show all");
            changeFabIcon(3, R.drawable.ic_star_border_black_24dp);
        } else {
            changeFabTitle(3, "Favorites only");
            changeFabIcon(3, R.drawable.ic_star_black_24dp);
        }
        if (show_actives) {
            changeFabTitle(2, "Show closed venues");
            changeFabIcon(2, R.drawable.ic_location_off_black_24dp);
        } else {
            title += "Closed ";
            changeFabTitle(2, "Show open venues");
            changeFabIcon(2, R.drawable.ic_location_on_black_24dp);
        }
        mNav.setTitle(title + "Venues");

        List<Venue> venues = getVenues();

        venue_list.clear();
        for (Venue v : venues) {
            venue_list.add(new Item_Venue(v.getId(), v.getName(), v.getIsFavorite()));
        }
        venue_adapter.notifyDataSetChanged();
	}

    private View.OnClickListener lvItemClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String vId = (String) view.getTag();
            mNav.viewVenueDetails(vId);
        }
    };

    private View.OnClickListener cbClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String vId = (String) ((View) view.getParent()).getTag();

            Venue v = Venue.getFromId(database(), vId);
            v.setIsFavorite(((CheckBox) view).isChecked());
            v.update();
        }
    };

    private List<Venue> getVenues() {
        List<Venue> venues = new ArrayList<>();
        try {
            venues = Venue.getVenues(database(), show_actives, show_favorites);
        } catch (CouchbaseLiteException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            loge("Retrieval of venues failed. ", e);
        }
        return venues;
    }
}
