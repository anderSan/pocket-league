package com.twobits.pocketleague;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.twobits.pocketleague.backend.Fragment_TopList;
import com.twobits.pocketleague.backend.Item_GameType;
import com.twobits.pocketleague.backend.ListAdapter_GameType;
import com.twobits.pocketleague.gameslibrary.GameType;

import java.util.ArrayList;
import java.util.List;


public class List_GameTypes extends Fragment_TopList {
    private GridView gv;
	private ListAdapter_GameType gametype_adapter;
	private List<Item_GameType> gametypes_list = new ArrayList<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        mNav.setTitle("Select Game");
        mNav.setDrawerItemChecked(2);
		rootView = inflater.inflate(R.layout.fragment_view_gametypes, container, false);

		gv = (GridView) rootView.findViewById(R.id.gametypes_view);
		gametype_adapter = new ListAdapter_GameType(context, R.layout.grid_item, gametypes_list);
		gv.setAdapter(gametype_adapter);
		gv.setOnItemClickListener(gvItemClicked);
		// gv.setOnItemLongClickListener(elvItemLongClicked);

		return rootView;
	}

    @Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

	}

	public void refreshDetails() {
		gametypes_list.clear();

		for (GameType gt : GameType.values()) {
			gametypes_list.add(new Item_GameType(gt));
		}

//		gametype_adapter.notifyDataSetChanged(); // required if list has changed
	}

	private OnItemClickListener gvItemClicked = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			GameType gt = (GameType) view.getTag();
			mNav.viewGameSubtypes(gt.name());
		}
	};
}
