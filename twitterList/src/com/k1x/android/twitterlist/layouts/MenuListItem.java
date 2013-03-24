package com.k1x.android.twitterlist.layouts;


import com.k1x.android.twitterlist.R;
import com.k1x.android.twitterlist.entities.SlideMenuItem;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
		Integer imageID = item.getImageID();
		if (imageID != null) {
			menuImage.setImageResource(imageID);
		}
		menuItemName.setText(item.getTextID());
	}

	public SlideMenuItem getItem() {
		return item;
	}

	public void setItem(SlideMenuItem item) {
		this.item = item;
	}
	


}
