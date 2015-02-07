package com.twobits.pocketleague.gameslibrary.Rulesets;

import com.twobits.pocketleague.gameslibrary.GameType;
import com.twobits.pocketleague.gameslibrary.RuleSet;
import com.twobits.pocketleague.gameslibrary.ScoreType;

public class Golf implements RuleSet {
	public GameType getGameType() {
		return GameType.GOLF;
	}

	public String getName() {
		return "Golf";
	}

	public String getDescription() {
		return "Basic Golf.";
	}

	public boolean allowed_nTeams(int n_teams) {
        return n_teams >= 1;
	}

	public boolean allowed_teamSize(int team_size) {
        return team_size == 1 || team_size == 2;
	}

	public ScoreType getScoreType() {
		return ScoreType.POINTS_INVERSE;
	}

	public boolean detailedRuleSet() {
		return false;
	}
}