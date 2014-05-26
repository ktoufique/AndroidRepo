package com.feed;

import java.util.Calendar;
import java.util.Date;

import org.bson.types.ObjectId;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity
//@Embedded
public class Item implements Comparable <Item> {

	@Id ObjectId id;
	private String link;
	private String title;
	private String description;
	private String uri;
	private Calendar publishedDate;
	private String Author;




	public Item() {
		this.link=null;
		this.title=null;
		this.description=null;
		this.uri=null;
		this.publishedDate=null;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
	

	public String getAuthor() {
		return Author;
	}

	public void setAuthor(String author) {
		Author = author;
	}

	public Item(String link, String title, String description, String uri, Date publishedDate) {
		this.link = link;
		this.title = title;
		this.description = description;
		this.uri = uri;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title=title;
	}


	public String getDescription() {
		return this.description;
	}
	public void setDescription(String description){
		System.out.println("Setting Description");
		Document doc = Jsoup.parse(description);
		doc.getElementsByTag("img").remove();
		this.description = doc.html();;
	}


	public String getUri() {
		return this.uri;
	}

	public Calendar getPublishedDate() {
		return this.publishedDate;
	}

	public void setPublishedDate(Date publishedDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(publishedDate);
		this.publishedDate = cal;
	}

	@Override
	public int compareTo(Item item) {
		return this.publishedDate.compareTo(item.getPublishedDate());		
	}

	
}

