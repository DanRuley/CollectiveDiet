package com.example.thecollectivediet.Me_Fragment_Components;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.thecollectivediet.Me_Fragment_Components.Food_Editing.EditMealFragment;

/*
    This class is used as the adapter in MeFragment. This allows users to view other fragments
    in a tab layout which can be navigated via the tabs or by swiping left or right.

 */
public class MeFragmentAdapter extends FragmentStateAdapter {
    public MeFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position)
        {
            case 1 :
                return new com.example.thecollectivediet.Me_Fragment_Components.Food_Editing.EditMealFragment();
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