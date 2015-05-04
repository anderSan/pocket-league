package info.andersonpa.pocketleague.gameslibrary.Descriptors;

import info.andersonpa.pocketleague.gameslibrary.GameDescriptor;
import info.andersonpa.pocketleague.gameslibrary.GameType;
import info.andersonpa.pocketleague.gameslibrary.ScoreType;

public class PolishHorseshoes implements GameDescriptor {
    public GameType getGameType() {
        return GameType.POLISH_HORSESHOES;
    }

	public String getName() {
		return "Polish Horseshoes";
	}

	public String getDescription() {
		return "Polish Horseshoes.";
	}

	public Integer min_number_teams() {
		return 2;
	}

	public Integer max_number_teams() {
		return 2;
	}

	public Integer min_team_size() {
		return 1;
	};

	public Integer max_team_size() {
		return 2;
	};

	public ScoreType getScoreType() {
		return ScoreType.POINTS;
	}

    public String actionString() {
        return "info.andersonpa.polishhorseshoes.PLAY_GAME";
    }
}