package com.example.thecollectivediet.Me_Fragment_Components;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.thecollectivediet.Me_Fragment_Components.Food_Logging.FoodLogFragment;
import com.example.thecollectivediet.Me_Fragment_Components.Food_Logging.ManualFoodSearch;
import com.example.thecollectivediet.R;
import com.hsalf.smileyrating.SmileyRating;

import org.w3c.dom.Text;

public class TodayFragment extends Fragment implements View.OnClickListener {

    Button manualEntry;
    //SmileyRating smileyRating;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

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




        return v;
    }

    @Override
    public void onClick(View v) {

    }
}
