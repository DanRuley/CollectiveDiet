package com.example.thecollectivediet.API_Utilities;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.thecollectivediet.JSON_Marshall_Objects.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class User_API_Controller {

    public static void handleNewSignIn(GoogleSignInAccount account, Context ctx, VolleyResponseListener<User> listener) {

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
