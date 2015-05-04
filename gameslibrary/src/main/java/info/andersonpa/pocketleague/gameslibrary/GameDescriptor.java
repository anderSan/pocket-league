package info.andersonpa.pocketleague.gameslibrary;

public interface GameDescriptor {
    GameType getGameType();

    String getName();

    String getDescription();

    Integer min_number_teams();

    Integer max_number_teams();

    Integer min_team_size();

    Integer max_team_size();

    ScoreType getScoreType();

    String actionString();
}
