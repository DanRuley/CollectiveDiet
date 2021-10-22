package com.example.thecollectivediet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class Stats_fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedStateInstance){
        View v = inflater.inflate(R.layout.fragment_stats, container, false);

        return v;
    }
}
