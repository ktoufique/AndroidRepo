package com.enseirb.rssread;

public class RssObject {
	String title, description;

	public RssObject() {
		this.title ="Empty title";
		this.description = "Empty description";
	}

	public String getTitle(){
		return title;
	}

	public String getDescription(){
		return description;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public void setDescription(String description){
		this.description = description;
	}
}

