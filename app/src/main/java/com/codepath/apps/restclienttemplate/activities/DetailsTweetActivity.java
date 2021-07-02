package com.codepath.apps.restclienttemplate.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.databinding.ActivityDetailsTweetBinding;
import com.codepath.apps.restclienttemplate.databinding.ActivityTimelineBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailsTweetActivity extends AppCompatActivity {

    private static final int RADIUS = 50;
    ActivityDetailsTweetBinding binding;
    Tweet tweet;
    TwitterClient client;
    int position;
    private static final String TAG = "DetailsTweetActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsTweetBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        client = TwitterApp.getRestClient(this);
        position = getIntent().getIntExtra("position", 0);
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        binding.tvName.setText(tweet.user.name);
        binding.tvScreenName.setText("@" + tweet.user.screenName);
        binding.tvRetweetCount.setText(tweet.formatCount(tweet.retweets));
        binding.tvLikeCount.setText(Tweet.formatCount(tweet.likes));
        binding.tvBody.setText(tweet.body);
        binding.tvTimeCreated.setText(tweet.createdAt);
        final Tweet copy = tweet;
        binding.ivLike.setClickable(true);
        if (tweet.liked) {
            binding.ivLike.setImageResource(R.drawable.ic_vector_heart);
        } else {
            binding.ivLike.setImageResource(R.drawable.ic_vector_heart_stroke);
        }
        binding.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (copy.liked) {
                    copy.liked = false;
                    binding.ivLike.setImageResource(R.drawable.ic_vector_heart_stroke);
                    binding.tvLikeCount.setText("" + Tweet.formatCount((Integer.parseInt(binding.tvLikeCount.getText().toString().replaceAll(",", "")) - 1)));
                    copy.likes = copy.likes - 1;
                    client.unLike(copy.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "UnLiked");
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "Couldn't unlike: " + copy.id, throwable);
                        }
                    });
                } else {
                    copy.liked = true;
                    binding.ivLike.setImageResource(R.drawable.ic_vector_heart);
                    binding.tvLikeCount.setText("" + Tweet.formatCount((Integer.parseInt(binding.tvLikeCount.getText().toString().replaceAll(",", "")) + 1)));
                    copy.likes = copy.likes + 1;
                    client.like(copy.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "Liked");
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "Couldn't like: " + copy.id, throwable);
                        }
                    });
                }
            }
        });
        tweet.liked = copy.liked;
        binding.ivRetweet.setClickable(true);
        if (tweet.retweeted) {

            binding.ivRetweet.setImageResource(R.drawable.ic_vector_retweet);
        } else {
            binding.ivRetweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
        }
        binding.ivRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (copy.retweeted) {
                    copy.retweeted = false;
                    binding.ivRetweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
                    binding.tvRetweetCount.setText("" + Tweet.formatCount((Integer.parseInt(binding.tvRetweetCount.getText().toString().replaceAll(",", "")) - 1)));
                    copy.retweets = copy.retweets - 1;
                    client.unRetweet(copy.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "unRetweeted");
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "Couldn't unretweet: " + copy.id, throwable);
                        }
                    });
                } else {
                    copy.retweeted = true;
                    binding.ivRetweet.setImageResource(R.drawable.ic_vector_retweet);
                    binding.tvRetweetCount.setText("" + Tweet.formatCount((Integer.parseInt(binding.tvRetweetCount.getText().toString().replaceAll(",", "")) + 1)));
                    copy.retweets = copy.retweets + 1;
                    client.retweet(copy.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "Retweeted");
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "Couldn't retweet: " + copy.id, throwable);
                        }
                    });
                }
            }
        });
        tweet.retweeted = copy.retweeted;
        Glide.with(this).load(tweet.user.profileImageUrl)
                .fitCenter()
                .transform(new RoundedCorners(RADIUS))
                .into(binding.ivProfileImage);
        if (tweet.media != null && !tweet.media.isEmpty()) {
            binding.ivMedia.getLayoutParams().height = tweet.media.get(0).height;
            binding.ivMedia.getLayoutParams().width = tweet.media.get(0).width;
            Glide.with(this).load(tweet.media.get(0).url)
                    .into(binding.ivMedia);
            Log.i("Adapter", tweet.user.screenName + ": " + tweet.media.get(0).url);
        } else {
            binding.ivMedia.getLayoutParams().height = 0;
            binding.ivMedia.getLayoutParams().width = 0;
        }

        tweet = copy;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
        Log.i(TAG, "Tweet: " + tweet.user.screenName);
        Log.i(TAG, "Position: " + position);
        intent.putExtra("position", position);
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }
}