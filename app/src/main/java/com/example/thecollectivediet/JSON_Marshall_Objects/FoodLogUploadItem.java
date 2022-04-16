package com.example.thecollectivediet.JSON_Marshall_Objects;

import androidx.annotation.NonNull;

/**
 * Simple class for serializing food log JSON data to provide to API.
 * Note: comments are minimal because most methods are self-explanatory getters/setters.
 */
public class FoodLogUploadItem {

    Long entry_id;
    String user_id;
    Long food_id;
    String log_dts;
    Float portion_size;
    String portion_unit;
    String category;

    public FoodLogUploadItem(long entry_id, String user_id, long food_id, String log_dts, Float portion_size, String portion_unit, String category) {
        this.entry_id = entry_id;
        this.user_id = user_id;
        this.food_id = food_id;
        this.log_dts = log_dts;
        this.portion_size = portion_size;
        this.portion_unit = portion_unit;
        this.category = category;
    }

    public FoodLogUploadItem(String user_id, long food_id, String log_dts, Float portion_size, String portion_unit, String category) {
        this.entry_id = entry_id;
        this.user_id = user_id;
        this.food_id = food_id;
        this.log_dts = log_dts;
        this.portion_size = portion_size;
        this.portion_unit = portion_unit;
        this.category = category;
    }

    public long getEntry_id() {
        return entry_id;
    }

    public void setEntry_id(long entry_id) {
        this.entry_id = entry_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public long getFood_id() {
        return food_id;
    }

    public void setFood_id(long food_id) {
        this.food_id = food_id;
    }

    public String getLog_dts() {
        return log_dts;
    }

    public void setLog_dts(String log_dts) {
        this.log_dts = log_dts;
    }

    public Float getPortion_size() {
        return portion_size;
    }

    public void setPortion_size(Float portion_size) {
        this.portion_size = portion_size;
    }

    public String getPortion_unit() {
        return portion_unit;
    }

    public void setPortion_unit(String portion_unit) {
        this.portion_unit = portion_unit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @NonNull
    @Override
    public String toString() {
        return "FoodLogItem{" +
                "entry_id=" + entry_id +
                ", user_id='" + user_id + '\'' +
                ", food_id=" + food_id +
                ", log_dts='" + log_dts + '\'' +
                ", portion_size=" + portion_size +
                ", portion_unit='" + portion_unit + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}


