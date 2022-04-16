package com.example.thecollectivediet.JSON_Marshall_Objects;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class EditFoodObject {

    private final String name;
    private final String info;
    @NonNull
    private final String date;
    Date currentTime;

    //For serialization
    private static final String JSON_NAME = "name";
    private static final String JSON_SERVING_SIZE = "servingSize";
    private static final String JSON_DATE = "date";

    public EditFoodObject(String name, String info) {
        this.name = name;
        this.info = info;
        currentTime = Calendar.getInstance().getTime();
        this.date = currentTime.toString();
    }

    public EditFoodObject(@NonNull JSONObject jo) throws JSONException {
        this.name = jo.getString(JSON_NAME);
        this.info = jo.getString(JSON_SERVING_SIZE);
        this.date = jo.getString(JSON_DATE);
    }

    public String getNameText() {
        return name;
    }

    public String getInfoText() {
        return info;
    }

    @NonNull
    public String getDateText() {
        return date;
    }

    //Method to serialize FoodResult object
    @NonNull
    public JSONObject convertToJSON() throws JSONException {
        JSONObject jo = new JSONObject();

        jo.put(JSON_NAME, name);
        jo.put(JSON_SERVING_SIZE, info);
        jo.put(JSON_DATE, date);

        return jo;
    }
}
