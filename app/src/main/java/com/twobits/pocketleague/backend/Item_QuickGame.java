package com.twobits.pocketleague.backend;

import com.twobits.pocketleague.db.tables.GameMember;

public class Item_QuickGame {
    private GameMember gm;
    private String name;
    private int score;

    public Item_QuickGame(GameMember gm) {
        this.gm = gm;
        this.name = gm.getTeam().getName();
        this.score = gm.getScore();
    }

    public GameMember getGM() {
        return gm;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        if (gm.getScore() == 1) {
            return name + " (W)";
        }
        return name;
    }
}
