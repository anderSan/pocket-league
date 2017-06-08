package info.andersonpa.pocketleague;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;

import java.util.ArrayList;
import java.util.List;

import info.andersonpa.pocketleague.backend.Fragment_TopList;
import info.andersonpa.pocketleague.backend.Item_Session;
import info.andersonpa.pocketleague.backend.ListAdapter_Session;
import info.andersonpa.pocketleague.db.tables.Session;
import info.andersonpa.pocketleague.enums.SessionType;

public class List_Sessions extends Fragment_TopList {
    private RecyclerView rv;
    private ListAdapter_Session session_adapter;
    private List<Item_Session> session_list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setAddClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFABMenu();
                mNav.modifySession(null);
            }
        });

        mNav.setTitle("Sessions", "for " + mData.getCurrentGameType().toString());
        mNav.setDrawerItemChecked(0);
        rootView = inflater.inflate(R.layout.activity_view_listing, container, false);

        rv = (RecyclerView) rootView.findViewById(R.id.dbListing);
        rv.setLayoutManager(new LinearLayoutManager(context));

        session_adapter = new ListAdapter_Session(context, session_list, lvItemClicked, cbClicked);
        rv.setAdapter(session_adapter);

        setupFabButtons("Add new session", "Show closed sessions", "Favorites only");

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
            changeFabTitle(2, "Show closed sessions");
            changeFabIcon(2, R.drawable.ic_event_busy_black_24dp);
        } else {
            title += "Closed ";
            changeFabTitle(2, "Show open sessions");
            changeFabIcon(2, R.drawable.ic_event_available_black_24dp);
        }
        mNav.setTitle(title + "Sessions", "for " + mData.getCurrentGameType().toString());

        List<Session> sessions = getSessions();

        session_list.clear();
        for (Session s : sessions) {
            session_list.add(new Item_Session(s.getId(), s.getName(), s.getSessionType(),
                    s.getIsFavorite()));
        }
        session_adapter.notifyDataSetChanged();
    }

    private View.OnClickListener lvItemClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String sId = (String) view.getTag();
            SessionType session_type = Session.getFromId(database(), sId).getSessionType();
            mNav.viewSessionDetails(sId, session_type);
        }
    };

    private View.OnClickListener cbClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String sId = (String) ((View) view.getParent()).getTag();

            Session s = Session.getFromId(database(), sId);
            s.setIsFavorite(((CheckBox) view).isChecked());
            s.update();
        }
    };

    private List<Session> getSessions() {
        List<Session> sessions = new ArrayList<>();
        try {
            sessions = Session.getSessions(database(), mData.getCurrentGameSubtype(), show_actives, show_favorites);
        } catch (CouchbaseLiteException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            loge("Retrieval of sessions failed. ", e);
        }
        return sessions;
    }
}
