package com.twobits.pocketleague;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.twobits.pocketleague.backend.Fragment_TopList;
import com.twobits.pocketleague.backend.Item_Player;
import com.twobits.pocketleague.backend.ListAdapter_Player;
import com.twobits.pocketleague.db.tables.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class List_Players extends Fragment_TopList {
    ListView lv;
    private ListAdapter_Player player_adapter;
    private List<Item_Player> player_list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setAddClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav.modifyPlayer(null);
            }
        });

        mNav.setTitle("Players");
        mNav.setDrawerItemChecked(3);
        rootView = inflater.inflate(R.layout.activity_view_listing, container, false);

        lv = (ListView) rootView.findViewById(R.id.dbListing);
        player_adapter = new ListAdapter_Player(context, R.layout.list_item_player, player_list,
                cbClicked);
        lv.setAdapter(player_adapter);
        lv.setOnItemClickListener(lvItemClicked);

        setupBarButtons();

        return rootView;
    }

    @Override
    public void refreshDetails() {
        player_adapter.clear();
        List <Player> players = getPlayers();

        for (Player p : players) {
            player_adapter.add(new Item_Player(p.getId(), p.getFullName(), p.getName(),
                    p.getColor(), p.getIsFavorite()));
        }
    }

    private AdapterView.OnItemClickListener lvItemClicked = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String pId = player_list.get(position).getId();
            mNav.viewPlayerDetails(pId);
        }
    };

    private View.OnClickListener cbClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String pId = (String) view.getTag();

            Player p = Player.getFromId(database, pId);
            p.setIsFavorite(((CheckBox) view).isChecked());
            p.update(database);
        }
    };

    private List<Player> getPlayers() {
        List<Player> players = null;
        try {
            Query query = database.getView("all-players").createQuery();
            query.setStartKey(Arrays.asList(show_actives, show_favorites));
            query.setEndKey(Arrays.asList(show_actives, new HashMap<String, Object>()));
            QueryEnumerator result = query.run();

            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow row = it.next();
                players.add(Player.getFromId(database, row.getDocumentId()));
            }
        } catch (CouchbaseLiteException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            loge("Retrieval of players failed. ", e);
        }
        return players;
    }
}
