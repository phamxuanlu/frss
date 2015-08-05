package com.framgia.lupx.frss.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.framgia.lupx.frss.AppConfigs;
import com.framgia.lupx.frss.FRSSRequestQueue;
import com.framgia.lupx.frss.FakeData;
import com.framgia.lupx.frss.R;
import com.framgia.lupx.frss.asynctasks.LoadImageAsyncTask;
import com.framgia.lupx.frss.asynctasks.RSSParsingAsyncTask;
import com.framgia.lupx.frss.cache.DiskBitmapCache;
import com.framgia.lupx.frss.database.DatabaseHelper;
import com.framgia.lupx.frss.fragments.CategoryDetailFragment;
import com.framgia.lupx.frss.fragments.GetDataCallback;
import com.framgia.lupx.frss.models.RSSCategory;
import com.framgia.lupx.frss.models.RSSItem;

import java.util.List;

/**
 * Created by FRAMGIA\pham.xuan.lu on 24/07/2015.
 */
public class CategoryDetailActivity extends AppCompatActivity implements GetDataCallback<RSSCategory> {
    public static final String CATEGORY_URL_ID = "CATEGORY_URL_ID";
    private static final String IS_DISPLAY_TYPE_GRID = "IS_DISPLAY_TYPE_GRID";
    private Toolbar toolbar;
    private RSSCategory currCat;
    private ProgressBar loading;
    private boolean isShowGrid = false;
    private SharedPreferences pref;
    private ActionBar actionBar;
    private CategoryDetailFragment listFragment;
    private CategoryDetailFragment gridFragment;
    private boolean isFavorite;
    private DatabaseHelper<RSSCategory> catDbHelper;
    private DatabaseHelper<RSSItem> itemDbHelper;
    private RSSParsingAsyncTask task;
    private ProgressDialog progress;
    private int numpic = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category_detail, menu);
        MenuItem item = menu.findItem(R.id.action_show_grid);
        item.setIcon(isShowGrid ? R.drawable.ic_list : R.drawable.ic_grid);
        MenuItem favItem = menu.findItem(R.id.action_category_favorite);
        favItem.setIcon(isFavorite ? R.drawable.ic_favorited32 : R.drawable.ic_no_favorite32);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_show_grid) {
            isShowGrid = !isShowGrid;
            item.setIcon(isShowGrid ? R.drawable.ic_list : R.drawable.ic_grid);
            if (isShowGrid) {
                showGrid();
            } else {
                showList();
            }
            return true;
        }
        if (id == R.id.action_category_favorite) {
            isFavorite = !isFavorite;
            setFavorite(isFavorite);
            item.setIcon(isFavorite ? R.drawable.ic_favorited32 : R.drawable.ic_no_favorite32);
        }
        if (id == R.id.action_maps) {
            Intent intent = new Intent(this, MapActivity.class);
            Bundle bundle = new Bundle();
            bundle.putDouble("LAT", currCat.cordinateLat);
            bundle.putDouble("LONG", currCat.cordinateLong);
            bundle.putString("CITY_NAME", currCat.name);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFavorite(boolean isFavorite) {
        if (isFavorite) {
            progress.show();
            int catid = catDbHelper.insert(currCat);
            for (int i = 0; i < currCat.items.size(); i++) {
                RSSItem item = currCat.items.get(i);
                item.catId = catid;
                item.id = itemDbHelper.insert(item);
                if (item.thumbnail != null) {
                    LoadImageAsyncTask saveTask = new LoadImageAsyncTask(this, null);
                    saveTask.setIsDiskCache(true);
                    numpic++;
                    saveTask.setOnExecutedListener(new LoadImageAsyncTask.OnExecutedListener() {
                        @Override
                        public void onExecuted(Bitmap bm) {
                            numpic--;
                            if (numpic == 0) {
                                progress.dismiss();
                            }
                        }
                    });
                    saveTask.execute(item.thumbnail);
                }
            }
            Toast.makeText(this, currCat.name + " added to favorites", Toast.LENGTH_SHORT).show();
        } else {
            catDbHelper.delete(" url LIKE '" + currCat.url + "'");
            for (int i = 0; i < currCat.items.size(); i++) {
                RSSItem item = currCat.items.get(i);
                itemDbHelper.delete(" link LIKE '" + item.link + "'");
                if (item.thumbnail != null) {
                    DiskBitmapCache.getInstance(this).remove(item.thumbnail);
                }
            }
            Toast.makeText(this, currCat.name + "was remove from favorites", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFavorite() {
        try {
            catDbHelper = new DatabaseHelper<>(this, AppConfigs.getInstance().DATABASE_NAME, RSSCategory.class);
            itemDbHelper = new DatabaseHelper<>(this, AppConfigs.getInstance().DATABASE_NAME, RSSItem.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<RSSCategory> cats = catDbHelper.select(" url LIKE '" + currCat.url + "'");
        if (cats != null && cats.size() > 0) {
            isFavorite = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        setupViews();
        loadCategory();
        loadFavorite();
    }

    private void setupViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        loading = (ProgressBar) findViewById(R.id.loading);
        progress = new ProgressDialog(this);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setMessage("Processing ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    private void loadCategory() {
        pref = getSharedPreferences("FRSS", MODE_PRIVATE);
        String lastUrl = pref.getString(CATEGORY_URL_ID, CATEGORY_URL_ID);
        isShowGrid = pref.getBoolean(IS_DISPLAY_TYPE_GRID, false);
        String url = getIntent().getStringExtra(CATEGORY_URL_ID);
        if (url == null) {
            url = lastUrl;
        }
        int size = FakeData.getInstance().fakeCategories.size();
        for (int i = 0; i < size; i++) {
            if (FakeData.getInstance().fakeCategories.get(i).url.equals(url)) {
                currCat = FakeData.getInstance().fakeCategories.get(i);
                pref.edit().putString(CATEGORY_URL_ID, url).apply();
                break;
            }
        }
        actionBar.setTitle(currCat.name);
        fetchData(url);
    }

    private void fetchData(String url) {
        loading.setVisibility(View.VISIBLE);
        FRSSRequestQueue.getInstance(this).addRequest(new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        task = new RSSParsingAsyncTask();
                        task.setOnParsedListener(new RSSParsingAsyncTask.RSSParserCompletedListener() {
                            @Override
                            public void onParsed(RSSCategory category) {
                                if (category != null) {
                                    currCat.items = category.items;
                                }
                                showData();
                            }
                        });
                        task.execute(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadCache();
                    }
                }));
    }

    private void showData() {
        loading.setVisibility(View.GONE);
        if (isShowGrid) {
            showGrid();
        } else {
            showList();
        }
    }

    private void loadCache() {
        List<RSSCategory> lst = catDbHelper.select("url LIKE '" + currCat.url + "'");
        if (lst.size() > 0) {
            currCat = lst.get(0);
            List<RSSItem> items = itemDbHelper.select("catId = " + currCat.id);
            currCat.items = items;
            showData();
        } else {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Network Error")
                    .setMessage("No network connections, try again?")
                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fetchData(currCat.url);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .create();
            dialog.show();
        }
    }

    private void showGrid() {
        if (gridFragment == null) {
            gridFragment = new CategoryDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(CategoryDetailFragment.IS_SHOW_GRID, true);
            gridFragment.setArguments(bundle);
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container, gridFragment);
        transaction.commit();
        pref.edit().putBoolean(IS_DISPLAY_TYPE_GRID, true).apply();
    }

    private void showList() {
        if (listFragment == null) {
            listFragment = new CategoryDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(CategoryDetailFragment.IS_SHOW_GRID, false);
            listFragment.setArguments(bundle);
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container, listFragment);
        transaction.commit();
        pref.edit().putBoolean(IS_DISPLAY_TYPE_GRID, false).apply();
    }

    @Override
    public RSSCategory getData() {
        return currCat;
    }

    @Override
    protected void onPause() {
        if (task != null && !task.isCancelled()) {
            task.cancel(true);
        }
        super.onPause();
    }

}