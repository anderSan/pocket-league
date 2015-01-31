package com.twobits.pocketleague;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.twobits.pocketleague.backend.Fragment_TopList;
import com.twobits.pocketleague.backend.Item_Team;
import com.twobits.pocketleague.backend.ListAdapter_Team;
import com.twobits.pocketleague.db.tables.Team;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class List_Teams extends Fragment_TopList {
    private ListView lv;
    private ListAdapter_Team team_adapter;
    private List<Item_Team> team_list = new ArrayList<>();
    private Dao<Team, Long> tDao = null;

    public List_Teams() {
        LOGTAG = "List_Teams";
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			                 Bundle savedInstanceState) {
        setAddClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, NewTeam.class));
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
	public void refreshListing() {
		team_adapter.clear();

		try {
            List<Team> teams;
			tDao = getHelper().getTeamDao();

            if (show_favorites) {
                teams = tDao.queryBuilder().where().ne(Team.TEAM_SIZE, 1)
                        .and().eq(Team.IS_FAVORITE, show_favorites)
                        .and().eq(Team.IS_ACTIVE, show_actives).query();
            } else {
                teams = tDao.queryBuilder().where().ne(Team.TEAM_SIZE, 1)
                        .and().eq(Team.IS_ACTIVE, show_actives).query();
            }

			for (Team t : teams) {
				team_adapter.add(new Item_Team(t.getId(), t.getTeamName(), t.getIsFavorite()));
			}
		} catch (SQLException e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			loge("Retrieval of teams failed. ", e);
		}

//        team_adapter.notifyDataSetChanged(); // required in case the list has changed
	}

    private AdapterView.OnItemClickListener lvItemClicked = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Long tId = team_list.get(position).getId();
            mNav.viewTeamDetails(tId);
        }
    };

    private View.OnClickListener cbClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            long tId = (long) view.getTag();
            try {
                Team t = tDao.queryForId(tId);
                t.setIsFavorite(((CheckBox) view).isChecked());
                tDao.update(t);
            } catch (SQLException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                loge("Retrieval of team failed", e);
            }
        }
    };
}
