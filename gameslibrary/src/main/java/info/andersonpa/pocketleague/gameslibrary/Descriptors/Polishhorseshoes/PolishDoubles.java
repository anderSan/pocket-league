package info.andersonpa.pocketleague.gameslibrary.Descriptors.Polishhorseshoes;

import info.andersonpa.pocketleague.gameslibrary.GameDescriptor;
import info.andersonpa.pocketleague.gameslibrary.GameType;
import info.andersonpa.pocketleague.gameslibrary.ScoreType;

public class PolishDoubles implements GameDescriptor {
    public GameType getGameType() {
        return GameType.POLISH_HORSESHOES;
    }

	public String getName() {
		return "Polish Horseshoes Doubles";
	}

	public String getDescription() {
		return "2v2 Polish Horseshoes.";
	}

	public boolean allowed_nTeams(int n_teams) {
        return n_teams == 2;
	}

	public boolean allowed_teamSize(int team_size) {
        return team_size == 2;
	}

	public ScoreType getScoreType() {
		return ScoreType.POINTS;
	}

    public String actionString() {
        return "info.andersonpa.polishhorseshoes.doubles.PLAY_GAME";
    }
}