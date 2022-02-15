package com.example.thecollectivediet.Me_Fragment_Components.Food_Logging;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodNutrients;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodResult;
import com.example.thecollectivediet.JSON_Utilities.JSONSerializer;
import com.example.thecollectivediet.R;

public class FoodConfirmDialog extends Dialog {
    Context ctx;
    FoodNutrients nutrients;
    FoodResult food;

    ImageView image;
    TextView calorieVal;
    TextView proteinVal;
    TextView fatVal;
    TextView carbVal;

    Spinner servingUnit;
    TextView servingQtyVal;

    public FoodConfirmDialog(Context ctx, FoodNutrients nutrients, FoodResult food) {
        super(ctx);

        this.ctx = ctx;
        this.nutrients = nutrients;
        this.food = food;

        initializeComponents();
    }

    private void initializeComponents() {
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.setContentView(R.layout.confirm_food_add);
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        image = findViewById(R.id.confirm_add_img);
        calorieVal = findViewById(R.id.calories_txt_value);
        proteinVal = findViewById(R.id.protein_val);
        fatVal = findViewById(R.id.fat_val);
        carbVal = findViewById(R.id.carb_val);
        servingQtyVal = findViewById(R.id.serving_sz_txt);

        setupServingSpinner();
        setupFoodImage();

        this.findViewById(R.id.add_food_btn).setOnClickListener(v -> {
            JSONSerializer.addFoodToList(food.getProduct_name(), "100 grams", ctx);
            Glide.with(ctx).load("https://i2.wp.com/www.safetysuppliesunlimited.net/wp-content/uploads/2020/06/ISO473AP.jpg?fit=288%2C288&ssl=1").into(image);
        });
    }

    private void setupFoodImage() {
        // create a ProgressDrawable object which we will show as placeholder
        CircularProgressDrawable drawable = new CircularProgressDrawable(ctx);
        drawable.setColorSchemeColors(R.color.design_default_color_primary, R.color.design_default_color_primary_dark, R.color.teal_700);
        drawable.setCenterRadius(30f);
        drawable.setStrokeWidth(5f);
        // set all other properties as you would see fit and start it
        drawable.start();

        Glide.with(ctx).load(food.getImage_url() == null ? "https://d2eawub7utcl6.cloudfront.net/images/nix-apple-grey.png" : food.getImage_url()).placeholder(drawable).into(image);
    }

    private void setupServingSpinner() {
        servingUnit = this.findViewById(R.id.serving_unit_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ctx, R.array.serving_qty_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servingUnit.setAdapter(adapter);

        servingUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.getItemAtPosition(0);
            }
        });
    }
}
