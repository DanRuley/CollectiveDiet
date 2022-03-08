package com.example.thecollectivediet.Me_Fragment_Components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.thecollectivediet.R;
import com.google.android.material.tabs.TabLayout;


/*
    This fragment will hold a tab layout for other fragments related to the "Me" part of the app.
    This class/fragment will use the class FragmentAdapter to allow users to navigate the tabs
    by swiping left or right.
 */
public class MeTabLayoutFragment extends Fragment {

    int pos = 0;

    //declare components (Buttons, Views, Text, etc...)
    TabLayout tabLayout;
    ViewPager2 pager2;
    MeFragmentAdapter adapter;

    public MeTabLayoutFragment() {

    }

    public MeTabLayoutFragment(int pos) {
        this.pos = pos;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflate the view
        View v = inflater.inflate(R.layout.fragment_me_tab_layout, container, false);

        //Initialize components
        tabLayout = v.findViewById(R.id.tab_layout);
        pager2 = v.findViewById(R.id.view_pager2);

        //Setup FragmentAdapter and tabs in tab layout
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        adapter = new MeFragmentAdapter(fm, getLifecycle());
        pager2.setAdapter(adapter);

        //if returning from EditProfileFragment, go to ProfileFragment in tab
        // layout
        if (pos != 0) {
            //Set tab layout to ProfileFragment
            pager2.setCurrentItem(pos);
            //Set scroll bar to proper position
            tabLayout.setScrollPosition(pos, 0, true);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
        return v;
    }
}



