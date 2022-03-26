package com.example.thecollectivediet.Me_Fragment_Components.Food_Logging_Editing;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.thecollectivediet.API_Utilities.FoodLog_API_Controller;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodNutrients;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodResult;
import com.example.thecollectivediet.JSON_Utilities.JSONSerializer;
import com.example.thecollectivediet.ModelViewUser;
import com.example.thecollectivediet.R;

import java.util.HashMap;

@SuppressLint("DefaultLocale")
public class FoodConfirmDialog extends Dialog {
    private MealSelectDialog.MealType mealType;
    Context ctx;
    FoodNutrients nutrients;
    FoodResult food;

    ImageView image;
    TextView foodName;
    EditText calorieVal;
    TextView proteinVal;
    TextView fatVal;
    TextView carbVal;

    Spinner servingUnitSpinner;
    Spinner mealTypeSpinner;
    EditText servingQtyVal;

    ModelViewUser modelViewUser;

    public FoodConfirmDialog(Context ctx, FoodNutrients nutrients, FoodResult food, MealSelectDialog.MealType mealType, ManualFoodSearch manualFoodSearch) {
        super(ctx);

        //Creates or gets existing view model to pass around the user data
        modelViewUser = new ViewModelProvider(manualFoodSearch).get(ModelViewUser.class);

        this.ctx = ctx;
        this.nutrients = nutrients;
        this.food = food;
        this.mealType = mealType;

        initializeComponents();
    }

    private void initializeComponents() {
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.setContentView(R.layout.confirm_food_add);
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        image = findViewById(R.id.confirm_add_img);
        calorieVal = findViewById(R.id.calories_txt_value);
        calorieVal.setEnabled(false);
        proteinVal = findViewById(R.id.protein_val);
        fatVal = findViewById(R.id.fat_val);
        carbVal = findViewById(R.id.carb_val);
        servingQtyVal = findViewById(R.id.serving_qty_val);
        foodName = findViewById(R.id.foodNameTxt);

        foodName.setText(food.getProduct_name());
        servingQtyVal.setText(String.valueOf(100));

        calorieVal.setText(String.format("%.1f", nutrients.getEnergy_kcal_100g()));
        proteinVal.setText(String.format("%.1f %s", nutrients.getProteins_100g(), nutrients.getProteins_unit()));
        carbVal.setText(String.format("%.1f %s", nutrients.getCarbohydrates_100g(), nutrients.getCarbohydrates_unit()));
        fatVal.setText(String.format("%.1f %s", nutrients.getFat_100g(), nutrients.getFat_unit()));

        setupServingSpinner();
        setupMealTypeSpinner();

        ServingCalculator servingCalculator = new ServingCalculator(nutrients, servingUnitSpinner, this);
        servingUnitSpinner.setOnItemSelectedListener(servingCalculator);
        servingQtyVal.addTextChangedListener(servingCalculator);

        setupFoodImage();

        this.findViewById(R.id.add_food_btn).setOnClickListener(v -> {
            FoodLog_API_Controller.pushFoodLogEntry(ctx, food, modelViewUser.getUser(), Float.parseFloat(servingQtyVal.getText().toString()), servingUnitSpinner.getSelectedItem().toString(), mealType.toString());
            JSONSerializer.addFoodToList(food.getProduct_name(), "100 grams", ctx);
            Glide.with(ctx).load("https://i2.wp.com/www.safetysuppliesunlimited.net/wp-content/uploads/2020/06/ISO473AP.jpg?fit=288%2C288&ssl=1").into(image);
            new CountDownTimer(500, 250) {

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    onStop();
                }
            }.start();
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        dismiss();
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
        servingUnitSpinner = this.findViewById(R.id.serving_unit_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ctx, R.array.serving_qty_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servingUnitSpinner.setAdapter(adapter);

        servingUnitSpinner.setOnItemSelectedListener(new ServingCalculator(nutrients, servingUnitSpinner, this));
    }

    private void setupMealTypeSpinner() {
        mealTypeSpinner = this.findViewById(R.id.meal_type_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ctx, R.array.meal_option_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mealTypeSpinner.setAdapter(adapter);

        mealTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            boolean firstTime = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (firstTime) {
                    position = mealType == null ? 0 : mealType.ordinal();
                    firstTime = false;
                }

                parent.setSelection(position);
                mealType = MealSelectDialog.MealType.values()[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void updateServing(Double multiplier) {
        String servTxt = servingQtyVal.getText().toString();

        double serving = servTxt.length() > 0 ? Double.parseDouble(servTxt) : 0;
        serving *= multiplier;
        servingQtyVal.setText(String.format("%.2f", serving));
    }

    private void updateFields(double newCals, double newFat, double newCarb, double newProtein) {
        calorieVal.setText(String.format("%.1f", newCals));
        fatVal.setText(String.format("%.1f %s", newFat, nutrients.getFat_unit()));
        carbVal.setText(String.format("%.1f %s", newCarb, nutrients.getCarbohydrates_unit()));
        proteinVal.setText(String.format("%.1f %s", newProtein, nutrients.getProteins_unit()));
    }


    @SuppressWarnings("UnnecessaryReturnStatement")
    static class ServingCalculator implements TextWatcher, AdapterView.OnItemSelectedListener {

        //1 cup == 128 g == 4.5oz
        //1 oz. == 28.35 g == 0.2215 cup
        //1 g. == 0.03527 oz == 0.00781 cup
        double caloriesPerGram;
        double proteinPerGram;
        double carbPerGram;
        double fatPerGram;
        boolean unitChange;
        String currentUnit;
        HashMap<String, Double> multipliers;
        FoodConfirmDialog view;

        public ServingCalculator(FoodNutrients nutrients, AdapterView<?> parent, FoodConfirmDialog view) {
            this.view = view;

            //helps us not recalculate fields during a unit change
            unitChange = false;
            caloriesPerGram = nutrients.getEnergy_kcal_100g() / 100;
            proteinPerGram = nutrients.getProteins_100g() / 100;
            carbPerGram = nutrients.getCarbohydrates_100g() / 100;
            fatPerGram = nutrients.getFat_100g() / 100;
            currentUnit = parent.getItemAtPosition(0).toString();

            multipliers = new HashMap<>();
            multipliers.put("g_to_oz", 0.03527396195);
            multipliers.put("g_to_cups", 0.0078125);
            multipliers.put("oz_to_g", 28.34952);
            multipliers.put("oz_to_cups", 0.221483942414);
            multipliers.put("cups_to_g", 128.0);
            multipliers.put("cups_to_oz", 4.515);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (unitChange || s.toString().equals("."))
                return;
            else if (s.toString().length() == 0)
                recalculateFields(0.0);
            else
                recalculateFields(Double.parseDouble(s.toString()));
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            unitChange = true;
            parent.getItemAtPosition(position);
            String newUnit = parent.getItemAtPosition(position).toString();
            if (!newUnit.equals(currentUnit))
                recalculateServing(newUnit);
            currentUnit = newUnit;
            unitChange = false;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            parent.getItemAtPosition(0);
        }

        private void recalculateFields(Double currentServing) throws IllegalArgumentException {
            Double multiplier = currentUnit.equals("g") ? (Double) 1.0 : multipliers.get(currentUnit + "_to_g");
            assert (multiplier != null);

            multiplier *= currentServing;

            double calories = caloriesPerGram * multiplier;
            double fat = fatPerGram * multiplier;
            double carb = carbPerGram * multiplier;
            double protein = proteinPerGram * multiplier;

            view.updateFields(calories, fat, carb, protein);
        }

        private void recalculateServing(String newUnit) {
            Double multiplier = multipliers.get(currentUnit + "_to_" + newUnit);
            view.updateServing(multiplier);
        }

    }
}
