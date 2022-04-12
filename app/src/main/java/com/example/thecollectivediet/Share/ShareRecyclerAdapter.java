package com.example.thecollectivediet.Share;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.core.Amplify;
import com.example.thecollectivediet.JSON_Marshall_Objects.UserPostUploadItem;
import com.example.thecollectivediet.MainActivity;
import com.example.thecollectivediet.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ShareRecyclerAdapter extends RecyclerView.Adapter<ShareRecyclerAdapter.ViewHolder> {

    private UserPostUploadItem[] localDataSet;
    private Context ctx;
    private MainActivity activity;

    //constants
    private static final String append = "file:/";

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //elements of holder element
        private final TextView userName;
        private final TextView comment;
        private final ImageView image;
        private final View divider;
        private final TextView date;

        public ViewHolder(View v) {
            super(v);

            userName = v.findViewById(R.id.tv_shared_post_user_name);
            comment = v.findViewById(R.id.tv_shared_post_comment);
            divider = v.findViewById(R.id.divider);
            image = v.findViewById(R.id.iv_shared_post_image);
            date = v.findViewById(R.id.tv_shared_post_date);
        }

        public TextView getComment() {
            return comment;
        }
    }

    public ShareRecyclerAdapter(UserPostUploadItem[] dataSet, Context ctx, MainActivity activity) {

        localDataSet = dataSet;
        this.ctx = ctx;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ShareRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.share_feed_element, parent, false);

        return new ShareRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShareRecyclerAdapter.ViewHolder holder, int position) {

        //set name of user
        holder.userName.setText(localDataSet[position].getUser_name());

        //set comment of element
        if (localDataSet[position].getComment() != null) {
            holder.getComment().setText(localDataSet[position].getComment());
        }

        //set image of element
        String imageToPull = localDataSet[position].getImage_key();
        if (localDataSet[position].getImage_key() != null) {

            Amplify.Storage.getUrl(
                    imageToPull,
                    result -> {
                        Log.i("MyAmplifyApp", "Successfully generated: " + result.getUrl());

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                ImageLoader imageLoader = ImageLoader.getInstance();

                                imageLoader.displayImage(String.valueOf(result.getUrl()), holder.image, new ImageLoadingListener() {
                                    @Override
                                    public void onLoadingStarted(String imageUri, View view) {
                                        // progressBar.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                        // progressBar.setVisibility(View.INVISIBLE);
                                    }

                                    @Override
                                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                        // progressBar.setVisibility(View.INVISIBLE);
                                    }

                                    @Override
                                    public void onLoadingCancelled(String imageUri, View view) {
                                        //progressBar.setVisibility(View.INVISIBLE);
                                    }
                                });

                            }
                        });
                    },
                    error -> Log.e("MyAmplifyApp", "URL generation failure", error)
            );

            holder.date.setText(localDataSet[position].getDate());

        }
    }

    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}
