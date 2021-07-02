package com.codepath.apps.restclienttemplate.models;

import android.content.Context;
import android.util.Log;

import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Parcel
public class User {

    public String name;
    public String screenName;
    public String profileImageUrl;
    public String description;
    public BigInteger id;

    // empty constructor needed for Parceler library
    public User() {}

    public static User fromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.name = jsonObject.getString("name");
        user.screenName = jsonObject.getString("screen_name");
        user.profileImageUrl = jsonObject.getString("profile_image_url_https");
        user.description = jsonObject.getString("description");
        user.id = new BigInteger(jsonObject.getString("id_str"));
        return user;
    }

    // Get a list of tweets from a JSON array
    public static List<User> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            users.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return users;

    }

    // Get a list of user ids from a JSON array
    public static List<String> idsFromJsonArray(JSONArray jsonArray) throws JSONException {
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            String id = jsonArray.getString(i);
            Log.i("User", "User id: " + id);
            ids.add(id);
        }
        return ids;

    }
}
