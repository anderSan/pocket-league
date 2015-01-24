package com.twobits.pocketleague.backend;

public class Item_Venue {
	public String id;
    public String name;

    public Item_Venue(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getId(){
    	return id;
    }

    public String getName(){
    	return name;
    }
    
    public void setId(String id){
    	this.id = id;
    }

    public void setName(String name){
    	this.name = name;
    }
    
}
