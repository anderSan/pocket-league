package info.andersonpa.pocketleague.gameslibrary.Descriptors.Darts;

import info.andersonpa.pocketleague.gameslibrary.GameDescriptor;
import info.andersonpa.pocketleague.gameslibrary.GameType;
import info.andersonpa.pocketleague.gameslibrary.ScoreType;

public class Cricket implements GameDescriptor {
    public GameType getGameType() {
        return GameType.DARTS;
    }

	public String getName() {
		return "Cricket";
	}

	public String getDescription() {
		return "Cricket rules for darts.";
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
        return "com.twobits.darts.cricket.PLAY_GAME";
    }
}