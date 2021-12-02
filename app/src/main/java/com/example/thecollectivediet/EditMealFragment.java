package com.example.thecollectivediet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thecollectivediet.JSON_Marshall_Objects.FoodResult;

import java.util.ArrayList;
import java.util.List;

public class EditMealFragment extends Fragment {

    private JSONSerializer serializer;
    private List<FoodResult> list;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_edit_meal, container, false);

        serializer = new JSONSerializer("EditMealsList.json", getActivity());

        try{
            list = serializer.load();
        } catch (Exception e){
            list = new ArrayList<FoodResult>();

        }

        recyclerView = v.findViewById(R.id.editRecycler);
        adapter = new RecyclerEditFood(getActivity(), list);
        return v;
    }
}
