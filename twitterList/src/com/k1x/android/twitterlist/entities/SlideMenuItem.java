package com.k1x.android.twitterlist.entities;

public class SlideMenuItem {

	private Integer textID;
	private Integer imageID;
	private Runnable action;
	
	public SlideMenuItem(Integer textID, Integer imageID, Runnable action)
	{
		this.textID = textID;
		this.imageID = imageID;
		this.action = action;
	}
	
	public Integer getTextID() {
		return textID;
	}

	public void setTextID(Integer textID) {
		this.textID = textID;
	}
	
	public Runnable getAction() {
		return action;
	}

	public void setAction(Runnable action) {
		this.action = action;
	}

	public Integer getImageID() {
		return imageID;
	}

	public void setImageID(Integer imageID) {
		this.imageID = imageID;
	}




}
