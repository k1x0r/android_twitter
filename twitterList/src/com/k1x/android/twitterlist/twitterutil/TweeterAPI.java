package com.k1x.android.twitterlist.twitterutil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.k1x.android.twitterlist.R;
import com.k1x.android.twitterlist.entities.SearchData;
import com.k1x.android.twitterlist.entities.TweetData;
import com.k1x.android.twitterlist.entities.UserInfo;
import com.k1x.android.twitterlist.entities.UserList;


public class TweeterAPI {

	public static int FOLOWINGS = 0;
	public static int FOLOWERS = 1;
	public static final String TWEETS_COUNT = "20";
	public static final String TAG = "Trololo";
	public static final Pattern ID_PATTERN = Pattern.compile(".*?\"id_str\":\"(\\d*)\".*");
	public static final Pattern SCREEN_NAME_PATTERN = Pattern.compile(".*?\"screen_name\":\"([^\"]*).*");
	private InputStream is;

    protected CommonsHttpOAuthConsumer oAuthConsumer;

    public TweeterAPI(String accessToken, String secretToken, Context context) {
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
    
    
    public UserList getFolowingsFolowers(int mode, String username, String cursor)
    {
		try {
			UserList info; 
			
		    HttpParams params = new BasicHttpParams();
		    HttpConnectionParams.setSoTimeout(params, 0);
		    HttpClient httpClient = new DefaultHttpClient(params);
            Uri.Builder builder = new Uri.Builder();

			if (mode == FOLOWERS) {
				builder.appendPath("followers");
			} else {
				builder.appendPath("friends");
			}
            
            builder.appendPath("list.json");

            if(cursor!=null) {
            	builder.appendQueryParameter("cursor", cursor);
            }
            if(username!=null) {
            	builder.appendQueryParameter("screen_name", username);
            }
            
            Uri man = builder.build();
		    //prepare the HTTP GET call 
            
            System.out.println("https://api.twitter.com/1.1" + man.toString());
		    HttpGet httpget = new HttpGet("https://api.twitter.com/1.1" + man.toString());
            oAuthConsumer.sign(httpget);

		    HttpEntity entity = httpClient.execute(httpget).getEntity();
		    if (entity != null) {
		    	is = entity.getContent();
		    	InputStreamReader reader = new InputStreamReader(is);	    	
		        Gson gson = new GsonBuilder().create();
		        info = gson.fromJson(reader, UserList.class);
//	            String jsonResponseStr = convertStreamToString(entity.getContent());
//		        System.out.println(jsonResponseStr);
		        httpClient.getConnectionManager().shutdown();
		        return info;
		    }
		}catch (Exception e) {
		    e.printStackTrace();
	        return null;
		}
        return null;
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
    
    
	public LinkedList<TweetData> getUserTimeline(String userLogin, String maxTweetID) throws IllegalStateException, IOException, JsonSyntaxException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
	    HttpParams params = new BasicHttpParams();
	    HttpConnectionParams.setSoTimeout(params, 0);
	    HttpClient httpClient = new DefaultHttpClient(params);

        Uri.Builder builder = new Uri.Builder();
        builder.appendPath("statuses").appendPath("user_timeline.json")
                .appendQueryParameter("include_rts", "true")
                .appendQueryParameter("include_entities", "true")
                .appendQueryParameter("count", "7");

        if(userLogin!=null ) {
               	builder.appendQueryParameter("screen_name", userLogin);
        }
        if(maxTweetID!=null ) {
           	builder.appendQueryParameter("max_id", maxTweetID);
        }
        
        Uri man = builder.build();
        System.out.println("https://api.twitter.com/1.1" + man.toString());
        
        LinkedList<TweetData> tweetData = getArrayTweets(httpClient, man);
	    httpClient.getConnectionManager().shutdown();
		
	    return tweetData;
	}
	
	public SearchData searchTweets(String searchText, String maxTweetID) throws IllegalStateException, IOException, JsonSyntaxException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
	    HttpParams params = new BasicHttpParams();
	    HttpConnectionParams.setSoTimeout(params, 0);
	    HttpClient httpClient = new DefaultHttpClient(params);

        Uri.Builder builder = new Uri.Builder();
        builder.appendPath("search").appendPath("tweets.json");

        if(searchText!=null ) {
             builder.appendQueryParameter("q",'"'+searchText+'"');
        }
        if(maxTweetID!=null ) {
           	builder.appendQueryParameter("max_id", maxTweetID);
        }
        builder.appendQueryParameter("count", "7");
        
        Uri man = builder.build();
        
		HttpGet httpget = new HttpGet("https://api.twitter.com/1.1" + man.toString());
        oAuthConsumer.sign(httpget);

	    HttpEntity entity = httpClient.execute(httpget).getEntity();
	    if (entity != null) {
	    	
	    	InputStream is = entity.getContent();
	    	InputStreamReader reader = new InputStreamReader(is);
	        
	        Gson gson = new GsonBuilder().create();
	        SearchData data = gson.fromJson(reader, SearchData.class);
	        
			httpClient.getConnectionManager().shutdown();

			return data;
		} else {
			httpClient.getConnectionManager().shutdown();
			return null;
		}
	}
	
	public LinkedList<TweetData> getHomeTimeline(String maxTweetID) throws IllegalStateException, IOException, JsonSyntaxException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
	    HttpParams params = new BasicHttpParams();
	    HttpConnectionParams.setSoTimeout(params, 0);
	    HttpClient httpClient = new DefaultHttpClient(params);

        Uri.Builder builder = new Uri.Builder();
        builder.appendPath("statuses").appendPath("home_timeline.json")
                .appendQueryParameter("include_rts", "true")
                .appendQueryParameter("include_entities", "true")
                .appendQueryParameter("count", "7");

        if(maxTweetID!=null ) {
           	builder.appendQueryParameter("max_id", maxTweetID);
        }
        
        Uri man = builder.build();
        System.out.println("https://api.twitter.com/1.1" + man.toString());
        
		HttpGet httpget = new HttpGet("https://api.twitter.com/1.1" + man.toString());
        oAuthConsumer.sign(httpget);
        
        LinkedList<TweetData> tweetData = getArrayTweets(httpClient, man);
 	    httpClient.getConnectionManager().shutdown();
		
	    return tweetData;
	}


	private LinkedList<TweetData> getArrayTweets(HttpClient httpClient, Uri man)
			throws OAuthMessageSignerException,	OAuthExpectationFailedException, OAuthCommunicationException,
			IOException, ClientProtocolException {
		
		HttpGet httpget = new HttpGet("https://api.twitter.com/1.1" + man.toString());
        oAuthConsumer.sign(httpget);

	    HttpEntity entity = httpClient.execute(httpget).getEntity();
	    if (entity != null) {
	    	
	    	InputStream is = entity.getContent();
	    	InputStreamReader reader = new InputStreamReader(is);
	        
	        Gson gson = new GsonBuilder().create();
	        Type collectionType = new TypeToken<LinkedList<TweetData>>(){}.getType();
	        LinkedList<TweetData> tweetData = gson.fromJson(reader, collectionType);
	        
	        
	        return tweetData;
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


