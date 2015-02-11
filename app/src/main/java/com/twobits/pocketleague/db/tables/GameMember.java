package com.twobits.pocketleague.db.tables;

import java.sql.SQLException;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.twobits.pocketleague.db.DatabaseHelper;

@DatabaseTable
public class GameMember {
	public static final String GAME = "game_id";
	public static final String TEAM = "team_id";
	public static final String IS_WINNER = "is_winner";

	@DatabaseField(generatedId = true)
	private long id;

	@DatabaseField(canBeNull = false, uniqueCombo = true, foreign = true)
	private Game game;

	@DatabaseField(canBeNull = false, uniqueCombo = true, foreign = true, foreignAutoRefresh = true)
	private Team team;

	@DatabaseField(canBeNull = false)
	private boolean is_winner;

	public GameMember() {
	}

	public GameMember(Game game, Team team) {
		super();
		this.game = game;
		this.team = team;
	}

	public static Dao<GameMember, Long> getDao(Context context) {
		DatabaseHelper helper = new DatabaseHelper(context);
		Dao<GameMember, Long> d = null;
		try {
			d = helper.getGameMemberDao();
		} catch (SQLException e) {
			throw new RuntimeException("Could not get game member dao: ", e);
		}
		return d;
	}

	public long getId() {
		return id;
	}

	public Game getGame() {
		return game;
	}

	public Team getTeam() {
		return team;
	}

    public boolean getIsWinner() {
        return is_winner;
    }

    public void setIsWinner(boolean is_winner) {
        this.is_winner = is_winner;
    }
}
