package com.framgia.lupx.frss;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by FRAMGIA\pham.xuan.lu on 27/07/2015.
 */
public class FRSSRequestQueue {
    private Context context;
    private RequestQueue _request;
    private static FRSSRequestQueue _instance;

    private FRSSRequestQueue(Context context) {
        this.context = context;
    }

    public static FRSSRequestQueue getInstance(Context context) {
        if (_instance == null)
            _instance = new FRSSRequestQueue(context);

        return _instance;
    }

    public RequestQueue getRequestQueue() {
        if (_request == null)
            _request = Volley.newRequestQueue(context);
        return _request;
    }

    public <T> void addRequest(Request<T> req) {
        getRequestQueue().add(req);
    }

}