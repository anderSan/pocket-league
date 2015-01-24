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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.twobits.pocketleague.backend.ListAdapter_GameType;
import com.twobits.pocketleague.backend.Item_GameType;
import com.twobits.pocketleague.db.OrmLiteFragment;
import com.twobits.pocketleague.gameslibrary.GameType;

import java.util.ArrayList;
import java.util.List;


public class View_GameTypes extends OrmLiteFragment {
	private static final String LOGTAG = "View_GameTypes";
    private View rootView;
    private Context context;

    private GridView gv;
	private ListAdapter_GameType gametype_adapter;
	private List<Item_GameType> gametypes_list = new ArrayList<>();


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_view_gametypes, container, false);

		gv = (GridView) rootView.findViewById(R.id.gametypes_view);
		gametype_adapter = new ListAdapter_GameType(context, R.layout.grid_item, gametypes_list);
		gv.setAdapter(gametype_adapter);
		gv.setOnItemClickListener(gvItemClicked);
		// gv.setOnItemLongClickListener(elvItemLongClicked);

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = getActivity();
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem fav = menu.add("New Game");
		fav.setIcon(R.drawable.ic_menu_add);
		fav.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		fav.setIntent(new Intent(context, NewGame.class));
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshGameTypesListing();
	}

	protected void refreshGameTypesListing() {
		gametypes_list.clear();

		for (GameType gt : GameType.values()) {
			gametypes_list.add(new Item_GameType(gt));
		}

		gametype_adapter.notifyDataSetChanged(); // required if list has changed
	}

	private OnItemClickListener gvItemClicked = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			GameType gt = (GameType) view.getTag();
			setCurrentGameType(gt);
			mNav.viewSessions();
		}
	};
}
