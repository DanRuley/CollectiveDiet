package com.example.thecollectivediet.Me_Fragment_Components.Food_Logging_Editing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodResult;
import com.example.thecollectivediet.R;

import java.util.List;

/**
 * Provide an adaptor for displaying the scrollable list of food search results.
 */
public class FoodSearchRecyclerViewAdapter extends RecyclerView.Adapter<FoodSearchRecyclerViewAdapter.FoodSearchHolder> {

    public interface OnFoodItemClickListener {
        void onFoodItemClick(FoodResult foodItem);
    }

    List<FoodResult> foodResults;
    Context ctx;
    OnFoodItemClickListener listener;

    /**
     * Construct recycler adapter
     *
     * @param foodResults
     * @param _ctx
     * @param listener
     */
    public FoodSearchRecyclerViewAdapter(List<FoodResult> foodResults, Context _ctx, OnFoodItemClickListener listener) {
        this.foodResults = foodResults;
        this.ctx = _ctx;
        this.listener = listener;
    }

    /**
     * Create the view holder and inflate the layout
     */
    @NonNull
    @Override
    public FoodSearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //inflater parameters: the layout you want to inflate, the parent, and attach to root T/F
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_recycler_element, parent, false);
        return new FoodSearchHolder(v);
    }

    /**
     * Bind the given item to the view holder
     */
    @Override
    public void onBindViewHolder(@NonNull FoodSearchHolder holder, int position) {
        holder.bind(foodResults.get(position), listener, position, ctx);
    }

    /**
     * Return count of food items
     */
    @Override
    public int getItemCount() {
        return foodResults.size();
    }

    /**
     * View holder for the recycler adapter
     */
    public class FoodSearchHolder extends RecyclerView.ViewHolder {
        ImageView foodPicture;
        TextView foodName;
        TextView foodServing;

        /**
         * Holds one food search item
         *
         * @param itemView
         */
        public FoodSearchHolder(@NonNull View itemView) {
            super(itemView);
            foodPicture = itemView.findViewById(R.id.foodRecImage);
            foodName = itemView.findViewById(R.id.foodRecName);
            foodServing = itemView.findViewById(R.id.foodRecServing);
        }

        /**
         * Bind a food item into the view holder
         */
        public void bind(@NonNull final FoodResult food, @NonNull final OnFoodItemClickListener listener, final int position, @NonNull Context ctx) {
            // create a ProgressDrawable object which we will show as placeholder
            CircularProgressDrawable drawable = new CircularProgressDrawable(ctx);
            drawable.setColorSchemeColors(R.color.design_default_color_primary, R.color.design_default_color_primary_dark, R.color.teal_700);
            drawable.setCenterRadius(30f);
            drawable.setStrokeWidth(5f);
            // set all other properties as you would see fit and start it
            drawable.start();

            this.foodName.setText(foodResults.get(position).getProduct_name());
            this.foodServing.setText("One serving");

            String imgURL = food.getImage_url() == null ? "https://d2eawub7utcl6.cloudfront.net/images/nix-apple-grey.png" : food.getImage_url();
            Glide.with(ctx).load(imgURL).placeholder(drawable).into(this.foodPicture);

            itemView.setOnClickListener(v -> listener.onFoodItemClick(food));
        }


    }

}