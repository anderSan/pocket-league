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
import info.andersonpa.pocketleague.backend.Item_Player;
import info.andersonpa.pocketleague.backend.ListAdapter_Player;
import info.andersonpa.pocketleague.db.tables.Player;

public class List_Players extends Fragment_TopList {
    private RecyclerView rv;
    private ListAdapter_Player player_adapter;
    private List<Item_Player> player_list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setAddClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFABMenu();
                mNav.modifyPlayer(null);
            }
        });

        mNav.setTitle("Players");
        mNav.setDrawerItemChecked(3);
        rootView = inflater.inflate(R.layout.activity_view_listing, container, false);

        rv = (RecyclerView) rootView.findViewById(R.id.dbListing);
        rv.setLayoutManager(new LinearLayoutManager(context));

        player_adapter = new ListAdapter_Player(context, player_list, lvItemClicked, cbClicked);
        rv.setAdapter(player_adapter);

        setupFabButtons("Add new player", "Show inactive players", "Favorites only");

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
            changeFabTitle(2, "Show inactive players");
            changeFabIcon(2, R.drawable.ic_person_outline_black_24dp);
        } else {
            title += "Inactive ";
            changeFabTitle(2, "Show active players");
            changeFabIcon(2, R.drawable.ic_person_black_24dp);
        }
        mNav.setTitle(title + "Players");

        List <Player> players = getPlayers();

        player_list.clear();
        for (Player p : players) {
            player_list.add(new Item_Player(p.getId(), p.getFullName(), p.getName(),
                    p.getColor(), p.getIsFavorite()));
        }
        player_adapter.notifyDataSetChanged();
    }

    private View.OnClickListener lvItemClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String vId = (String) view.getTag();
            mNav.viewPlayerDetails(vId);
        }
    };

    private View.OnClickListener cbClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String pId = (String) ((View) view.getParent()).getTag();

            Player p = Player.getFromId(database(), pId);
            p.setIsFavorite(((CheckBox) view).isChecked());
            p.update();
        }
    };

    private List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        try {
            players = Player.getPlayers(database(), show_actives, show_favorites);
        } catch (CouchbaseLiteException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            loge("Retrieval of players failed. ", e);
        }
        return players;
    }
}
