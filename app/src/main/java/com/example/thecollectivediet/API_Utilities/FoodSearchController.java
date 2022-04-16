package com.example.thecollectivediet.API_Utilities;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodNutrients;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodResult;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class for Food Search API calls using the Volley Library.
 */
public class FoodSearchController {

    private final Context ctx;
    private static HashMap<String, String> headers;

    public FoodSearchController(Context _ctx) {
        this.ctx = _ctx;

        headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("x-app-id", "efc835c2");
        headers.put("x-app-key", "4d6be0c8e692f9a473f0b30d5377ce69");
    }

    /**
     * Given a foodID, returns the food nutrients from the backend database.  This data is sent to the provided callback method.
     */
    public void getNutrients(String foodID, @NonNull VolleyResponseListener<FoodNutrients> listener) {
        String url = "https://k1gc92q8zk.execute-api.us-east-2.amazonaws.com/FoodIdSearch?food_id=" + foodID;

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Gson gson = new Gson();
                        FoodNutrients nutrients = gson.fromJson(response.toString(), FoodNutrients.class);
                        listener.onResponse(nutrients);
                    } catch (JsonSyntaxException e) {
                        listener.onError(e.getMessage());
                    }
                },
                error -> listener.onError(error.toString())
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        API_RequestSingleton.getInstance(ctx).addToRequestQueue(req);
    }

    /**
     * Makes an API call to search for the given food name string.  Returns a list of FoodItems found in the database to the provided callback listener.
     */
    public void searchFoodByName(String foodName, @NonNull VolleyResponseListener<List<FoodResult>> listener) {
        List<FoodResult> foods = new ArrayList<>();

        String url = "https://k1gc92q8zk.execute-api.us-east-2.amazonaws.com/RelatedFoods?food=" + foodName;
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Gson gson = new Gson();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject food = (JSONObject) response.get(i);
                            foods.add(gson.fromJson(food.toString(), FoodResult.class));
                        }
                        listener.onResponse(foods);
                    } catch (@NonNull JSONException | JsonSyntaxException e) {
                        e.printStackTrace();
                        listener.onError(e.getMessage());
                    }
                },
                error -> listener.onError(error.toString())
        );
        API_RequestSingleton.getInstance(ctx).addToRequestQueue(req);
    }
}
