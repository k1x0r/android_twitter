package com.k1x.android.twitterlist.httputil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HTTPUtil {

	private static HashMap<String, Bitmap> cacheMap;
	
	static {
		cacheMap = new HashMap<String, Bitmap>();
	}
	
	public static Bitmap getImage(String imageURL) throws IOException
	{
		if(cacheMap.get(key))
		URL url = new URL(imageURL);
		HttpURLConnection connection  = (HttpURLConnection) url.openConnection();
		InputStream is = connection.getInputStream();
		Bitmap outBitmap = BitmapFactory.decodeStream(is);
		cacheMap.put(imageURL, outBitmap);
		return outBitmap; 
	}
}
