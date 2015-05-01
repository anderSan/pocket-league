package info.andersonpa.pocketleague.gameslibrary.Descriptors;

import info.andersonpa.pocketleague.gameslibrary.GameDescriptor;
import info.andersonpa.pocketleague.gameslibrary.GameType;
import info.andersonpa.pocketleague.gameslibrary.ScoreType;

public class Golf implements GameDescriptor {
    public GameType getGameType() {
        return GameType.GOLF;
    }

	public String getName() {
		return "Golf";
	}

	public String getDescription() {
		return "Basic Golf.";
	}

	public boolean allowed_nTeams(int n_teams) {
        return n_teams >= 1;
	}

	public boolean allowed_teamSize(int team_size) {
        return team_size == 1 || team_size == 2;
	}

	public ScoreType getScoreType() {
		return ScoreType.POINTS_INVERSE;
	}

    public String actionString() {
        return "com.twobits.golf.PLAY_GAME";
    }
}