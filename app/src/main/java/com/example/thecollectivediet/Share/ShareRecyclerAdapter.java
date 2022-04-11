package com.example.thecollectivediet.Share;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thecollectivediet.JSON_Marshall_Objects.UserPostUploadItem;
import com.example.thecollectivediet.R;

public class ShareRecyclerAdapter extends RecyclerView.Adapter<ShareRecyclerAdapter.ViewHolder> {

    private UserPostUploadItem[] localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView textView;
        private final View divider;
        public ViewHolder(View v){
            super(v);

            textView = v.findViewById(R.id.tv_shared_post);
            divider = v.findViewById(R.id.divider);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public ShareRecyclerAdapter(UserPostUploadItem[] dataSet) {
        localDataSet = dataSet;
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
        if(localDataSet[position].getComment() != null)
        holder.getTextView().setText(localDataSet[position].getComment());
    }

    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}
