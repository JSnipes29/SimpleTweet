package com.codepath.apps.restclienttemplate.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.UserAdapter;
import com.codepath.apps.restclienttemplate.databinding.ActivityDetailsUserBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;


public class DetailsUserActivity extends AppCompatActivity {

    private static final int RADIUS = 50;
    private static final String TAG = "DetailsUserActivity";
    TwitterClient client;
    ActivityDetailsUserBinding binding;
    Tweet tweet;
    ArrayAdapter<String> adapter;
    UserAdapter rvAdapter;
    List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsUserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        client = TwitterApp.getRestClient(this);
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        binding.tvName.setText(tweet.user.name);
        binding.tvScreenName.setText("@" + tweet.user.screenName);
        String description = tweet.user.description;
        if (description != null) {
            binding.tvDescription.setText(description);
        }
        Glide.with(this).load(tweet.user.profileImageUrl)
                .fitCenter()
                .transform(new RoundedCorners(RADIUS))
                .into(binding.ivProfileImage);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        adapter.add("Followers");
        adapter.add("Following");
        binding.spinner.setAdapter(adapter);
        users = new ArrayList<User>();
        rvAdapter = new UserAdapter(this, users,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rvUsers.setLayoutManager(linearLayoutManager);
        binding.rvUsers.setAdapter(rvAdapter);
        populateUsers(0);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                users.clear();
                int code = 0;
                String option = parent.getItemAtPosition(position).toString();
                if (option.equals("Following")) {
                    code = 1;
                }
                populateUsers(code);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void populateUsers(int code) {
        Log.i(TAG, tweet.user.id.toString());
        client.getFollowersFriends(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess! " + json.toString());
                try {
                    JSONArray jsonArray = json.jsonObject.getJSONArray("ids");
                    List<String> ids = User.idsFromJsonArray(jsonArray);
                    client.getUsers(new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "onSuccess! " + json.toString());
                            try {
                                JSONArray jsonArray1 = json.jsonArray;
                                users.addAll(User.fromJsonArray(jsonArray1));
                                rvAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                Log.i(TAG, "Couldn't load users");
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "Couldn't get users", throwable);
                        }
                    }, ids);
                } catch (JSONException e) {
                    Log.e(TAG, "Json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG,"onFailure! " + response, throwable);
            }
        }, tweet.user.id, code);
    }

}