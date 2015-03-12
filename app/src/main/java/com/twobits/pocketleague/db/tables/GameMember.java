package com.twobits.pocketleague.db.tables;

import android.util.ArrayMap;

import java.util.Comparator;
import java.util.Map;

public class GameMember implements Comparable<GameMember> {
    public static final String TEAM_ID = "team_id";
    public static final String SCORE = "score";
    public static final String PLAY_ORDER = "play_order";

    private Team team;
    private int score = 0;
    int play_order = -1;

    public GameMember(Team team) {
        this.team = team;
    }

    GameMember(Team team, int score, int play_order) {
        this(team);
        setScore(score);
        setPlayOrder(play_order);
    }

    public Team getTeam() {
        return team;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    void setPlayOrder(int play_order) {
        this.play_order = play_order;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> contents = new ArrayMap<>();
        contents.put(TEAM_ID, team.getId());
        contents.put(SCORE, score);
        contents.put(PLAY_ORDER, play_order);
        return contents;
    }

    public String toString() {
        return team.getName();
    }

    public int compareTo(GameMember another) {
        return ((Integer) play_order).compareTo(another.play_order);
    }

    static final Comparator<GameMember> SCORE_ORDER =
        new Comparator<GameMember>() {
            public int compare(GameMember e1, GameMember e2) {
                return ((Integer) e2.score).compareTo(e1.score);
            }
        };
}