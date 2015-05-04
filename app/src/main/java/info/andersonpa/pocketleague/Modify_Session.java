package info.andersonpa.pocketleague;

import android.os.Bundle;
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
import info.andersonpa.pocketleague.backend.Add_Teams;
import info.andersonpa.pocketleague.backend.Fragment_Edit;
import info.andersonpa.pocketleague.backend.SpinnerAdapter;
import info.andersonpa.pocketleague.db.tables.Session;
import info.andersonpa.pocketleague.db.tables.SessionMember;
import info.andersonpa.pocketleague.db.tables.Team;
import info.andersonpa.pocketleague.db.tables.Venue;
import info.andersonpa.pocketleague.enums.SessionType;
import info.andersonpa.pocketleague.gameslibrary.GameSubtype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Modify_Session extends Fragment_Edit implements Add_Teams {
    String sId;
	Session s;

	Button btn_create;
	Button btn_select;
	Button btn_reseed;
	TextView tv_name;
	Spinner sp_sessionType;
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
		btn_reseed = (Button) rootView.findViewById(R.id.btn_reseed);
		tv_name = (TextView) rootView.findViewById(R.id.editText_sessionName);
		sp_sessionType = (Spinner) rootView.findViewById(R.id.newSession_sessionType);
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

		btn_reseed.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mNav.selectReseedSession();
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
		btn_select.setVisibility(View.GONE);
		btn_reseed.setVisibility(View.GONE);
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
				createSession(session_name, session_type, current_venue, is_favorite);
			}
		}
	}

	@Override
	public void setTeams(List<Team> teams) {
		this.teams = teams;
		tv_num_selected.setText(teams.size() + " selected");
	}

	private void createSession(String session_name, SessionType session_type, Venue current_venue,
							   boolean is_favorite) {
		GameSubtype game_subtype = mData.getCurrentGameSubtype();
		int team_size = teams.size();

        List<SessionMember> roster = new ArrayList<>();
        int seed = 0;
        for (Team team : teams) {
            roster.add(new SessionMember(team, seed));
            seed++;
        }

        Session newSession = new Session(database(), session_name, session_type, game_subtype,
                team_size, current_venue, roster, is_favorite);
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
