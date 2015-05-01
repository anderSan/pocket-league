package info.andersonpa.pocketleague;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import info.andersonpa.pocketleague.backend.BracketHolder;
import info.andersonpa.pocketleague.backend.Detail_Session_Base;
import info.andersonpa.pocketleague.enums.SessionType;

public class Detail_Session_Elimination extends Detail_Session_Base {
	private BracketHolder bracketHolder = null;

    @Override
    public void createSessionLayout(LayoutInflater inflater, ViewGroup container) {
		rootView = inflater.inflate(R.layout.fragment_detail_session_elimination, container, false);
		ScrollView sv = (ScrollView) rootView.findViewById(R.id.scrollView1);

//		DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
		rootView.getMeasuredWidth();

		boolean isDblElim = s.getSessionType() == SessionType.DBL_ELIM;
		bracketHolder = new BracketHolder(sv, s, isDblElim) {
			@Override
			public void onClick(View v) {
				mInfo = getMatch(v.getId());
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
		if (bracketHolder != null) {
			bracketHolder.refreshBrackets();
		}
	}
}
