package com.framgia.lupx.frss.asynctasks;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.framgia.lupx.frss.maps.DirectionJSONParser;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by FRAMGIA\pham.xuan.lu on 04/08/2015.
 */
public class DirectionParseAsyncTask extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {

    public interface OnParsedListener {
        void onParsed(PolylineOptions polylineOptions);
    }

    private OnParsedListener listener;

    public DirectionParseAsyncTask(OnParsedListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... params) {
        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;
        try {
            jObject = new JSONObject(params[0]);
            DirectionJSONParser parser = new DirectionJSONParser();
            routes = parser.parse(jObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
        ArrayList<LatLng> points;
        PolylineOptions lineOptions = null;
        HashMap<String, String> point;
        double lat;
        double lng;
        for (int i = 0; i < lists.size(); i++) {
            points = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();
            List<HashMap<String, String>> path = lists.get(i);
            for (int j = 0; j < path.size(); j += 2) {
                point = path.get(j);
                lat = Double.parseDouble(point.get("lat"));
                lng = Double.parseDouble(point.get("lng"));
                LatLng pos = new LatLng(lat, lng);
                points.add(pos);
            }
            Log.v("NUMBER OF POINTS", "" + points.size());
            lineOptions.addAll(points);
            lineOptions.width(5);
            lineOptions.color(Color.BLUE);
        }
        if (listener != null) {
            listener.onParsed(lineOptions);
        }
    }

}