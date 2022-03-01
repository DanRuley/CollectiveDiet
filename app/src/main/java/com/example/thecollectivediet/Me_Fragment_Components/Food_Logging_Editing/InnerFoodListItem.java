package com.example.thecollectivediet.Me_Fragment_Components.Food_Logging_Editing;

public class InnerFoodListItem {
    String name;
    Double serving;
    int calories;

    public  InnerFoodListItem(){

    }

    public InnerFoodListItem(String name, double serving, int calories){
        this.name = name;
        this.serving = serving;
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getServing() {
        return serving;
    }

    public void setServing(Double serving) {
        this.serving = serving;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }
}
