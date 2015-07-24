package com.framgia.lupx.frss;

import android.graphics.Typeface;

/**
 * Created by FRAMGIA\pham.xuan.lu on 24/07/2015.
 */
public class AppConfigs {
    private static AppConfigs _instance;
    public Typeface openSansRegular;
    public Typeface openSansLight;

    private AppConfigs() {
        super();
    }

    public static AppConfigs getInstance() {
        if (_instance == null)
            _instance = new AppConfigs();
        return _instance;
    }

}