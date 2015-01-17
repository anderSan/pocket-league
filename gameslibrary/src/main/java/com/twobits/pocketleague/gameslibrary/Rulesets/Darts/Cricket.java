package com.twobits.pocketleague.gameslibrary.Rulesets.Darts;

import com.twobits.pocketleague.gameslibrary.GameType;
import com.twobits.pocketleague.gameslibrary.RuleSet;
import com.twobits.pocketleague.gameslibrary.ScoreType;

public class Cricket implements RuleSet {
	public GameType getGameType() {
		return GameType.BILLIARDS;
	}

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

	public boolean detailedRuleSet() {
		return false;
	}
}