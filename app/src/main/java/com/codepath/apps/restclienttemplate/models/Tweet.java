package com.codepath.apps.restclienttemplate.models;

import com.facebook.stetho.inspector.jsonrpc.JsonRpcException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;

public class Tweet {

    public String body;
    public String createdAt;
    public User user;

    public static Tweet fromJson(JSONObject jSonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jSonObject.getString("text");
        tweet.createdAt = jSonObject.getString("created_at");
        tweet.user = User.fromJson(jSonObject.getJSONObject("user"));
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;

    }
}
