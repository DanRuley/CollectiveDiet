package com.example.thecollectivediet.Me_Fragment_Components.Food_Logging;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thecollectivediet.API_Utilities.FoodSearchController;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodDetails;
import com.example.thecollectivediet.Me_Fragment_Components.Food_Logging.ManualFoodSearch;
import com.example.thecollectivediet.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FoodLogFragment extends Fragment {

    Button addFoodBtn;
    Button b;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    Context ctx;
    TextView res;
    FoodSearchController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_food_log, container, false);

        initializeComponents(v);


        return v;
    }

    private void initializeComponents(View v) {
        ctx = this.getActivity();

        addFoodBtn = v.findViewById(R.id.addFoodBtn);
        b = v.findViewById(R.id.button);

        recyclerView = v.findViewById(R.id.addFoodRecycler);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);

        res = v.findViewById(R.id.res);

        addFoodBtn.setOnClickListener(v1 -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentHolder, new ManualFoodSearch());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        controller = new FoodSearchController(ctx);
        b.setOnClickListener(view ->
                controller.getCommonNutrients("apple", new FoodSearchController.VolleyResponseListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            FileOutputStream fos = ctx.openFileOutput( "myfile.txt", Context.MODE_PRIVATE );
                            fos.write( response.getBytes() );
                            fos.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            res.setText(e.getMessage());

                        }


                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                    }
                }));
    }
}
