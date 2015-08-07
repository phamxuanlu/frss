package com.framgia.lupx.frss.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.framgia.lupx.frss.AppConfigs;
import com.framgia.lupx.frss.FRSSRequestQueue;
import com.framgia.lupx.frss.R;
import com.framgia.lupx.frss.database.DatabaseHelper;
import com.framgia.lupx.frss.models.RSSItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class ArticleDetailActivity extends AppCompatActivity {

    public static final String ARTICLE_URL = "ARTICLE_URL";

    private WebView browser;
    private Toolbar toolbar;
    private ProgressBar loading;
    private ActionBar actionBar;
    private boolean isFavorite;
    private RSSItem item;
    private DatabaseHelper<RSSItem> itemDbHelper;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.article_detail_menu, menu);
        MenuItem favItem = menu.findItem(R.id.action_category_favorite);
        favItem.setIcon(isFavorite ? R.drawable.ic_favorited32 : R.drawable.ic_no_favorite32);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_category_favorite) {
            isFavorite = !isFavorite;
            if (setFavorite(isFavorite)) {
                item.setIcon(isFavorite ? R.drawable.ic_favorited32 : R.drawable.ic_no_favorite32);
            }
        }
        if (id == R.id.action_maps) {
            Intent intent = new Intent(this, MapActivity.class);
            Bundle bundle = new Bundle();
            bundle.putDouble("LAT", getIntent().getDoubleExtra("LAT", 0));
            bundle.putDouble("LONG", getIntent().getDoubleExtra("LONG", 0));
            bundle.putString("CITY_NAME", getIntent().getStringExtra("CITY_NAME"));
            intent.putExtras(bundle);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean setFavorite(boolean favorite) {
        if (favorite) {
            if (item.htmlContent == null) {
                Toast.makeText(this, "Please wait while content is loading...", Toast.LENGTH_SHORT).show();
                isFavorite = !isFavorite;
                return false;
            }
            item.id = itemDbHelper.insert(item);
            Toast.makeText(this, "This article was added to favorite", Toast.LENGTH_SHORT).show();
        } else {
            itemDbHelper.delete("id=" + item.id + " AND htmlContent IS NOT NULL");
            Toast.makeText(this, "Remove from favorite", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void setupViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextAppearance(this, R.style.ArticleDetailTitle);
        loading = (ProgressBar) findViewById(R.id.loading);
        browser = (WebView) findViewById(R.id.webview);
        browser.getSettings().setJavaScriptEnabled(true);
        //browser.getSettings().setUseWideViewPort(true);
        browser.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        init();
        setupViews();
        loadHtml(item.link);
    }

    private void init() {
        String url = getIntent().getStringExtra(ARTICLE_URL);
        item = new RSSItem();
        item.link = url;
        try {
            itemDbHelper = new DatabaseHelper<RSSItem>(this, AppConfigs.getInstance().DATABASE_NAME, RSSItem.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<RSSItem> caches = itemDbHelper.select("link LIKE'" + url + "' AND htmlContent IS NOT NULL");
        if (caches.size() > 0) {
            isFavorite = true;
            item = caches.get(0);
        }
    }

    private void loadHtml(String url) {
        loading.setVisibility(View.VISIBLE);
        FRSSRequestQueue.getInstance(this).addRequest(new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                processHtml(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadHtmlToBrowser();
            }
        }));
    }

    private void loadHtmlToBrowser() {
        if (item.htmlContent != null) {
            browser.loadData(item.htmlContent, "text/html; charset=UTF-8", null);
            loading.setVisibility(View.GONE);
        } else {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Network Error")
                    .setMessage("No network connections, try again?")
                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            loadHtml(item.link);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .create();
            dialog.show();
        }
    }

    private void processHtml(String html) {
        Document doc = Jsoup.parse(html);
        Elements rmCm = doc.select("div.bylineComments");
        for (Element el : rmCm) {
            el.remove();
        }
        Elements storyDiv = doc.select("div.story");
        Elements imgs = storyDiv.select("img");
        for (Element el : imgs) {
            el.attr("width", "100%");
            el.attr("height", "auto");
        }
        Elements relateDiv = storyDiv.select("div#tmg-related-links");
        for (Element el : relateDiv) {
            el.remove();
        }
        Element storyHead = doc.select("div.storyHead > h1").first();
        Element sliderHead = doc.select("div.tmglSlideshowTop > h1.tmglSlideshowTitle").first();
        String title = storyHead != null ? storyHead.text() : (sliderHead != null ? sliderHead.text() : "Can't read this article.");
        actionBar.setTitle(title);
        item.htmlContent = storyDiv.html();
        loadHtmlToBrowser();
    }

}