package com.example.thecollectivediet.API_Utilities;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodResult;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FoodSearchController {

    private static int foodCount = 10;
    private static String queryURLStart = "https://api.nal.usda.gov/fdc/v1/foods/search?query=";
    private static String queryURLEnd = "&pageSize=2&api_key=NGhWlKmkJ6qwX4mLMBhvwU6b68vgICJiReWayDEg";
    private Context ctx;

    public FoodSearchController(Context _ctx) {
        this.ctx = _ctx;
    }

    /**
     * Methods making Json Requests operate asynchronously - therefore when we attach GUI elements to those methods we cannot just use a return value.
     * Instead, we create an anonymous class instance of this interface and implement the response/error callbacks for the async requests.
     */
    public interface VolleyResponseListener<T> {
        void onResponse(T response);

        void onError(String error);
    }

    public void searchFoodByName(String foodName, final VolleyResponseListener<List<FoodResult>> listener) {

        System.out.println("ran");
        String url = queryURLStart + foodName + queryURLEnd;
        List<FoodResult> foods = new ArrayList<>();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                System.out.println("ran");
                JSONArray foodList = response.getJSONArray("foods");
                Gson gson = new Gson();
                for (int i = 0; i < foodList.length(); i++) {
                    JSONObject food = (JSONObject) foodList.get(i);
                    foods.add(gson.fromJson(food.toString(), FoodResult.class));
                }
                listener.onResponse(foods);
            } catch (JSONException e) {
                listener.onError((e.getMessage()));
            }

        }, error -> {
            listener.onError(error.getMessage());
        });

        API_RequestSingleton.getInstance(ctx).addToRequestQueue(req);
    }
}
