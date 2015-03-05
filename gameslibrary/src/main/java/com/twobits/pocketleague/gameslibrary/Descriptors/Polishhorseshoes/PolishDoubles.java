package com.twobits.pocketleague.gameslibrary.Descriptors.Polishhorseshoes;

import com.twobits.pocketleague.gameslibrary.GameDescriptor;
import com.twobits.pocketleague.gameslibrary.GameType;
import com.twobits.pocketleague.gameslibrary.ScoreType;

public class PolishDoubles implements GameDescriptor {
    public GameType getGameType() {
        return GameType.POLISH_HORSESHOES;
    }

	public String getName() {
		return "Polish Horseshoes Doubles";
	}

	public String getDescription() {
		return "2v2 Polish Horseshoes.";
	}

	public boolean allowed_nTeams(int n_teams) {
        return n_teams == 2;
	}

	public boolean allowed_teamSize(int team_size) {
        return team_size == 2;
	}

	public ScoreType getScoreType() {
		return ScoreType.POINTS;
	}

    public String actionString() {
        return "com.twobits.polishhorseshoes.doubles.PLAY_GAME";
    }
}