package info.andersonpa.pocketleague;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import info.andersonpa.pocketleague.backend.Fragment_Edit;
import info.andersonpa.pocketleague.db.tables.Player;
import info.andersonpa.pocketleague.db.tables.Team;

import java.util.ArrayList;
import java.util.List;


public class Select_Teams extends Fragment_Edit {
    Button btn_done;
    TextView tv_num_selected;
    ListView lv_roster;

    List<Team> teams = new ArrayList<>();
    List<Integer> teamIdxList = new ArrayList<>();
    List<String> teamNames = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_select_teams, container, false);

//        Bundle args = getArguments();
//        sId = args.getString("SID");

        btn_done = (Button) rootView.findViewById(R.id.btn_done);
        tv_num_selected = (TextView) rootView.findViewById(R.id.tv_num_selected);
        lv_roster = (ListView) rootView.findViewById(R.id.lv_teamSelection);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneButtonPushed();
            }
        });

        try {
            teams.clear();
            teamNames.clear();
            teams.addAll(Player.getPlayers(database(), true, false));
            teams.addAll(Team.getTeams(database(), 1, true, false));
            for (Team t : teams) {
                teamNames.add(t.getName());
            }
        } catch (CouchbaseLiteException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        updateRosterCheckList();
        lv_roster.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View view, int pos, long arg3) {
                if (teamIdxList.contains(pos)) {
                    teamIdxList.remove((Integer) pos);
                } else {
                    teamIdxList.add(pos);
                }
                tv_num_selected.setText(teamIdxList.size() + " selected");
            }
        });

        return rootView;
    }

    public void updateRosterCheckList() {
        lv_roster.setAdapter(new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_multiple_choice, teamNames));
    }

    public void doneButtonPushed() {
        List<Team> team_ids = new ArrayList<>();
        for (Integer teamIdx : teamIdxList) {
            team_ids.add(teams.get(teamIdx));
        }
        mNav.setTeams(team_ids);
    }
}
