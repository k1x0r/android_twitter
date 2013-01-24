package com.k1x.android.twitterlist;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.k1x.android.twiterlist.R;
import com.k1x.android.twitterlist.jsonobj.TweetData;
import com.korovyansk.android.slideout.SlideoutActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class TweetListActivity extends ListActivity {

	private InputStream is;
	private ArrayList<TweetData> tweetData;
	private TweetListAdapter listAdapter;
	private ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setUpViews();
		
		Thread T = new Thread(new Runnable() {
			@Override
			public void run() {
				loadTweets();
				listView.postInvalidate();
			}});
		T.start();
	}
	
	private void setUpViews() {
		setContentView(R.layout.activity_tweetlist);
		Button closeButton = (Button)findViewById(R.id.close_button);
		closeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		Button slideButton = (Button)findViewById(R.id.slideButton);
		slideButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
						SlideoutActivity.prepare(TweetListActivity.this, R.id.tweetlist_layout, width);
						startActivity(new Intent(TweetListActivity.this, MenuActivity.class));
						overridePendingTransition(0, 0);
					}
				});
		
	    listAdapter = new TweetListAdapter(this);  

		listView = (ListView) findViewById(android.R.id.list);
		listView.setAdapter(listAdapter);
	}
	
	
	private void loadTweets()
	{
		try {
		    HttpParams params = new BasicHttpParams();
		    HttpConnectionParams.setSoTimeout(params, 0);
		    HttpClient httpClient = new DefaultHttpClient(params);

		    //prepare the HTTP GET call 
		    HttpGet httpget = new HttpGet("https://api.twitter.com/1/statuses/user_timeline.json?include_entities=true&include_rts=true&screen_name=and1_john&count=10");
		    //get the response entity
		    HttpEntity entity = httpClient.execute(httpget).getEntity();
		    if (entity != null) {
		    	
		    	is = entity.getContent();
		    	InputStreamReader reader = new InputStreamReader(is);
		    	
		        // When HttpClient instance is no longer needed, shut down the connection manager to ensure immediate deallocation of all system resources
		        
		        Gson gson = new GsonBuilder().create();
		        Type collectionType = new TypeToken<ArrayList<TweetData>>(){}.getType();
		        tweetData = gson.fromJson(reader, collectionType);
		        	
		        runOnUiThread(new Runnable() {

					@Override
					public void run() {
		  		        for(TweetData data: tweetData)
				        	listAdapter.add(data);
		  		        
		  		        	listAdapter.notifyDataSetChanged();
					}});

		        System.out.println(tweetData);
		        
		        
		        httpClient.getConnectionManager().shutdown();

		    }
		}catch (Exception e) {
		    e.printStackTrace();
		}
	}
	


}
