package com.example.thecollectivediet.Share;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thecollectivediet.API_Utilities.User_API_Controller;
import com.example.thecollectivediet.API_Utilities.VolleyResponseListener;
import com.example.thecollectivediet.JSON_Marshall_Objects.UserPostUploadItem;
import com.example.thecollectivediet.MainActivity;
import com.example.thecollectivediet.R;
import com.example.thecollectivediet.UserPostFragment;

public class SharedFragment extends Fragment {

    //elements
    LinearLayoutManager linearLayoutManager;
    ShareRecyclerAdapter adapter;
    RecyclerView recyclerView;
    Button makePost;
    AppCompatImageButton refresh;

    //variables
    UserPostUploadItem[] dataset;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shared, container, false);

        makePost = v.findViewById(R.id.shared_make_post_btn);
        makePost.setOnClickListener(v1 -> {
            MainActivity.commitFragmentTransaction(requireActivity(), R.id.fragmentHolder, new UserPostFragment());
        });

        refresh = v.findViewById(R.id.shared_refresh_btn);
        refresh.setOnClickListener(v1 -> MainActivity.commitFragmentTransaction(requireActivity(), R.id.fragmentHolder, new SharedFragment()));

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