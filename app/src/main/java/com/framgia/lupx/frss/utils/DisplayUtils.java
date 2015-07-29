package com.framgia.lupx.frss.utils;

import com.framgia.lupx.frss.AppConfigs;

/**
 * Created by FRAMGIA\pham.xuan.lu on 29/07/2015.
 */
public class DisplayUtils {

    public static int dpToPixels(int dp) {
        return Math.round(dp * AppConfigs.getInstance().SCREEN_DENSITY);
    }

}