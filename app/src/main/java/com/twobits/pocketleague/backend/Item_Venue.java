package com.twobits.pocketleague.backend;

public class Item_Venue {
    public long vId;
    public String name;
    private boolean is_favorite;

    public Item_Venue(long vId, String name, boolean is_favorite) {
        this.vId = vId;
        this.name = name;
        this.is_favorite = is_favorite;
    }

    public long getId() {
        return vId;
    }

    public String getName() {
        return name;
    }

    public boolean getIsFavorite() {
        return is_favorite;
    }
}
