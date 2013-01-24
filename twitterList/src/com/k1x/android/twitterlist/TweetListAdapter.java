package com.k1x.android.twitterlist;

import java.util.LinkedList;

import com.k1x.android.twiterlist.R;
import com.k1x.android.twitterlist.jsonobj.TweetData;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TweetListAdapter extends BaseAdapter {
	
    private LinkedList<TweetData> list;
	private Context context;

    public TweetListAdapter(Context context)
    {
		this.context = context;
    	list = new LinkedList<TweetData>();   	
    }
    
    public void add(TweetData data)
    {
    	list.add(data);
    }
    
	@Override
	public int getCount() {
		System.out.println("listSize " + list.size());
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
		System.out.println("got View!");
		TweetListItem tweetItem;
		if(null == convertView)
		{
			tweetItem = (TweetListItem)View.inflate(context, R.layout.tweetrow, null);
		}
		else 
		{
			tweetItem = (TweetListItem)convertView;
		}
		tweetItem.setTweet(list.get(position));
		return tweetItem;
	}
	
}

