package com.example.thecollectivediet.API_Utilities;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodLogItemView;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodLogUploadItem;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodResult;
import com.example.thecollectivediet.JSON_Marshall_Objects.User;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class FoodLog_API_Controller {


    @NonNull
    public static String getDateString() {
        java.util.Date dts = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        return sdf.format(dts);
    }

    public static void pushFoodLogEntry(Context ctx, @NonNull FoodResult food, @NonNull User user, Float portionSize, String portionUnit, String mealCategory, String date) {
        String url = "https://k1gc92q8zk.execute-api.us-east-2.amazonaws.com/add_food_log_item";

        FoodLogUploadItem toAdd = new FoodLogUploadItem(user.getUser_id(), food.getId(), date, portionSize, portionUnit, mealCategory);

        JSONObject foodLogJSON = null;

        try {
            foodLogJSON = new JSONObject(new Gson().toJson(toAdd, FoodLogUploadItem.class));
        } catch (JSONException e) {
            Log.d("food log json parse", e.getMessage());
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, foodLogJSON,
                response -> Log.d("success!", response.toString()), error -> Log.d("add food log lambda", error.getMessage() == null ? "See AWS logs" : error.getMessage())) {
            @NonNull
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        API_RequestSingleton.getInstance(ctx).addToRequestQueue(req);
    }

    public static void getFoodLogEntries(Context ctx, @NonNull User user, String dt, @NonNull VolleyResponseListener<HashMap<String, List<FoodLogItemView>>> listener) {
        String url = String.format(Locale.US, "https://k1gc92q8zk.execute-api.us-east-2.amazonaws.com/get_food_log_items?uid=%s&date=%s", user.getUser_id(), dt);



        HashMap<String, List<FoodLogItemView>> results = getEmptyFoodItemMap();

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Gson gson = new Gson();
                        for (int i = 0; i < response.length(); i++) {
                            String jsonString = response.get(i).toString();
                            FoodLogItemView logItem = gson.fromJson(jsonString, FoodLogItemView.class);
                            logItem.setLog_date(dt);
                            String category = logItem.getCategory();
                            switch (category) {
                                case "Breakfast":
                                    Objects.requireNonNull(results.get("Breakfast")).add(logItem);
                                    break;
                                case "Lunch":
                                    Objects.requireNonNull(results.get("Lunch")).add(logItem);
                                    break;
                                case "Dinner":
                                    Objects.requireNonNull(results.get("Dinner")).add(logItem);
                                    break;
                                default:
                                    Objects.requireNonNull(results.get("Snacks")).add(logItem);
                                    break;
                            }
                        }
                        listener.onResponse(results);
                    } catch (@NonNull JSONException | JsonSyntaxException e) {
                        listener.onError(e.getMessage());
                    }
                },
                error -> listener.onError(error.toString())
        );
        API_RequestSingleton.getInstance(ctx).addToRequestQueue(req);
    }

    @NonNull
    private static HashMap<String, List<FoodLogItemView>> getEmptyFoodItemMap() {
        HashMap<String, List<FoodLogItemView>> map = new HashMap<>();
        map.put("Breakfast", new ArrayList<>());
        map.put("Lunch", new ArrayList<>());
        map.put("Dinner", new ArrayList<>());
        map.put("Snacks", new ArrayList<>());

        return map;
    }
}
