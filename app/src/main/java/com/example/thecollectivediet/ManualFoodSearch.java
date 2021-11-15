package com.example.thecollectivediet;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thecollectivediet.API_Utilities.FoodSearchController;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodResult;

import java.util.List;

public class FoodSearch extends Fragment {

    EditText foodInput;
    Button searchBtn;
    Context ctx;
    FoodSearchController controller;

    public FoodSearch() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_food_search, container, false);

        initializeComponents(v);

        return v;
    }

    private void initializeComponents(View v) {
        ctx = this.getActivity();
        controller = new FoodSearchController(ctx);

        foodInput = v.findViewById(R.id.foodInput);
        searchBtn = v.findViewById(R.id.foodSearchButton);

        controller = new FoodSearchController(ctx);

        searchBtn.setOnClickListener(view -> controller.searchFoodByName(foodInput.getText().toString(), new FoodSearchController.VolleyResponseListener<List<FoodResult>>() {

            @Override
            public void onResponse(List<FoodResult> response) {
                Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        }));
    }
}