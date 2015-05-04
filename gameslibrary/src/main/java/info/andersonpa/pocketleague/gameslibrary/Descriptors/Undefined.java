package info.andersonpa.pocketleague.gameslibrary.Descriptors;

import info.andersonpa.pocketleague.gameslibrary.GameDescriptor;
import info.andersonpa.pocketleague.gameslibrary.GameType;
import info.andersonpa.pocketleague.gameslibrary.ScoreType;

public class Undefined implements GameDescriptor {
    public GameType getGameType() {
        return GameType.UNDEFINED;
    }

	public String getName() {
		return "Undefined Game";
	}

	public String getDescription() {
		return "N/A.";
	}

	public Integer min_number_teams() {
		return 1;
	}

	public Integer max_number_teams() {
		return null;
	}

	public Integer min_team_size() {
		return 1;
	};

	public Integer max_team_size() {
		return null;
	};

	public ScoreType getScoreType() {
		return ScoreType.POINTS;
	}

    public String actionString() {
        return "com.twobits.undefined.PLAY_GAME";
    }
}