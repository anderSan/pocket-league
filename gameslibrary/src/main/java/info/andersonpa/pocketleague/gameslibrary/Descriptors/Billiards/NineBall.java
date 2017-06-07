package info.andersonpa.pocketleague.gameslibrary.Descriptors.Billiards;

import info.andersonpa.pocketleague.gameslibrary.GameDescriptor;
import info.andersonpa.pocketleague.gameslibrary.GameType;
import info.andersonpa.pocketleague.gameslibrary.ScoreType;

public class NineBall implements GameDescriptor {
    public GameType getGameType() {
        return GameType.BILLIARDS;
    }

	public String getName() {
		return "9-ball";
	}

	public String getDescription() {
		return "Standard billiards rules for 9-ball.";
	}

	public Integer min_number_teams() {
		return 2;
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
		return ScoreType.BINARY;
	}

    public String actionString() {
        return "info.andersonpa.billiards.nineball.PLAY_GAME";
    }
}