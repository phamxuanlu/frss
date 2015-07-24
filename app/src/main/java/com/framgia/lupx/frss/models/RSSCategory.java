package com.framgia.lupx.frss.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FRAMGIA\pham.xuan.lu on 24/07/2015.
 */
public class RSSCategory {
    public String url;
    public String name;

    public List<RSSItem> items = new ArrayList<>();

    public RSSCategory(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public RSSCategory() {
        super();
    }

}