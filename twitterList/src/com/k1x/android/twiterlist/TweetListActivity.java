package com.k1x.android.twiterlist;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.k1x.android.twiterlist.jsonobj.TweetData;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
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
