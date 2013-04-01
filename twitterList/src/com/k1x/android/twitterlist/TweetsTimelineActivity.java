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

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TweetsTimelineActivity extends BaseActivity implements PopupMenu.OnMenuItemClickListener {
	
	private LinkedList<TweetData> tweetData;
	private TweetListAdapter listAdapter;
	private ListView listView;
	private String text;
	private TwitterListApplication app;
	private String maxId;
	private String searchSinceId;

	private String userLogin;
	private boolean isLoading = false;
	private int activityMode = 0;

	private CharSequence[] items = { Constants.SEARCH_MODE_TEXT_TWEETS,
			Constants.SEARCH_MODE_USERS_TWEETS };
	private int itemId = 0;
	private String mode = Constants.SEARCH_MODE_TEXT_TWEETS;
	private boolean searchMode = false;
	private UserInfo userInfo;
	private TextView errorMessageView;
	private SearchView searchView;
	private boolean menuEnabled;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_base_tweetlist);
	}
	
	@Override
	protected void onCreate() {
        app = (TwitterListApplication) getApplication();
		userLogin = (String) getIntent().getStringExtra(Constants.KEY_USER_LOGIN);
		activityMode = getIntent().getIntExtra(Constants.TWEETLIST_MODE, 0);
		setUpViews();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.options, menu);
	    
	    searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
	    
	    searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				if (menuEnabled) {
					searchTweets(query);
					return true;
				} else {
					return true;
				}
			}

			@Override
			public boolean onQueryTextChange(String arg0) {
				return false;
			}
		});

	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (menuEnabled) {
			switch (item.getItemId()) {
			case R.id.menu_search_tweet_text:
				itemId = 0;
				mode = (String) items[itemId];
				item.setChecked(true);
				return true;
			case R.id.menu_search_username:
				itemId = 1;
				mode = (String) items[itemId];
				item.setChecked(true);
				return true;
			case R.id.menu_reload_form:
				reloadActivity();
				return true;
			case R.id.menu_share:
				View menuItemView = findViewById(R.id.menu_share); // SAME ID AS
																	// MENU ID
				PopupMenu popupMenu = new PopupMenu(this, menuItemView);
				popupMenu.inflate(R.menu.options_menu);
				popupMenu.getMenu().getItem(itemId).setChecked(true);
				popupMenu.setOnMenuItemClickListener(this);
				popupMenu.show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
		} else {
			return false;
		}
	}
	
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_search_tweet_text:
				itemId = 0;
				mode = (String) items[itemId];
	            if (item.isChecked()) item.setChecked(false);
	            else item.setChecked(true);
	            return true;
			case R.id.menu_search_username:
				itemId = 1;
				mode = (String) items[itemId];
	            if (item.isChecked()) item.setChecked(false);
	            else item.setChecked(true);
	            return true;
			case R.id.menu_reload_form:
				reloadActivity();
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
	}
	
	@Override
	protected void onGettingUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
		menuEnabled = true;
		listAdapter.clear();
		text = getUserLogin();
		loadTweetsTask(text);
	}
	
	@Override
    protected void onLogout() {
		menuEnabled = false;
	}


	private String getUserLogin() {
		String userName;
		if(userLogin!=null) {
			userName = userLogin;
		} else if (userInfo.getScreen_name()!=null) { 
			userName = userInfo.getScreen_name();
		} else {
			userName = "and1_john";
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
				Intent I = new Intent(TweetsTimelineActivity.this, FullTweetActivity.class);
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
				boolean allowed =   (maxId != null) && (itemId==1 || !searchMode)  || searchMode && (searchSinceId != null);
							
				if (isAtTheEnd && !isLoading && allowed) {
					System.out.println("Loading tweets...");
					loadTweetsTask(text);
				}
			}
		});
	}
	
	private void reloadActivity() {
		searchMode = false;
		clearIDs();
		listAdapter.clear();
		text = getUserLogin();
		loadTweetsTask(text);
	}
	
	private void searchTweets(String text) {
		searchMode = true;
		clearIDs();
		listAdapter.clear();
		tweetData.clear();
		this.text = text;
		loadTweetsTask(text);
	}
	
	private void clearIDs() {
		maxId = null;
		searchSinceId = null;
	}
	
	private void loadTweetsTask(String tweetParam) {
		new LoadTweetsTask().execute(tweetParam);
	}
	
	private class LoadTweetsTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			loadTweets(params[0]);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			listView.postInvalidate();
		}

	}
	
	private void loadTweets(String tweetParam)
	{
		try {
			isLoading = true;
			if (mode.equals(Constants.SEARCH_MODE_TEXT_TWEETS) && searchMode) {
				SearchData searchData = getTweeter().searchTweets(tweetParam, searchSinceId);
				addSearchDataToAdapter(searchData);
			} else if (mode.equals(Constants.SEARCH_MODE_USERS_TWEETS) && searchMode) {
				tweetData = getTweeter().getUserTimeline(tweetParam, maxId);
				addDataToAdapter();
			} else if (activityMode == Constants.MODE_HOMETIMELINE) {
				tweetData = getTweeter().getHomeTimeline(maxId);
				addDataToAdapter();
			} else if (activityMode == Constants.MODE_USERTIMELINE) {
				tweetData = getTweeter().getUserTimeline(getUserLogin(), maxId);
				addDataToAdapter();
			} else if (activityMode == Constants.MODE_FAVOURITES) {
				tweetData = getTweeter().getUserFavourites(getUserLogin(), maxId);
				addDataToAdapter();
			} else if (activityMode == Constants.MODE_MENTIONS) {
				tweetData = getTweeter().getUserMentions(getUserLogin(), maxId);
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
				Toast.makeText(getApplicationContext(), "Something went wrong...", Toast.LENGTH_SHORT).show();			
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
	
	/*	
	private void alertDialogChooseSearchMode() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Choose search mode:");
		
		alert.setSingleChoiceItems(items, itemId,	new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						UserHomeTimelineActivity.this.itemId = item;
					}
				});
		
		alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				mode = (String) items[itemId];
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
*/

}