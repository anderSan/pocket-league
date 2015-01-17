package com.twobits.pocketleague.gameslibrary;

public interface RuleSet {
    public GameType getGameType();

    public String getName();

    public String getDescription();

    public boolean allowed_nTeams(int n_teams);

    public boolean allowed_teamSize(int team_size);

    public ScoreType getScoreType();

    public boolean detailedRuleSet();
}