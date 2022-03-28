package com.example.thecollectivediet.JSON_Marshall_Objects;

import androidx.annotation.NonNull;

public class WeightUploadItem {

    String user_id;
    float user_weight;
    String log_date;

    public WeightUploadItem(String user_id, float weight, String log_dts ){
        this.user_id = user_id;
        this.user_weight = weight;
        this.log_date = log_dts;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public float getWeight() {
        return user_weight;
    }

    public void setWeight(float weight) {
        this.user_weight = weight;
    }

    public String getLog_date() {
        return log_date;
    }

    public void setLog_date(String log_date) {
        this.log_date = log_date;
    }

    @NonNull
    @Override
    public String toString() {
        return "WeightLogItem{" +

                "user_id='" + user_id + '\'' +
                ", goal_wgt=" + user_weight +
                ", log_dts='" + log_date + '\'' +

                '}';
    }
}
