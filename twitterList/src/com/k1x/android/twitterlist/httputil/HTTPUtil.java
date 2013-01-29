package com.k1x.android.twitterlist.httputil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HTTPUtil {

	public static Bitmap getImage(String imageURL) throws IOException
	{
		System.out.println("'"+imageURL+"'");
		URL url = new URL(imageURL);
		HttpURLConnection connection  = (HttpURLConnection) url.openConnection();

		InputStream is = connection.getInputStream();
		return BitmapFactory.decodeStream(is); 
	}
}
