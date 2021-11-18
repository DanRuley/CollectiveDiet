package com.example.thecollectivediet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

public class MeFragment extends Fragment {

    TabLayout tabLayout;
    FrameLayout frameLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedStateInstance){
        View v = inflater.inflate(R.layout.fragment_me, container, false);

        tabLayout = v.findViewById(R.id.tabLayout);
        //frameLayout = v.findViewById(R.id.include);
        // perform setOnTabSelectedListener event on TabLayout
        CameraFragment fragment = new CameraFragment();
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();

        fm.replace(R.id.simpleFrameLayout, fragment);
        fm.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fm.commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
// called when tab selected
                //Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        CameraFragment fragment = new CameraFragment();
                        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();

                        fm.replace(R.id.simpleFrameLayout, fragment);
                        fm.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fm.commit();
                        break;
//                    case 1:
//                        fragment = new SecondFragment();
//                        break;
//                    case 2:
//                        fragment = new ThirdFragment();
//                        break;
                }
               // FragmentManager fm = getSupportFragmentManager();
//                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
//                //FragmentTransaction ft = fm.beginTransaction();
//                fm.replace(R.id.simpleFrameLayout, fragment);
//                fm.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                fm.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
// called when tab unselected
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
// called when a tab is reselected
            }
        });
        return v;
    }
}
