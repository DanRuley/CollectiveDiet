package com.example.thecollectivediet.JSON_Marshall_Objects;

public class StatsUploadItem {

    int calories;
    int carbs;
    int fat;
    String user_id;
    String log_date;

    public StatsUploadItem(int calories, int carbs, int fats, String id, String date){

        this.calories = calories;
        this.carbs = carbs;
        this.fat = fats;
        this.user_id = id;
        this.log_date = date;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLog_date() {
        return log_date;
    }

    public void setLog_date(String log_date) {
        this.log_date = log_date;
    }
}
