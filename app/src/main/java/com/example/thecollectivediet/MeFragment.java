package com.example.thecollectivediet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;

public class MeFragment extends Fragment {

        TabLayout tabLayout;
        ViewPager2 pager2;
        FragmentAdapter adapter;


        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View v = inflater.inflate(R.layout.fragment_me, container, false);

            tabLayout = v.findViewById(R.id.tab_layout);
            pager2 = v.findViewById(R.id.view_pager2);

            FragmentManager fm = getActivity().getSupportFragmentManager();
            adapter = new FragmentAdapter(fm, getLifecycle());
            pager2.setAdapter(adapter);



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



