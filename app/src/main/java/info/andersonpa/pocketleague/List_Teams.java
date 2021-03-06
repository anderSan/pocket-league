package info.andersonpa.pocketleague;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;

import java.util.ArrayList;
import java.util.List;

import info.andersonpa.pocketleague.backend.Fragment_TopList;
import info.andersonpa.pocketleague.backend.Item_Team;
import info.andersonpa.pocketleague.backend.ListAdapter_Team;
import info.andersonpa.pocketleague.db.tables.Team;

public class List_Teams extends Fragment_TopList {
    private RecyclerView rv;
    private SeekBar sb;
    private ListAdapter_Team team_adapter;
    private List<Item_Team> team_list = new ArrayList<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			                 Bundle savedInstanceState) {
        setAddClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFABMenu();
                mNav.modifyTeam(null);
            }
        });

        mNav.setTitle("Teams");
        mNav.setDrawerItemChecked(4);
		rootView = inflater.inflate(R.layout.activity_view_listing, container, false);

        sb = (SeekBar) rootView.findViewById(R.id.seekBar);
        sb.setVisibility(View.VISIBLE);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                refreshDetails();
            }
        });

		rv = (RecyclerView) rootView.findViewById(R.id.dbListing);
        rv.setLayoutManager(new LinearLayoutManager(context));

		team_adapter = new ListAdapter_Team(context, team_list, lvItemClicked, cbClicked);
		rv.setAdapter(team_adapter);

        setupFabButtons("Add new team", "Show inactive teams", "Favorites only");

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
            changeFabTitle(2, "Show inactive teams");
            changeFabIcon(2, R.drawable.ic_people_outline_black_24dp);
        } else {
            title += "Inactive ";
            changeFabTitle(2, "Show active teams");
            changeFabIcon(2, R.drawable.ic_people_black_24dp);
        }
        mNav.setTitle(title + "Teams", "with " + String.valueOf(sb.getProgress() + 2) + " members.");

		List<Team> teams = getTeams();

        team_list.clear();
        for (Team t : teams) {
            team_list.add(new Item_Team(t.getId(), t.getName(), t.getColor(), t.getIsFavorite()));
        }
        team_adapter.notifyDataSetChanged();
	}

    private View.OnClickListener lvItemClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String tId = (String) view.getTag();
            mNav.viewTeamDetails(tId);
        }
    };

    private View.OnClickListener cbClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String tId = (String) ((View) view.getParent()).getTag();

            Team t = Team.getFromId(database(), tId);
            t.setIsFavorite(((CheckBox) view).isChecked());
            t.update();
        }
    };

    private List<Team> getTeams() {
        List<Team> teams = new ArrayList<>();
        try {
            teams = Team.getTeams(database(), sb.getProgress() + 2, show_actives, show_favorites);
        } catch (CouchbaseLiteException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            loge("Retrieval of teams failed. ", e);
        }
        return teams;
    }
}
