package com.k1x.android.twitterlist.listviews;

import java.util.LinkedList;

import com.k1x.android.twitterlist.AppInfoActivity;
import com.k1x.android.twitterlist.BaseActivity;
import com.k1x.android.twitterlist.R;
import com.k1x.android.twitterlist.TweetActivity;
import com.k1x.android.twitterlist.TweetsTimelineActivity;
import com.k1x.android.twitterlist.UserProfileActivity;
import com.k1x.android.twitterlist.UsersListActivity;
import com.k1x.android.twitterlist.constants.Constants;
import com.k1x.android.twitterlist.entities.SlideMenuItem;
import com.k1x.android.twitterlist.layouts.MenuListItem;
import com.k1x.android.twitterlist.twitterutil.TweeterAPI;


import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MenuListAdapter extends BaseAdapter {
	
    private LinkedList<SlideMenuItem> list;
	private BaseActivity activity;
	private SlideMenuItem loginItem;


	public MenuListAdapter(BaseActivity activity)
    {
		this.activity = activity;
    	list = new LinkedList<SlideMenuItem>();      	
    	setModel();
    	
    }
    	
    private void setModel() {
    	
    	loginItem = new SlideMenuItem(R.string.log_in, R.drawable.login, null);
    	list.add(loginItem);
    	
    	list.add(new SlideMenuItem(R.string.new_tweet, R.drawable.tweet, new Runnable() {

			@Override
			public void run() {
				getActivity().getMenu().toggle();
				Intent I = new Intent(getActivity(), TweetActivity.class);
				getActivity().startActivity(I);
				getActivity().finish();
			}}));
    	
    	list.add(new SlideMenuItem(R.string.profile, R.drawable.profile, new Runnable() {

			@Override
			public void run() {
				getActivity().getMenu().toggle();
				Intent I = new Intent(getActivity(), UserProfileActivity.class);
				getActivity().startActivity(I);	
				getActivity().finish();
			}}));
    	
    	list.add(new SlideMenuItem(R.string.folowings, R.drawable.folowings, new Runnable() {

			@Override
			public void run() {
				getActivity().getMenu().toggle();
				Intent I = new Intent(getActivity(), UsersListActivity.class);
				I.putExtra(Constants.KEY_MODE, TweeterAPI.FOLOWINGS);
				getActivity().startActivity(I);		
				getActivity().finish();
			}}));
    	
    	list.add(new SlideMenuItem(R.string.folowers, R.drawable.folowers, new Runnable() {

			@Override
			public void run() {
				getActivity().getMenu().toggle();
				Intent I = new Intent(getActivity(), UsersListActivity.class);
				I.putExtra(Constants.KEY_MODE, TweeterAPI.FOLOWERS);
				getActivity().startActivity(I);	
				getActivity().finish();
			}}));
    	
    	list.add(new SlideMenuItem(R.string.blockers , R.drawable.blockers, new Runnable() {

			@Override
			public void run() {
				getActivity().getMenu().toggle();
				Intent I = new Intent(getActivity(), UsersListActivity.class);
				I.putExtra(Constants.KEY_MODE, TweeterAPI.BLOCKERS);
				getActivity().startActivity(I);	
				getActivity().finish();
			}}));
    	
    	list.add(new SlideMenuItem(R.string.tweets, R.drawable.tweets, new Runnable() {

			@Override
			public void run() {
				getActivity().getMenu().toggle();
				Intent I = new Intent(getActivity(), TweetsTimelineActivity.class);
				I.putExtra(Constants.TWEETLIST_MODE, Constants.MODE_USERTIMELINE);

				getActivity().startActivity(I);
				getActivity().finish();
			}}));		
    	
    	list.add(new SlideMenuItem(R.string.timeline, R.drawable.timeline, new Runnable() {

			@Override
			public void run() {
				getActivity().getMenu().toggle();
				Intent I = new Intent(getActivity(), TweetsTimelineActivity.class);
				I.putExtra(Constants.TWEETLIST_MODE, Constants.MODE_HOMETIMELINE);
				getActivity().startActivity(I);
				getActivity().finish();
			}}));	
    	
    	list.add(new SlideMenuItem(R.string.favourites, R.drawable.favorites, new Runnable() {

			@Override
			public void run() {
				getActivity().getMenu().toggle();
				Intent I = new Intent(getActivity(), TweetsTimelineActivity.class);
				I.putExtra(Constants.TWEETLIST_MODE, Constants.MODE_FAVOURITES);
				getActivity().startActivity(I);
				getActivity().finish();
			}}));
    	
    	
    	list.add(new SlideMenuItem(R.string.mentions, R.drawable.mention, new Runnable() {

			@Override
			public void run() {
				getActivity().getMenu().toggle();
				Intent I = new Intent(getActivity(), TweetsTimelineActivity.class);
				I.putExtra(Constants.TWEETLIST_MODE, Constants.MODE_MENTIONS);
				getActivity().startActivity(I);
				getActivity().finish();
			}}));
    	
    	list.add(new SlideMenuItem(R.string.about, R.drawable.about, new Runnable() {

			@Override
			public void run() {
				getActivity().getMenu().toggle();
				Intent I = new Intent(getActivity(), AppInfoActivity.class);
				I.putExtra(Constants.TWEETLIST_MODE, Constants.MODE_MENTIONS);
				getActivity().startActivity(I);
				getActivity().finish();
			}}));
	}

	public SlideMenuItem getLoginItem() {
		return loginItem;
	}
    
	public void add(SlideMenuItem data)
    {
    	list.add(data);
    }
    
    public void clear()
    {
    	list.clear(); 	
    }
    
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int num) {
		return (list == null )? null :list.get(num);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MenuListItem menuItem;
		if(null == convertView)
		{
			menuItem = (MenuListItem)View.inflate(activity, R.layout.listview_slidebar_item, null);
		}
		else 
		{
			menuItem = (MenuListItem)convertView;
		}
		menuItem.setMenuItem(list.get(position));
		return menuItem;
	}

    public BaseActivity getActivity() {
		return activity;
	}

	public void setActivity(BaseActivity activity) {
		this.activity = activity;
	}

	
}

