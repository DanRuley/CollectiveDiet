package com.example.thecollectivediet.Me_Fragment_Components.Food_Editing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thecollectivediet.JSON_Marshall_Objects.EditFoodObject;
import com.example.thecollectivediet.JSON_Utilities.JSONSerializer;

import java.util.List;

import com.example.thecollectivediet.R;

public class EditMealFragment extends Fragment {

    private JSONSerializer serializer;
    private List<EditFoodObject> list;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_edit_meal, container, false);

        serializer = new JSONSerializer("EditMealsList.json", getActivity());

        try{
            list = serializer.load();
        } catch (Exception e){
            //list = new ArrayList<FoodResult>();

        }

        recyclerView = v.findViewById(R.id.editResultRecycler);
        adapter = new RecyclerEditFoodAdapter(getActivity(), list);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());///////////??????????

        recyclerView.setAdapter(adapter);
        return v;
    }
}
