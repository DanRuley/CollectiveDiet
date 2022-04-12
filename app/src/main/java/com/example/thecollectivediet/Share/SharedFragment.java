package com.example.thecollectivediet.Share;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amplifyframework.core.Amplify;
import com.example.thecollectivediet.API_Utilities.User_API_Controller;
import com.example.thecollectivediet.API_Utilities.VolleyResponseListener;
import com.example.thecollectivediet.JSON_Marshall_Objects.UserPostUploadItem;
import com.example.thecollectivediet.MainActivity;
import com.example.thecollectivediet.R;

import java.io.File;

public class SharedFragment extends Fragment {

    //elements
    LinearLayoutManager linearLayoutManager;
    ShareRecyclerAdapter adapter;
    RecyclerView recyclerView;

    //variables
    UserPostUploadItem[] dataset;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shared, container, false);

       // InitializeDataset(View v);
        //dataset = new String[]{"ass","butt", "cat"};

        //hook elements
       InitializeDataset(v);



        return v;
    }

    private void InitializeDataset(View v){

        User_API_Controller.getPosts(getContext(), new VolleyResponseListener<UserPostUploadItem[]>() {
            @Override
            public void onResponse(UserPostUploadItem[] response) {
                dataset = response;
                recyclerView = v.findViewById(R.id.rv_shared_post);
                linearLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter = new ShareRecyclerAdapter(dataset, getContext(), (MainActivity) requireActivity());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}