package info.andersonpa.pocketleague;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import info.andersonpa.pocketleague.backend.Fragment_Detail;
import info.andersonpa.pocketleague.db.tables.Game;
import info.andersonpa.pocketleague.db.tables.Player;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Detail_Game extends Fragment_Detail {
	String gId;
	Game g;
	Player[] p = new Player[2];

    TextView game_p1;
    TextView game_p2;
    TextView game_id;
    TextView game_session;
    TextView game_venue;
    TextView game_date;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_detail_game, container, false);

        Bundle args = getArguments();
        gId = args.getString("GID");

        game_p1 = (TextView) rootView.findViewById(R.id.gDet_p1);
        game_p2 = (TextView) rootView.findViewById(R.id.gDet_p2);
        game_id = (TextView) rootView.findViewById(R.id.gDet_id);
        game_session = (TextView) rootView.findViewById(R.id.gDet_session);
        game_venue = (TextView) rootView.findViewById(R.id.gDet_venue);
        game_date = (TextView) rootView.findViewById(R.id.gDet_date);

        return rootView;
	}

	public void refreshDetails() {
		if (gId != null) {
            g = Game.getFromId(database(), gId);
            // playerDao.refresh(g.getFirstPlayer());
            // playerDao.refresh(g.getSecondPlayer());

            // p[0] = g.getFirstPlayer();
            // p[1] = g.getSecondPlayer();
		}

		game_p1.setText(p[0].getName());
		game_p2.setText(p[1].getName());
		game_id.setText(String.valueOf(g.getId()));
		game_session.setText(g.getSession().getName());
		game_venue.setText(g.getVenue().getName());

		// TextView gameRuleSet = (TextView) findViewById(R.id.gDet_ruleSet);
		// gameRuleSet.setText("(" + RuleType.map.get(g.ruleSetId).getId() +
		// ") "
		// + RuleType.map.get(g.ruleSetId).getDescription());

		// TextView gameScore = (TextView) findViewById(R.id.gDet_score);
		// gameScore.setText(String.valueOf(g.getFirstPlayerScore()) + "/"
		// + String.valueOf(g.getSecondPlayerScore()));

		DateFormat df = new SimpleDateFormat("EEE MMM dd, yyyy @HH:mm", Locale.US);

		game_date.setText(df.format(g.getDatePlayed()));
	}

	// public void deleteGame(View view) {
	// AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
	// view.getContext());
	// alertDialogBuilder.setTitle("Delete this game?");
	// alertDialogBuilder
	// .setMessage("This action can not be undone.")
	// .setPositiveButton("Delete",
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int id) {
	// try {
	// Dao<Throw, Long> tDao = Throw
	// .getDao(getApplicationContext());
	// DeleteBuilder<Throw, Long> tdb = tDao
	// .deleteBuilder();
	// tdb.where().eq(Throw.GAME_ID, g.getId());
	// tDao.delete(tdb.prepare());
	//
	// gDao.deleteById(g.getId());
	// finish();
	// } catch (SQLException e) {
	// Toast.makeText(getApplicationContext(),
	// e.getMessage(), Toast.LENGTH_LONG)
	// .show();
	// }
	// }
	// })
	// .setNegativeButton("Cancel",
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int id) {
	// dialog.cancel();
	// }
	// });
	//
	// AlertDialog alertDialog = alertDialogBuilder.create();
	// alertDialog.show();
	//
	// }

}
