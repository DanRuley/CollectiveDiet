package com.example.thecollectivediet;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    List<FoodResult> foodResults;
    Context ctx;

    public RecyclerViewAdapter(List<FoodResult> foodResults, Context _ctx) {


        this.foodResults = foodResults;
        this.ctx = _ctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //inflater parameters: the layout you want to inflate, the parent, and attach to root T/F
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_recycler_element, parent, false);
        MyViewHolder holder = new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // create a ProgressDrawable object which we will show as placeholder
        CircularProgressDrawable drawable = new CircularProgressDrawable(this.ctx);
        drawable.setColorSchemeColors(R.color.design_default_color_primary, R.color.design_default_color_primary_dark, R.color.teal_700);
        drawable.setCenterRadius(30f);
        drawable.setStrokeWidth(5f);
        // set all other properties as you would see fit and start it
        drawable.start();

        holder.foodName.setText(foodResults.get(position).getFood_name());
        holder.foodServing.setText(foodResults.get(position).getServing_qty() + " " + foodResults.get(position).getServing_unit());

        Glide.with(this.ctx).load(foodResults.get(position).getPhotoURL()).placeholder(drawable).into(holder.foodPicture);
    }

    @Override
    public int getItemCount() {

        return foodResults.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView foodPicture;
        TextView foodName;
        TextView foodServing;
        ImageButton addButton;

        ArrayList<FoodResult> list;
        JSONSerializer serializer;
        JSONObject jo;
        FoodResult fr;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            foodPicture = itemView.findViewById(R.id.foodRecImage);
            foodName = itemView.findViewById(R.id.foodRecName);
            foodServing = itemView.findViewById(R.id.foodRecServing);
            addButton = itemView.findViewById(R.id.addFoodIcon);

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    serializer = new JSONSerializer("EditMealsList.json", ctx);



                    try {
                        list = serializer.load();

                        if (list.size() > 31) {

                        } else {
                            fr = new FoodResult(foodName.getText().toString(), "ass", "ass", foodServing.getText().toString());
                            list.add(fr);
                            serializer.save(list);
                        }

                    } catch (Exception e) {
                        list = new ArrayList<FoodResult>();
                    }
                }
            });
        }



    }
}
