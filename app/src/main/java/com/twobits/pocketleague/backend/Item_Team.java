package com.twobits.pocketleague.backend;

public class Item_Team {
    public String tId;
    public String name;
    private boolean is_favorite;

    public Item_Team(String tId, String name, boolean is_favorite) {
        this.tId = tId;
        this.name = name;
        this.is_favorite = is_favorite;
    }

    public String getId() {
        return tId;
    }

    public String getName() {
        return name;
    }

    public boolean getIsFavorite() {
        return is_favorite;
    }
}
