package info.andersonpa.pocketleague.gameslibrary;

public interface GameDescriptor {
    GameType getGameType();

    String getName();

    String getDescription();

    boolean allowed_nTeams(int n_teams);

    boolean allowed_teamSize(int team_size);

    ScoreType getScoreType();

    String actionString();
}
