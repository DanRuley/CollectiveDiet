package com.example.thecollectivediet.Me_Fragment_Components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.thecollectivediet.Me_Fragment_Components.Food_Logging.FoodLogFragment;
import com.example.thecollectivediet.Me_Fragment_Components.Food_Logging.ManualFoodSearch;
import com.example.thecollectivediet.R;
import com.hsalf.smileyrating.SmileyRating;

public class TodayFragment extends Fragment {

    Button manualEntry;
    SmileyRating smileyRating;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedStateInstance) {
        View v = inflater.inflate(R.layout.fragment_today, container, false);

        manualEntry = v.findViewById(R.id.logFood);

        manualEntry.setOnClickListener(v1 -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentHolder, new ManualFoodSearch());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        smileyRating = v.findViewById(R.id.smile_rating);
        smileyRating.setSmileySelectedListener(new SmileyRating.OnSmileySelectedListener() {
            @Override
            public void onSmileySelected(SmileyRating.Type type) {
                // You can compare it with rating Type
                if (SmileyRating.Type.GREAT == type) {
                    //Log.i(TAG, "Wow, the user gave high rating");
                }
                // You can get the user rating too
                // rating will between 1 to 5
                // rating will between 1 to 5, but -1 is none selected
                int rating = type.getRating();


                //todo
                //send rating out to sergio
            }
        });

        return v;
    }
}
