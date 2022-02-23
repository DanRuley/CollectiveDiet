package com.example.thecollectivediet.Me_Fragment_Components.Food_Logging;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.thecollectivediet.API_Utilities.FoodSearchController;
import com.example.thecollectivediet.FragmentSignIn;
import com.example.thecollectivediet.MainActivity;
import com.example.thecollectivediet.R;

import java.util.Objects;

public class FoodLogFragment extends Fragment implements View.OnClickListener {

    Context ctx;
    FoodSearchController controller;

    public enum MealType {
        Breakfast,
        Lunch,
        Dinner,
        Snack
    }

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
        MealType mealType;

        if (MainActivity.getCurrentUser() == null) {
            Toast.makeText(ctx, "Please sign in before logging meals", Toast.LENGTH_SHORT).show();
            FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            FragmentSignIn frag = new FragmentSignIn();
            transaction.replace(R.id.fragmentHolder, frag);
            transaction.addToBackStack(null);
            transaction.commit();

            return;
        }

        if (id == R.id.breakfast_add_btn)
            mealType = MealType.Breakfast;
        else if (id == R.id.lunch_add_btn)
            mealType = MealType.Lunch;
        else if (id == R.id.dinner_add_btn)
            mealType = MealType.Dinner;
        else
            mealType = MealType.Snack;

        inflateFoodSearchFrag(mealType);
    }

    private void inflateFoodSearchFrag(MealType mealType) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        Fragment f = new ManualFoodSearch();
        Bundle args = new Bundle();
        args.putInt("mealType", mealType.ordinal());
        f.setArguments(args);
        transaction.replace(R.id.fragmentHolder, f);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
