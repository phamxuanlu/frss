package com.framgia.lupx.frss.activities;

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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.framgia.lupx.frss.FakeData;
import com.framgia.lupx.frss.FRSSRequestQueue;
import com.framgia.lupx.frss.R;
import com.framgia.lupx.frss.asynctasks.RSSParsingAsyncTask;
import com.framgia.lupx.frss.fragments.CategoryDetailFragment;
import com.framgia.lupx.frss.fragments.GetDataCallback;
import com.framgia.lupx.frss.models.RSSCategory;

/**
 * Created by FRAMGIA\pham.xuan.lu on 24/07/2015.
 */

public class CategoryDetailActivity extends AppCompatActivity implements GetDataCallback<RSSCategory> {
    public static final String CATEGORY_URL_ID = "CATEGORY_URL_ID";
    private Toolbar toolbar;
    private RSSCategory currCat;
    private ProgressBar loading;
    private boolean isShowGrid = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_show_grid) {
            isShowGrid = !isShowGrid;
            if (isShowGrid) {
                showGrid();
            } else {
                showList();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        loading = (ProgressBar) findViewById(R.id.loading);
        String url = getIntent().getStringExtra(CATEGORY_URL_ID);
        int size = FakeData.getInstance().fakeCategories.size();
        for (int i = 0; i < size; i++) {
            if (FakeData.getInstance().fakeCategories.get(i).url.equals(url)) {
                currCat = FakeData.getInstance().fakeCategories.get(i);
                break;
            }
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
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
                        RSSParsingAsyncTask task = new RSSParsingAsyncTask();
                        task.setOnParsedListener(new RSSParsingAsyncTask.RSSParserCompletedListener() {
                            @Override
                            public void onParsed(RSSCategory category) {
                                currCat = category;
                                loading.setVisibility(View.GONE);
                                if (isShowGrid) {
                                    showGrid();
                                } else {
                                    showList();
                                }
                            }
                        });
                        task.execute(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }));
    }

    private void showGrid() {
        CategoryDetailFragment fragment = new CategoryDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(CategoryDetailFragment.IS_SHOW_GRID, true);
        fragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void showList() {
        CategoryDetailFragment fragment = new CategoryDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(CategoryDetailFragment.IS_SHOW_GRID, false);
        fragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public RSSCategory getData() {
        return currCat;
    }

}