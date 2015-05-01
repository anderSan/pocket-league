package info.andersonpa.pocketleague;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import info.andersonpa.pocketleague.backend.Detail_Session_Base;
import info.andersonpa.pocketleague.backend.LadderTable;

public class Detail_Session_Ladder extends Detail_Session_Base {
    private LadderTable ladder_table;

    @Override
    public void createSessionLayout(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.fragment_detail_session_ladder, container, false);
        FrameLayout fl = (FrameLayout) rootView.findViewById(R.id.frame1);

        ladder_table = new LadderTable(fl, s) {
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
        if (ladder_table != null) {
            ladder_table.refreshTable();
        }
    }
}
