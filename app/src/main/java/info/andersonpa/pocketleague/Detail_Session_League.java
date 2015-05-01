package info.andersonpa.pocketleague;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import info.andersonpa.pocketleague.backend.Detail_Session_Base;
import info.andersonpa.pocketleague.backend.LeagueTable;

public class Detail_Session_League extends Detail_Session_Base {
    private LeagueTable league_table;

    @Override
    public void createSessionLayout(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.fragment_detail_session_league, container, false);
        ScrollView sv = (ScrollView) rootView.findViewById(R.id.scrollView1);

        league_table = new LeagueTable(sv, s) {
            @Override
            public void onClick(View v) {
                mInfo = getMatch((int) v.getTag());
                log("gId: " + mInfo.getIdInSession() + ", create: "
                        + mInfo.getCreatable() + ", view: "
                        + mInfo.getViewable() + ", marquee: " + mInfo.getTitle()
                        + ", " + mInfo.getSubtitle());
                mActionMode = rootView.startActionMode(new ActionBarCallBack());
                v.setSelected(true);
            }
        };
    }

    @Override
    public void refreshDetails() {
        if (league_table != null) {
            league_table.refreshTable();
        }
    }
}
