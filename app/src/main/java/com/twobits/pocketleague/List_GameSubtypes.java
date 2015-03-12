package com.twobits.pocketleague;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.twobits.pocketleague.backend.DialogFragment_Base;
import com.twobits.pocketleague.gameslibrary.GameSubtype;
import com.twobits.pocketleague.gameslibrary.GameType;

import java.util.ArrayList;
import java.util.List;


public class List_GameSubtypes extends DialogFragment_Base {
    private ListView lv;
	private List<GameSubtype> subtypes = new ArrayList<>();

    static List_GameSubtypes newInstance(String gametype) {
        List_GameSubtypes f = new List_GameSubtypes();
        Bundle args = new Bundle();
        args.putString("GAMETYPE", gametype);
        f.setArguments(args);

        return f;
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_view_gamesubtypes, container, false);

        Bundle args = getArguments();
        subtypes = GameType.valueOf(args.getString("GAMETYPE")).toGameSubtype();

		lv = (ListView) rootView.findViewById(R.id.lv_gamesubtypes);
        ListAdapter adp = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,
                subtypes);

        lv.setAdapter(adp);
        lv.setOnItemClickListener(new OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  mData.setCurrentGameSubtype(subtypes.get(position));
                  dismiss();
                  mNav.viewSessions();
              }
        });

        getDialog().setTitle("Select Variation:");

		return rootView;
	}
}
