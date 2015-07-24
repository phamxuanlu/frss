package com.framgia.lupx.frss;

import android.app.Application;
import android.graphics.Typeface;

/**
 * Created by FRAMGIA\pham.xuan.lu on 24/07/2015.
 */
public class FRSSApplication extends Application {

    private String FONT_OPENSANS_REGULAR = "font/OpenSans-Regular.ttf";
    private String FONT_OPENSANS_LIGHT = "font/OpenSans-Light.ttf";

    @Override
    public void onCreate() {
        super.onCreate();
        AppConfigs.getInstance().openSansRegular = Typeface.createFromAsset(getAssets(), FONT_OPENSANS_REGULAR);
        AppConfigs.getInstance().openSansLight = Typeface.createFromAsset(getAssets(), FONT_OPENSANS_LIGHT);
    }

}