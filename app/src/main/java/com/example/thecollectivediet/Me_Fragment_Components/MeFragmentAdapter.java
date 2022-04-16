package com.example.thecollectivediet.Me_Fragment_Components;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.thecollectivediet.Goals_Fragment_Components.FragmentGoals;
import com.example.thecollectivediet.Me_Fragment_Components.Food_Logging_Editing.FragmentFoodLog;


/**
 * This class is used as the adapter in MeFragment. This allows users to view other fragments and
 * navigate by swiping left or right.
 */
public class MeFragmentAdapter extends FragmentStateAdapter {
    public MeFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    /**
     * Create the correct fragment given the position in the tab list.
     *
     * @param position - which tab is selected?
     * @return relevant fragment
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0: {
                return new TodayFragment();
            }
            case 1: {
                return new FragmentFoodLog();
            }
            case 2: {
                return new FragmentGoals();
            }
        }

        return new TodayFragment();
    }

    /**
     * How many tabs are there?
     */
    @Override
    public int getItemCount() {
        return 3;
    }
}