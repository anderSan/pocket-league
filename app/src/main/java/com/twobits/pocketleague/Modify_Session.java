package com.twobits.pocketleague;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.twobits.pocketleague.backend.Fragment_Edit;
import com.twobits.pocketleague.backend.SpinnerAdapter;
import com.twobits.pocketleague.db.tables.Session;
import com.twobits.pocketleague.db.tables.SessionMember;
import com.twobits.pocketleague.db.tables.Team;
import com.twobits.pocketleague.db.tables.Venue;
import com.twobits.pocketleague.enums.SessionType;
import com.twobits.pocketleague.gameslibrary.GameSubtype;
import com.twobits.pocketleague.gameslibrary.GameType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Modify_Session extends Fragment_Edit {
	Long sId;
	Session s;
	Dao<Session, Long> sDao;
	Dao<SessionMember, Long> smDao;
    Dao<Team, Long> tDao;
    Dao<Venue, Long> vDao;

	Button btn_create;
	TextView tv_name;
	Spinner sp_sessionType;
	Spinner sp_ruleSet;
	Spinner sp_venues;
	TextView tv_num_selected;
	ListView lv_roster;
	CheckBox cb_isFavorite;

	List<Team> teams = new ArrayList<>();
	List<Integer> teamIdxList = new ArrayList<>();
	List<String> teamNames = new ArrayList<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_modify_session, container, false);

        Bundle args = getArguments();
        sId = args.getLong("SID", -1);

		sDao = mData.getSessionDao();
		smDao = mData.getSessionMemberDao();
        tDao = mData.getTeamDao();
        vDao = mData.getVenueDao();

		btn_create = (Button) rootView.findViewById(R.id.button_createSession);
		tv_name = (TextView) rootView.findViewById(R.id.editText_sessionName);
		sp_sessionType = (Spinner) rootView.findViewById(R.id.newSession_sessionType);
		sp_ruleSet = (Spinner) rootView.findViewById(R.id.newSession_ruleSet);
		sp_venues = (Spinner) rootView.findViewById(R.id.newSession_venues);
		tv_num_selected = (TextView) rootView.findViewById(R.id.tv_num_selected);
		lv_roster = (ListView) rootView.findViewById(R.id.newSession_teamSelection);
		cb_isFavorite = (CheckBox) rootView.findViewById(R.id.newSession_isFavorite);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneButtonPushed();
            }
        });

		List<String> sessionTypes = new ArrayList<>();
		for (SessionType st : SessionType.values()) {
			sessionTypes.add(st.toString());
		}
		ArrayAdapter<String> stAdapter = new SpinnerAdapter(context,
				android.R.layout.simple_spinner_item, sessionTypes,
				Arrays.asList(SessionType.values()));
		sp_sessionType.setAdapter(stAdapter);

		List<String> ruleSetDescriptions = new ArrayList<>();
		GameType currentGameType = mData.getCurrentGameType();
		for (GameSubtype gr : currentGameType.toGameSubtype()) {
			ruleSetDescriptions.add(gr.toDescriptor().getDescription());
		}
		ArrayAdapter<String> rsAdapter = new SpinnerAdapter(context,
				android.R.layout.simple_spinner_item, ruleSetDescriptions,
				currentGameType.toGameSubtype());
		sp_ruleSet.setAdapter(rsAdapter);

		try {
			List<Venue> venues = vDao.queryForAll();
			List<String> venueNames = new ArrayList<>();
			for (Venue v : venues) {
				venueNames.add(v.getName());
			}
			ArrayAdapter<String> vAdapter = new SpinnerAdapter(context,
					android.R.layout.simple_spinner_dropdown_item, venueNames,
					venues);
			sp_venues.setAdapter(vAdapter);
		} catch (SQLException e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}

		try {
			teams = tDao.queryForAll();
			teamNames.clear();
			for (Team t : teams) {
				teamNames.add(t.getTeamName());
			}
		} catch (SQLException e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}

		updateRosterCheckList();
		lv_roster.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView arg0, View view, int pos,
					long arg3) {
				if (teamIdxList.contains(pos)) {
					teamIdxList.remove((Integer) pos);
				} else {
					teamIdxList.add(pos);
				}
				tv_num_selected.setText(teamIdxList.size() + " selected");
			}
		});

		if (sId != -1) {
			loadSessionValues();
		}

        return rootView;
	}

	private void loadSessionValues() {
		try {
			s = sDao.queryForId(sId);
			btn_create.setText("Modify");
			tv_name.setText(s.getSessionName());
			sp_sessionType.setVisibility(View.GONE);
			sp_ruleSet.setVisibility(View.GONE);
			cb_isFavorite.setChecked(s.getIsFavorite());

			// TODO: if loading a session, show player/team names or hide
			// box but dont allow session roster to change or bad things
			// could happen!
			teamNames.clear();
		} catch (SQLException e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	public void updateRosterCheckList() {
		lv_roster.setAdapter(new ArrayAdapter<>(context,
				android.R.layout.simple_list_item_multiple_choice, teamNames));
	}

	public void doneButtonPushed() {
		String session_name = tv_name.getText().toString().trim();
		if (session_name.isEmpty()) {
			Toast.makeText(context, "Session name is required.", Toast.LENGTH_LONG)
					.show();
		} else {
			SessionType session_type = (SessionType) sp_sessionType
					.getSelectedView().getTag();
			GameSubtype game_rule = (GameSubtype) sp_ruleSet.getSelectedView()
					.getTag();
			Venue current_venue = (Venue) sp_venues.getSelectedView().getTag();
			Boolean is_favorite = cb_isFavorite.isChecked();

			if (sId != -1) {
				modifySession(session_name, current_venue, is_favorite);
			} else {
				createSession(session_name, game_rule, session_type,
						current_venue, is_favorite);
			}
		}
	}

	private void createSession(String session_name, GameSubtype game_rule,
			SessionType session_type, Venue current_venue, boolean is_favorite) {
		int team_size = teamIdxList.size();
		Session newSession = new Session(session_name, mData.getCurrentGameType(),
				game_rule, session_type, team_size, current_venue);
		newSession.setIsFavorite(is_favorite);

		List<Team> roster = new ArrayList<>();
		for (Integer teamIdx : teamIdxList) {
			roster.add(teams.get(teamIdx));
		}
		roster = seedRoster(roster);

		try {
			sDao.create(newSession);
			int seed = 0;
			for (Team t : roster) {
				SessionMember sm = new SessionMember(newSession, t, seed);
				smDao.create(sm);
				seed++;
			}
			Toast.makeText(context, "Session created!", Toast.LENGTH_SHORT).show();
			mNav.onBackPressed();
		} catch (SQLException e) {
			loge("Could not create session", e);
			Toast.makeText(context, "Could not create session.",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void modifySession(String session_name, Venue current_venue,
			boolean is_favorite) {
		s.setSessionName(session_name);
		s.setCurrentVenue(current_venue);
		s.setIsFavorite(is_favorite);

		try {
			sDao.update(s);
			Toast.makeText(context, "Session modified.", Toast.LENGTH_SHORT)
					.show();
			mNav.onBackPressed();
		} catch (SQLException e) {
			e.printStackTrace();
			loge("Could not modify session", e);
			Toast.makeText(context, "Could not modify session.",
					Toast.LENGTH_SHORT).show();
		}
	}

	public List<Team> seedRoster(List<Team> roster) {
		// only random seeding so far...
		Collections.shuffle(roster);

		return roster;
	}

}
