package com.framgia.lupx.frss;

import android.app.Application;
import android.graphics.Typeface;

/**
 * Created by FRAMGIA\pham.xuan.lu on 24/07/2015.
 */
public class FRSSApplication extends Application {

    private String FONT_OPENSANS_REGULAR = "fonts/OpenSans-Regular.ttf";
    private String FONT_OPENSANS_LIGHT = "fonts/OpenSans-Light.ttf";
    private String FONT_ROBOTO_CONDENSED_LIGHT = "fonts/RobotoCondensed-Light.ttf";

    @Override
    public void onCreate() {
        super.onCreate();
        AppConfigs.getInstance().ROBOTO_CL = Typeface.createFromAsset(getAssets(), FONT_ROBOTO_CONDENSED_LIGHT);
        AppConfigs.getInstance().openSansLight = Typeface.createFromAsset(getAssets(), FONT_OPENSANS_LIGHT);
        AppConfigs.getInstance().SCREEN_DENSITY = getApplicationContext().getResources().getDisplayMetrics().density;
    }

}