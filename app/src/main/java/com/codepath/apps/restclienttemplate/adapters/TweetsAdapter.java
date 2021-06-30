package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    Context context;
    List<Tweet> tweets;


    // Pass in the context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.tweets = tweets;
        this.context = context;
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    // For each row, inflate the layout

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    // Bind values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        // Get the data at position
        Tweet tweet = tweets.get(position);
        // Bind the tweet with the viewholder
        holder.bind(tweet);

    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }







    // Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {

        public static final String TAG = "ViewHolder";
        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvName;
        TextView tvTimeSince;
        ImageView ivMedia;
        final ImageView ivRetweet;
        final ImageView ivLike;
        final TextView tvRetweetCount;
        final TextView tvLikeCount;

        TwitterClient client;
        public static final int RADIUS = 50;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvName = itemView.findViewById(R.id.tvName);
            tvTimeSince = itemView.findViewById(R.id.tvTimeSince);
            ivMedia = itemView.findViewById(R.id.ivMedia);
            ivRetweet = itemView.findViewById(R.id.ivRetweet);
            ivLike = itemView.findViewById(R.id.ivLike);
            tvRetweetCount = itemView.findViewById(R.id.tvRetweetCount);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
            client = TwitterApp.getRestClient(context);
        }
        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText("@" + tweet.user.screenName);
            tvName.setText(tweet.user.name);
            tvTimeSince.setText(Tweet.getRelativeTimeAgo(tweet.createdAt));
            tvLikeCount.setText(Tweet.formatCount(tweet.likes));
            tvRetweetCount.setText(Tweet.formatCount(tweet.retweets));
            final Tweet copy = tweet;
            ivLike.setClickable(true);
            if (tweet.liked) {
                ivLike.setImageResource(R.drawable.ic_vector_heart);
            } else {
                ivLike.setImageResource(R.drawable.ic_vector_heart_stroke);
            }
            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (copy.liked) {
                        copy.liked = false;
                        ivLike.setImageResource(R.drawable.ic_vector_heart_stroke);
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
                        ivLike.setImageResource(R.drawable.ic_vector_heart);
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
            ivRetweet.setClickable(true);
            if (tweet.retweeted) {

                ivRetweet.setImageResource(R.drawable.ic_vector_retweet);
            } else {
                ivRetweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
            }
            ivRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (copy.retweeted) {
                        copy.retweeted = false;
                        ivRetweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
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
                        ivRetweet.setImageResource(R.drawable.ic_vector_retweet);
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
            Glide.with(context).load(tweet.user.profileImageUrl)
                    .fitCenter()
                    .transform(new RoundedCorners(RADIUS))
                    .into(ivProfileImage);
            if (tweet.media != null && !tweet.media.isEmpty()) {
                ivMedia.getLayoutParams().height = tweet.media.get(0).height;
                ivMedia.getLayoutParams().width = tweet.media.get(0).width;
                Glide.with(context).load(tweet.media.get(0).url)
                        .into(ivMedia);
                Log.i("Adapter", tweet.user.screenName + ": " + tweet.media.get(0).url);
            } else {
                ivMedia.getLayoutParams().height = 0;
                ivMedia.getLayoutParams().width = 0;
            }
        }
    }
}