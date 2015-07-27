package com.framgia.lupx.frss.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.framgia.lupx.frss.FakeData;
import com.framgia.lupx.frss.R;
import com.framgia.lupx.frss.models.RSSCategory;

/**
 * Created by FRAMGIA\pham.xuan.lu on 24/07/2015.
 */
public class CategoryDetailActivity extends AppCompatActivity {
    public static final String CATEGORY_URL_ID = "CATEGORY_URL_ID";
    private Toolbar toolbar;
    private RSSCategory category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        String url = getIntent().getStringExtra(CATEGORY_URL_ID);
        int size = FakeData.getInstance().fakeCategories.size();
        for (int i = 0; i < size; i++) {
            if (FakeData.getInstance().fakeCategories.get(i).url.equals(url)) {
                category = FakeData.getInstance().fakeCategories.get(i);
                break;
            }
        }
        if (actionBar != null) {
            actionBar.setTitle(category.name);
        }
    }

}