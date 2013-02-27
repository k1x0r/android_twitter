package com.k1x.android.twitterlist;

import java.util.ArrayList;



import com.google.gson.JsonSyntaxException;
import com.k1x.android.twitterlist.R;
import com.k1x.android.twitterlist.constants.Constants;
import com.k1x.android.twitterlist.entities.TweetData;
import com.k1x.android.twitterlist.entities.UserInfo;
import com.k1x.android.twitterlist.layouts.TweetListItem;
import com.k1x.android.twitterlist.listviews.TweetListAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class UserHomeTimelineActivity extends BaseActivity  {
	

	public static final String TWEET_BITMAP = "tweetBitmap";
	public static final String TWEET_DATA = "tweetData";
	
	private ArrayList<TweetData> tweetData;
	private TweetListAdapter listAdapter;
	private ListView listView;
	private EditText searchTweetsEditText;
	private Button searchTweetsButton;
	private String targetUser;
	private TwitterListApplication app;
	private String userLogin;
	private boolean isLoading = false;
	private boolean isHomeTimeline;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_base_tweetlist);
        app = (TwitterListApplication) getApplication();
		userLogin = (String) getIntent().getStringExtra(Constants.KEY_USER_LOGIN);
		isHomeTimeline = getIntent().getBooleanExtra(Constants.KEY_USER_HOME_TIMELINE, false);
		setUpViews();
	}
	
	@Override
	protected void onGettingUserInfo(UserInfo userInfo, Bitmap bitmap) {
		super.onGettingUserInfo(userInfo, bitmap);
		if(userLogin!=null) {
			targetUser = userLogin;
		} else { 
			targetUser = userInfo.getScreen_name();
		}
		loadTweetsTask(targetUser);
	}

	private void setUpViews() {
		
	    listAdapter = new TweetListAdapter(this);  
		listView = (ListView) findViewById(android.R.id.list);
		listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TweetListItem item = (TweetListItem) arg1;
				Intent I = new Intent(UserHomeTimelineActivity.this, TweetInfoActivity.class);
				I.putExtra(TWEET_DATA, item.getTweetData());
				I.putExtra(TWEET_BITMAP, item.getTweetData().getUser().getUserBitmap());
				startActivity(I);
			}
		});
        
        listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int loadedItems = firstVisibleItem + visibleItemCount;
				if((loadedItems == totalItemCount ) && !isLoading) {
					loadTweetsTask(targetUser);
 				}
			}
		});
		
		
		searchTweetsEditText = (EditText) findViewById(R.id.tl_searchText);
		searchTweetsButton = (Button) findViewById(R.id.tl_searchButton);
		searchTweetsButton.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				listAdapter.clear();
				targetUser = searchTweetsEditText.getText().toString();
				loadTweetsTask(targetUser);
			}


		});	
	}
	
	private void loadTweetsTask(final String name) {
		Thread T = new Thread(new Runnable() {
			@Override
			public void run() {
				loadTweets(name);
				listView.postInvalidate();
			}});
		T.start();		
	}
	
	private void addDataToAdapter() {
        runOnUiThread(new Runnable() {
			@Override
			public void run() {
  		        for(TweetData data: tweetData)
		        	listAdapter.add(data);
  		        	listAdapter.notifyDataSetChanged();
			}});
	}
	
	private void loadTweets(String username)
	{
		try {
			isLoading = true;
			if(isHomeTimeline) {
				tweetData = getTweeter().getHomeTimeline(listAdapter.getMaxId());
			} else {
				tweetData = getTweeter().getUserTimeline(username, listAdapter.getMaxId());
			}
			addDataToAdapter();
			isLoading = false;
		}
		catch (JsonSyntaxException e)
		{
			System.out.println(e.getMessage());
	        runOnUiThread(new Runnable() {       
			@Override
			public void run() {
				Toast toast = Toast.makeText(getApplicationContext(), 
						   "Page does not exist!", Toast.LENGTH_SHORT);			
				toast.show();  
	        }});        
		}
		catch (Exception e) {
		    e.printStackTrace();
		}
	}
	


}
