package com.twobits.pocketleague;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.twobits.pocketleague.backend.Item_Player;
import com.twobits.pocketleague.backend.ListAdapter_Player;
import com.twobits.pocketleague.db.OrmLiteFragment;
import com.twobits.pocketleague.db.tables.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class View_Players extends OrmLiteFragment {
	private static final String LOGTAG = "View_Players";
    private View rootView;
    private Context context;

    private ListView lv;
    private ListAdapter_Player player_adapter;
    private List<Item_Player> player_list = new ArrayList<>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_view_listing, container, false);

		lv = (ListView) rootView.findViewById(R.id.dbListing);
		player_adapter = new ListAdapter_Player(context, R.layout.list_item_player, player_list);
		lv.setAdapter(player_adapter);
        lv.setOnItemClickListener(lvItemClicked);

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = getActivity();
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem fav = menu.add("New Player");
		fav.setIcon(R.drawable.ic_menu_add);
		fav.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		fav.setIntent(new Intent(context, NewPlayer.class));
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshPlayersListing();
	}

	protected void refreshPlayersListing() {
        player_adapter.clear();

		try {
            Dao<Player, Long> pDao = getHelper().getPlayerDao();
			for (Player p : pDao) {
                player_adapter.add(new Item_Player(String.valueOf(p.getId()),
                        p.getFirstName() + " " + p.getLastName(), p.getNickName(), p.getColor()));
			}
		} catch (SQLException e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			loge("Retrieval of players failed. ", e);
		}

//		player_adapter.notifyDataSetChanged(); // required in case the list has changed
	}

	private AdapterView.OnItemClickListener lvItemClicked = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Long pId = Long.valueOf(player_list.get(position).getId());

            String name = player_list.get(position).getNickname();
            Toast.makeText(context, "Selected " + name, Toast.LENGTH_SHORT).show();
            mNav.viewPlayerDetails(pId);
        }
    };
}
