package com.twobits.pocketleague.gameslibrary.Descriptors.Cards;

import com.twobits.pocketleague.gameslibrary.GameDescriptor;
import com.twobits.pocketleague.gameslibrary.GameType;
import com.twobits.pocketleague.gameslibrary.ScoreType;

public class Spades implements GameDescriptor {
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

    public String actionString() {
        return "com.twobits.cards.spades.PLAY_GAME";
    }
}