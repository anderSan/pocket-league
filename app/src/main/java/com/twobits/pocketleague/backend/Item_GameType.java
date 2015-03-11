package com.twobits.pocketleague.backend;

import com.twobits.pocketleague.gameslibrary.GameType;

public class Item_GameType {
    private GameType gametype;
    public String name;
    private int drawable_id;

    public Item_GameType(GameType gametype) {
        this.gametype = gametype;
        this.name = gametype.toString();
        this.drawable_id = gametype.toDrawableId();
    }

    public Item_GameType(GameType gametype, String name, int drawable_id) {
        this.gametype = gametype;
        this.name = name;
        this.drawable_id = drawable_id;
    }

    public GameType getGameType() {
        return gametype;
    }

    public void setGameType(GameType gametype) {
        this.gametype = gametype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrawableId() {
        return drawable_id;
    }

    public void setDrawableId(int drawable_id) {
        this.drawable_id = drawable_id;
    }
}
