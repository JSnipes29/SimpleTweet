package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.activities.DetailsTweetActivity;
import com.codepath.apps.restclienttemplate.activities.DetailsUserActivity;
import com.codepath.apps.restclienttemplate.fragments.ComposeTweetFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

import okhttp3.Headers;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    Context context;
    AppCompatActivity mContext;
    List<User> users;


    // Pass in the context and list of tweets
    public UserAdapter(Context context, List<User> users, AppCompatActivity a) {
        this.users = users;
        this.context = context;
        this.mContext = a;
    }

    // Clean all elements of the recycler
    public void clear() {
        users.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<User> list) {
        users.addAll(list);
        notifyDataSetChanged();
    }

    // For each row, inflate the layout

    @NonNull
    @NotNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    // Bind values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserAdapter.ViewHolder holder, int position) {
        // Get the data at position
        User user = users.get(position);
        // Bind the tweet with the viewholder
        holder.bind(user);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }







    // Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder  {

        public static final String TAG = "ViewHolder";
        ImageView ivProfileImage;
        TextView tvDescription;
        TextView tvScreenName;
        TextView tvName;

        TwitterClient client;
        public static final int RADIUS = 50;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvName = itemView.findViewById(R.id.tvName);
            client = TwitterApp.getRestClient(context);
        }
        public void bind(User user) {
            tvDescription.setText(user.description);
            tvScreenName.setText("@" + user.screenName);
            tvName.setText(user.name);
            Glide.with(context).load(user.profileImageUrl)
                    .fitCenter()
                    .transform(new RoundedCorners(RADIUS))
                    .into(ivProfileImage);
        }

    }
}
