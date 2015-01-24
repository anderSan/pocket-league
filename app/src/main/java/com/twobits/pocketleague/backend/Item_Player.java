package com.twobits.pocketleague.backend;

public class Item_Player {
	public String id;
	public String name;
	public String nickname;
	public int color;

    public Item_Player(String id, String name, String nickname, int color){
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.color = color;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getColor() {
		return color;
	}

	public void setColor(Integer color) {
		this.color = color;
	}
}
