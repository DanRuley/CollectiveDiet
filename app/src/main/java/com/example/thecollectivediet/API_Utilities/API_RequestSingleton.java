package com.example.thecollectivediet.API_Utilities;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * This class is used as an async request queue for API requests to our AWS backend.  It uses the Singleton pattern,
 * so only one instance of the class ever exists.
 */
@SuppressLint("StaticFieldLeak")
public class API_RequestSingleton {

    private static API_RequestSingleton instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    /**
     * Construct the API_RequestSingleton given the application context.
     */
    private API_RequestSingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    /**
     * Get the singleton instance of the request queue.
     * @param context - Application context
     * @return - request RequestSingleton instance
     */
    public static synchronized API_RequestSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new API_RequestSingleton(context);
        }
        return instance;
    }

    /**
     * Expose the request queue to the user
     * @return API Request queue
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
