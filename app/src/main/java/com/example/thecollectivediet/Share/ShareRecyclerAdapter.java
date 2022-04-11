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
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.amplifyframework.core.Amplify;
import com.bumptech.glide.Glide;
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

        private final TextView textView;
        private final ImageView imageView;
        private final View divider;

        public ViewHolder(View v) {
            super(v);

            textView = v.findViewById(R.id.tv_shared_post);
            divider = v.findViewById(R.id.divider);
            imageView = v.findViewById(R.id.iv_shared_post);
        }

        public TextView getTextView() {
            return textView;
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

        //holder.getTextView().setText(localDataSet[position]);
        if (localDataSet[position].getComment() != null)
            holder.getTextView().setText(localDataSet[position].getComment());

        String imageToPull = localDataSet[position].getImage_key();
        if (localDataSet[position].getImage_key() != null) {

//            String imgURL = localDataSet[position].getUrl() == null ? "https://d2eawub7utcl6.cloudfront.net/images/nix-apple-grey.png" : localDataSet[position].getUrl();
//            Glide.with(ctx).load(imgURL).into(holder.imageView);

            Amplify.Storage.getUrl(
                    imageToPull,
                    result -> {

// create a ProgressDrawable object which we will show as placeholder
//                        CircularProgressDrawable drawable = new CircularProgressDrawable(ctx);
//                        drawable.setColorSchemeColors(R.color.design_default_color_primary, R.color.design_default_color_primary_dark, R.color.teal_700);
//                        drawable.setCenterRadius(30f);
//                        drawable.setStrokeWidth(5f);
//                        // set all other properties as you would see fit and start it
//                        drawable.start();

                        Log.i("MyAmplifyApp", "Successfully generated: " + result.getUrl());
//                        String imgURL = localDataSet[position].getUrl() == null ? "https://d2eawub7utcl6.cloudfront.net/images/nix-apple-grey.png" : localDataSet[position].getUrl();
//                        Glide.with(ctx).load(imgURL).into(holder.imageView);

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                ImageLoader imageLoader = ImageLoader.getInstance();

                                imageLoader.displayImage(String.valueOf(result.getUrl()), holder.imageView, new ImageLoadingListener() {
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


        }
    }

    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}
