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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
            holder.progressBar = convertView.findViewById(R.id.pb_post_progressBar);
            holder.image = convertView.findViewById(R.id.gv_gridImageView);

            //Tag will store the view holder in memory instead of in app page
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        String imgURL = getItem(position);

        ImageLoader imageLoader = ImageLoader.getInstance();


        imageLoader.displayImage(append + imgURL , holder.image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if(holder.progressBar != null){
                    holder.progressBar.setVisibility(View.GONE);
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
