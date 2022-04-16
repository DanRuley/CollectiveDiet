package com.example.thecollectivediet.Profile_Fragment_Components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.example.thecollectivediet.R;

/**
 * Controls the layout the shows the user their profile.
 */
public class FragmentProfileParent extends Fragment {
    FragmentContainerView fcv;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_parent, container, false);

        fcv = v.findViewById(R.id.fragmentContainerView);

        return v;
    }
}
