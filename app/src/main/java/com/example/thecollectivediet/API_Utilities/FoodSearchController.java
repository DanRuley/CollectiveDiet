package com.example.thecollectivediet.API_Utilities;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodResult;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FoodSearchController {

    private static int foodCount = 10;
    private static String queryURLStart = "https://api.nal.usda.gov/fdc/v1/foods/search?query=";
    private static String queryURLEnd = "&pageSize=10&api_key=NGhWlKmkJ6qwX4mLMBhvwU6b68vgICJiReWayDEg";
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

    public void searchFoodByName(String foodName, VolleyResponseListener<List<FoodResult>> listener) {
        List<FoodResult> foods = new ArrayList<>();

        String url = "https://trackapi.nutritionix.com/v2/search/instant?query=apple";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Gson gson = new Gson();
                        JSONArray commonFoods = response.getJSONArray("common");
                        JSONArray brandedFoods = response.getJSONArray("branded");
                        for (int i = 0; i < commonFoods.length(); i++) {
                            JSONObject food = (JSONObject) commonFoods.get(i);
                            foods.add(gson.fromJson(food.toString(), FoodResult.class));
                        }
                        for (int i = 0; i < brandedFoods.length(); i++) {
                            JSONObject food = (JSONObject) brandedFoods.get(i);
                            foods.add(gson.fromJson(food.toString(), FoodResult.class));
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("x-app-id", "efc835c2");
                params.put("x-app-key", "4d6be0c8e692f9a473f0b30d5377ce69");

                return params;
            }
        };
        API_RequestSingleton.getInstance(ctx).addToRequestQueue(req);
    }


//    public void searchFoodByName(String foodName, final VolleyResponseListener<List<FoodResult>> listener) {
//
//        System.out.println("ran");
//        String url = queryURLStart + foodName + queryURLEnd;
//        List<FoodResult> foods = new ArrayList<>();
//
//        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
//            try {
//                System.out.println("ran");
//                JSONArray foodList = response.getJSONArray("foods");
//                Gson gson = new Gson();
//                for (int i = 0; i < foodList.length(); i++) {
//                    JSONObject food = (JSONObject) foodList.get(i);
//                    foods.add(gson.fromJson(food.toString(), FoodResult.class));
//                }
//                listener.onResponse(foods);
//            } catch (JSONException e) {
//                listener.onError((e.getMessage()));
//            }
//
//        }, error -> {
//            listener.onError(error.getMessage());
//        });
//
//        API_RequestSingleton.getInstance(ctx).addToRequestQueue(req);
//    }
}
