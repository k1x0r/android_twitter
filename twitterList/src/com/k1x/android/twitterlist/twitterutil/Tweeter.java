package com.k1x.android.twitterlist.twitterutil;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.k1x.android.twitterlist.R;
import com.k1x.android.twitterlist.jsonobj.UserInfo;


public class Tweeter {
    public static final String TAG = "Trololo";

	public static final Pattern ID_PATTERN = Pattern.compile(".*?\"id_str\":\"(\\d*)\".*");
	public static final Pattern SCREEN_NAME_PATTERN = Pattern.compile(".*?\"screen_name\":\"([^\"]*).*");
	private InputStream is;

    protected CommonsHttpOAuthConsumer oAuthConsumer;

    public Tweeter(String accessToken, String secretToken, Context context) {
        oAuthConsumer = new CommonsHttpOAuthConsumer(context.getString(R.string.twitter_oauth_consumer_key),
        		context.getString(R.string.twitter_oauth_consumer_secret));
        oAuthConsumer.setTokenWithSecret(accessToken, secretToken);
    }

    public String tweet(String message) {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            Uri.Builder builder = new Uri.Builder();
            builder.appendPath("statuses").appendPath("update.json")
                    .appendQueryParameter("status", message);
            Uri man = builder.build();
            HttpPost post = new HttpPost("https://api.twitter.com/1.1" + man.toString());
            System.out.println(man.toString());
            
            oAuthConsumer.sign(post);
            HttpResponse resp = httpClient.execute(post);
            String jsonResponseStr = convertStreamToString(resp.getEntity().getContent());
            Log.i(TAG,"response: " + jsonResponseStr);
            String id = getFirstMatch(ID_PATTERN,jsonResponseStr);
            Log.i(TAG,"id: " + id);
            String screenName = getFirstMatch(SCREEN_NAME_PATTERN,jsonResponseStr);
            Log.i(TAG,"screen name: " + screenName);

            final String url = MessageFormat.format("https://twitter.com/#!/{0}/status/{1}",screenName,id);
            Log.i(TAG,"url: " + url);


            return "Tweeted: " + url;
            
        } catch (Exception e) {
            Log.e(TAG,"trying to tweet: " + message, e);
            return "Failed";
        }

    }
    
    public UserInfo getUserInfo()
    {
		try {
			UserInfo info; 
			
		    HttpParams params = new BasicHttpParams();
		    HttpConnectionParams.setSoTimeout(params, 0);
		    HttpClient httpClient = new DefaultHttpClient(params);

		    //prepare the HTTP GET call 
		    HttpGet httpget = new HttpGet("https://api.twitter.com/1/account/verify_credentials.json");
            oAuthConsumer.sign(httpget);

		    HttpEntity entity = httpClient.execute(httpget).getEntity();
		    if (entity != null) {
		    	is = entity.getContent();
		    	InputStreamReader reader = new InputStreamReader(is);
		        Gson gson = new GsonBuilder().create();
		        info = gson.fromJson(reader, UserInfo.class);
		        System.out.println(info);
		        httpClient.getConnectionManager().shutdown();
		        return info;
		    }
		}catch (Exception e) {
		    e.printStackTrace();
	        return null;
		}
        return null;

    }
    
    public static String convertStreamToString(java.io.InputStream is) {
        try {
            return new java.util.Scanner(is).useDelimiter("\\A").next();
        } catch (java.util.NoSuchElementException e) {
            return "";
        }
    }

    public static String getFirstMatch(Pattern pattern, String str){
        Matcher matcher = pattern.matcher(str);
        if(matcher.matches()){
            return matcher.group(1);
        }
        return null;
    }
}



