package com.example.thecollectivediet.Me_Fragment_Components.Food_Logging_Editing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thecollectivediet.R;

import java.util.ArrayList;

public class InnerMealFoodListRecyclerAdapter extends RecyclerView.Adapter<InnerMealFoodListRecyclerAdapter.HorizontalRVViewHolder> {

    Context context;
    ArrayList<InnerFoodListItem> arrayList;
    public InnerMealFoodListRecyclerAdapter(Context context, ArrayList<InnerFoodListItem> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public HorizontalRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_inner_food,parent,false);
        return new HorizontalRVViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalRVViewHolder holder, int position) {
        InnerFoodListItem innerFoodListItem = arrayList.get(position);


        holder.mTitle.setText(innerFoodListItem.getName());
        holder.mServing.setText(String.valueOf(innerFoodListItem.getServing()));
        holder.mCalories.setText(String.valueOf(innerFoodListItem.getCalories()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class HorizontalRVViewHolder extends RecyclerView.ViewHolder{

        TextView mTitle;
        ImageView mImage;
        TextView mServing;
        TextView mCalories;

        public HorizontalRVViewHolder(View itemView){
            super(itemView);

            mTitle = itemView.findViewById(R.id.tv_recycler_item_food_name);
            mImage = itemView.findViewById(R.id.iv_recycler_item_image);
            mServing = itemView.findViewById(R.id.tv_recycler_item_serving);
            mCalories = itemView.findViewById(R.id.tv_recycler_item_calories);
        }
    }
}