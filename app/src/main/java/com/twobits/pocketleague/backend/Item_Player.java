package com.twobits.pocketleague.backend;

public class Item_Player {
	public long pId;
	public String name;
	public String nickname;
	public int color;
    private boolean is_favorite;

    public Item_Player(long pId, String name, String nickname, int color, boolean is_favorite){
        this.pId = pId;
        this.name = name;
        this.nickname = nickname;
        this.color = color;
        this.is_favorite = is_favorite;
    }

	public long getId() {
		return pId;
	}

	public String getName() {
		return name;
	}

	public String getNickname() {
		return nickname;
	}

	public Integer getColor() {
		return color;
	}

    public boolean getIsFavorite() {
        return is_favorite;
    }

    public void setIsFavorite(boolean is_favorite) {
        this.is_favorite = is_favorite;
    }
}
