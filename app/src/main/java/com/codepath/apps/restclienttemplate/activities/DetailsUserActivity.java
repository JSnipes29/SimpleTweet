package com.codepath.apps.restclienttemplate.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.adapters.UserAdapter;
import com.codepath.apps.restclienttemplate.databinding.ActivityDetailsUserBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


public class DetailsUserActivity extends AppCompatActivity {

    private static final int RADIUS = 50;

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
    }
}