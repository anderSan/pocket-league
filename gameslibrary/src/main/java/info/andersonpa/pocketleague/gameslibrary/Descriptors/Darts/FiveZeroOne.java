package info.andersonpa.pocketleague.gameslibrary.Descriptors.Darts;

import info.andersonpa.pocketleague.gameslibrary.GameDescriptor;
import info.andersonpa.pocketleague.gameslibrary.GameType;
import info.andersonpa.pocketleague.gameslibrary.ScoreType;

public class FiveZeroOne implements GameDescriptor {
    public GameType getGameType() {
        return GameType.DARTS;
    }

	public String getName() {
		return "501";
	}

	public String getDescription() {
		return "501 rules for darts.";
	}

	public Integer min_number_teams() {
		return 2;
	}

	public Integer max_number_teams() {
		return 2;
	}

	public Integer min_team_size() {
		return 1;
	}

	public Integer max_team_size() {
		return 2;
	}

	public ScoreType getScoreType() {
		return ScoreType.POINTS_INVERSE;
	}

    public String actionString() {
        return "info.andersonpa.darts.fivezeroone.PLAY_GAME";
    }
}