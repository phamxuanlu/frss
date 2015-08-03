package com.framgia.lupx.frss.maps;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by FRAMGIA\pham.xuan.lu on 04/08/2015.
 */
public class DirectionUtils {

    public static String getDirectionUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

}