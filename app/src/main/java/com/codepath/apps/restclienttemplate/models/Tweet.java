package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;
import android.util.Log;

import com.codepath.apps.restclienttemplate.TimelineActivity;
import com.facebook.stetho.inspector.jsonrpc.JsonRpcException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

@Parcel
public class Tweet {

    public String body;
    public String createdAt;
    public User user;
    public List<Media> media;
    public int id;

    // empty constructor needed by Parceler library
    public Tweet() {};

    // Get a tweet from a JSON Object
    public static Tweet fromJson(JSONObject jSonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jSonObject.getString("text");
        tweet.createdAt = jSonObject.getString("created_at");
        tweet.id = jSonObject.getInt("id");
        Log.i("TweetID","ID: " + tweet.id);
        if (tweet.id > 1 && tweet.id < TimelineActivity.maxId) {
            TimelineActivity.maxId = tweet.id - 1;
        }
        tweet.user = User.fromJson(jSonObject.getJSONObject("user"));
        try {
            JSONObject entries = jSonObject.getJSONObject("extended_entities");
            Log.i("Tweet", "We have entries!");
            JSONArray media = entries.getJSONArray("media");
            Log.i("Tweet", "We have media!");
            tweet.media = Media.fromJsonArray(media);

        } catch (Exception e) {
            // No media in the tweet
            tweet.media = null;
        }
        return tweet;
    }

    // Get a list of tweets from a JSON array
    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;

    }

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
