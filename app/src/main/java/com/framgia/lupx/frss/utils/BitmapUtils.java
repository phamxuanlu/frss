package com.framgia.lupx.frss.utils;

import android.graphics.BitmapFactory;

/**
 * Created by FRAMGIA\pham.xuan.lu on 06/08/2015.
 */
public class BitmapUtils {

    public static int inSampleSize(BitmapFactory.Options options, int W, int H) {
        final int rW = options.outWidth;
        final int rH = options.outHeight;
        int sampleSize = 1;
        if (rH > H || rW > W) {
            final int h = rH / 2;
            final int w = rW / 2;
            while ((h / sampleSize) > H && (w / sampleSize) > W) {
                sampleSize *= 2;
            }
        }
        return sampleSize;
    }

}