package info.andersonpa.pocketleague.gameslibrary.Descriptors;

import info.andersonpa.pocketleague.gameslibrary.GameDescriptor;
import info.andersonpa.pocketleague.gameslibrary.GameType;
import info.andersonpa.pocketleague.gameslibrary.ScoreType;

public class DiscGolf implements GameDescriptor {
    public GameType getGameType() {
        return GameType.DISC_GOLF;
    }

	public String getName() {
		return "Disc Golf";
	}

	public String getDescription() {
		return "Disc Golf.";
	}

	public Integer min_number_teams() {
		return 1;
	}

	public Integer max_number_teams() {
		return null;
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
        return "info.andersonpa.discgolf.PLAY_GAME";
    }
}