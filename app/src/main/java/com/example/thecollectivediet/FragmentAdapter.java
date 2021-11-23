package com.example.thecollectivediet;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/*
    This class is used as the adapter in MeFragment. This allows users to view other fragments
    in a tab layout which can be navigated via the tabs or by swiping left or right.

 */
public class FragmentAdapter extends FragmentStateAdapter {
    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position)
        {
            case 1 :
                return new EditMealFragment();
           // case 2 :
             //   return new ThirdFragment();
        }

        return new TodayFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}