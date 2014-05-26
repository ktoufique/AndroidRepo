package com.feed;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Embedded;

@Embedded
public class Feed {

	private ObjectId id;
	@Embedded
	List <Item> ListItems;

	public Feed(){
		this.ListItems= null;
	}
	public Feed(List <Item> list){
		this.ListItems=list;
	}

	public List<Item> getListItems() {
		return ListItems;
	}
	public void setListItems(List<Item> listItems) {
		this.ListItems = listItems;
	}

	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public void sortByDate(){
		Collections.sort(this.ListItems, new Comparator<Item>() {
			public int compare(Item o1, Item o2) {
				return o1.getPublishedDate().compareTo(o2.getPublishedDate()) * -1;
			}
		});
	}
}
