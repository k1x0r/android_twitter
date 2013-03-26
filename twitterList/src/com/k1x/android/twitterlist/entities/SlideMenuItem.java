package com.k1x.android.twitterlist.entities;

import com.k1x.android.twitterlist.TwitterListApplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View.OnLongClickListener;

public class SlideMenuItem {

	private String text;
	private Drawable image;
	private Runnable action;
	private OnLongClickListener longClickAction;
	private Context context;

	
	public SlideMenuItem(Integer textID, Integer imageID, Runnable action) 	{
		context = TwitterListApplication.getTwitterAppContext();
		this.text = context.getResources().getString(textID);
		if (imageID != null) {
			this.image = context.getResources().getDrawable(imageID);
		}
		this.action = action;
	}
		
	public Runnable getAction() {
		return action;
	}

	public void setAction(Runnable action) {
		this.action = action;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Drawable getImage() {
		return image;
	}

	public void setImage(Drawable image) {
		this.image = image;
	}
	
	public void setImageFromResourse(Integer resId) {
		if (resId != null) {
			this.image = context.getResources().getDrawable(resId);
		}
	}

	public OnLongClickListener getLongClickAction() {
		return longClickAction;
	}

	public void setLongClickAction(OnLongClickListener longClickAction) {
		this.longClickAction = longClickAction;
	}

}
