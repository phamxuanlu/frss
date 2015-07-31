package com.framgia.lupx.frss.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.framgia.lupx.frss.AppConfigs;
import com.framgia.lupx.frss.utils.HashUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by FRAMGIA\pham.xuan.lu on 31/07/2015.
 */
public class DiskBitmapCache {

    private static DiskBitmapCache _instance;
    private Context context;
    private File dir;
    private Bitmap.CompressFormat format;
    private int BITMAP_COMPRESS_QUALITY = 100;

    private DiskBitmapCache(Context context) {
        this.context = context;
        if (dir == null) {
            this.dir = getCacheDir(AppConfigs.getInstance().CACHE_DIR);
        }
        this.format = Bitmap.CompressFormat.JPEG;
    }

    public static DiskBitmapCache getInstance(Context context) {
        if (_instance == null) {
            _instance = new DiskBitmapCache(context);
        }
        return _instance;
    }

    public void put(String key, Bitmap bitmap) {
        if (containKey(key)) {
            return;
        } else {
            String filename = HashUtils.MD5(key);
            File file = new File(dir, filename);
            try {
                OutputStream outStream = new FileOutputStream(file);
                bitmap.compress(format, 100, outStream);
                Log.v("DISK_CACHE", filename + "  -  " + key);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap get(String key) {
        if (containKey(key)) {
            String filename = HashUtils.MD5(key);
            File file = new File(dir, filename);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            return bitmap;
        } else {
            return null;
        }
    }

    public void remove(String key) {
        if (containKey(key)) {
            String filename = HashUtils.MD5(key);
            File file = new File(dir, filename);
            file.delete();
        }
    }

    public boolean containKey(String key) {
        String filename = HashUtils.MD5(key);
        File file = new File(dir, filename);
        return file.exists();
    }

    public File getCacheDir(String unq) {
        final String cachePath = context.getExternalCacheDir().getPath();
        Log.v("CACHE_DIR", cachePath);
        File dir = new File(cachePath + File.separator + unq);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public void clearCache(final String dirPath) {
        new AsyncTask<Void, Void, Void>() {

            private void deleteFiles(File dir) {
                if (dir.isDirectory()) {
                    File[] files = dir.listFiles();
                    for (File file : files) {
                        deleteFiles(file);
                    }
                }
                if (dir.isFile()) {
                    dir.delete();
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                deleteFiles(getCacheDir(dirPath));
                return null;
            }
        }.execute();
    }

}