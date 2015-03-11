package com.twobits.pocketleague.backend;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.twobits.pocketleague.db.tables.Game;
import com.twobits.pocketleague.db.tables.Session;
import com.twobits.pocketleague.db.tables.SessionMember;
import com.twobits.pocketleague.enums.BrNodeType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BracketHolder implements View.OnClickListener {
	public static String LOGTAG = "BracketHolder";
	public Context context;
	private Session s;
	private RelativeLayout rl;
	public List<SessionMember> sMembers = new ArrayList<>();
	private Boolean isDoubleElim;
	private Bracket wBr; // winners bracket
	private Bracket lBr; // losers bracket
	private Bracket fBr; // finals bracket

	public BracketHolder(ScrollView sv, Session s, Boolean isDoubleElim) {
		super();
		this.context = sv.getContext();
		this.s = s;
		this.isDoubleElim = isDoubleElim;

        sMembers = s.getMembers();
        Collections.sort(sMembers, SessionMember.SEED_ORDER);

		rl = new RelativeLayout(context);

		foldRoster();
		wBr = new Bracket(sMembers, rl);
		if (isDoubleElim) {
			wBr.labelText = "Winners Bracket";
		}
		wBr.buildBracket(context, this);

		if (isDoubleElim) {
			lBr = new Bracket(sMembers.size(), false, rl);
			lBr.changeOffsets(wBr.lastHeaderId, 0);
			lBr.labelText = "Losers Bracket";
			lBr.seedFromParentBracket(wBr);
			lBr.buildBracket(context, 64, wBr.lowestViewId(), 1, this);

			fBr = new Bracket(sMembers.size(), true, rl);
			fBr.changeOffsets(lBr.lastHeaderId, lBr.lastMatchId + 1);
			fBr.labelText = "Finals";
			fBr.copyBracketMaps(wBr);
			fBr.buildBracket(context, 150, lBr.lowestViewId(), 1, this);
			fBr.setFinalsRespawnText();
		}
		sv.addView(rl);
	}

	public void refreshBrackets() {
		List<Game> sGamesList = s.getGames();

		sGamesList = wBr.matchMatches(sGamesList);
		wBr.refreshViews();

		if (isDoubleElim) {
			lBr.respawnFromParentBracket(wBr);
			sGamesList = lBr.matchMatches(sGamesList);
			lBr.refreshViews();

			fBr.respawnFromParentBracket(wBr);
			fBr.respawnFromParentBracket(lBr);
			sGamesList = fBr.matchMatches(sGamesList);
			fBr.refreshViews();
		}
		assert sGamesList.isEmpty();
	}

	public void foldRoster() {
		// expand the list size to the next power of two
		Integer n = Bracket.factorTwos(sMembers.size());

		SessionMember dummy_sMember = new SessionMember(BrNodeType.BYE.value(),
				-1000);

		while (sMembers.size() < Math.pow(2, n)) {
			sMembers.add(dummy_sMember);
		}
		List<SessionMember> tempRoster = new ArrayList<>();
		for (Integer i = 0; i < n - 1; i++) {
			tempRoster.clear();
			for (Integer j = 0; j < sMembers.size() / Math.pow(2, i + 1); j++) {
				tempRoster.addAll(sMembers.subList(j * (int) Math.pow(2, i),
						(j + 1) * (int) Math.pow(2, i)));
				tempRoster.addAll(sMembers.subList(sMembers.size() - (j + 1)
						* (int) Math.pow(2, i), sMembers.size() - (j)
						* (int) Math.pow(2, i)));
			}
			sMembers.clear();
			sMembers.addAll(tempRoster);
		}
	}

	public MatchInfo getMatchInfo(int viewId) {
		MatchInfo mInfo = wBr.getMatchInfo(viewId);

		if (isDoubleElim) {
			if (lBr.hasView(viewId)) {
				mInfo = lBr.getMatchInfo(viewId);
			} else if (fBr.hasView(viewId)) {
				mInfo = fBr.getMatchInfo(viewId);
			}
		}

		return mInfo;
	}

	@Override
	public void onClick(View v) {
		Log.i(LOGTAG, "View " + v.getId() + " was clicked");
	}
}
