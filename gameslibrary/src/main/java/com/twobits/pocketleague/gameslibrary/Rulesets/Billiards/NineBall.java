package com.twobits.pocketleague.gameslibrary.Rulesets.Billiards;

import com.twobits.pocketleague.gameslibrary.GameType;
import com.twobits.pocketleague.gameslibrary.RuleSet;
import com.twobits.pocketleague.gameslibrary.ScoreType;

public class NineBall implements RuleSet {
	public GameType getGameType() {
		return GameType.BILLIARDS;
	}

	public String getName() {
		return "9-ball";
	}

	public String getDescription() {
		return "Standard billiards rules for 9-ball.";
	}

	public boolean allowed_nTeams(int n_teams) {
        return n_teams >= 2;
	}

	public boolean allowed_teamSize(int team_size) {
        return team_size == 1 || team_size == 2;
	}

	public ScoreType getScoreType() {
		return ScoreType.BINARY;
	}

	public boolean detailedRuleSet() {
		return false;
	}
}