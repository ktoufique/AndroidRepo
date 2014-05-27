package com.feed;

import java.util.List;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import com.mongodb.Mongo;

public class FeedDao extends BasicDAO<Feed,String> {

	public FeedDao(Mongo mongo, Morphia morphia)
	{
		super( mongo, morphia, "example" );
	}

	public List<Feed> findAll()
	{
		return ds.find(Feed.class).asList();
	}
	public Feed findFeedByName(String link){
		return ds.find(Feed.class).field("link").equal(link).asList().get(0);
	}
	public void addNewItem(Feed feed, Item item){
		UpdateOperations<Feed> ops;
		Query<Feed> updateQuery = ds.createQuery(Feed.class).field("_id").equal(feed.getId());
		ops = ds.createUpdateOperations(Feed.class).add("ListItems", item);
		ds.update(updateQuery, ops);
	}
}
