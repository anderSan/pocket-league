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
import com.twobits.pocketleague.backend.Item_Session;
import com.twobits.pocketleague.backend.ListAdapter_Session;
import com.twobits.pocketleague.db.tables.Session;
import com.twobits.pocketleague.enums.SessionType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class List_Sessions extends Fragment_TopList {
    ListView lv;
    private ListAdapter_Session session_adapter;
    private List<Item_Session> session_list = new ArrayList<>();
    private Dao<Session, Long> sDao = null;

    public List_Sessions() {
        LOGTAG = "List_Sessions";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setAddClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, NewSession.class));
            }
        });

        mNav.setTitle("Sessions");
        mNav.setDrawerItemChecked(0);
        rootView = inflater.inflate(R.layout.activity_view_listing, container, false);

        lv = (ListView) rootView.findViewById(R.id.dbListing);
        session_adapter = new ListAdapter_Session(context, R.layout.list_item_session,
                session_list, cbClicked);
        lv.setAdapter(session_adapter);
        lv.setOnItemClickListener(lvItemClicked);

        setupBarButtons(getString(R.string.open), getString(R.string.closed));

        return rootView;
    }

    @Override
    public void refreshListing() {
        session_adapter.clear();

        try {
            List<Session> sessions;
            sDao = getHelper().getSessionDao();

            if (show_favorites) {
                sessions = sDao.queryBuilder().where().eq(Session.GAME_TYPE, getCurrentGameType())
                        .and().eq(Session.IS_FAVORITE, show_favorites)
                        .and().eq(Session.IS_ACTIVE, show_actives).query();
            } else {
                sessions = sDao.queryBuilder().where().eq(Session.GAME_TYPE, getCurrentGameType())
                        .and().eq(Session.IS_ACTIVE, show_actives).query();
            }

            for (Session s : sessions) {
                session_adapter.add(new Item_Session(s.getId(), s.getSessionName(),
                        s.getSessionType(), s.getIsFavorite()));
            }
        } catch (SQLException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            loge("Retrieval of sessions failed", e);
        }

        //		session_adapter.notifyDataSetChanged(); // required in case the list has changed
    }

    private AdapterView.OnItemClickListener lvItemClicked = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            long sId = session_list.get(position).getId();
            SessionType session_type = session_list.get(position).getSessionType();

            //            mNav.viewSessionDetails(sId);
//            Intent intent = new Intent(context, session_type.toClass());
//            intent.putExtra("SID", sId);
//            startActivity(intent);
            mNav.viewSessionDetails(sId, session_type);
        }
    };

    private View.OnClickListener cbClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            long sId = (long) view.getTag();
            try {
                Session s = sDao.queryForId(sId);
                s.setIsFavorite(((CheckBox) view).isChecked());
                sDao.update(s);
            } catch (SQLException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                loge("Retrieval of session failed", e);
            }
        }
    };
}
