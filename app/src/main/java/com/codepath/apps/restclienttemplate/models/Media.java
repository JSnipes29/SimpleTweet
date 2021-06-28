package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Media {
    public String url;
    public int height;
    public int width;


    public static Media fromJson(JSONObject jSonObject) throws JSONException {
        Media media = new Media();
        media.url = jSonObject.getString("media_url_https");
        JSONObject size = jSonObject.getJSONObject("sizes").getJSONObject("small");
        media.width = size.getInt("w");
        media.height = size.getInt("h");
        Log.i("Media", media.url);
        return media;
    }

    public static List<Media> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Media> media = new ArrayList<>();
        Log.i("Media", "We are in fromJSON: " + jsonArray.length());
        for (int j = 0; j < jsonArray.length(); j++) {
            Log.i("Media", "We are in the loop");
            media.add(Media.fromJson(jsonArray.getJSONObject(j)));
            Log.i("Media", media.get(j).url);
        }
        return media;

    }
}
