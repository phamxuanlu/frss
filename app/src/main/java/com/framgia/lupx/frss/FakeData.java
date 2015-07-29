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
        lst.add(new RSSCategory("South Africa", "http://www.telegraph.co.uk/travel/destinations/africaandindianocean/southafrica/rss"));
        lst.add(new RSSCategory("Washington DC", "http://www.telegraph.co.uk/travel/destinations/northamerica/usa/washingtondc/rss"));
        lst.add(new RSSCategory("Thailand", "http://www.telegraph.co.uk/travel/destinations/asia/thailand/rss"));
        lst.add(new RSSCategory("San Francisco", "http://www.telegraph.co.uk/travel/destinations/northamerica/usa/sanfrancisco/rss"));
        lst.add(new RSSCategory("Scotland", "http://www.telegraph.co.uk/travel/destinations/europe/uk/scotland/rss"));
        lst.add(new RSSCategory("Singapore", "http://www.telegraph.co.uk/travel/destinations/asia/singapore/rss"));
        lst.add(new RSSCategory("Norway", "http://www.telegraph.co.uk/travel/destinations/europe/norway/rss"));
        lst.add(new RSSCategory("New Zealand", "http://www.telegraph.co.uk/travel/destinations/australiaandpacific/newzealand/rss"));
        lst.add(new RSSCategory("Netherlands", "http://www.telegraph.co.uk/travel/destinations/europe/netherlands/rss"));
        lst.add(new RSSCategory("Munich", "http://www.telegraph.co.uk/travel/destinations/europe/germany/munich/rss"));
        lst.add(new RSSCategory("Milan", "http://www.telegraph.co.uk/travel/destinations/europe/italy/milan/rss"));

        return lst;
    }

    public static FakeData getInstance() {
        if (_instance == null)
            _instance = new FakeData();
        return _instance;
    }

}