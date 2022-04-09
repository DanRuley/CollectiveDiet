package com.example.thecollectivediet.Share;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.thecollectivediet.R;

public class SharedFragment extends Fragment {

    //elements
    LinearLayoutManager linearLayoutManager;
    ShareRecyclerAdapter adapter;
    RecyclerView recyclerView;

    //variables
    String[] dataset;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shared, container, false);

        dataset = new String[]{"ass","butt", "cat"};
        //hook elements
        recyclerView = v.findViewById(R.id.rv_shared_post);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ShareRecyclerAdapter(dataset);
        recyclerView.setAdapter(adapter);



        return v;
    }
}