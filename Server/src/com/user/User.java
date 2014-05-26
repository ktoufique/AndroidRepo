package com.user;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.feed.Feed;
import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity
public class User {

	@Id ObjectId id;
	private String name;
	private String password;
	List <String> listURLS = new ArrayList<String>();
	
//	@Reference 
	@Embedded
    private Feed favoris = new Feed(); 

	public User(){
		this.name=null;
		this.password=null;
		this.favoris=null;
	}
	public User(String name, String password,Feed feed){
		this.name=name;
		this.password=password;
		this.favoris=feed;
	}
	public Feed getFavoris() {
		return favoris;
	}
	public void setFavoris(Feed favoris) {
		this.favoris = favoris;
	}
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<String> getListURLS() {
		return listURLS;
	}
	public void setListURLS(List<String> listURLS) {
		this.listURLS = listURLS;
	}
	
}
