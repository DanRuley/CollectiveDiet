package com.example.thecollectivediet.Me_Fragment_Components.Food_Logging_Editing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thecollectivediet.JSON_Marshall_Objects.FoodLogItemView;
import com.example.thecollectivediet.R;

import java.util.List;
import java.util.Locale;

public class MealFoodListRecyclerAdapter extends RecyclerView.Adapter<MealFoodListRecyclerAdapter.FoodListHolder> {

    List<FoodLogItemView> foodLogItems;
    String title;

    public MealFoodListRecyclerAdapter(List<FoodLogItemView> items, String title) {
        this.title = title;
        foodLogItems = items;
    }

    @NonNull
    @Override
    public MealFoodListRecyclerAdapter.FoodListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //inflater parameters: the layout you want to inflate, the parent, and attach to root T/F
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_log_recycler, parent, false);
        return new MealFoodListRecyclerAdapter.FoodListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListHolder holder, int position) {
        holder.bind(foodLogItems.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class FoodListHolder extends RecyclerView.ViewHolder {
        TextView foodName;
        TextView serving;
        TextView calories;

        public FoodListHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.tv_recycler_item_food_name);
            serving = itemView.findViewById(R.id.tv_recycler_item_serving);
            calories = itemView.findViewById(R.id.tv_recycler_item_calories);
        }

        public void bind(final FoodLogItemView food) {
            String servingTxt = food.getPortion_size() + food.getPortion_unit();
            String caloriesTxt = String.format(Locale.US, "%.2f Calories", food.getEnergy_kcal_100g());

            foodName.setText(food.getProduct_name());
            serving.setText(servingTxt);
            calories.setText(caloriesTxt);
        }
    }
}
