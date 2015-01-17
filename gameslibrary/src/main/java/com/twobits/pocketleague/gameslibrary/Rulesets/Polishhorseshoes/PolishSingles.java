package com.twobits.pocketleague.gameslibrary.Rulesets.Polishhorseshoes;

import com.twobits.pocketleague.gameslibrary.GameType;
import com.twobits.pocketleague.gameslibrary.RuleSet;
import com.twobits.pocketleague.gameslibrary.ScoreType;

public class PolishSingles implements RuleSet {
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

	public boolean detailedRuleSet() {
		return false;
	}
}