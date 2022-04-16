package com.example.thecollectivediet.Me_Fragment_Components.Food_Logging_Editing;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thecollectivediet.API_Utilities.FoodLog_API_Controller;
import com.example.thecollectivediet.API_Utilities.VolleyResponseListener;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodLogItemView;
import com.example.thecollectivediet.MainActivity;
import com.example.thecollectivediet.R;
import com.example.thecollectivediet.ViewModelUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * This is the main food log fragment.  It displays user food log data and lets users select a date and log new items.
 */
public class FragmentFoodLog extends Fragment implements View.OnClickListener {

    AppCompatButton mAddFoodButton;
    AppCompatButton mDatePickButton;
    TextView showDateTxt;

    int selectedYear;
    int selectedMonth;
    int selectedDay;

    ArrayList<OuterMealRecyclerItem> arrayListVertical;

    @Nullable
    OuterMealListRecycler foodLogAdapter;
    @Nullable
    Dialog foodLogDialog;
    @Nullable
    DatePickerDialog datePickerDialog;

    ArrayList<FoodLogItemView> innerBreakfastItems;
    ArrayList<FoodLogItemView> innerLunchItems;
    ArrayList<FoodLogItemView> innerDinnerItems;
    ArrayList<FoodLogItemView> innerSnacksItems;

    ViewModelUser viewModelUser;

    /**
     * Inflate the view and return it
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedStateInstance) {

        View v = inflater.inflate(R.layout.fragment_food_log, container, false);

        //Creates or gets existing view model to pass around the user data
        viewModelUser = new ViewModelProvider(requireActivity()).get(ViewModelUser.class);


        arrayListVertical = new ArrayList<>();
        innerBreakfastItems = new ArrayList<>();
        innerLunchItems = new ArrayList<>();
        innerDinnerItems = new ArrayList<>();
        innerSnacksItems = new ArrayList<>();

        //User u = MainActivity.getCurrentUser();
        //hook elements
        mAddFoodButton = v.findViewById(R.id.btn_add_food);
        mAddFoodButton.setOnClickListener(this);

        mDatePickButton = v.findViewById(R.id.btn_date_picker);
        mDatePickButton.setOnClickListener(this);

        showDateTxt = v.findViewById(R.id.show_selected_date);

        //hook recycler views and adapters
        RecyclerView outerRec = v.findViewById(R.id.rv_main);
        outerRec.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        //set data in recycler views
        setData();

        //set up calendar widget
        initializeDatePickerDialog();
        String normal = formatDate(selectedYear, selectedMonth, selectedDay, false);
        String sql = formatDate(selectedYear, selectedMonth, selectedDay, true);

        foodLogAdapter = new OuterMealListRecycler(getActivity(), arrayListVertical);

        outerRec.setAdapter(foodLogAdapter);

        //set the observer to get info for user
        viewModelUser.getList().observe(requireActivity(), nameObserver);

        return v;
    }

    //create an observer that watches the LiveData<User> object
    final Observer<HashMap<String,List<FoodLogItemView>>> nameObserver = new Observer<HashMap<String,List<FoodLogItemView>>>() {
        @Override
        public void onChanged(HashMap<String,List<FoodLogItemView>> list) {
            //Update the ui if this data variable changes
            if(list != null){
               populateRecyclerItems(list);
            }
        }
    };

    /**
     * Initialize the date picker so users can select which date they wish to view.
     */
    private void initializeDatePickerDialog() {
        String[] date = viewModelUser.getDate().split("-");

        int year = Integer.valueOf(date[0]);
        int month = Integer.valueOf(date[1]) - 1;
        int day = Integer.valueOf(date[2]);

        datePickerDialog = new DatePickerDialog(getActivity(), (view, year1, month1, dayOfMonth) -> setDate(year1, month1 + 1, dayOfMonth), year, month, day);

        datePickerDialog.getDatePicker().setSpinnersShown(true);
        setDate(year, month + 1, day);
    }

    /**
     * Set the date given a year/month/day
     */
    public void setDate(int year, int month, int day) {
        selectedYear = year;
        selectedMonth = month;
        selectedDay = day;

        onDateChanged();
    }

    /**
     * Callback for the date picker - changes the selected date.
     */
    private void onDateChanged() {
        viewModelUser.setDate(formatDate(selectedYear, selectedMonth, selectedDay, true));
        showDateTxt.setText(formatDate(selectedYear, selectedMonth, selectedDay, false));
        FoodLog_API_Controller.getFoodLogEntries(getActivity(), viewModelUser.getUser(), formatDate(selectedYear, selectedMonth, selectedDay, true), new VolleyResponseListener<HashMap<String, List<FoodLogItemView>>>() {
            @Override
            public void onResponse(@NonNull HashMap<String, List<FoodLogItemView>> response) {
               viewModelUser.setList(response);
            }

            @Override
            public void onError(String error) {
                Log.d("error", error);
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Populate the food log recycler with the food items for the given meal types.
     */
    private void populateRecyclerItems(@NonNull HashMap<String, List<FoodLogItemView>> logItems) {

        int cals = 0;

        for (OuterMealRecyclerItem outerList : arrayListVertical) {
            Double totalCal = Converter.getTotalMealCalories(Objects.requireNonNull(logItems.get(outerList.getTitle())));
            cals += totalCal;
            outerList.setCalorieString(String.format(Locale.US, "%.1f Calories", totalCal));
        }

        viewModelUser.setCalories(cals);
        foodLogAdapter.notifyDataSetChanged();

        //breakfast
        innerBreakfastItems.clear();
        for (FoodLogItemView item : Objects.requireNonNull(logItems.get("Breakfast"))) {
            Log.d("item", item.toString());
            innerBreakfastItems.add(item);
        }

        //lunch
        innerLunchItems.clear();
        for (FoodLogItemView item : Objects.requireNonNull(logItems.get("Lunch"))) {
            Log.d("item", item.toString());
            innerLunchItems.add(item);
        }

        //dinner
        innerDinnerItems.clear();
        for (FoodLogItemView item : Objects.requireNonNull(logItems.get("Dinner"))) {
            Log.d("item", item.toString());
            innerDinnerItems.add(item);
        }

        //snacks
        innerSnacksItems.clear();
        for (FoodLogItemView item : Objects.requireNonNull(logItems.get("Snacks"))) {
            Log.d("item", item.toString());
            innerSnacksItems.add(item);
        }

    }

    /**
     * Beautify the date string from SQL format to human readable format.
     */
    private String formatDate(int year, int month, int day, boolean sqlFormat) {
        String formatted;
        if (sqlFormat)
            formatted = String.format(Locale.US, "%d-%02d-%02d", year, month, day);
        else
            formatted = String.format(Locale.US, "Selected date: %02d/%02d/%d", month, day, year);

        return formatted;
    }

    //set data for both ArrayLists used in the nested recycler views
    private void setData() {

        //Vertical recycler views

        //breakfast
        OuterMealRecyclerItem outerMealRecyclerItemBreakfast = new OuterMealRecyclerItem();
        outerMealRecyclerItemBreakfast.setTitle("Breakfast");
        outerMealRecyclerItemBreakfast.setCalorieString("0.0 Calories");
        outerMealRecyclerItemBreakfast.setArrayList(innerBreakfastItems);
        arrayListVertical.add(outerMealRecyclerItemBreakfast);

        //lunch
        OuterMealRecyclerItem outerMealRecyclerItemLunch = new OuterMealRecyclerItem();
        outerMealRecyclerItemLunch.setTitle("Lunch");
        outerMealRecyclerItemLunch.setCalorieString("0.0 Calories");
        outerMealRecyclerItemLunch.setArrayList(innerLunchItems);
        arrayListVertical.add(outerMealRecyclerItemLunch);

        //Dinner
        OuterMealRecyclerItem outerMealRecyclerItemDinner = new OuterMealRecyclerItem();
        outerMealRecyclerItemDinner.setTitle("Dinner");
        outerMealRecyclerItemDinner.setCalorieString("0.0 Calories");
        outerMealRecyclerItemDinner.setArrayList(innerDinnerItems);
        arrayListVertical.add(outerMealRecyclerItemDinner);

        //snacks
        OuterMealRecyclerItem outerMealRecyclerItemSnacks = new OuterMealRecyclerItem();
        outerMealRecyclerItemSnacks.setTitle("Snacks");
        outerMealRecyclerItemSnacks.setCalorieString("0.0 Calories");
        outerMealRecyclerItemSnacks.setArrayList(innerSnacksItems);
        arrayListVertical.add(outerMealRecyclerItemSnacks);

        //editFoodAdapter.notifyDataSetChanged();
    }

    /**
     * Store the meal type and inflate the food search fragment
     * @param mealType meal type chosen
     */
    public void inflateFoodSearchFrag(@NonNull MealSelectDialog.MealType mealType) {
        Fragment f = new ManualFoodSearch();
        Bundle args = new Bundle();
        args.putInt("mealType", mealType.ordinal());
        f.setArguments(args);
        MainActivity.commitFragmentTransaction(requireActivity(), R.id.fragmentHolder, f);
    }

    /**
     * Click listener for date picker and add food btns.
     */
    @Override
    public void onClick(@NonNull View v) {
        if (v.getId() == R.id.btn_add_food) {
            foodLogDialog = new MealSelectDialog(getActivity(), this);
            foodLogDialog.show();
        } else if (v.getId() == R.id.btn_date_picker) {
            datePickerDialog.show();
        }
    }
}