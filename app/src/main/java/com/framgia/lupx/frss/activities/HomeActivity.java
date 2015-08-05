package com.framgia.lupx.frss.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.framgia.lupx.frss.R;
import com.framgia.lupx.frss.adapters.HomeViewPagerAdapter;
import com.framgia.lupx.frss.adapters.RecyclerViewItemClickListener;
import com.framgia.lupx.frss.dialog.AboutDialog;
import com.framgia.lupx.frss.fragments.CategoriesFragment;
import com.framgia.lupx.frss.fragments.DrawerFragment;
import com.framgia.lupx.frss.models.NavDrawerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FRAMGIA\pham.xuan.lu on 24/07/2015.
 */
public class HomeActivity extends AppCompatActivity implements DrawerFragment.GetNavItemsCallback {

    @Override
    public RecyclerViewItemClickListener getItemClickListener() {
        return new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                switch (position) {
                    case 0:
                        viewPager.setCurrentItem(0);
                        break;
                    case 1:
                        viewPager.setCurrentItem(1);
                        break;
                    case 2:
                        AboutDialog aboutDialog = new AboutDialog(HomeActivity.this);
                        aboutDialog.show();
                        break;
                }
                drawerFragment.closeNavDrawer();
            }
        };
    }

    private DrawerFragment drawerFragment;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public List<NavDrawerItem> getNavItems() {
        List<NavDrawerItem> items = new ArrayList<>();
        items.add(new NavDrawerItem(R.drawable.ic_news, "News"));
        items.add(new NavDrawerItem(R.drawable.ic_favorite, "Favorite"));
        items.add(new NavDrawerItem(R.drawable.ic_about, "About"));
        return items;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupViews();
    }

    private void setupViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.tabanim_viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabanim_tabs);
        tabLayout.setupWithViewPager(viewPager);
        drawerFragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setup(R.id.fragment_navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                toolbar);
    }

    private void setupViewPager(ViewPager viewPager) {
        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CategoriesFragment(), "News");
        CategoriesFragment favoriteFragment = new CategoriesFragment();
        favoriteFragment.setLoadFavorite(true);
        adapter.addFragment(favoriteFragment, "Favorite");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            AboutDialog aboutDialog = new AboutDialog(HomeActivity.this);
            aboutDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}