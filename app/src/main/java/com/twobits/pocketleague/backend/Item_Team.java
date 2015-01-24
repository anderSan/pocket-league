package com.twobits.pocketleague.backend;

public class Item_Team {
	public String id;
	public String name;

    public Item_Team(String id, String name) {
        this.id = id;
        this.name = name;
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

	public void setName(String teamName) {
		this.name = teamName;
	}

}
