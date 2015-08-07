package com.framgia.lupx.frss.asynctasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.framgia.lupx.frss.cache.BitmapLruCache;
import com.framgia.lupx.frss.BuildConfig;
import com.framgia.lupx.frss.cache.DiskBitmapCache;
import com.framgia.lupx.frss.utils.BitmapUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by FRAMGIA\pham.xuan.lu on 27/07/2015.
 */
public class LoadImageAsyncTask extends AsyncTask<String, Void, Void> {
    public interface OnExecutedListener {
        void onExecuted(Bitmap bm);
    }

    private static final int IMAGE_DESIRED_WIDTH = 150;
    private static final int IMAGE_DESIRED_HEIGHT = 150;
    private WeakReference<ImageView> imgRef;
    private Context context;
    private String link;
    private Bitmap bitmap;
    private boolean isDiskCache;
    private OnExecutedListener listener;

    public LoadImageAsyncTask(Context context, ImageView imgV) {
        this.context = context;
        this.imgRef = new WeakReference<ImageView>(imgV);
    }

    public void setIsDiskCache(boolean isLFD) {
        this.isDiskCache = isLFD;
    }

    public void setOnExecutedListener(OnExecutedListener listener) {
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(String... params) {
        link = params[0];
        if (BitmapLruCache.getInstance().get(link) != null) {
            bitmap = BitmapLruCache.getInstance().get(link);
            if (isDiskCache && bitmap != null) {
                DiskBitmapCache.getInstance(context).put(link, bitmap);
            }
            return null;
        } else if (DiskBitmapCache.getInstance(context).containKey(link)) {
            bitmap = DiskBitmapCache.getInstance(context).get(link);
            return null;
        }
        if (isCancelled()) {
            return null;
        }
        try {
            URL url = new URL(this.link);
            InputStream in = url.openStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            options.inSampleSize = BitmapUtils.inSampleSize(options, IMAGE_DESIRED_WIDTH, IMAGE_DESIRED_HEIGHT);
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(url.openStream(), null, options);
            if (BuildConfig.DEBUG && bitmap != null) {
                Log.v("BITMAP SIZE", "(W,H)=(" + bitmap.getWidth() + "," + bitmap.getHeight() + ")");
            }
            BitmapLruCache.getInstance().put(this.link, bitmap);
            if (isDiskCache) {
                DiskBitmapCache.getInstance(context).put(link, bitmap);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (isCancelled()) {
            return;
        }
        if (imgRef.get() != null) {
            String tag = (String) imgRef.get().getTag();
            if (tag != null && tag.compareTo(link) == 0) {
                if (bitmap != null) {
                    imgRef.get().setImageBitmap(bitmap);
                }
            }
        }
        if (listener != null) {
            listener.onExecuted(bitmap);
        }
    }

}