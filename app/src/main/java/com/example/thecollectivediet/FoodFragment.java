package com.example.thecollectivediet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FoodFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_food, container, false);

        //Initialize button to switch to camera
        Button takePic_button = v.findViewById(R.id.take_pic_button);
        //listener for takePic_button
        takePic_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Camera_Fragment camera_fragment = new Camera_Fragment();
                transaction.replace(R.id.fragmentHolder,camera_fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return v;
    }
}
