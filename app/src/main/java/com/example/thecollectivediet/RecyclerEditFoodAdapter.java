package com.example.thecollectivediet;

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

import java.util.List;

public class RecyclerEditFoodAdapter extends RecyclerView.Adapter<RecyclerEditFoodAdapter.EditViewHolder> {

    private List<FoodResult> foodResultList;
    private Context context;

    public RecyclerEditFoodAdapter(Context context, List<FoodResult> foodResults){

        //todo probably need to deserialize list here instead of using foodResults **********************
        this.context = context;
        this.foodResultList = foodResults;
    }

    @NonNull
    @Override
    public RecyclerEditFoodAdapter.EditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.edit_food_recycler_element, parent, false);

        return new EditViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerEditFoodAdapter.EditViewHolder holder, int position) {

        // create a ProgressDrawable object which we will show as placeholder
        CircularProgressDrawable drawable = new CircularProgressDrawable(this.context);
        drawable.setColorSchemeColors(R.color.design_default_color_primary, R.color.design_default_color_primary_dark, R.color.teal_700);
        drawable.setCenterRadius(30f);
        drawable.setStrokeWidth(5f);

        holder.editFoodName.setText(foodResultList.get(position).getFood_name());
        holder.editFoodServing.setText(foodResultList.get(position).getServing_qty() + " " + foodResultList.get(position).getServing_unit());

        //Glide.with(this.context).load(foodResultList.get(position).getPhotoURL()).placeholder(drawable).into(holder.editFoodPicture);
    }

    @Override
    public int getItemCount() {
        return foodResultList.size();
    }

    public class EditViewHolder extends RecyclerView.ViewHolder {

        ImageView editFoodPicture;
        TextView editFoodName;
        TextView editFoodServing;
        public EditViewHolder(View view){

            super(view);

            editFoodPicture = itemView.findViewById(R.id.editRecImage);
            editFoodName = itemView.findViewById(R.id.editRecName);
            editFoodServing = itemView.findViewById(R.id.editRecServing);
        }
    }
}
