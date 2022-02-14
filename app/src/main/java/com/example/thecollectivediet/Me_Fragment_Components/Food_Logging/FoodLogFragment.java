package com.example.thecollectivediet.Me_Fragment_Components.Food_Logging;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.thecollectivediet.API_Utilities.FoodSearchController;
import com.example.thecollectivediet.R;

public class FoodLogFragment extends Fragment implements View.OnClickListener {

    Context ctx;
    FoodSearchController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_food_log, container, false);

        initializeComponents(v);

        return v;
    }

    private void initializeComponents(View v) {
        ctx = this.getActivity();

        v.findViewById(R.id.breakfast_add_btn).setOnClickListener(this);
        v.findViewById(R.id.lunch_add_btn).setOnClickListener(this);
        v.findViewById(R.id.dinner_add_btn).setOnClickListener(this);
        v.findViewById(R.id.snack_add_btn).setOnClickListener(this);

        controller = new FoodSearchController(ctx);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        String mealType = "";

        if (id == R.id.breakfast_add_btn)
            mealType = "breakfast";
        else if (id == R.id.lunch_add_btn)
            mealType = "lunch";
        else if (id == R.id.dinner_add_btn)
            mealType = "dinner";
        else if (id == R.id.snack_add_btn)
            mealType = "snack";

        inflateFoodSearchFrag(mealType);
    }

    private void inflateFoodSearchFrag(String mealType) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        Fragment f = new ManualFoodSearch();
        Bundle args = new Bundle();
        args.putString("mealType", mealType);
        transaction.replace(R.id.fragmentHolder, new ManualFoodSearch());
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
