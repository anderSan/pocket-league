package com.twobits.pocketleague.backend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.j256.ormlite.dao.Dao;
import com.twobits.pocketleague.NewPlayer;
import com.twobits.pocketleague.NewSession;
import com.twobits.pocketleague.Quick_Game;
import com.twobits.pocketleague.R;
import com.twobits.pocketleague.db.tables.Game;
import com.twobits.pocketleague.db.tables.GameMember;
import com.twobits.pocketleague.db.tables.Session;
import com.twobits.pocketleague.db.tables.SessionMember;
import com.twobits.pocketleague.db.tables.Team;

import java.sql.SQLException;

public class Detail_Session_Base extends Fragment_Detail {
	private static final String LOGTAG = "Detail_Session";

	public Long sId;
	public Session s;

	public Dao<Session, Long> sDao;
	public Dao<SessionMember, Long> smDao;
	public Dao<GameMember, Long> gmDao;

	public MatchInfo mInfo;
	public ActionMode mActionMode;

    @Override
    public void onAttach(Activity activity) {
        setModifyClicked(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(context, NewPlayer.class);
                intent.putExtra("SID", sId);
                startActivity(intent);
                return false;
            }
        });

        setFavoriteClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sId != -1) {
                    boolean is_favorite = ((CheckBox) v).isChecked();
                    s.setIsFavorite(is_favorite);
//                    updateSession();
                }
            }
        });
        super.onAttach(activity);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
		sId = args.getLong("SID", -1);

		if (sId != -1) {
			try {
				sDao = Session.getDao(context);
				smDao = SessionMember.getDao(context);
				gmDao = GameMember.getDao(context);

				s = sDao.queryForId(sId);
			} catch (SQLException e) {
				Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}

        createSessionLayout(inflater, container);

        return rootView;
	}

	public void createSessionLayout(LayoutInflater inflater, ViewGroup container) {
		rootView = inflater.inflate(R.layout.activity_detail_session, container, false);
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshBaseDetails();
	}

	public void refreshBaseDetails() {
		TextView sName = (TextView) rootView.findViewById(R.id.sDet_name);
		TextView sId = (TextView) rootView.findViewById(R.id.sDet_id);
		TextView sessionRuleSet = (TextView) rootView.findViewById(R.id.sDet_ruleSet);

		sName.setText(s.getSessionName());
		sId.setText(String.valueOf(s.getId()));
		sessionRuleSet.setText(s.getRuleSet().getName());
        mi_isFavorite.setChecked(s.getIsFavorite());
	}

	public void refreshDetails() {

	}

	public class ActionBarCallBack implements ActionMode.Callback {
		// Called when the action mode is created; startActionMode() was called
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// Inflate a menu resource providing context menu items
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.context_menu, menu);

			mode.setTitle(mInfo.title);
			mode.setSubtitle(mInfo.subtitle);

			MenuItem mItm = menu.findItem(R.id.action_match);
			if (mInfo.getCreatable()) {
				mItm.setTitle("Create");
			} else if (mInfo.getViewable()) {
				mItm.setTitle("Load");
			} else {
				mItm.setVisible(false);
			}
			return true;
		}

		// Called each time the action mode is shown. Always called after
		// onCreateActionMode, but
		// may be called multiple times if the mode is invalidated.
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false; // Return false if nothing is done
		}

		// Called when the user selects a contextual menu item
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.action_match:
				if (mInfo.getCreatable()) {
					createMatch();
				} else if (mInfo.getViewable()) {
					loadMatch(mInfo.getGameId());
				}
				mode.finish();
				return true;
			default:
				return false;
			}
		}

		// Called when the user exits the action mode
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mActionMode = null;
		}
	}

	private void createMatch() {
		Intent intent = new Intent(context, Quick_Game.class);
		Dao<Game, Long> gDao = Game.getDao(context);
		Dao<GameMember, Long> gmDao = GameMember.getDao(context);
		Dao<Team, Long> tDao = Team.getDao(context);

		Game g = new Game(s, mInfo.getIdInSession(), s.getCurrentVenue(), false);
		GameMember t1 = new GameMember(g, mInfo.getTeam1());
		GameMember t2 = new GameMember(g, mInfo.getTeam2());

		try {
			gDao.setObjectCache(true);
			gDao.create(g);
			gmDao.create(t1);
			gmDao.create(t2);
		} catch (SQLException e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}

		intent.putExtra("gId", g.getId());
		startActivity(intent);

		// load a game that is in progress
		// Intent intent = new Intent(v.getContext(), GameInProgress.class);
		// intent.putExtra("GID", mInfo.gameId);
		// startActivity(intent);
		// finish();

		// load a game that is finished
		// Intent intent = new Intent(v.getContext(), Detail_Game.class);
		// intent.putExtra("GID", mInfo.gameId);
		// startActivity(intent);
	}

	private void loadMatch(long game_id) {
		Intent intent = new Intent(context, Quick_Game.class);
		intent.putExtra("gId", game_id);
		startActivity(intent);
	}
}
