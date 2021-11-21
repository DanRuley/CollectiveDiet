package com.example.thecollectivediet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.thecollectivediet.API_Utilities.FoodSearchController;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodResult;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class MeFragment extends Fragment {

    Button manualEntry;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedStateInstance) {
        View v = inflater.inflate(R.layout.fragment_me, container, false);

        manualEntry = v.findViewById(R.id.manualEntry);

        manualEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentHolder, new ManualFoodSearch());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return v;
    }
}
