package com.example.thecollectivediet.Me_Fragment_Components.Food_Logging_Editing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thecollectivediet.JSON_Marshall_Objects.FoodLogItemView;
import com.example.thecollectivediet.R;

import java.util.ArrayList;

/**
 * Used to configure inner list in nested recycler list
 */
public class InnerMealFoodListRecyclerAdapter extends RecyclerView.Adapter<InnerMealFoodListRecyclerAdapter.HorizontalRVViewHolder> {

    Context context;
    ArrayList<FoodLogItemView> arrayList;

    /**
     * Construct adapter given application context and food log list.
     */
    public InnerMealFoodListRecyclerAdapter(Context context, ArrayList<FoodLogItemView> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    /**
     * Inflate the view and return it
     */
    @NonNull
    @Override
    public HorizontalRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_inner_food, parent, false);
        return new HorizontalRVViewHolder(v);
    }

    /**
     * Called after binding item to view holder - set various text fields.
     */
    @Override
    public void onBindViewHolder(@NonNull HorizontalRVViewHolder holder, int position) {
        FoodLogItemView food = arrayList.get(position);

        holder.mTitle.setText(food.getProduct_name());
        holder.mServing.setText("Serving Size: " + food.getPortion_size() + food.getPortion_unit());
        holder.mCalories.setText(Converter.getCalorieString(food.getEnergy_kcal_100g(), food.getPortion_unit(), food.getPortion_size()));
    }

    /**
     * Return the item count.
     */
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    /**
     * Inner item view holder class
     */
    public class HorizontalRVViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle;
        TextView mServing;
        TextView mCalories;

        public HorizontalRVViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.tv_recycler_item_food_name);
            mServing = itemView.findViewById(R.id.tv_recycler_item_serving);
            mCalories = itemView.findViewById(R.id.tv_recycler_item_calories);
        }
    }
}
