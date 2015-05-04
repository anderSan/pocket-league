package info.andersonpa.pocketleague.gameslibrary.Descriptors;

import info.andersonpa.pocketleague.gameslibrary.GameType;
import info.andersonpa.pocketleague.gameslibrary.GameDescriptor;
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

	public boolean allowed_nTeams(int n_teams) {
		return false;
	}

	public boolean allowed_teamSize(int team_size) {
		return false;
	}

	public ScoreType getScoreType() {
		return ScoreType.POINTS;
	}

    public String actionString() {
        return "com.twobits.undefined.PLAY_GAME";
    }
}