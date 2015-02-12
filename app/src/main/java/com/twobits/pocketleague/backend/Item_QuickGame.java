package com.twobits.pocketleague.backend;

import com.twobits.pocketleague.db.tables.GameMember;

public class Item_QuickGame {
    private GameMember gm;
    private String name;
    private boolean is_winner;

    public Item_QuickGame(GameMember gm) {
        this.gm = gm;
        this.name = gm.getTeam().getTeamName();
        this.is_winner = gm.getIsWinner();
    }

    public GameMember getGM() {
        return gm;
    }

    public String getName() {
        return name;
    }

    public boolean getIsWinner() {
        return is_winner;
    }

    @Override
    public String toString() {
        if (gm.getIsWinner()) {
            return name + " (W)";
        }
        return name;
    }
}
