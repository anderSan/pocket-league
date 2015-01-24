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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.twobits.pocketleague.backend.Item_Team;
import com.twobits.pocketleague.backend.ListAdapter_Team;
import com.twobits.pocketleague.db.OrmLiteFragment;
import com.twobits.pocketleague.db.tables.Team;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class View_Teams extends OrmLiteFragment {
	private static final String LOGTAG = "View_Teams";
    private View rootView;
    private Context context;

    private ListView lv;
    private ListAdapter_Team team_adapter;
    private List<Item_Team> team_list = new ArrayList<>();

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
		team_adapter = new ListAdapter_Team(context, R.layout.list_item_team, team_list);
		lv.setAdapter(team_adapter);
		lv.setOnItemClickListener(lvItemClicked);

        return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = getActivity();
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem fav = menu.add("New Team");
		fav.setIcon(R.drawable.ic_menu_add);
		fav.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		fav.setIntent(new Intent(context, NewTeam.class));
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshTeamsListing();
	}

	protected void refreshTeamsListing() {
		team_adapter.clear();

		try {
			Dao<Team, Long> teamDao = getHelper().getTeamDao();
			List<Team> allTeams = teamDao.queryBuilder().where().ne(Team.TEAM_SIZE, 1).query();
			for (Team t : allTeams) {
				team_adapter.add(new Item_Team(String.valueOf(t.getId()), t.getTeamName()));
			}
		} catch (SQLException e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			loge("Retrieval of teams failed. ", e);
		}

//		team_adapter.notifyDataSetChanged(); // required in case the list has changed
	}

    private AdapterView.OnItemClickListener lvItemClicked = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Long tId = Long.valueOf(team_list.get(position).getId());

            String name = team_list.get(position).getName();
            Toast.makeText(context, "Selected " + name, Toast.LENGTH_SHORT).show();
            mNav.viewTeamDetails(tId);
        }
    };
}
