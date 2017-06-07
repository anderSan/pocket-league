package info.andersonpa.pocketleague.gameslibrary.Descriptors.Cards;

import info.andersonpa.pocketleague.gameslibrary.GameDescriptor;
import info.andersonpa.pocketleague.gameslibrary.GameType;
import info.andersonpa.pocketleague.gameslibrary.ScoreType;

public class Hearts implements GameDescriptor {
	public GameType getGameType() {
		return GameType.CARDS;
	}

	public String getName() {
		return "Hearts";
	}

	public String getDescription() {
		return "Hearts.";
	}

	public Integer min_number_teams() {
		return 4;
	}

	public Integer max_number_teams() {
		return 4;
	}

	public Integer min_team_size() {
		return 1;
	}

	public Integer max_team_size() {
		return 1;
	}

	public ScoreType getScoreType() {
		return ScoreType.POINTS_INVERSE;
	}

    public String actionString() {
        return "info.andersonpa.cards.hearts.PLAY_GAME";
    }
}