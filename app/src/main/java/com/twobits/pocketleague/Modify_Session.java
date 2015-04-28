package com.twobits.pocketleague;

import android.os.Bundle;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.twobits.pocketleague.backend.Fragment_Edit;
import com.twobits.pocketleague.backend.SpinnerAdapter;
import com.twobits.pocketleague.db.tables.Session;
import com.twobits.pocketleague.db.tables.SessionMember;
import com.twobits.pocketleague.db.tables.Team;
import com.twobits.pocketleague.db.tables.Venue;
import com.twobits.pocketleague.enums.SessionType;
import com.twobits.pocketleague.gameslibrary.GameSubtype;
import com.twobits.pocketleague.gameslibrary.GameType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Modify_Session extends Fragment_Edit {
    String sId;
	Session s;

	Button btn_create;
	Button btn_select;
	TextView tv_name;
	Spinner sp_sessionType;
	Spinner sp_ruleSet;
	Spinner sp_venues;
	TextView tv_num_selected;
	CheckBox cb_isFavorite;

	List<Team> teams = new ArrayList<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_modify_session, container, false);

        Bundle args = getArguments();
        sId = args.getString("SID");

		btn_create = (Button) rootView.findViewById(R.id.button_createSession);
		btn_select = (Button) rootView.findViewById(R.id.btn_chooseTeams);
		tv_name = (TextView) rootView.findViewById(R.id.editText_sessionName);
		sp_sessionType = (Spinner) rootView.findViewById(R.id.newSession_sessionType);
		sp_ruleSet = (Spinner) rootView.findViewById(R.id.newSession_ruleSet);
		sp_venues = (Spinner) rootView.findViewById(R.id.newSession_venues);
		tv_num_selected = (TextView) rootView.findViewById(R.id.tv_num_selected);
		cb_isFavorite = (CheckBox) rootView.findViewById(R.id.newSession_isFavorite);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneButtonPushed();
            }
        });

		btn_select.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mNav.selectTeams();
			}
		});

		List<String> sessionTypes = new ArrayList<>();
		for (SessionType st : SessionType.values()) {
			sessionTypes.add(st.toString());
		}
		ArrayAdapter<String> stAdapter = new SpinnerAdapter(context,
				android.R.layout.simple_spinner_dropdown_item, sessionTypes,
				Arrays.asList(SessionType.values()));
		sp_sessionType.setAdapter(stAdapter);

		List<String> ruleSetDescriptions = new ArrayList<>();
		GameType currentGameType = mData.getCurrentGameType();
		for (GameSubtype gr : currentGameType.toGameSubtype()) {
			ruleSetDescriptions.add(gr.toDescriptor().getDescription());
		}
		ArrayAdapter<String> rsAdapter = new SpinnerAdapter(context,
				android.R.layout.simple_spinner_dropdown_item, ruleSetDescriptions,
				currentGameType.toGameSubtype());
		sp_ruleSet.setAdapter(rsAdapter);

		try {
            List<String> venueNames = new ArrayList<>();
            List<Venue> venues = Venue.getVenues(database(), true, false);

            for (Venue v : venues) {
                venueNames.add(v.getName());
            }

			ArrayAdapter<String> vAdapter = new SpinnerAdapter(context,
					android.R.layout.simple_spinner_dropdown_item, venueNames, venues);
			sp_venues.setAdapter(vAdapter);
		} catch (CouchbaseLiteException e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}


//		tv_num_selected.setText(teamIdxList.size() + " selected");


		if (sId != null) {
			loadSessionValues();
		}

        return rootView;
	}

	private void loadSessionValues() {
        s = Session.getFromId(database(), sId);
        btn_create.setText("Modify");
        tv_name.setText(s.getName());
        sp_sessionType.setVisibility(View.GONE);
        sp_ruleSet.setVisibility(View.GONE);
		btn_select.setVisibility(View.GONE);
        cb_isFavorite.setChecked(s.getIsFavorite());
	}

	public void doneButtonPushed() {
		String session_name = tv_name.getText().toString().trim();
		if (session_name.isEmpty()) {
			Toast.makeText(context, "Session name is required.", Toast.LENGTH_LONG).show();
		} else {
			Venue current_venue = (Venue) sp_venues.getSelectedView().getTag();
			Boolean is_favorite = cb_isFavorite.isChecked();

			if (sId != null) {
				modifySession(session_name, current_venue, is_favorite);
			} else {
				SessionType session_type = (SessionType) sp_sessionType.getSelectedView().getTag();
				GameSubtype game_rule = (GameSubtype) sp_ruleSet.getSelectedView().getTag();
				createSession(session_name, game_rule, session_type, current_venue, is_favorite);
			}
		}
	}

	@Override @SuppressWarnings("unchecked")
	public void putResult(Object result) {
		teams = (List<Team>) result;
		tv_num_selected.setText(String.valueOf(teams.size()));
	}

	private void createSession(String session_name, GameSubtype game_subtype,
			SessionType session_type, Venue current_venue, boolean is_favorite) {
		int team_size = teams.size();
        int ruleset_id = 0;

        List<SessionMember> roster = new ArrayList<>();
        int seed = 0;
        for (Team team : teams) {
            roster.add(new SessionMember(team, seed));
            seed++;
        }

        Session newSession = new Session(database(), session_name, session_type, game_subtype,
                ruleset_id, team_size, current_venue, roster, is_favorite);
        newSession.setIsFavorite(is_favorite);
        newSession.update();

        Toast.makeText(context, "Session created!", Toast.LENGTH_SHORT).show();
        mNav.onBackPressed();
	}

	private void modifySession(String session_name, Venue current_venue, boolean is_favorite) {
		s.setName(session_name);
		s.setCurrentVenue(current_venue);
		s.setIsFavorite(is_favorite);

        s.update();
        Toast.makeText(context, "Session modified.", Toast.LENGTH_SHORT).show();
        mNav.onBackPressed();
	}
}
