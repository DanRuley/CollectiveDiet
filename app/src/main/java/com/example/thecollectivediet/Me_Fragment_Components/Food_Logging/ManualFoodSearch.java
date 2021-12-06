package com.example.thecollectivediet.Me_Fragment_Components.Food_Logging;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thecollectivediet.API_Utilities.FoodSearchController;
import com.example.thecollectivediet.JSON_Utilities.JSONSerializer;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodResult;
import com.example.thecollectivediet.MainActivity;
import com.example.thecollectivediet.Me_Fragment_Components.Food_Logging.RecyclerViewAdapter;
import com.example.thecollectivediet.R;

import java.util.List;

public class ManualFoodSearch extends Fragment {

    static String savedText;
    EditText foodInput;
    Button searchBtn;
    Context ctx;
    FoodSearchController controller;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_food_search, container, false);

        initializeComponents(v);

        if (savedText != null) {
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

        mAdapter = new RecyclerViewAdapter(response, ctx, foodItem -> {

            controller.getCommonNutrients(foodItem.getFood_name(), new FoodSearchController.VolleyResponseListener<String>() {
                @Override
                public void onResponse(String response) {
                    Dialog dialog = new Dialog(ctx);

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    dialog.setContentView(R.layout.confirm_food_add);
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                    TextView f = dialog.findViewById(R.id.foodNameTxt);
                    f.setText(response);

                    dialog.findViewById(R.id.foodConfirmBtn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                             JSONSerializer.addFoodToList(foodItem.getFood_name(), foodItem.getServing_qty(), ctx);
                        }
                    });
                    dialog.findViewById(R.id.cancelBtn).setOnClickListener(v -> dialog.dismiss());

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