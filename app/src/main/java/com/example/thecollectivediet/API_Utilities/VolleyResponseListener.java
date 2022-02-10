package com.example.thecollectivediet.API_Utilities;

public interface VolleyResponseListener<T> {
    /**
     * Methods making Json Requests operate asynchronously - therefore when we attach GUI elements to those methods we cannot just use a return value.
     * Instead, we create an anonymous class instance of this interface and implement the response/error callbacks for the async requests.
     */

    void onResponse(T response);

    void onError(String error);

}
