package com.framgia.lupx.frss;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Typeface;

/**
 * Created by FRAMGIA\pham.xuan.lu on 24/07/2015.
 */
public class FRSSApplication extends Application {

    private String FONT_ROBOTO_THIN = "fonts/Roboto-Thin.ttf";
    private String FONT_ROBOTO_LIGHT = "fonts/Roboto-Light.ttf";
    private String PACKAGE_NAME;
    private final int DISK_CACHE_SIZE = 10 * 1024 * 1024;
    private final Bitmap.CompressFormat DISK_CACHE_FORMAT = Bitmap.CompressFormat.JPEG;
    private final int DISK_CACHE_QUALITY = 80;

    @Override
    public void onCreate() {
        super.onCreate();
        PACKAGE_NAME = getPackageName();
        AppConfigs.getInstance().DATABASE_NAME = getPackageName();
        AppConfigs.getInstance().ROBOTO_LIGHT = Typeface.createFromAsset(getAssets(), FONT_ROBOTO_LIGHT);
        AppConfigs.getInstance().ROBOTO_THIN = Typeface.createFromAsset(getAssets(), FONT_ROBOTO_THIN);
        AppConfigs.getInstance().SCREEN_DENSITY = getApplicationContext().getResources().getDisplayMetrics().density;
        AppConfigs.getInstance().CACHE_DIR = ".thumbnails";
    }

}