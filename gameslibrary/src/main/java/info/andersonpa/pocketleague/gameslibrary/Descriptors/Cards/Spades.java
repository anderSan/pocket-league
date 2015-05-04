package info.andersonpa.pocketleague.gameslibrary.Descriptors.Cards;

import info.andersonpa.pocketleague.gameslibrary.GameDescriptor;
import info.andersonpa.pocketleague.gameslibrary.GameType;
import info.andersonpa.pocketleague.gameslibrary.ScoreType;

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

	public Integer min_number_teams() {
		return 2;
	}

	public Integer max_number_teams() {
		return 2;
	}

	public Integer min_team_size() {
		return 2;
	};

	public Integer max_team_size() {
		return 2;
	};

	public ScoreType getScoreType() {
		return ScoreType.POINTS;
	}

    public String actionString() {
        return "com.twobits.cards.spades.PLAY_GAME";
    }
}