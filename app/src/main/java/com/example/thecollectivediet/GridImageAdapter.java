package com.example.thecollectivediet;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//import com.android.volley.toolbox.ImageLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Controls the grid in the SharedFragment class.
 */
public class GridImageAdapter extends ArrayAdapter<String> {

    private Context cxt;
    private LayoutInflater inflater;
    private int layoutResource;
    private String append;
    private ArrayList<String> imgURLs;

    public GridImageAdapter(Context cxt, int layoutResource, String append, ArrayList<String> imgURLs) {
        super(cxt, layoutResource, imgURLs);
        this.cxt = cxt;
        //this.inflater = inflater;
        inflater = (LayoutInflater) cxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutResource = layoutResource;
        this.append = append;
        this.imgURLs = imgURLs;
    }

    private static class ViewHolder{
        ImageView image;
        ProgressBar progressBar;
    }

//    @Override public void getView(int position, View convertView, ViewGroup parent) {
//         ViewHolder view = (ViewHolder) convertView.getTag();
//        if (view == null) {
//            view = new ViewHolder();
//        }
//        String url = getItem(position);
//
//        Picasso.get().load(url).into(view);
//    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        /*
        Viewholder build pattern (similar to recyclerview)
         */
        final ViewHolder holder;

        if(convertView == null){
            convertView = inflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();
            holder.progressBar = convertView.findViewById(R.id.pb_grid_image_progress_bar);
            holder.image = convertView.findViewById(R.id.gv_gridImageView);


            //Tag will store the view holder in memory instead of in app page
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        String imgURL = getItem(position);

        ImageLoader imageLoader = ImageLoader.getInstance();


        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .considerExifParams(true)
                .cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).build();

        imageLoader.displayImage(append + imgURL , holder.image,options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if(holder.progressBar != null){
                    holder.progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if(holder.progressBar != null){
                    holder.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if(holder.progressBar != null){
                    holder.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if(holder.progressBar != null){
                    holder.progressBar.setVisibility(View.GONE);
                }
            }
        });

        return convertView;
    }
}
