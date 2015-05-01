package info.andersonpa.pocketleague.gameslibrary.Descriptors.Billiards;

import info.andersonpa.pocketleague.gameslibrary.GameType;
import info.andersonpa.pocketleague.gameslibrary.GameDescriptor;
import info.andersonpa.pocketleague.gameslibrary.ScoreType;

public class EightBall implements GameDescriptor {
    public GameType getGameType() {
        return GameType.BILLIARDS;
    }

	public String getName() {
		return "8-ball";
	}

	public String getDescription() {
		return "Standard billiards rules for 8-ball.";
	}

	public boolean allowed_nTeams(int n_teams) {
        return n_teams == 2;
	}

	public boolean allowed_teamSize(int team_size) {
        return team_size == 1 || team_size == 2;
	}

	public ScoreType getScoreType() {
		return ScoreType.BINARY;
	}

	public String actionString() {
		return "com.twobits.billiards.eightball.PLAY_GAME";
	}
}