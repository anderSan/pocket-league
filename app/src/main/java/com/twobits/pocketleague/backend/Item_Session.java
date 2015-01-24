package com.twobits.pocketleague.backend;

import com.twobits.pocketleague.enums.SessionType;

public class Item_Session {
	public String id;
	public String name;
    public SessionType session_type;

    public Item_Session(String id, String name, SessionType session_type){
        this.id = id;
        this.name = name;
        this.session_type = session_type;
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

    public SessionType getSessionType() {
        return session_type;
    }

    public void setSessionType(SessionType session_type) {
        this.session_type = session_type;
    }
}
