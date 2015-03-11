package com.twobits.pocketleague.backend;

import com.twobits.pocketleague.enums.SessionType;

public class Item_Session {
    private String sId;
    private String name;
    private SessionType session_type;
    private boolean is_favorite;

    public Item_Session(String sId, String name, SessionType session_type, boolean is_favorite) {
        this.sId = sId;
        this.name = name;
        this.session_type = session_type;
        this.is_favorite = is_favorite;
    }

    public String getId() {
        return sId;
    }

    public String getName() {
        return name;
    }

    public SessionType getSessionType() {
        return session_type;
    }

    public boolean getIsFavorite() {
        return is_favorite;
    }

    public void setIsFavorite(boolean is_favorite) {
        this.is_favorite = is_favorite;
    }
}
