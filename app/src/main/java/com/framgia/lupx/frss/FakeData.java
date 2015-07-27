package com.framgia.lupx.frss;

import com.framgia.lupx.frss.models.RSSCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FRAMGIA\pham.xuan.lu on 24/07/2015.
 */
public class FakeData {
    private static FakeData _instance;
    public List<RSSCategory> fakeCategories;

    private FakeData() {
        fakeCategories = getCategories();
    }

    private static List<RSSCategory> getCategories() {
        List<RSSCategory> lst = new ArrayList<>();
        lst.add(new RSSCategory("Vietnam", "http://www.telegraph.co.uk/travel/destinations/asia/vietnam/rss"));
        lst.add(new RSSCategory("Vienna", "http://www.telegraph.co.uk/travel/destinations/europe/austria/vienna/rss"));
        lst.add(new RSSCategory("Toronto", "http://www.telegraph.co.uk/travel/destinations/northamerica/canada/toronto/rss"));
        lst.add(new RSSCategory("South Korea", "http://www.telegraph.co.uk/travel/destinations/asia/southkorea/rss"));
        return lst;
    }

    public static FakeData getInstance() {
        if (_instance == null)
            _instance = new FakeData();
        return _instance;
    }

}