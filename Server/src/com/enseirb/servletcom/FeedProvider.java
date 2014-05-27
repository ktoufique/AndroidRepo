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

		String received = "foo@example.com";
		UserDao userDao = new UserDao(mongo, morphia);

		List <String> userList = new ArrayList<String>();
		userList = userDao.findUserByName(received).getListURLS();



		XmlReader.setDefaultEncoding("UTF-8");

		for (String urlString : userList){

			URL url = new URL(urlString);
			SyndFeedInput syndFeedInput = new SyndFeedInput();
			SyndFeed syndFeed = null;
			List <Item> list = new ArrayList<Item>();
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
			Feed feed = new Feed(list);
			feed.setLink(urlString);
			try {
				Feed feedToDelete = feedDao.findFeedByName(urlString);
				feedDao.delete(feedToDelete);
			}catch(NullPointerException e){
			} catch(IndexOutOfBoundsException e){
			}

			feedDao.save(feed);	


		}
		response.setStatus(HttpServletResponse.SC_OK);


		response.getOutputStream().println("Hurray !! FeedProvider working !! ");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			ObjectMapper mapper = new ObjectMapper();
			String received = mapper.readValue(request.getInputStream(), String.class);
			System.out.println("Connection from: "+ received);
			
			Mongo mongo = new Mongo("localhost", 27017);

			Morphia morphia = new Morphia();
			morphia.map(Feed.class);

			FeedDao feedDao = new FeedDao(mongo, morphia);
			List<Item> listFinal = new ArrayList<Item>();
			List<Feed> listFeed = new ArrayList<Feed>();
			listFeed = feedDao.findAll();

			for (int i = 0; i < listFeed.size(); i++){
				listFinal.addAll(listFeed.get(i).getListItems());	
			}
			Feed feed = new Feed(listFinal);					
			feed.sortByDate();
			response.setStatus(HttpServletResponse.SC_OK);
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