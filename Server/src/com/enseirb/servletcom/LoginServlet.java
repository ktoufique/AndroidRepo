package com.enseirb.servletcom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.enseirb.rssread.RssObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feed.Feed;
import com.feed.Item;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.user.User;
import com.user.UserDao;

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LoginServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Mongo mongo = new Mongo("localhost", 27017);

		// Create a Morphia object and map our model classes
		Morphia morphia = new Morphia();
		morphia.map(Item.class);
		morphia.map(User.class);

		// Create a DAOs
		UserDao userDao = new UserDao(mongo, morphia);

		List <String> listURLS = new ArrayList <String>();
		listURLS.add("http://www.lemonde.fr/rss/une.xml");
		listURLS.add("http://readwrite.com/main/feed/articles.xml");

		User dummyUser = new User(); 
		dummyUser.setName("foo@example.com"); 
		dummyUser.setPassword("hello");
		dummyUser.setListURLS(listURLS);

		if (!userDao.authentification(dummyUser.getName(), dummyUser.getPassword())){
			userDao.save(dummyUser);	
		}

		response.getOutputStream().println("Hurray !! DataBase working !!");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {			
			ObjectMapper mapper = new ObjectMapper();

			RssObject receivedString = mapper.readValue(request.getInputStream(), RssObject.class);

			final String mEmail = receivedString.getTitle();
			final String mPassword = receivedString.getDescription();
			Boolean isTrue = false;

			response.setStatus(HttpServletResponse.SC_OK);

			Mongo mongo = new Mongo("localhost", 27017);

			// Create a Morphia object and map our model classes
			Morphia morphia = new Morphia();
			morphia.map(Item.class);
			morphia.map(User.class);
			morphia.map(Feed.class);

			// Create a DAOs
			UserDao userDao = new UserDao(mongo, morphia);

			isTrue = userDao.authentification(mEmail, mPassword);

			mapper.writeValue(response.getOutputStream(), isTrue);


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