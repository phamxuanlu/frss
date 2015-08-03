package com.framgia.lupx.frss.fragments;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.lupx.frss.R;
import com.framgia.lupx.frss.activities.CategoryDetailActivity;
import com.framgia.lupx.frss.activities.MapActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * Created by FRAMGIA\pham.xuan.lu on 03/08/2015.
 */
public class CustomMapFragment extends Fragment {

    private MapView mapView;
    private GoogleMap map;
    private float zoomLevel;
    private LatLng location;
    private String cityName;
    private Location myLocation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_map, container, false);
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        setupViews(view);
        return view;
    }

    public void addPolyLine(PolylineOptions lines) {
        this.map.addPolyline(lines);
    }

    public void addMarker(MarkerOptions mark) {
        this.map.addMarker(mark);
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void setZoomLevel(float lv) {
        this.zoomLevel = lv;
    }

    public void animateLatLng(LatLng location) {
        map.animateCamera(CameraUpdateFactory.newLatLng(location));
    }

    private void setupViews(View view) {
        cityName = ((MapActivity) getActivity()).getLocationName();
        try {
            map = mapView.getMap();
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.setMyLocationEnabled(true);
            MapsInitializer.initialize(getActivity());
            CameraUpdate init = CameraUpdateFactory.newLatLngZoom(this.location, this.zoomLevel);
            map.addMarker(new MarkerOptions()
                    .position(this.location)
                    .title(cityName));
            map.animateCamera(init);
            myLocation = getBestKnowLocation();
            if (myLocation == null) {
                myLocation = new Location("");
                myLocation.setLatitude(20.9397);
                myLocation.setLongitude(106.3306);
            }
            Log.v("LOCATION", "(Lat,Long)=(" + myLocation.getLatitude() + "," + myLocation.getLongitude() + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Location getBestKnowLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        List<String> provoders = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : provoders) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}