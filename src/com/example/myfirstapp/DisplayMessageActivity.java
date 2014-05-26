package com.example.myfirstapp;


import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feed.Feed;
import com.feed.Item;



public class DisplayMessageActivity extends ListActivity {	

	private GetFeedTask mAuthTask = null;
	private ProgressBar progressBar;
	Feed feedList = new Feed();
	ArrayList <String> list = new ArrayList<String>();
	public final static String EXTRA_TITLE = "com.example.myfirstapp.TITLE";
	public final static String EXTRA_DESCRIPTION = "com.example.myfirstapp.DESCRIPTION";

	@Override
	public void finish(){
		String[] from = { "title", "description" };
		int[] to = { android.R.id.text1, android.R.id.text2 };

		final Intent intentFromList = new Intent(this, FeedDetailActivity.class);

		ArrayList<Map<String, String>> list = buildData(feedList);
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				android.R.layout.simple_list_item_2, from, to);

		setListAdapter(adapter);
		ListView list2 = getListView();
		list2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				intentFromList.putExtra(EXTRA_TITLE, feedList.getListItems().get(position).getTitle());
				intentFromList.putExtra(EXTRA_DESCRIPTION, feedList.getListItems().get(position).getDescription());
				startActivity(intentFromList);
			}
		});
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Object o = this.getListAdapter().getItem(position);
		String keyword = o.toString();
		Toast.makeText(this, "You selected: " + keyword, Toast.LENGTH_SHORT)
		.show();
	}

	/** Called when the user clicks the Send button */
	public void goToItem(View view) {
		Intent intent = new Intent(this, FeedDetailActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_display_message);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		showProgress(false);
		getFeed();
	}

	public void getFeed(){
		showProgress(true);
		mAuthTask = new GetFeedTask();
		mAuthTask.execute((Void) null);			
	}

	private ArrayList<Map<String, String>> buildData(Feed feed) {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		List<Item> listItems = new ArrayList<Item>();
		listItems = feed.getListItems();

		for (Item rss : listItems){
			list.add(putData(rss.getTitle(), formatData(rss)));
		}
		return list;
	}

	private String formatData(Item rss){
		String description = Integer.toString(rss.getPublishedDate().get(Calendar.DAY_OF_MONTH)) 
				+ "/" + Integer.toString(rss.getPublishedDate().get(Calendar.MONTH)) 
				+ "/"+ Integer.toString(rss.getPublishedDate().get(Calendar.YEAR));
		if (rss.getAuthor().length() > 0){
			description +=" - By "+ rss.getAuthor();
		}
				
		return description;
	}
	private HashMap<String, String> putData(String title, String description) {
		HashMap<String, String> item = new HashMap<String, String>();
		item.put("title", title);
		item.put("description", description);
		return item;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_display_message,
					container, false);
			return rootView;
		}
	}

	private void showProgress(final boolean show) {
		if (show){
			progressBar.setVisibility(View.VISIBLE);	
		} else {
			progressBar.setVisibility(View.GONE);
		}

	}

	/**
	 * Represents an asynchronous task used to get the feed for
	 * the user.
	 */
	public class GetFeedTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
			String toSend = "foo@example.com";
			try {
				URL url = new URL("http://ktoufique.rmorpheus.enseirb.fr/MyServletProject/GetFeed");
				URLConnection connection = url.openConnection();				

				connection.setDoOutput(true);						

				ObjectMapper mapper = new ObjectMapper();
				mapper.writeValue(connection.getOutputStream(), toSend);
				feedList = mapper.readValue(connection.getInputStream(), Feed.class);

			} catch (Exception e) {
				return false;
			}

			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);
			if (success) {
				finish();
			}
		}


		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}

}

