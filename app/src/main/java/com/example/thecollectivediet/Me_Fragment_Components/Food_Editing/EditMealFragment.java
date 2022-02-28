package com.example.thecollectivediet.Me_Fragment_Components.Food_Editing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thecollectivediet.JSON_Marshall_Objects.EditFoodObject;
import com.example.thecollectivediet.JSON_Utilities.JSONSerializer;

import java.util.ArrayList;
import java.util.List;

import com.example.thecollectivediet.Me_Fragment_Components.Food_Logging.FoodLogFragment;
import com.example.thecollectivediet.R;

import org.checkerframework.checker.units.qual.A;

public class EditMealFragment extends Fragment implements View.OnClickListener {

    AppCompatButton mAddFoodButton;

    private RecyclerView mBreakfast;
    private RecyclerView mLunch;
    private RecyclerView mDinner;
    private RecyclerView mSnacks;

    RecyclerView.Adapter breakfastAdapter;
    ArrayList<VerticalModel> arrayListVertical;

    RecyclerEditFoodAdapter editFoodAdapter;

    private JSONSerializer serializer;
    private List<EditFoodObject> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedStateInstance) {

        View v = inflater.inflate(R.layout.fragment_edit_meal, container, false);

        arrayListVertical = new ArrayList<>();

        //hook elements
        mAddFoodButton = v.findViewById(R.id.btn_add_food);
        mAddFoodButton.setOnClickListener(this);

        //hook recycler views and adapters
        mBreakfast = v.findViewById(R.id.rv_doh);
        mBreakfast.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        //mBreakfast.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        editFoodAdapter = new RecyclerEditFoodAdapter(getActivity(), arrayListVertical);

        mBreakfast.setAdapter(editFoodAdapter);

        //set data for recycler views
        setData();


        return v;
    }

    private void setData() {

        //Vertical recycler views
        VerticalModel verticalModelBreakfast = new VerticalModel();
        verticalModelBreakfast.setTitle("Breakfast:");
        ArrayList<HorizontalModel> arrayListHorizontal1 = new ArrayList<>();
        verticalModelBreakfast.setArrayList(arrayListHorizontal1);
        arrayListVertical.add(verticalModelBreakfast);

        //lunch
        VerticalModel verticalModelLunch = new VerticalModel();
        verticalModelLunch.setTitle("Lunch:");
        ArrayList<HorizontalModel> arrayListHorizontal2 = new ArrayList<>();
        verticalModelLunch.setArrayList(arrayListHorizontal2);
        arrayListVertical.add(verticalModelLunch);

        //Dinner
        VerticalModel verticalModelDinner = new VerticalModel();
        verticalModelDinner.setTitle("Dinner:");
        ArrayList<HorizontalModel> arrayListHorizontal3 = new ArrayList<>();
        verticalModelDinner.setArrayList(arrayListHorizontal3);
        arrayListVertical.add(verticalModelDinner);

        //snacks
        VerticalModel verticalModelSnacks = new VerticalModel();
        verticalModelSnacks.setTitle("Snacks:");
        ArrayList<HorizontalModel> arrayListHorizontal4 = new ArrayList<>();
        verticalModelSnacks.setArrayList(arrayListHorizontal4);
        arrayListVertical.add(verticalModelSnacks);

//        HorizontalModel ho = new HorizontalModel();
//        ho.setDescription("Des");
//        ho.setName("ass");
//        arrayListHorizontal1.add(ho);



//        for(int i=1; i <= 5; i++){
//            VerticalModel verticalModel = new VerticalModel();
//            verticalModel.setTitle("Title: "+ i);
//            ArrayList<HorizontalModel> arrayListHorizontal = new ArrayList<>();
//
//            for(int j = 0; j <= 5; j++){
//                HorizontalModel horizontalModel = new HorizontalModel();
//                horizontalModel.setDescription("Description: " + j);
//                horizontalModel.setName("Name: " + j);
//
//                arrayListHorizontal.add(horizontalModel);
//            }
//
//            verticalModel.setArrayList(arrayListHorizontal);
//            arrayListVertical.add(verticalModel);
//        }

        editFoodAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btn_add_food: {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentHolder, new FoodLogFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            }
        }
    }
}


