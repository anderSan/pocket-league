package com.twobits.polishhorseshoes.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.twobits.polishhorseshoes.enums.ThrowResult;
import com.twobits.polishhorseshoes.enums.ThrowType;

@DatabaseTable
public class Game {
	public static final String FIRST_TEAM = "firstTeam_id";
	public static final String SECOND_TEAM = "secondTeam_id";
	public static final String DATE_PLAYED = "date_played";
	public static final String IS_COMPLETE = "isComplete";

	@DatabaseField(generatedId = true)
	private long id;

    @DatabaseField
    private long pocketleague_id;

    @DatabaseField(canBeNull = false, foreign = true)
	private long team_1_id;

	@DatabaseField(canBeNull = false, foreign = true)
	private long team_2_id;

	@DatabaseField(canBeNull = false)
	public int ruleset_id;

	@DatabaseField(canBeNull = false)
	public boolean firstPlayerOnTop;

	@DatabaseField(canBeNull = false)
	private Date date_played;

	@DatabaseField
	private int team_1_score;

	@DatabaseField
	private int team_2_score;

	@DatabaseField
	private boolean isComplete = false;

	public Game() {
		super();
	}

	public Game(long team_1_id, long team_2_id, int ruleSet, boolean isTracked, Date date_played) {
		super();
		this.team_1_id = team_1_id;
		this.team_2_id = team_2_id;
		this.ruleset_id = ruleSet;
		this.date_played = date_played;

	}

	public Game(long team_1_id, long team_2_id, int ruleSet, boolean isTracked) {
		super();
		this.team_1_id = team_1_id;
		this.team_2_id = team_2_id;
		this.ruleset_id = ruleSet;
		this.date_played = new Date();
	}

	public static Dao<Game, Long> getDao(Context context) {
		DatabaseHelper helper = new DatabaseHelper(context);
		Dao<Game, Long> d = null;
		try {
			d = helper.getGameDao();
		} catch (SQLException e) {
			throw new RuntimeException("Couldn't get game dao: ", e);
		}
		return d;
	}

	public static List<Game> getAll(Context context) throws SQLException {
		Dao<Game, Long> d = Game.getDao(context);
		List<Game> games = new ArrayList<Game>();
		for (Game g : d) {
			games.add(g);
		}
		return games;
	}

	public boolean isValidThrow(Throw t) {
		boolean isValid = true;
		int idx = t.throwIdx;
		switch (idx % 2) {
		// TODO: do players need to be refreshed now that foreign variable is
		// used?
		// first player is on offense
		case 0:
			isValid = isValid && (t.getOffensiveTeamId() == team_1_id);
			break;
		// second player is on defense
		case 1:
			isValid = isValid && (t.getDefensiveTeamId() == team_2_id);
			break;
		default:
			throw new RuntimeException("invalid index " + idx);
		}
		return isValid;
	}

	public ArrayList<Throw> getThrowList(Context context) throws SQLException {
		int tidx, maxThrowIndex;
		ArrayList<Throw> throwArray = new ArrayList<Throw>();

		HashMap<Integer, Throw> throwMap = new HashMap<Integer, Throw>();
		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("game_id", getId());

		Dao<Throw, Long> d = Throw.getDao(context);
		List<Throw> dbThrows = d.queryForFieldValuesArgs(m);

		maxThrowIndex = 0;
		if (!dbThrows.isEmpty()) {
			Collections.sort(dbThrows);

			for (Throw t : dbThrows) {
				tidx = t.throwIdx;

				// purge any throws with negative index
				if (tidx < 0) {
					d.delete(t);
				}

				// populate the map
				throwMap.put(tidx, t);

				// keep track of the maximum index
				if (tidx > maxThrowIndex) {
					maxThrowIndex = tidx;
				}
			}

			// ensure throws in correct order and complete
			Throw t = null;
			for (int i = 0; i <= maxThrowIndex; i++) {
				t = throwMap.get(i);
				// infill with a caught strike if necessary
				if (t == null) {
					t = makeNewThrow(i);
					t.throwType = ThrowType.STRIKE;
					t.throwResult = ThrowResult.CATCH;
				}
				throwArray.add(t);
			}
		}

		return throwArray;
	}

	public Throw makeNewThrow(int throwNumber) {
		long offensiveTeam_id, defensiveTeam_id;
		if (throwNumber % 2 == 0) {
			offensiveTeam_id = getTeam_1_id();
			defensiveTeam_id = getTeam_2_id();
		} else {
			offensiveTeam_id = getTeam_2_id();
			defensiveTeam_id = getTeam_1_id();
		}
		Date timestamp = new Date(System.currentTimeMillis());
		Throw t = new Throw(throwNumber, this, offensiveTeam_id,
				defensiveTeam_id, timestamp);

		return t;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTeam_1_id() {
		return team_1_id;
	}

	public void setTeam_1_id(long team_1_id) {
		this.team_1_id = team_1_id;
	}

	public long getTeam_2_id() {
		return team_2_id;
	}

	public void setTeam_2_id(long team_2_id) {
		this.team_2_id = team_2_id;
	}

	public Date getDate_played() {
		return date_played;
	}

	public void setDate_played(Date date_played) {
		this.date_played = date_played;
	}

	public int getTeam_1_score() {
		return team_1_score;
	}

	public void setTeam_1_score(int team_1_score) {
		this.team_1_score = team_1_score;
		checkGameComplete();
	}

	public int getTeam_2_score() {
		return team_2_score;
	}

	public void setTeam_2_score(int team_2_score) {
		this.team_2_score = team_2_score;
		checkGameComplete();
	}

	public boolean getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	public void checkGameComplete() {
		Integer s1 = getTeam_1_score();
		Integer s2 = getTeam_2_score();
		if (Math.abs(s1 - s2) >= 2 && (s1 >= 11 || s2 >= 11)) {
			setIsComplete(true);
		} else {
			setIsComplete(false);
		}
	}

	public long getWinner() {
		// TODO: should raise an error if game is not complete
		long winner = team_1_id;
		if (getTeam_2_score() > getTeam_1_score()) {
			winner = team_2_id;
		}
		return winner;
	}

	public long getLoser() {
		long loser = team_1_id;
		if (getTeam_2_score() < getTeam_1_score()) {
			loser = team_2_id;
		}
		return loser;
	}
}
