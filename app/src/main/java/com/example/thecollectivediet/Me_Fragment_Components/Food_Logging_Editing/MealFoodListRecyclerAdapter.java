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

public class MealFoodListRecyclerAdapter extends RecyclerView.Adapter<MealFoodListRecyclerAdapter.FoodListHolder>{

    @NonNull
    @Override
    public MealFoodListRecyclerAdapter.FoodListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //inflater parameters: the layout you want to inflate, the parent, and attach to root T/F
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_recycler_element, parent, false);
        return new MealFoodListRecyclerAdapter.FoodListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class FoodListHolder extends RecyclerView.ViewHolder {
        ImageView foodPicture;
        TextView foodName;
        TextView foodServing;

        public FoodListHolder(@NonNull View itemView) {
            super(itemView);
            foodPicture = itemView.findViewById(R.id.foodRecImage);
            foodName = itemView.findViewById(R.id.foodRecName);
            foodServing = itemView.findViewById(R.id.foodRecServing);
        }

        public void bind(final FoodResult food, final FoodSearchRecyclerViewAdapter.OnFoodItemClickListener listener, final int position, Context ctx) {

        }


    }
}
