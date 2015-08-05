package com.framgia.lupx.frss.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.framgia.lupx.frss.R;
import com.framgia.lupx.frss.asynctasks.DirectionParseAsyncTask;
import com.framgia.lupx.frss.asynctasks.GetDirectionDataAsyncTask;
import com.framgia.lupx.frss.fragments.CustomMapFragment;
import com.framgia.lupx.frss.maps.DirectionUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Map;

/**
 * Created by FRAMGIA\pham.xuan.lu on 03/08/2015.
 */
public class MapActivity extends AppCompatActivity {

    private CustomMapFragment mapFragment;
    private Toolbar toolbar;
    private String locationName;
    private double lat;
    private double lng;
    private ProgressDialog progress;
    private boolean isProcessing;
    private LatLng dest;
    private LatLng origin;

    public String getLocationName() {
        return this.locationName;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_direction) {
            if (!isProcessing) {
                drawDirections();
            }
            return true;
        }
        if (id == R.id.action_reset) {
            mapFragment.animateLatLng(new LatLng(lat, lng));
        }
        return super.onOptionsItemSelected(item);
    }


    private void drawDirections() {
        isProcessing = true;
        progress.show();
        dest = new LatLng(lat, lng);
        Location currentLocation = mapFragment.getMyLocation();
        origin = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(origin).title("Your Location");
        mapFragment.addMarker(markerOptions);
        String directionAPI = DirectionUtils.getDirectionUrl(origin, dest);
        new GetDirectionDataAsyncTask(new GetDirectionDataAsyncTask.OnExecuteCompleteListener() {
            @Override
            public void onExecuted(String result) {
                drawPolyLine(result);
            }
        }).execute(directionAPI);
    }

    private void drawPolyLine(String result) {
        new DirectionParseAsyncTask(new DirectionParseAsyncTask.OnParsedListener() {
            @Override
            public void onParsed(PolylineOptions polylineOptions) {
                if (polylineOptions == null) { //No way
                    AlertDialog alertDialog = new AlertDialog.Builder(MapActivity.this)
                            .setMessage("Sorry, no way from here to " + locationName)
                            .setPositiveButton("OK", null)
                            .create();
                    alertDialog.show();
                } else {
                    mapFragment.addPolyLine(polylineOptions);
                }
                mapFragment.fitPoints(origin, dest);
                isProcessing = false; //End direction
                progress.dismiss();
            }
        }).execute(result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setupViews();
    }

    private void setupViews() {
        mapFragment = new CustomMapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.mapContainer, mapFragment).commit();
        lat = getIntent().getDoubleExtra("LAT", 0);
        lng = getIntent().getDoubleExtra("LONG", 0);
        this.locationName = getIntent().getStringExtra("CITY_NAME");
        getSupportActionBar().setTitle(this.locationName);
        LatLng location = new LatLng(lat, lng);
        mapFragment.setLocation(location);
        mapFragment.setZoomLevel(10);
        progress = new ProgressDialog(this);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setMessage("Processing ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}