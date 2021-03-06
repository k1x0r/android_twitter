package com.k1x.android.twitterlist;

import java.net.UnknownHostException;
import java.util.LinkedList;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;

import com.google.gson.JsonSyntaxException;
import com.k1x.android.twitterlist.R;
import com.k1x.android.twitterlist.constants.Constants;
import com.k1x.android.twitterlist.entities.SearchData;
import com.k1x.android.twitterlist.entities.TweetData;
import com.k1x.android.twitterlist.entities.UserInfo;
import com.k1x.android.twitterlist.layouts.TweetListItem;
import com.k1x.android.twitterlist.listviews.TweetListAdapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("NewApi")
public class TweetsTimelineActivity extends BaseActivity  
{
	
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

	private int[] items = { R.string.search_by_tweet_text,
			R.string.search_by_username };
	private int itemId = 0;
	private int mode = R.string.search_by_tweet_text;
	private boolean searchMode = false;
	private UserInfo userInfo;
	private SearchView searchView;
	private boolean menuEnabled;
	private EditText tweetEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_base_tweetlist);
	}
	
	@Override
	protected void onCreate() {
        app = (TwitterListApplication) getApplication();
		userLogin = (String) getIntent().getStringExtra(Constants.KEY_USER_LOGIN);
		activityMode = getIntent().getIntExtra(Constants.TWEETLIST_MODE, Constants.MODE_HOMETIMELINE);
		setUpViews();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.options, menu);

		searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			private void hideSearchViewKeyboard() {
				InputMethodManager imm = (InputMethodManager) getApplicationContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				if (menuEnabled) {
					hideSearchViewKeyboard();
					searchTweets(query);
					return true;
				} else {
					hideSearchViewKeyboard();
					Toast.makeText(getApplicationContext(),
							R.string.you_must_log_in_first, Toast.LENGTH_SHORT)
							.show();
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
				mode = items[itemId];
				item.setChecked(true);
				return true;
			case R.id.menu_search_username:
				itemId = 1;
				mode = items[itemId];
				item.setChecked(true);
				return true;
			case R.id.menu_reload_form:
				reloadActivity();
				return true;
			case R.id.menu_share:
				View menuItemView = findViewById(R.id.menu_share); // SAME ID AS
				if(getApp().isSupportsHoneyComb()) {
					popupMenuChooseSearchMode(menuItemView);
				} else {
					alertDialogChooseSearchMode();
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
		} else {
			Toast.makeText(getApplicationContext(), R.string.you_must_log_in_first, Toast.LENGTH_SHORT).show();			
			return false;
		}
	}

	private void popupMenuChooseSearchMode(View menuItemView) {
		PopupMenu popupMenu = new PopupMenu(this, menuItemView);
		popupMenu.getMenuInflater().inflate(R.menu.options_menu, popupMenu.getMenu());
		popupMenu.getMenu().getItem(itemId).setChecked(true);
		popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(android.view.MenuItem item) {
				return onPopupItemClick(item);
			}
		});
		popupMenu.show();
	}

	public boolean onPopupItemClick(android.view.MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_search_tweet_text:
				itemId = 0;
				mode = items[itemId];
	            item.setChecked(!item.isChecked());
	            return true;
			case R.id.menu_search_username:
				itemId = 1;
				mode = items[itemId];
	            item.setChecked(!item.isChecked());
	            return true;
			case R.id.menu_reload_form:
				reloadActivity();
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
	}
	private void alertDialogChooseSearchMode() {
		String[] itemsString = new String[items.length];
		for(int i = 0; i<items.length; i++ ) {
			itemsString[i] = getResources().getString(items[i]);
		}
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(R.string.choose_search_mode);
		alert.setSingleChoiceItems(itemsString, itemId,	new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						TweetsTimelineActivity.this.itemId = item;
					}
				});
		alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				mode =  items[itemId];
			}
		});
		alert.setNeutralButton(R.string.no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		alert.setNegativeButton(R.string.refresh, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				reloadActivity();
				dialog.cancel();
			}
		});
		AlertDialog ad = alert.create();
		ad.show(); 
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
		
		tweetEditText = (EditText) findViewById(R.id.tweetText);
		tweetEditText.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				new TweetTask().execute(v.getText().toString());
				hideTweetKeyboard();
				tweetEditText.setText(null);
				return false;
			}

			private void hideTweetKeyboard() {
				InputMethodManager imm = (InputMethodManager) getApplicationContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(tweetEditText.getWindowToken(), 0);
			}
		});
		
		
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
		if (tweetData != null) {
			tweetData.clear();
		}
		text = getUserLogin();
		loadTweetsTask(text);
	}
	
	private void searchTweets(String text) {
		searchMode = true;
		clearIDs();
		listAdapter.clear();
		if (tweetData != null) {
			tweetData.clear();
		}
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
	

	
	private void loadTweets(String tweetParam)
	{
		try {
			isLoading = true;
			if (mode == R.string.search_by_tweet_text && searchMode) {
				SearchData searchData = getTweeter().searchTweets(tweetParam, searchSinceId);
				addSearchDataToAdapter(searchData);
			} else if (mode == R.string.search_by_username && searchMode) {
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
				Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();			
	        }});        
		} catch (UnknownHostException e) {
			showToastMessage(R.string.unknown_host);
		} catch (Exception e) {
			showToastMessage(e.getMessage());
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
	
	private class TweetTask extends AsyncTask<String, Void, Boolean> {
		
		
		@Override
		protected Boolean doInBackground(String... params) {
			try {
				String tweetText = tweetEditText.getText().toString();
				String result;
				result = getTweeter().tweet(tweetText).getText();

				if (result != null) {
					return true;
				} else {
					return false;
				}
			} catch (UnknownHostException e) {
				showToastMessage(R.string.unknown_host);
				return false;
			} catch (Exception e) {
				showToastMessage(e.getMessage());
				return false;
			}
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if(result) {
				searchMode = false;
				activityMode = Constants.MODE_USERTIMELINE;
				reloadActivity();
			}
		}

	}
	



}
