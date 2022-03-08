package com.example.thecollectivediet.Profile_Fragment_Components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.thecollectivediet.R;

public class FragmentProfileParent extends Fragment {
    ViewPager2 pager2;
    FragmentContainerView fcv;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_parent, container, false);

        fcv = v.findViewById(R.id.fragmentContainerView);

        //Setup FragmentAdapter and tabs in tab layout
        FragmentManager fm = this.getChildFragmentManager();

        return v;
    }
}
