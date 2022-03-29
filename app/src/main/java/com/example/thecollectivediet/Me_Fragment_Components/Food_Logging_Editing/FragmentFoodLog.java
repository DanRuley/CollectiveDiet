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
import com.example.thecollectivediet.JSON_Marshall_Objects.User;
import com.example.thecollectivediet.MainActivity;
import com.example.thecollectivediet.ModelViewMeals;
import com.example.thecollectivediet.ModelViewUser;
import com.example.thecollectivediet.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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

    ModelViewUser modelViewUser;
    ModelViewMeals modelViewMeals;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedStateInstance) {

        View v = inflater.inflate(R.layout.fragment_food_log, container, false);

        //Creates or gets existing view model to pass around the user data
        modelViewUser = new ViewModelProvider(requireActivity()).get(ModelViewUser.class);
        modelViewMeals = new ViewModelProvider(requireActivity()).get(ModelViewMeals.class);

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
        RecyclerView outerRec = v.findViewById(R.id.rv_main);
        outerRec.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        setData();

        initializeDatePickerDialog();
        String normal = formatDate(selectedYear, selectedMonth, selectedDay, false);
        String sql = formatDate(selectedYear, selectedMonth, selectedDay, true);

        foodLogAdapter = new OuterMealListRecycler(getActivity(), arrayListVertical);

        outerRec.setAdapter(foodLogAdapter);

        //set the observer to get info for user
        modelViewMeals.getList().observe(requireActivity(), nameObserver);

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

    private void initializeDatePickerDialog() {
        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(), (view, year1, month1, dayOfMonth) -> setDate(year1, month1 + 1, dayOfMonth), year, month, day);

        datePickerDialog.getDatePicker().setSpinnersShown(true);
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
        FoodLog_API_Controller.getFoodLogEntries(getActivity(), modelViewUser.getUser(), formatDate(selectedYear, selectedMonth, selectedDay, true), new VolleyResponseListener<HashMap<String, List<FoodLogItemView>>>() {
            @Override
            public void onResponse(@NonNull HashMap<String, List<FoodLogItemView>> response) {
               // populateRecyclerItems(response);// erase later after viewmodel implementation
                modelViewMeals.setList(response);
            }

            @Override
            public void onError(String error) {
                Log.d("error", error);
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateRecyclerItems(@NonNull HashMap<String, List<FoodLogItemView>> logItems) {

        for (OuterMealRecyclerItem outerList : arrayListVertical) {
            Double totalCal = Converter.getTotalMealCalories(Objects.requireNonNull(logItems.get(outerList.getTitle())));
            outerList.setCalorieString(String.format(Locale.US, "%.1f Calories", totalCal));
        }

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

    public void inflateFoodSearchFrag(@NonNull MealSelectDialog.MealType mealType) {
        Fragment f = new ManualFoodSearch();
        Bundle args = new Bundle();
        args.putInt("mealType", mealType.ordinal());
        f.setArguments(args);
        MainActivity.commitFragmentTransaction(requireActivity(), R.id.fragmentHolder, f);
    }

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