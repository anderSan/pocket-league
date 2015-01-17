package com.twobits.pocketleague.gameslibrary.Rulesets.Undefined;

import com.twobits.pocketleague.gameslibrary.GameType;
import com.twobits.pocketleague.gameslibrary.RuleSet;
import com.twobits.pocketleague.gameslibrary.ScoreType;

public class Undefined implements RuleSet {
	public GameType getGameType() {
		return GameType.UNDEFINED;
	}

	public String getName() {
		return "Undefined Game";
	}

	public String getDescription() {
		return "N/A.";
	}

	public boolean allowed_nTeams(int n_teams) {
		return false;
	}

	public boolean allowed_teamSize(int team_size) {
		return false;
	}

	public ScoreType getScoreType() {
		return ScoreType.POINTS;
	}

	public boolean detailedRuleSet() {
		return false;
	}
}