package com.feed;

import java.util.List;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
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
}
