package com.twobits.pocketleague.backend;

public class Item_Team {
    public long tId;
    public String name;
    private boolean is_favorite;

    public Item_Team(long tId, String name, boolean is_favorite) {
        this.tId = tId;
        this.name = name;
        this.is_favorite = is_favorite;
    }

    public long getId() {
        return tId;
    }

    public String getName() {
        return name;
    }

    public boolean getIsFavorite() {
        return is_favorite;
    }
}
