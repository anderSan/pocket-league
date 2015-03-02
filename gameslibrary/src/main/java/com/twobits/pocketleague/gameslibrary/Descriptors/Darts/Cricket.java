package com.twobits.pocketleague.gameslibrary.Descriptors.Darts;

import com.twobits.pocketleague.gameslibrary.GameDescriptor;
import com.twobits.pocketleague.gameslibrary.GameType;
import com.twobits.pocketleague.gameslibrary.ScoreType;

public class Cricket implements GameDescriptor {
	public String getName() {
		return "Cricket";
	}

	public String getDescription() {
		return "Cricket rules for darts.";
	}

	public boolean allowed_nTeams(int n_teams) {
        return n_teams == 2;
	}

	public boolean allowed_teamSize(int team_size) {
        return team_size == 1 || team_size == 2;
	}

	public ScoreType getScoreType() {
		return ScoreType.POINTS;
	}

    public String actionString() {
        return "com.twobits.darts.cricket.PLAY_GAME";
    }
}