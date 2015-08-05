package com.framgia.lupx.frss;

import com.framgia.lupx.frss.models.RSSCategory;
import com.google.android.gms.maps.model.LatLng;

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
        lst.add(new RSSCategory("Vietnam", "http://www.telegraph.co.uk/travel/destinations/asia/vietnam/rss", new LatLng(21.0285, 105.8542), R.drawable.vietnam));
        lst.add(new RSSCategory("Vienna", "http://www.telegraph.co.uk/travel/destinations/europe/austria/vienna/rss", new LatLng(48.2000, 16.3667), R.drawable.vienna));
        lst.add(new RSSCategory("Toronto", "http://www.telegraph.co.uk/travel/destinations/northamerica/canada/toronto/rss", new LatLng(43.691200, -79.341667), R.drawable.toronto));
        lst.add(new RSSCategory("South Korea", "http://www.telegraph.co.uk/travel/destinations/asia/southkorea/rss", new LatLng(37.5500, 126.9667), R.drawable.southkorea));
        lst.add(new RSSCategory("South Africa", "http://www.telegraph.co.uk/travel/destinations/africaandindianocean/southafrica/rss", new LatLng(30.0000, 25.0000), R.drawable.southafrica));
        lst.add(new RSSCategory("Washington DC", "http://www.telegraph.co.uk/travel/destinations/northamerica/usa/washingtondc/rss", new LatLng(38.9047, 77.0164), R.drawable.washington));
        lst.add(new RSSCategory("Thailand", "http://www.telegraph.co.uk/travel/destinations/asia/thailand/rss", new LatLng(13.7500, 100.4833), R.drawable.thailand));
        lst.add(new RSSCategory("San Francisco", "http://www.telegraph.co.uk/travel/destinations/northamerica/usa/sanfrancisco/rss", new LatLng(37.7833, -122.431297), R.drawable.sanfrancisco));
        lst.add(new RSSCategory("Scotland", "http://www.telegraph.co.uk/travel/destinations/europe/uk/scotland/rss", new LatLng(55.9500, 3.1833), R.drawable.scotland));
        lst.add(new RSSCategory("Singapore", "http://www.telegraph.co.uk/travel/destinations/asia/singapore/rss", new LatLng(1.3000, 103.8000), R.drawable.singapore));
        lst.add(new RSSCategory("Norway", "http://www.telegraph.co.uk/travel/destinations/europe/norway/rss", new LatLng(61.0000, 8.0000), R.drawable.norway));
        lst.add(new RSSCategory("New Zealand", "http://www.telegraph.co.uk/travel/destinations/australiaandpacific/newzealand/rss", new LatLng(42.0000, 174.0000), R.drawable.newzealand));
        lst.add(new RSSCategory("Netherlands", "http://www.telegraph.co.uk/travel/destinations/europe/netherlands/rss", new LatLng(52.3167, 5.5500), R.drawable.netherlands));
        lst.add(new RSSCategory("Munich", "http://www.telegraph.co.uk/travel/destinations/europe/germany/munich/rss", new LatLng(48.1333, 11.5667), R.drawable.munich));
        lst.add(new RSSCategory("Milan", "http://www.telegraph.co.uk/travel/destinations/europe/italy/milan/rss", new LatLng(45.4667, 9.1833), R.drawable.milan));

        return lst;
    }

    public static FakeData getInstance() {
        if (_instance == null)
            _instance = new FakeData();
        return _instance;
    }

}