package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.codepath.apps.restclienttemplate.activities.TimelineActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

@Parcel
@Entity(foreignKeys = @ForeignKey(entity=User.class, parentColumns="id", childColumns="userId"))
public class Tweet {

    @ColumnInfo
    public String body;
    @ColumnInfo
    public String createdAt;
    @Ignore
    public User user;
    @ColumnInfo
    public int retweets;
    @ColumnInfo
    public boolean retweeted;
    @ColumnInfo
    public int likes;
    @ColumnInfo
    public boolean liked;
    @ColumnInfo
    public String userId;

    public List<Media> media;
    @ColumnInfo
    public Tweet reTweet;
    @ColumnInfo
    @PrimaryKey
    public BigInteger id;


    // empty constructor needed by Parceler library
    public Tweet() {};

    // Get a tweet from a JSON Object
    public static Tweet fromJson(JSONObject jSonObject) throws JSONException {
        Tweet tweet = new Tweet();
        try {
            tweet.reTweet = Tweet.fromJson(jSonObject.getJSONObject("retweeted_status"));
            Log.i("Tweet","This is a retweet");
        } catch (Exception e) {
            tweet.reTweet = null;
        }
        tweet.id = new BigInteger(jSonObject.getString("id"));
        tweet.body = jSonObject.getString("text");
        tweet.createdAt = jSonObject.getString("created_at");
        tweet.retweets = jSonObject.getInt("retweet_count");
        tweet.retweeted = jSonObject.getBoolean("retweeted");
        tweet.likes = jSonObject.getInt("favorite_count");
        tweet.liked = jSonObject.getBoolean("favorited");

        Log.i("TweetID","ID: " + tweet.id);
        if (TimelineActivity.maxId == null) {
            TimelineActivity.maxId = new BigInteger(tweet.id.toString());
        }
        if (tweet.id.compareTo(new BigInteger("1")) >= 1 && tweet.id.compareTo(TimelineActivity.maxId) <= -1) {
            TimelineActivity.maxId = tweet.id.subtract(new BigInteger("1"));
        }
        tweet.user = User.fromJson(jSonObject.getJSONObject("user"));
        tweet.userId = tweet.user.id;
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

    public static String formatCount(int count) {
        return String.format("%,d", count);
    }
}
