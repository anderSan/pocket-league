package info.andersonpa.pocketleague.gameslibrary.Descriptors;

import info.andersonpa.pocketleague.gameslibrary.GameDescriptor;
import info.andersonpa.pocketleague.gameslibrary.GameType;
import info.andersonpa.pocketleague.gameslibrary.ScoreType;

public class Badminton implements GameDescriptor {
    public GameType getGameType() {
        return GameType.BADMINTON;
    }

	public String getName() {
		return "Badminton";
	}

	public String getDescription() {
		return "Normal Badminton.";
	}

	public Integer min_number_teams() {
		return 1;
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
		return ScoreType.POINTS;
	}

    public String actionString() {
        return "info.andersonpa.badminton.PLAY_GAME";
    }
}