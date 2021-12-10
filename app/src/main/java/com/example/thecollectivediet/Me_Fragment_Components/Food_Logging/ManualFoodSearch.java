package com.example.thecollectivediet.Me_Fragment_Components.Food_Logging;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.thecollectivediet.API_Utilities.FoodSearchController;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodNutrients;
import com.example.thecollectivediet.JSON_Utilities.JSONSerializer;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodResult;
import com.example.thecollectivediet.MainActivity;
import com.example.thecollectivediet.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ManualFoodSearch extends Fragment {

    static String savedText;
    EditText foodInput;
    Button searchBtn;
    Context ctx;
    FoodSearchController controller;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_food_search, container, false);

        initializeComponents(v);

        prefs = ctx.getSharedPreferences("TheCollectiveDiet", Context.MODE_PRIVATE);
        editor = prefs.edit();

        String food = prefs.getString("prediction", "null");

        if(!food.equals("null"))
        {
            editor.putString("prediction", "null");
            editor.commit();
            foodInput.setText(food);
            searchBtn.performClick();
        }
        else if (savedText != null) {
            foodInput.setText(savedText);
            searchBtn.performClick();
        }

        return v;
    }


    private void initializeComponents(View v) {
        ctx = this.getActivity();
        controller = new FoodSearchController(ctx);

        foodInput = v.findViewById(R.id.foodInput);
        searchBtn = v.findViewById(R.id.foodSearchButton);

        recyclerView = v.findViewById(R.id.foodResultRecycler);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);

        controller = new FoodSearchController(ctx);

        searchBtn.setOnClickListener(view -> controller.searchFoodByName(foodInput.getText().toString(), new FoodSearchController.VolleyResponseListener<List<FoodResult>>() {

            @Override
            public void onResponse(List<FoodResult> response) {
                savedText = foodInput.getText().toString();
                populateRecycler(response);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        }));
    }


    private void populateRecycler(List<FoodResult> response) {
        MainActivity.hideKeyboard(getActivity());

        mAdapter = new FoodSearchRecyclerViewAdapter(response, ctx, foodItem -> {

            controller.getNutrients(foodItem, new FoodSearchController.VolleyResponseListener<FoodNutrients>() {
                @Override
                public void onResponse(FoodNutrients response) {
                    setupLogAddDialog(response, foodItem.getPhotoURL());
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                }
            });
        });
        recyclerView.setAdapter(mAdapter);
    }

    private void setupLogAddDialog(FoodNutrients response, String photoURL) {

        Dialog dialog = new Dialog(ctx);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setContentView(R.layout.confirm_food_add);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        ((TextView) dialog.findViewById(R.id.foodNameTxt)).setText(String.format("%s, Serving Size: %s %s", response.getFood_name(), response.getServing_qty(), response.getServing_unit()));
        ((TextView) dialog.findViewById(R.id.foodCalTxt)).setText(String.format("%d Calories", Math.round(response.getNf_calories())));
        ((TextView) dialog.findViewById(R.id.foodFatTxt)).setText(String.format("Fat:                       %.2f g", response.getNf_total_fat()));
        ((TextView) dialog.findViewById(R.id.foodCarbTxt)).setText(String.format("Carbohydrates:    %.2f g", response.getNf_total_carbohydrate()));
        ((TextView) dialog.findViewById(R.id.foodProteinTxt)).setText(String.format("Protein:                %.2f g", response.getNf_protein()));

        // create a ProgressDrawable object which we will show as placeholder
        CircularProgressDrawable drawable = new CircularProgressDrawable(ctx);
        drawable.setColorSchemeColors(R.color.design_default_color_primary, R.color.design_default_color_primary_dark, R.color.teal_700);
        drawable.setCenterRadius(30f);
        drawable.setStrokeWidth(5f);
        // set all other properties as you would see fit and start it
        drawable.start();

        ImageView img = dialog.findViewById(R.id.confirmImage);
        Glide.with(ctx).load(photoURL).placeholder(drawable).into(img);

        dialog.findViewById(R.id.foodConfirmBtn).setOnClickListener(v -> {
            JSONSerializer.addFoodToList(response.getFood_name(), response.getServing_qty() + response.getServing_unit(), ctx);
            Glide.with(ctx).load("https://i2.wp.com/www.safetysuppliesunlimited.net/wp-content/uploads/2020/06/ISO473AP.jpg?fit=288%2C288&ssl=1").placeholder(drawable).into(img);
        });

        dialog.findViewById(R.id.cancelBtn).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}