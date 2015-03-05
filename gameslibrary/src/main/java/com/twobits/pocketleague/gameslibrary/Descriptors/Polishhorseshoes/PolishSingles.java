package com.twobits.pocketleague.gameslibrary.Descriptors.Polishhorseshoes;

import com.twobits.pocketleague.gameslibrary.GameType;
import com.twobits.pocketleague.gameslibrary.GameDescriptor;
import com.twobits.pocketleague.gameslibrary.ScoreType;

public class PolishSingles implements GameDescriptor {
    public GameType getGameType() {
        return GameType.POLISH_HORSESHOES;
    }

	public String getName() {
		return "Polish Horseshoes Singles";
	}

	public String getDescription() {
		return "1v1 Polish Horseshoes.";
	}

	public boolean allowed_nTeams(int n_teams) {
        return n_teams == 2;
	}

	public boolean allowed_teamSize(int team_size) {
        return team_size == 1;
	}

	public ScoreType getScoreType() {
		return ScoreType.POINTS;
	}

    public String actionString() {
        return "com.twobits.polishhorseshoes.singles.PLAY_GAME";
    }
}