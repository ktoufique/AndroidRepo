package com.user;

import java.util.List;

import com.feed.Item;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import com.mongodb.Mongo;

public class UserDao extends BasicDAO<User,String> {

	public UserDao(Mongo mongo, Morphia morphia)
	{
		super( mongo, morphia, "example" );
	}

	public List<User> findAll()
	{
		return ds.find(User.class).asList();
	}

	public User findUserByName(String name){
		return ds.find(User.class).field("name").equal(name).asList().get(0);
	}

	public void addFavoris(User user, Item item){
		UpdateOperations<User> ops;
		Query<User> updateQuery = ds.createQuery(User.class).field("_id").equal(user.getId());
		ops = ds.createUpdateOperations(User.class).add("favoris.ListItems", item);
		ds.update(updateQuery, ops);
	}

	public void deleteFavoris(User user, Item item){
		UpdateOperations<User> ops;
		Query<User> updateQuery = ds.createQuery(User.class).field("_id").equal(user.getId());
		ops = ds.createUpdateOperations(User.class).removeAll("favoris.ListItems", item);
		ds.update(updateQuery, ops);
	}

	public boolean authentification(String name, String password){
		try{
			return this.findUserByName(name).getPassword().equals(password);
		}catch(NullPointerException e){
			return false;
		} catch(IndexOutOfBoundsException e){
			return false;
		}
	}
}
