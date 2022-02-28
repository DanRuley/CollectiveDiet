package com.example.thecollectivediet.Me_Fragment_Components.Food_Logging_Editing;

import java.util.ArrayList;

public class OuterMealRecyclerItem {

    String title;
    ArrayList<InnerFoodListItem> arrayList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<InnerFoodListItem> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<InnerFoodListItem> arrayList) {
        this.arrayList = arrayList;
    }
}
