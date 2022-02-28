package com.example.thecollectivediet.Me_Fragment_Components.Food_Editing;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thecollectivediet.JSON_Marshall_Objects.EditFoodObject;
import com.example.thecollectivediet.JSON_Utilities.JSONSerializer;
import com.example.thecollectivediet.Me_Fragment_Components.Food_Logging.FoodLogDialog;
import com.example.thecollectivediet.Me_Fragment_Components.Food_Logging.ManualFoodSearch;
import com.example.thecollectivediet.R;

import java.util.ArrayList;
import java.util.List;

public class EditMealFragment extends Fragment implements View.OnClickListener {

    AppCompatButton mAddFoodButton;

    private RecyclerView mBreakfast;
    private RecyclerView mLunch;
    private RecyclerView mDinner;
    private RecyclerView mSnacks;

    RecyclerView.Adapter breakfastAdapter;
    ArrayList<VerticalModel> arrayListVertical;

    RecyclerEditFoodAdapter editFoodAdapter;
    Dialog foodLogDialog;

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

    public void inflateFoodSearchFrag(FoodLogDialog.MealType mealType) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        Fragment f = new ManualFoodSearch();
        Bundle args = new Bundle();
        args.putInt("mealType", mealType.ordinal());
        f.setArguments(args);
        transaction.replace(R.id.fragmentHolder, f);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_add_food) {
            foodLogDialog = new FoodLogDialog(getActivity(), this);
            foodLogDialog.show();
        }
    }
}


