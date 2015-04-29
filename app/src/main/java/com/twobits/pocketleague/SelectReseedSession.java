package com.twobits.pocketleague;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.twobits.pocketleague.backend.Fragment_Edit;
import com.twobits.pocketleague.db.tables.Session;
import com.twobits.pocketleague.db.tables.SessionMember;
import com.twobits.pocketleague.db.tables.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SelectReseedSession extends Fragment_Edit {
    Button btn_done;
    ListView lv_sessions;

    List<Session> sessions = new ArrayList<>();
    Session s;
    List<String> sessionNames = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_select_reseed_session, container, false);

        btn_done = (Button) rootView.findViewById(R.id.btn_done);
        lv_sessions = (ListView) rootView.findViewById(R.id.lv_SessionSelect);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneButtonPushed();
            }
        });

        try {
            sessions.clear();
            sessionNames.clear();
            sessions.addAll(Session.getSessions(database(), mData.getCurrentGameType(), true, false));
            for (Session s : sessions) {
                sessionNames.add(s.getName());
            }
        } catch (CouchbaseLiteException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        updateRosterCheckList();
        lv_sessions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View view, int pos, long arg3) {
                s = sessions.get(pos);
            }
        });

        return rootView;
    }

    public void updateRosterCheckList() {
        lv_sessions.setAdapter(new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_single_choice, sessionNames));
    }

    public void doneButtonPushed() {
        List<SessionMember> members = s.getMembers();
        List<Team> teams = new ArrayList<>();
        Collections.sort(members);
        for (SessionMember sm : members) {
            teams.add(sm.getTeam());
        }
        mNav.setTeams(teams);
    }
}
