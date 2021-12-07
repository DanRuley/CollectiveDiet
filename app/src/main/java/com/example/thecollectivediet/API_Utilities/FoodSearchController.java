package com.example.thecollectivediet.API_Utilities;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.thecollectivediet.JSON_Marshall_Objects.*;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FoodSearchController {

    private Context ctx;
    private static HashMap<String, String> headers;

    public FoodSearchController(Context _ctx) {
        this.ctx = _ctx;

        headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("x-app-id", "efc835c2");
        headers.put("x-app-key", "4d6be0c8e692f9a473f0b30d5377ce69");
    }

    /**
     * Methods making Json Requests operate asynchronously - therefore when we attach GUI elements to those methods we cannot just use a return value.
     * Instead, we create an anonymous class instance of this interface and implement the response/error callbacks for the async requests.
     */
    public interface VolleyResponseListener<T> {
        void onResponse(T response);

        void onError(String error);
    }

    public void getNutrients(FoodResult foodSearchResult, VolleyResponseListener<String> listener) {
        if (foodSearchResult instanceof CommonFoodResult)
            getCommonNutrients(foodSearchResult.getAPI_Identifier(), listener);
        else
            getBrandedNutrients(foodSearchResult.getAPI_Identifier(), listener);
    }

    private void getBrandedNutrients(String food_name, VolleyResponseListener<String> listener) {


    }

    public void getCommonNutrients(String commonFoodName, VolleyResponseListener<String> listener) {
        FoodDetails details;
        JSONObject jsonBody = null;
        String url = "https://trackapi.nutritionix.com/v2/natural/nutrients";
        try {
            jsonBody = new JSONObject("{\"query\":\"" + commonFoodName + "\"}");
        } catch (JSONException e) {
            listener.onError(e.getMessage());
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    Gson gson = new Gson();
                    String s = response.toString();
                    Log.d("API Response", s);
                    listener.onResponse(s);

                },
                error -> {
                    Log.d("ERROR", "error => " + error.toString());
                    listener.onError(error.toString());
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        API_RequestSingleton.getInstance(ctx).addToRequestQueue(req);
    }

    public void searchFoodByName(String foodName, VolleyResponseListener<List<FoodResult>> listener) {
        List<FoodResult> foods = new ArrayList<>();

        String url = "https://trackapi.nutritionix.com/v2/search/instant?query=" + foodName;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Gson gson = new Gson();
                        JSONArray commonFoods = response.getJSONArray("common");
                        JSONArray brandedFoods = response.getJSONArray("branded");
                        for (int i = 0; i < commonFoods.length(); i++) {
                            JSONObject food = (JSONObject) commonFoods.get(i);
                            foods.add(gson.fromJson(food.toString(), CommonFoodResult.class));
                        }
                        for (int i = 0; i < brandedFoods.length(); i++) {
                            JSONObject food = (JSONObject) brandedFoods.get(i);
                            foods.add(gson.fromJson(food.toString(), BrandedFoodResult.class));
                        }
                        listener.onResponse(foods);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError(e.getMessage());
                    }
                },
                error -> {
                    Log.d("ERROR", "error => " + error.toString());
                    listener.onError(error.toString());
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };
        API_RequestSingleton.getInstance(ctx).addToRequestQueue(req);
    }
}
