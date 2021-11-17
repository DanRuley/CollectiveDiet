package com.example.thecollectivediet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TableLayout;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

public class MeFragment extends Fragment {

    TabLayout tableLayout;
    FrameLayout frameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedStateInstance){
        View v = inflater.inflate(R.layout.fragment_me, container, false);

        tableLayout = v.findViewById(R.id.tabLayout);
        //frameLayout = v.findViewById(R.id.include);
        // perform setOnTabSelectedListener event on TabLayout

        return v;
    }
}
