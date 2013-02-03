package com.k1x.android.twitterlist.layouts;

import java.io.IOException;

import com.k1x.android.twitterlist.R;
import com.k1x.android.twitterlist.TweetListActivity;
import com.k1x.android.twitterlist.R.id;
import com.k1x.android.twitterlist.entities.SlideMenuItem;
import com.k1x.android.twitterlist.entities.TweetData;
import com.k1x.android.twitterlist.httputil.HTTPUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MenuListItem extends LinearLayout {

	private TextView menuItemName;
	private ImageView menuImage;

	public MenuListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	private SlideMenuItem item;
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		menuItemName = (TextView)findViewById(R.id.slidebar_item_text);
		menuImage = (ImageView)findViewById(R.id.slidebar_item_image);
	}
	
	public void setMenuItem(SlideMenuItem item)
	{
		this.item = item;
		Bitmap image = item.getImage();
		if(image!=null) {
		menuImage.setImageBitmap(item.getImage());
		}
		menuItemName.setText(item.getName());
	}

	public SlideMenuItem getItem() {
		return item;
	}

	public void setItem(SlideMenuItem item) {
		this.item = item;
	}
	


}
