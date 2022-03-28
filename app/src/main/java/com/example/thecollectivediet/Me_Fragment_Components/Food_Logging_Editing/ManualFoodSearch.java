package com.example.thecollectivediet.Me_Fragment_Components.Food_Logging_Editing;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thecollectivediet.API_Utilities.FoodSearchController;
import com.example.thecollectivediet.API_Utilities.VolleyResponseListener;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodNutrients;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodResult;
import com.example.thecollectivediet.MainActivity;
import com.example.thecollectivediet.R;

import java.util.List;

public class ManualFoodSearch extends Fragment {

    static String savedText;
    MealSelectDialog.MealType mealType;
    boolean mealTypePrompt;
    EditText foodInput;
    Button searchBtn;
    @Nullable
    Context ctx;
    @Nullable
    FoodSearchController controller;
    RecyclerView recyclerView;
    @Nullable
    FoodSearchRecyclerViewAdapter mAdapter;
    @Nullable
    RecyclerView.LayoutManager layoutManager;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    ManualFoodSearch manualFoodSearch;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_food_search, container, false);

        manualFoodSearch = this;

        Bundle args = getArguments();

        if (args != null) {
            mealType = MealSelectDialog.MealType.values()[args.getInt("mealType")];
            mealTypePrompt = false;     //meal type was provided by the button the user clicked - no need to prompt for meal type.
        } else
            mealTypePrompt = true;      //If search is called from Camera, we need to ask user for meal type

        initializeComponents(v);

        prefs = ctx.getSharedPreferences("TheCollectiveDiet", Context.MODE_PRIVATE);
        editor = prefs.edit();

        String food = prefs.getString("prediction", "null");

        if (!food.equals("null")) {
            editor.putString("prediction", "null");
            editor.commit();
            foodInput.setText(food);
            searchBtn.performClick();
        } else if (savedText != null) {
            foodInput.setText(savedText);
            searchBtn.performClick();
        }

        return v;
    }

    private void initializeComponents(@NonNull View v) {
        ctx = this.getActivity();
        controller = new FoodSearchController(ctx);

        foodInput = v.findViewById(R.id.foodInput);
        searchBtn = v.findViewById(R.id.foodSearchButton);

        recyclerView = v.findViewById(R.id.foodResultRecycler);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);

        controller = new FoodSearchController(ctx);

        searchBtn.setOnClickListener(view -> controller.searchFoodByName(foodInput.getText().toString(), new VolleyResponseListener<List<FoodResult>>() {

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
        MainActivity.hideKeyboard(requireActivity());

        mAdapter = new FoodSearchRecyclerViewAdapter(response, ctx, foodItem -> {

            controller.getNutrients(String.valueOf(foodItem.getId()), new VolleyResponseListener<FoodNutrients>() {
                @Override
                public void onResponse(FoodNutrients nutrients) {
                    FoodConfirmDialog dialog = new FoodConfirmDialog(ctx, nutrients, foodItem, mealType, manualFoodSearch);
                    dialog.show();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                }
            });
        });
        recyclerView.setAdapter(mAdapter);
    }
}