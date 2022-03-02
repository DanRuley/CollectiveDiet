package com.example.thecollectivediet.Me_Fragment_Components.Food_Logging_Editing;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.thecollectivediet.API_Utilities.FoodLog_API_Controller;
import com.example.thecollectivediet.API_Utilities.VolleyResponseListener;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodLogItemView;
import com.example.thecollectivediet.MainActivity;
import com.example.thecollectivediet.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class FragmentFoodLog extends Fragment implements View.OnClickListener {

    AppCompatButton mAddFoodButton;
    AppCompatButton mDatePickButton;
    TextView showDateTxt;

    int selectedYear;
    int selectedMonth;
    int selectedDay;

    ArrayList<OuterMealRecyclerItem> arrayListVertical;

    OuterMealListRecycler editFoodAdapter;
    Dialog foodLogDialog;
    DatePickerDialog datePickerDialog;

    ArrayList<FoodLogItemView> innerBreakfastItems;
    ArrayList<FoodLogItemView> innerLunchItems;
    ArrayList<FoodLogItemView> innerDinnerItems;
    ArrayList<FoodLogItemView> innerSnacksItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedStateInstance) {

        View v = inflater.inflate(R.layout.fragment_food_log, container, false);

        arrayListVertical = new ArrayList<>();
        innerBreakfastItems = new ArrayList<>();
        innerLunchItems = new ArrayList<>();
        innerDinnerItems = new ArrayList<>();
        innerSnacksItems = new ArrayList<>();

        //hook elements
        mAddFoodButton = v.findViewById(R.id.btn_add_food);
        mAddFoodButton.setOnClickListener(this);

        mDatePickButton = v.findViewById(R.id.btn_date_picker);
        mDatePickButton.setOnClickListener(this);

        showDateTxt = v.findViewById(R.id.show_selected_date);

        //hook recycler views and adapters
        LinearLayout recyclerContainer = v.findViewById(R.id.recycler_container);


        initializeDatePickerDialog();



        return v;
    }

    private void initializeDatePickerDialog() {
        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                setDate(year, month + 1, dayOfMonth);
            }
        }, year, month, day);

        setDate(year, month + 1, day);
    }


    public void setDate(int year, int month, int day) {
        selectedYear = year;
        selectedMonth = month;
        selectedDay = day;

        onDateChanged();
    }

    private void onDateChanged() {
        showDateTxt.setText(formatDate(selectedYear, selectedMonth, selectedDay, false));
        FoodLog_API_Controller.getFoodLogEntries(getActivity(), MainActivity.getCurrentUser(), formatDate(selectedYear, selectedMonth, selectedDay, true), new VolleyResponseListener<HashMap<String, List<FoodLogItemView>>>() {
            @Override
            public void onResponse(HashMap<String, List<FoodLogItemView>> response) {
                //populateRecyclerItems(response);
            }

            @Override
            public void onError(String error) {
                Log.d("error", error);
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void populateRecyclerItems(HashMap<String, List<FoodLogItemView>> logItems) {
//
//        //breakfast
//        innerBreakfastItems.clear();
//        for (FoodLogItemView item : Objects.requireNonNull(logItems.get("Breakfast"))) {
//            Log.d("item", item.toString());
//            innerBreakfastItems.add(item);
//        }
//
//        //lunch
//        innerLunchItems.clear();
//        for (FoodLogItemView item : Objects.requireNonNull(logItems.get("Lunch"))) {
//            Log.d("item", item.toString());
//            innerLunchItems.add(item);
//        }
//
//        //dinner
//        innerDinnerItems.clear();
//        for (FoodLogItemView item : Objects.requireNonNull(logItems.get("Dinner"))) {
//            Log.d("item", item.toString());
//            innerDinnerItems.add(item);
//        }
//
//        //snacks
//        for (FoodLogItemView item : Objects.requireNonNull(logItems.get("Snack"))) {
//            Log.d("item", item.toString());
//            innerSnacksItems.add(item);
//        }
//
//        editFoodAdapter.notifyDataSetChanged();
//    }

    private String formatDate(int year, int month, int day, boolean sqlFormat) {
        String formatted;
        if (sqlFormat)
            formatted = String.format(Locale.US, "%d-%02d-%02d", year, month, day);
        else
            formatted = String.format(Locale.US, "Selected date: %02d/%02d/%d", month, day, year);

        return formatted;
    }

    //set data for both ArrayLists used in the nested recycler views
//    private void setData() {
//
//        //Vertical recycler views
//
//        //breakfast
//        OuterMealRecyclerItem outerMealRecyclerItemBreakfast = new OuterMealRecyclerItem();
//        outerMealRecyclerItemBreakfast.setTitle("Breakfast:");
////        ArrayList<FoodLogItemView> innerBreakfastItems = new ArrayList<>();
//        outerMealRecyclerItemBreakfast.setArrayList(innerBreakfastItems);
//        arrayListVertical.add(outerMealRecyclerItemBreakfast);
//
//        //lunch
//        OuterMealRecyclerItem outerMealRecyclerItemLunch = new OuterMealRecyclerItem();
//        outerMealRecyclerItemLunch.setTitle("Lunch:");
////        ArrayList<FoodLogItemView> innerLunchItems = new ArrayList<>();
//        outerMealRecyclerItemLunch.setArrayList(innerLunchItems);
//        arrayListVertical.add(outerMealRecyclerItemLunch);
//
//        //Dinner
//        OuterMealRecyclerItem outerMealRecyclerItemDinner = new OuterMealRecyclerItem();
//        outerMealRecyclerItemDinner.setTitle("Dinner:");
////        ArrayList<FoodLogItemView> innerDinnerItems = new ArrayList<>();
//        outerMealRecyclerItemDinner.setArrayList(innerDinnerItems);
//        arrayListVertical.add(outerMealRecyclerItemDinner);
//
//        //snacks
//        OuterMealRecyclerItem outerMealRecyclerItemSnacks = new OuterMealRecyclerItem();
//        outerMealRecyclerItemSnacks.setTitle("Snacks:");
////        ArrayList<FoodLogItemView> innerSnacksItems = new ArrayList<>();
//        outerMealRecyclerItemSnacks.setArrayList(innerSnacksItems);
//        arrayListVertical.add(outerMealRecyclerItemSnacks);
//
//        //editFoodAdapter.notifyDataSetChanged();
//    }

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
        } else if (v.getId() == R.id.btn_date_picker) {
            datePickerDialog.show();
        }
    }
}