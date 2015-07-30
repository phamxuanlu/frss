package com.framgia.lupx.frss.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by FRAMGIA\pham.xuan.lu on 27/07/2015.
 */
public class BitmapLruCache {
    private LruCache<String, Bitmap> bitmapLruCache;
    private static final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024.0);
    private static final int cacheSize = maxMemory / 8;
    private static BitmapLruCache _instance;

    private BitmapLruCache() {
        bitmapLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

    public static BitmapLruCache getInstance() {
        if (_instance == null) {
            _instance = new BitmapLruCache();
        }
        return _instance;
    }

    public void put(String key, Bitmap value) {
        if (bitmapLruCache.get(key) == null) {
            bitmapLruCache.put(key, value);
        }
    }

    public Bitmap get(String key) {
        return bitmapLruCache.get(key);
    }

}