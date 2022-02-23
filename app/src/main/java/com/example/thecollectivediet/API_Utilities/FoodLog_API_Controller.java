package com.example.thecollectivediet.API_Utilities;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodLogItem;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodResult;
import com.example.thecollectivediet.JSON_Marshall_Objects.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FoodLog_API_Controller {


    public static String getDateString() {
        java.util.Date dts = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        return sdf.format(dts);
    }

    public static void pushFoodLogEntry(Context ctx, FoodResult food, User user, Float portionSize, String portionUnit, String mealCategory) {
        String url = "https://k1gc92q8zk.execute-api.us-east-2.amazonaws.com/add_food_log_item";

        FoodLogItem toAdd = new FoodLogItem(user.getUser_id(), food.getId(), getDateString(), portionSize, portionUnit, mealCategory);

        JSONObject foodLogJSON = null;

        try {
            foodLogJSON = new JSONObject(new Gson().toJson(toAdd, FoodLogItem.class));
        } catch (JSONException e) {
            Log.d("food log json parse", e.getMessage());
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, foodLogJSON,
                response -> Log.d("success!", response.toString()), error -> Log.d("add food log lambda", error.getMessage())) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        API_RequestSingleton.getInstance(ctx).addToRequestQueue(req);
    }

}
