package com.enseirb.servletcom;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feed.Feed;
import com.feed.FeedDao;
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

@WebServlet("/GetFeed")
public class FeedProvider extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public FeedProvider() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Morphia morphia = new Morphia();
		morphia.map(Feed.class);
		
		Mongo mongo = new Mongo("localhost", 27017);
		
		FeedDao feedDao = new FeedDao(mongo, morphia);
		
		List <Item> list = new ArrayList<Item>();
		
		Item item = new Item("link", "title", "description", "uri", null);
		list.add(item);
		list.add(item);
		list.add(item);
		
		Feed feed = new Feed(list);
		feedDao.save(feed);
		
		response.getOutputStream().println("Hurray !! FeedProvider working !!");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {			
			ObjectMapper mapper = new ObjectMapper();
			String received = mapper.readValue(request.getInputStream(), String.class);

			Mongo mongo = new Mongo("localhost", 27017);
			// Create a Morphia object and map our model classes
			Morphia morphia = new Morphia();
			morphia.map(Item.class);
			morphia.map(User.class);
			
			// Create a DAOs
			UserDao userDao = new UserDao(mongo, morphia);
			
			List <String> userList = new ArrayList<String>();
			userList = userDao.findUserByName(received).getListURLS();

			List <Item> list = new ArrayList<Item>();
			XmlReader.setDefaultEncoding("UTF-8");

			for (String urlString : userList){
				URL url = new URL(urlString);
				SyndFeedInput syndFeedInput = new SyndFeedInput();
				SyndFeed syndFeed = null;
				
				XmlReader xmlReader = new XmlReader(url);				
				try {
					syndFeed = syndFeedInput.build(xmlReader);
					Iterator<?> it = syndFeed.getEntries().iterator();
					while (it.hasNext()){
						SyndEntry entry = (SyndEntry) it.next();
						Item item = new Item();
						item.setAuthor(entry.getAuthor());
						item.setLink(entry.getLink());
						item.setTitle(entry.getTitle());
						item.setPublishedDate(entry.getPublishedDate());
						item.setDescription(entry.getDescription().getValue());
						list.add(item);						
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (FeedException e) {
					e.printStackTrace();
				}
			}
			response.setStatus(HttpServletResponse.SC_OK);
			Feed feed = new Feed(list);
			feed.sortByDate();
			
			mapper.writeValue(response.getOutputStream(), feed);


		} catch (IOException e) {
			try{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().print(e.getMessage());
				response.getWriter().close();
			} catch (IOException ioe) {
			}
		}

	}

}