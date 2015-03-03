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
import com.twobits.pocketleague.backend.Item_Team;
import com.twobits.pocketleague.backend.ListAdapter_Team;
import com.twobits.pocketleague.db.tables.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class List_Teams extends Fragment_TopList {
    private ListView lv;
    private ListAdapter_Team team_adapter;
    private List<Item_Team> team_list = new ArrayList<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			                 Bundle savedInstanceState) {
        setAddClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav.modifyTeam(null);
            }
        });

        mNav.setTitle("Teams");
        mNav.setDrawerItemChecked(4);
		rootView = inflater.inflate(R.layout.activity_view_listing, container, false);

		lv = (ListView) rootView.findViewById(R.id.dbListing);
		team_adapter = new ListAdapter_Team(context, R.layout.list_item_team, team_list, cbClicked);
		lv.setAdapter(team_adapter);
		lv.setOnItemClickListener(lvItemClicked);

        setupBarButtons();

        return rootView;
	}

    @Override
	public void refreshDetails() {
		team_adapter.clear();
        List<Team> teams = getTeams();

        for (Team t : teams) {
            team_adapter.add(new Item_Team(t.getId(), t.getName(), t.getIsFavorite()));
        }
	}

    private AdapterView.OnItemClickListener lvItemClicked = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String tId = team_list.get(position).getId();
            mNav.viewTeamDetails(tId);
        }
    };

    private View.OnClickListener cbClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String tId = (String) view.getTag();

            Team t = Team.getFromId(database, tId);
            t.setIsFavorite(((CheckBox) view).isChecked());
            t.update();
        }
    };

    private List<Team> getTeams() {
        List<Team> teams = new ArrayList<>();
        try {
            Query query = database.getView("all-teams").createQuery();
            query.setStartKey(Arrays.asList(show_actives, show_favorites));
            query.setEndKey(Arrays.asList(show_actives, new HashMap<String, Object>()));
            QueryEnumerator result = query.run();

            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow row = it.next();
                teams.add(Team.getFromId(database, row.getDocumentId()));
            }
        } catch (CouchbaseLiteException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            loge("Retrieval of teams failed. ", e);
        }
        return teams;
    }
}
