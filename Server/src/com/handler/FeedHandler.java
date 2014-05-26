package com.handler;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.feed.Feed;
import com.feed.Item;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import com.user.User;
import com.user.UserDao;

public class FeedHandler {

	public static void main( String[] args ) throws IOException{
		User eddy = new User(); 
        eddy.setName( "jaures" ); 
        eddy.setPassword( "secret" ); 
		List <Item> list = new ArrayList<Item>();
		URL url = new URL("http://www.lemonde.fr/rss/une.xml");
//		URL url = new URL("http://readwrite.com/main/feed/articles.xml");
		SyndFeedInput syndFeedInput = new SyndFeedInput();
		SyndFeed syndFeed = null;
		XmlReader xmlReader = new XmlReader(url);
		try {
			syndFeed = syndFeedInput.build(xmlReader);
			Iterator<?> it = syndFeed.getEntries().iterator();

			while (it.hasNext())
			{
				SyndEntry entry = (SyndEntry) it.next();
				Item item = new Item();
				item.setTitle(entry.getTitle());
				item.setLink(entry.getLink());
				list.add(item);
				
			}


		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (FeedException e) {
			e.printStackTrace();
		}
		Feed feed = new Feed(list);
		eddy.setFavoris(feed);
		
		Mongo mongo = new Mongo("localhost", 27017);

		// Create a Morphia object and map our model classes
		Morphia morphia = new Morphia();
		morphia.map( Item.class );
		morphia.map(User.class);
		
		// Create a DAOs
        UserDao userDao = new UserDao(mongo, morphia);
		
        if (userDao.authentification("jaures", "sec")){
        	System.out.println("succeed");
        }else {
			System.out.println("failed");
		};
	}
}
