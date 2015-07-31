package com.framgia.lupx.frss;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Typeface;

/**
 * Created by FRAMGIA\pham.xuan.lu on 24/07/2015.
 */
public class FRSSApplication extends Application {

    private String FONT_OPENSANS_REGULAR = "fonts/OpenSans-Regular.ttf";
    private String FONT_OPENSANS_LIGHT = "fonts/OpenSans-Light.ttf";
    private String FONT_ROBOTO_CONDENSED_LIGHT = "fonts/RobotoCondensed-Light.ttf";
    private String PACKAGE_NAME;
    private final int DISK_CACHE_SIZE = 10 * 1024 * 1024;
    private final Bitmap.CompressFormat DISK_CACHE_FORMAT = Bitmap.CompressFormat.JPEG;
    private final int DISK_CACHE_QUALITY = 80;

    @Override
    public void onCreate() {
        super.onCreate();
        PACKAGE_NAME = getPackageName();
        AppConfigs.getInstance().DATABASE_NAME = getPackageName();
        AppConfigs.getInstance().ROBOTO_CL = Typeface.createFromAsset(getAssets(), FONT_ROBOTO_CONDENSED_LIGHT);
        AppConfigs.getInstance().openSansLight = Typeface.createFromAsset(getAssets(), FONT_OPENSANS_LIGHT);
        AppConfigs.getInstance().SCREEN_DENSITY = getApplicationContext().getResources().getDisplayMetrics().density;
    }

}