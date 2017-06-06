package info.andersonpa.pocketleague;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import info.andersonpa.pocketleague.backend.Fragment_Base;
import info.andersonpa.pocketleague.backend.Item_GameType;
import info.andersonpa.pocketleague.backend.ListAdapter_GameType;
import info.andersonpa.pocketleague.gameslibrary.GameSubtype;
import info.andersonpa.pocketleague.gameslibrary.GameType;


public class List_GameTypes extends Fragment_Base {
    private RecyclerView rv;
	private ListAdapter_GameType gametype_adapter;
	private List<Item_GameType> gametypes_list = new ArrayList<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        mNav.setTitle("Select Game");
        mNav.setDrawerItemChecked(2);
		rootView = inflater.inflate(R.layout.fragment_view_gametypes, container, false);

		rv = (RecyclerView) rootView.findViewById(R.id.gametypes_view);
        rv.setLayoutManager(new GridLayoutManager(context, 3));

		gametype_adapter = new ListAdapter_GameType(context, gametypes_list, gvItemClicked);
		rv.setAdapter(gametype_adapter);

		return rootView;
	}

    @Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

	}

	@Override
	public void onResume() {
		super.onResume();
		refreshDetails();
	}

	public void refreshDetails() {
		gametypes_list.clear();

		for (GameType gt : GameType.values()) {
			gametypes_list.add(new Item_GameType(gt));
		}
	}

	private View.OnClickListener gvItemClicked = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			GameType gt = (GameType) view.getTag();
            List<GameSubtype> gst = gt.toGameSubtype();
            if (gst.size() > 1) {
                mNav.viewGameSubtypes(gt.name());
            } else {
                mData.setCurrentGameSubtype(gst.get(0));
                mNav.viewSessions();
            }
		}
	};
}
