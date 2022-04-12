package com.example.thecollectivediet.API_Utilities;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.amplifyframework.core.Amplify;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodLogItemView;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodLogUploadItem;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodResult;
import com.example.thecollectivediet.JSON_Marshall_Objects.User;
import com.example.thecollectivediet.JSON_Marshall_Objects.UserPostUploadItem;
import com.example.thecollectivediet.JSON_Marshall_Objects.WeightUploadItem;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class User_API_Controller {

    public static void handleNewSignIn(@NonNull GoogleSignInAccount account, Context ctx, @NonNull VolleyResponseListener<User> listener) {

        String url = "https://k1gc92q8zk.execute-api.us-east-2.amazonaws.com/getUser?uid=" + account.getId();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        User signedInUser;

                        if (response.equals("null")) {
                            java.util.Date dts = new java.util.Date();
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                            signedInUser = new User(account.getId(), account.getDisplayName(), account.getEmail(), sdf.format(dts));
                            addNewUser(signedInUser, ctx);
                        } else {
                            Gson gson = new Gson();
                            signedInUser = gson.fromJson(response, User.class);
                        }
                        listener.onResponse(signedInUser);
                    } catch (JsonSyntaxException e) {
                        listener.onError(e.getMessage());
                    }
                },
                error -> {
                    listener.onError(error.getMessage());
                });
        API_RequestSingleton.getInstance(ctx).addToRequestQueue(stringRequest);
    }

    /**
     * Calls database to get the weights and dates of the user. The database will only send over
     * a limited amount of the most current weigh ins so the received array of weigh ins will
     * need to be reversed.
     * @param ctx
     * @param user
     * @param listener
     */
    public static void getWeighIns(Context ctx, @NonNull User user, @NonNull VolleyResponseListener<DataPoint[]> listener) {
        String url = String.format(Locale.US, "https://k1gc92q8zk.execute-api.us-east-2.amazonaws.com/getWeighIns?uid=%s", user.getUser_id());

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        DataPoint[] results = new DataPoint[response.length()];

                        for (int i = 0; i < response.length(); i++) {
                            String jsonString = response.get(i).toString();

                            //parse json response to get date
                            String []values = jsonString.split(":|,");
                            String v3 = values[3];
                            v3 = v3.replaceAll("\"", "");
                            v3 = v3.replace("}","");

                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                            Date date = formatter.parse(v3);

                            results[i] = new DataPoint(date, Integer.parseInt(values[1]));

                        }
                        //The received array of weigh ins are the most current in descending order
                        //so the array needs to be reversed.
                        results = reverse(results);

                        listener.onResponse(results);
                    } catch (@NonNull JSONException | JsonSyntaxException | ParseException e) {
                        listener.onError(e.getMessage());
                    }
                },
                error -> listener.onError(error.toString())
        );
        API_RequestSingleton.getInstance(ctx).addToRequestQueue(req);
    }

    private static void addNewUser(User signedInUser, Context ctx) {

        String url = "https://k1gc92q8zk.execute-api.us-east-2.amazonaws.com/addUser";
        JSONObject usrJSON = null;

        try {
            usrJSON = new JSONObject(new Gson().toJson(signedInUser, User.class));
        } catch (JSONException e) {
            Log.d("user json parse", e.getMessage());
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, usrJSON,
                response -> Log.d("ret", response.toString()), error -> Log.d("add user lambda", error.getMessage())) {
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

    public static void updateUserProfile(User currentUser, Context ctx) {
        String url = "https://k1gc92q8zk.execute-api.us-east-2.amazonaws.com/updateUserInfo";
        JSONObject usrJSON = null;

        try {
            usrJSON = new JSONObject(new Gson().toJson(currentUser, User.class));
        } catch (JSONException e) {
            Log.d("user json parse", e.getMessage());
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, usrJSON,
                response -> Toast.makeText(ctx, "User profile updated!", Toast.LENGTH_SHORT).show(), error -> Log.d("update user lambda", error.getMessage())) {

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

    /**
     * Push today's weigh in of the user to the database.
     * @param user
     * @param weight
     * @param ctx
     */
    public static void pushWeightLogEntry(@NonNull User user, Float weight,Context ctx) {
        String url = "https://k1gc92q8zk.execute-api.us-east-2.amazonaws.com/add_weight_log_item";

        java.util.Date dts = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.US);

        WeightUploadItem toAdd = new WeightUploadItem(user.getUser_id(), weight, sdf.format(dts));

        JSONObject weightLogJSON = null;

        try {
            weightLogJSON = new JSONObject(new Gson().toJson(toAdd, WeightUploadItem.class));
        } catch (JSONException e) {
            Log.d("weight log json parse", e.getMessage());
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, weightLogJSON,
                response -> Log.d("success!", response.toString()), error -> Log.d("add weight log lambda", error.getMessage() == null ? "See AWS logs" : error.getMessage())) {
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

    public static void pushUserPost(String user_id, String imageKey, String comment, Context ctx, String imageUrl, String user_name) {
        String url = "https://k1gc92q8zk.execute-api.us-east-2.amazonaws.com/addUserPost";

        java.util.Date dts = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.US);

        //WeightUploadItem toAdd = new WeightUploadItem(user.getUser_id(), weight, sdf.format(dts));
        UserPostUploadItem userPostUploadItem = new UserPostUploadItem(user_id, imageKey, comment, sdf.format(dts), imageUrl, user_name);

        JSONObject postJSON = null;

        try {

            postJSON = new JSONObject(new Gson().toJson(userPostUploadItem, UserPostUploadItem.class));
        } catch (JSONException e) {
            Log.d("post json parse", e.getMessage());
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, postJSON,
                response -> Log.d("successxxxxxxxxxxxxxxx!", response.toString()), error -> Log.d("add post log lambda", error.getMessage() == null ? "See AWS logs" : error.getMessage())) {
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
    //todo
    public static void pushUserPost(@NonNull String user_id, String imageKey, String comment, Context ctx, String user_name) {
        String url = "https://k1gc92q8zk.execute-api.us-east-2.amazonaws.com/addUserPost";

        java.util.Date dts = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.US);

        //WeightUploadItem toAdd = new WeightUploadItem(user.getUser_id(), weight, sdf.format(dts));
        UserPostUploadItem userPostUploadItem = new UserPostUploadItem(user_id, imageKey, comment, sdf.format(dts), user_name);

        JSONObject postJSON = null;

        try {

            postJSON = new JSONObject(new Gson().toJson(userPostUploadItem, UserPostUploadItem.class));
        } catch (JSONException e) {
            Log.d("post json parse", e.getMessage());
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, postJSON,
                response -> Log.d("success!", response.toString()), error -> Log.d("add post log lambda", error.getMessage() == null ? "See AWS logs" : error.getMessage())) {
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

    public static void getPosts(Context ctx, @NonNull VolleyResponseListener<UserPostUploadItem[]> listener) {
        String url = String.format(Locale.US, "https://k1gc92q8zk.execute-api.us-east-2.amazonaws.com/getPosts");

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    UserPostUploadItem[] results = new UserPostUploadItem[response.length()];
                    try {
                        Gson gson = new Gson();
                        for (int i = 0; i < response.length(); i++) {
                            String jsonString = response.get(i).toString();
                            UserPostUploadItem postItem = gson.fromJson(jsonString, UserPostUploadItem.class);


                            Amplify.Storage.getUrl(
                                    postItem.getImage_key(),
                                    result -> {

                                        Log.i("MyAmplifyApp", "Successfully generated: " + result.getUrl());

                                    },
                                    error -> Log.e("MyAmplifyApp", "URL generation failure", error)
                            );
                            results[i] = postItem;

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

    /**
     * GeeksforGeeks.com algorithm function swaps the array's first element with last
     * element, second element with last second element and so on.
     * @param arr
     * @return
     */
    private static DataPoint[] reverse(DataPoint[] arr)
        {
            int i;
            DataPoint t;
            for (i = 0; i < arr.length / 2; i++) {
                t = arr[i];
                arr[i] = arr[arr.length - i - 1];
                arr[arr.length - i - 1] = t;
            }
            return arr;
        }


}
