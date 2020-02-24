package com.lalovic.mladen.sportsfeed;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


class VolleySingleton {
    private static VolleySingleton mInstance;
    private static RequestQueue mRequestQueue;

    private VolleySingleton(Context context) {
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    RequestQueue getRequestQueue() {
        return mRequestQueue;
    }
}
