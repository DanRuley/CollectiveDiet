package com.example.thecollectivediet.Me_Fragment_Components.Food_Logging_Editing;

import com.example.thecollectivediet.JSON_Marshall_Objects.FoodLogItemView;

import java.util.ArrayList;

public class OuterMealRecyclerItem {

    String title;
    String calorieString;
    ArrayList<FoodLogItemView> arrayList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCalories() {
        return calorieString;
    }

    public void setCalorieString(String val) {
        calorieString = val;
    }

    public ArrayList<FoodLogItemView> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<FoodLogItemView> arrayList) {
        this.arrayList = arrayList;
    }
}
