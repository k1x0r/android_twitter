package com.k1x.android.twitterlist;

import java.util.LinkedList;

import com.google.gson.JsonSyntaxException;
import com.k1x.android.twitterlist.R;
import com.k1x.android.twitterlist.constants.Constants;
import com.k1x.android.twitterlist.entities.SearchData;
import com.k1x.android.twitterlist.entities.TweetData;
import com.k1x.android.twitterlist.entities.UserInfo;
import com.k1x.android.twitterlist.layouts.TweetListItem;
import com.k1x.android.twitterlist.listviews.TweetListAdapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class UserHomeTimelineActivity extends BaseActivity  {
	
	private LinkedList<TweetData> tweetData;
	private TweetListAdapter listAdapter;
	private ListView listView;
	private EditText searchTweetsEditText;
	private Button searchTweetsButton;
	private String text;
	private TwitterListApplication app;
	private String maxId;
	private String searchSinceId;

	private String userLogin;
	private boolean isLoading = false;
	private boolean isHomeTimeline;

	private CharSequence[] items = { Constants.MODE_TEXT_TWEETS,
			Constants.MODE_USERS_TWEETS };
	private int item = 0;
	private String mode = Constants.MODE_TEXT_TWEETS;
	private boolean searchMode = false;
	private UserInfo userInfo;
	private Button backButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_base_tweetlist);
        app = (TwitterListApplication) getApplication();
		userLogin = (String) getIntent().getStringExtra(Constants.KEY_USER_LOGIN);
		isHomeTimeline = getIntent().getBooleanExtra(Constants.KEY_USER_HOME_TIMELINE, false);
		setUpViews();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.options, menu);
	    final SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
	    searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				searchTweets(query);
				return true;
			}
			
			@Override
			public boolean onQueryTextChange(String arg0) {
				// TODO Auto-generated method stub
				return false;
			}
		});

	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	protected void onGettingUserInfo(UserInfo userInfo, Bitmap bitmap) {
		this.userInfo = userInfo;
		listAdapter.clear();
		text = getUserLogin();
		loadTweetsTask(text);
	}

	private String getUserLogin() {
		String userName;
		if(userLogin!=null) {
			userName = userLogin;
		} else { 
			userName = userInfo.getScreen_name();
		}
		return userName;
	}

	private void setUpViews() {
			
	    listAdapter = new TweetListAdapter(this, app.getTweetList());  
		listView = (ListView) findViewById(android.R.id.list);
		listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TweetListItem item = (TweetListItem) arg1;
				Intent I = new Intent(UserHomeTimelineActivity.this, TweetInfoActivity.class);
				I.putExtra(Constants.TWEET_DATA, item.getTweetData());
				I.putExtra(Constants.TWEET_BITMAP, item.getTweetData().getUser().getUserBitmap());
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
				boolean isAtTheEnd = loadedItems == totalItemCount;
				boolean allowed = !searchMode && (maxId != null) || searchMode && (searchSinceId != null);
							
				if (isAtTheEnd && !isLoading && allowed) {
					loadTweetsTask(text);
				}
			}
		});
		
		searchTweetsEditText = (EditText) findViewById(R.id.tl_searchText);
		searchTweetsEditText.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				alertDialogChooseSearchMode();
				return false;
			}
		});
		searchTweetsButton = (Button) findViewById(R.id.searchButton);
		searchTweetsButton.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				searchTweets(searchTweetsEditText.getText().toString());
			}

		});	
		
		backButton = (Button) findViewById(R.id.backButton);

		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				searchMode = false;
				clearIDs();
				listAdapter.clear();
				text = getUserLogin();
				loadTweetsTask(text);
			}
		});
	}
	
	private void searchTweets(String text) {
		searchMode = true;
		clearIDs();
		listAdapter.clear();
		tweetData.clear();
		loadTweetsTask(text);
	}
	
	private void clearIDs() {
		maxId = null;
		searchSinceId = null;
	}
	
	private void loadTweetsTask(final String tweetParam) {
		Thread T = new Thread(new Runnable() {
			@Override
			public void run() {
				loadTweets(tweetParam);
				listView.postInvalidate();
			}});
		T.start();		
	}
	
	private void loadTweets(String tweetParam)
	{
		try {
			isLoading = true;
			if (mode.equals(Constants.MODE_TEXT_TWEETS) && searchMode) {
				SearchData searchData = getTweeter().searchTweets(tweetParam, searchSinceId);
				addSearchDataToAdapter(searchData);
			} else if (mode.equals(Constants.MODE_USERS_TWEETS) && searchMode) {
				tweetData = getTweeter().getUserTimeline(tweetParam, maxId);
				addDataToAdapter();
			} else if (isHomeTimeline) {
				tweetData = getTweeter().getHomeTimeline(maxId);
				addDataToAdapter();
			} else {
				tweetData = getTweeter().getUserTimeline(getUserLogin(), maxId);
				addDataToAdapter();
			}
			
			isLoading = false;
		}
		catch (JsonSyntaxException e)
		{
			isLoading = false;
			System.out.println(e.getMessage());
	        runOnUiThread(new Runnable() {       
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), "Page does not exist!", Toast.LENGTH_SHORT).show();			
	        }});        
		}
		catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	private void addDataToAdapter() {
        runOnUiThread(new Runnable() {
			@Override
			public void run() {
  		        for(TweetData data: tweetData) {
		        	listAdapter.add(data);
  		        }
  		        listAdapter.notifyDataSetChanged();
				app.setDataList(listAdapter.getList());
			}});
    	long tweetId = Long.valueOf(tweetData.getLast().getId_str()) - 1;
    	maxId =  String.valueOf(tweetId);
	}
	
	private void addSearchDataToAdapter(final SearchData searchData) {
        runOnUiThread(new Runnable() {
			@Override
			public void run() {
  		        for(TweetData data: searchData.getTweets()) {
		        	listAdapter.add(data);
  		        }
  		        listAdapter.notifyDataSetChanged();
				app.setDataList(listAdapter.getList());
			}});
    	searchSinceId = String.valueOf(searchData.getSearchData().getSinceId() + 1);
	}
	
	private void alertDialogChooseSearchMode() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Choose search mode:");
		
		alert.setSingleChoiceItems(items, item,	new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						UserHomeTimelineActivity.this.item = item;
					}
				});
		
		alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				mode = (String) items[item];
			}
		});

		alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		AlertDialog ad = alert.create();
		ad.show();

	}

}
