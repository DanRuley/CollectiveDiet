package com.example.thecollectivediet.Me_Fragment_Pieces;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.thecollectivediet.R;

public class TodayFragment extends Fragment {

    Button manualEntry;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedStateInstance) {
        View v = inflater.inflate(R.layout.fragment_today, container, false);

        manualEntry = v.findViewById(R.id.logFood);

        manualEntry.setOnClickListener(v1 -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentHolder, new FoodLogFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return v;
    }
}
