package com.twobits.pocketleague;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.couchbase.lite.Database;
import com.twobits.pocketleague.backend.DialogFragment_Base;
import com.twobits.pocketleague.backend.Item_QuickGame;
import com.twobits.pocketleague.db.tables.Game;
import com.twobits.pocketleague.db.tables.GameMember;
import com.twobits.pocketleague.db.tables.Session;

import java.util.ArrayList;
import java.util.List;


public class Quick_Game extends DialogFragment_Base {
    String gId;
    Game g;
    Session s;
    List<Item_QuickGame> game_members = new ArrayList<>();

    private ListView lv;

    static Quick_Game newInstance(Database database, String gId) {
        Quick_Game f = new Quick_Game();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("GID", gId);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_quick_game, container, false);

        Bundle args = getArguments();
        gId = args.getString("GID");

        lv = (ListView) rootView.findViewById(R.id.lv_quickgame);

        if (gId != null) {

//            g = Game.getFromId(database, gId);
            s = g.getSession();


//            for (GameMember gm : g.getMembers()) {
//                game_members.add(new Item_QuickGame(gm));
//            }
            ListAdapter adp = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,
                    game_members);

            lv.setAdapter(adp);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {
                            for (Item_QuickGame gmi : game_members) {
                                if (gmi.getScore() == 1) {
//                                    try {
//                                        gmi.getGM().setScore(0);
//                                        gmDao.update(gmi.getGM());
//                                    } catch (SQLException e) {
//                                        Toast.makeText(context, e.getMessage(),
//                                                Toast.LENGTH_LONG).show();
//                                    }
                                }
                            }
//                            try {
                                GameMember gm = game_members.get(position).getGM();
                                gm.setScore(1);
//                                gmDao.update(gm);
                                g.setIsComplete(true);
//                                gDao.update(g);
//                            } catch (SQLException e) {
//                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
//                            }
                            getDialog().dismiss();
                            mNav.refreshFragment();
                        }
                    });
        }

        getDialog().setTitle("Select Winner:");

        return rootView;
    }
}
