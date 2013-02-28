package com.k1x.android.twitterlist.listviews;

import java.util.LinkedList;

import com.k1x.android.twitterlist.BaseActivity;
import com.k1x.android.twitterlist.R;
import com.k1x.android.twitterlist.TweetActivity;
import com.k1x.android.twitterlist.UserHomeTimelineActivity;
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

	public MenuListAdapter(BaseActivity activity)
    {
		this.activity = activity;
    	list = new LinkedList<SlideMenuItem>();      	
    	setModel();
    	
    }
    
    private void setModel() {
    	list.add(new SlideMenuItem("New Tweet", null, new Runnable() {

			@Override
			public void run() {
				getActivity().getMenu().toggle();
				Intent I = new Intent(getActivity(), TweetActivity.class);
				getActivity().startActivity(I);
				getActivity().finish();
			}}));
    	
    	list.add(new SlideMenuItem("Profile", null, new Runnable() {

			@Override
			public void run() {
				getActivity().getMenu().toggle();
				Intent I = new Intent(getActivity(), UserProfileActivity.class);
				getActivity().startActivity(I);	
				getActivity().finish();
			}}));
    	
    	list.add(new SlideMenuItem("Folowings", null, new Runnable() {

			@Override
			public void run() {
				getActivity().getMenu().toggle();
				Intent I = new Intent(getActivity(), UsersListActivity.class);
				I.putExtra(Constants.KEY_MODE, TweeterAPI.FOLOWINGS);
				getActivity().startActivity(I);		
				getActivity().finish();
			}}));
    	
    	list.add(new SlideMenuItem("Folowers", null, new Runnable() {

			@Override
			public void run() {
				getActivity().getMenu().toggle();
				Intent I = new Intent(getActivity(), UsersListActivity.class);
				I.putExtra(Constants.KEY_MODE, TweeterAPI.FOLOWERS);
				getActivity().startActivity(I);	
				getActivity().finish();
			}}));
    	
    	list.add(new SlideMenuItem("Tweets", null, new Runnable() {

			@Override
			public void run() {
				getActivity().getMenu().toggle();
				Intent I = new Intent(getActivity(), UserHomeTimelineActivity.class);
				I.putExtra(Constants.KEY_USER_HOME_TIMELINE, false);

				getActivity().startActivity(I);
				getActivity().finish();
			}}));		
    	
    	list.add(new SlideMenuItem("Timeline", null, new Runnable() {

			@Override
			public void run() {
				getActivity().getMenu().toggle();
				Intent I = new Intent(getActivity(), UserHomeTimelineActivity.class);
				I.putExtra(Constants.KEY_USER_HOME_TIMELINE, true);
				getActivity().startActivity(I);
				getActivity().finish();
			}}));	
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

