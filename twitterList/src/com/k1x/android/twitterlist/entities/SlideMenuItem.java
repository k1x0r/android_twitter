package com.k1x.android.twitterlist.entities;

import android.content.Context;
import android.graphics.Bitmap;

public class SlideMenuItem {

	private String name;
	private Bitmap image;
	private Runnable action;
	
	public SlideMenuItem(String name, Bitmap image, Runnable action)
	{
		this.name = name;
		this.image = image;
		this.action = action;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}
	
	public Runnable getAction() {
		return action;
	}

	public void setAction(Runnable action) {
		this.action = action;
	}
}
