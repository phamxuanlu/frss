package com.framgia.lupx.frss.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.framgia.lupx.frss.FRSSRequestQueue;
import com.framgia.lupx.frss.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ArticleDetailActivity extends AppCompatActivity {
    public static final String ARTICLE_URL = "ARTICLE_URL";

    private WebView browser;
    private Toolbar toolbar;
    private ProgressBar loading;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        String url = getIntent().getStringExtra(ARTICLE_URL);
        setupViews();
        loadHtml(url);
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
        browser.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
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
        browser.loadData(storyDiv.toString(), "text/html; charset=UTF-8", null);
        loading.setVisibility(View.GONE);
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

            }
        }));
    }

}