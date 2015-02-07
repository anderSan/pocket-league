package com.twobits.pocketleague.gameslibrary.Rulesets.Cards;

import com.twobits.pocketleague.gameslibrary.GameType;
import com.twobits.pocketleague.gameslibrary.RuleSet;
import com.twobits.pocketleague.gameslibrary.ScoreType;

public class Spades implements RuleSet {
	public GameType getGameType() {
		return GameType.CARDS;
	}

	public String getName() {
		return "Spades";
	}

	public String getDescription() {
		return "Spades.";
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

	public boolean detailedRuleSet() {
		return false;
	}
}