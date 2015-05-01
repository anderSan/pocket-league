package info.andersonpa.pocketleague;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.couchbase.lite.Database;
import info.andersonpa.pocketleague.backend.DialogFragment_Base;
import info.andersonpa.pocketleague.db.tables.Game;
import info.andersonpa.pocketleague.db.tables.GameMember;
import info.andersonpa.pocketleague.db.tables.Session;

import java.util.ArrayList;
import java.util.List;


public class Quick_Game extends DialogFragment_Base {
    String gId;
    Game g;
    Session s;
    List<GameMember> game_members = new ArrayList<>();

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

            g = Game.getFromId(database(), gId);
            s = g.getSession();
            game_members = g.getMembers();

            ListAdapter adp = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,
                    game_members);

            lv.setAdapter(adp);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for (GameMember gm : game_members) {
                        if (gm.getScore() == 1) {
                            gm.setScore(0);
                        }
                    }
                    game_members.get(position).setScore(1);
                    g.updateMembers(game_members);
                    g.setIsComplete(true);
                    g.update();
                    dismiss();
                    mNav.refreshFragment();
                }
            });
        }

        getDialog().setTitle("Select Winner:");

        return rootView;
    }
}
