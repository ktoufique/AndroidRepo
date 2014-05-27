package com.example.myfirstapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FeedDetailActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_feed_detail);

		
		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.feed_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} else if (id == R.id.action_search){
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
			View rootView = inflater.inflate(R.layout.fragment_feed_detail,
					container, false);
			
			Intent intent = this.getActivity().getIntent();
			String title = intent.getStringExtra(DisplayMessageActivity.EXTRA_TITLE);
			String description = intent.getStringExtra(DisplayMessageActivity.EXTRA_DESCRIPTION);

			TextView textView = (TextView) rootView.findViewById(R.id.titleView);
			
			textView.setText(title);

			TextView textView2 = (TextView) rootView.findViewById(R.id.descriptionView);
			textView2.setText(Html.fromHtml(description)); 
			
			textView2.setMovementMethod(new ScrollingMovementMethod());

			return rootView;
		}
	}

}
