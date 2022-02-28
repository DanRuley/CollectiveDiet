package com.example.thecollectivediet.Me_Fragment_Components.Food_Logging_Editing;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thecollectivediet.JSON_Marshall_Objects.EditFoodObject;
import com.example.thecollectivediet.JSON_Utilities.JSONSerializer;
import com.example.thecollectivediet.R;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentFoodLog extends Fragment implements View.OnClickListener {

    AppCompatButton mAddFoodButton;
    AppCompatButton mDatePickButton;
    TextView showDateTxt;

    private RecyclerView mBreakfast;
    private RecyclerView mLunch;
    private RecyclerView mDinner;
    private RecyclerView mSnacks;

    RecyclerView.Adapter breakfastAdapter;
    ArrayList<OuterMealRecyclerItem> arrayListVertical;

    OuterMealListRecycler editFoodAdapter;
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

        mDatePickButton = v.findViewById(R.id.btn_date_picker);
        mDatePickButton.setOnClickListener(this);

        showDateTxt = v.findViewById(R.id.show_selected_date);

        //hook recycler views and adapters
        mBreakfast = v.findViewById(R.id.rv_doh);
        mBreakfast.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        //mBreakfast.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        editFoodAdapter = new OuterMealListRecycler(getActivity(), arrayListVertical);

        mBreakfast.setAdapter(editFoodAdapter);

        //doesn't make sense to let them pick future dates
        CalendarConstraints dateConstraint = new CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now()).build();
        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker().setCalendarConstraints(dateConstraint);
        materialDateBuilder.setTitleText("Select Date to view food log");

        final MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();

        // handle select date button which opens the
        // material design date picker
        mDatePickButton.setOnClickListener(
                v1 -> materialDatePicker.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "MATERIAL_DATE_PICKER"));

        materialDatePicker.addOnPositiveButtonClickListener(
                selection -> {
                    showDateTxt.setText("Selected Date is: " + materialDatePicker.getHeaderText());
                });

        //set data for recycler views
        setData();


        return v;
    }

    private void setData() {

        //Vertical recycler views
        OuterMealRecyclerItem outerMealRecyclerItemBreakfast = new OuterMealRecyclerItem();
        outerMealRecyclerItemBreakfast.setTitle("Breakfast:");
        ArrayList<InnerFoodListItem> arrayListHorizontal1 = new ArrayList<>();
        outerMealRecyclerItemBreakfast.setArrayList(arrayListHorizontal1);
        arrayListVertical.add(outerMealRecyclerItemBreakfast);

        //lunch
        OuterMealRecyclerItem outerMealRecyclerItemLunch = new OuterMealRecyclerItem();
        outerMealRecyclerItemLunch.setTitle("Lunch:");
        ArrayList<InnerFoodListItem> arrayListHorizontal2 = new ArrayList<>();
        outerMealRecyclerItemLunch.setArrayList(arrayListHorizontal2);
        arrayListVertical.add(outerMealRecyclerItemLunch);

        //Dinner
        OuterMealRecyclerItem outerMealRecyclerItemDinner = new OuterMealRecyclerItem();
        outerMealRecyclerItemDinner.setTitle("Dinner:");
        ArrayList<InnerFoodListItem> arrayListHorizontal3 = new ArrayList<>();
        outerMealRecyclerItemDinner.setArrayList(arrayListHorizontal3);
        arrayListVertical.add(outerMealRecyclerItemDinner);

        //snacks
        OuterMealRecyclerItem outerMealRecyclerItemSnacks = new OuterMealRecyclerItem();
        outerMealRecyclerItemSnacks.setTitle("Snacks:");
        ArrayList<InnerFoodListItem> arrayListHorizontal4 = new ArrayList<>();
        outerMealRecyclerItemSnacks.setArrayList(arrayListHorizontal4);
        arrayListVertical.add(outerMealRecyclerItemSnacks);

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

    public void inflateFoodSearchFrag(MealSelectDialog.MealType mealType) {
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
        if (v.getId() == R.id.btn_add_food) {
            foodLogDialog = new MealSelectDialog(getActivity(), this);
            foodLogDialog.show();
        }
    }
}


