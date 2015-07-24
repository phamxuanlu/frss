package com.framgia.lupx.frss.asynctasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.framgia.lupx.frss.BitmapLruCache;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by FRAMGIA\pham.xuan.lu on 27/07/2015.
 */
public class LoadImageAsyncTask extends AsyncTask<String, Void, Void> {

    private WeakReference<ImageView> imgRef;
    private Context context;
    private String url;
    private Bitmap bitmap;

    public LoadImageAsyncTask(Context context, ImageView imgV) {
        this.context = context;
        this.imgRef = new WeakReference<ImageView>(imgV);
    }


    @Override
    protected Void doInBackground(String... params) {
        url = params[0];
        if (BitmapLruCache.getInstance().get(url) != null) {
            bitmap = BitmapLruCache.getInstance().get(url);
            return null;
        }
        if (isCancelled()) {
            return null;
        }
        try {
            InputStream in = new URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(in);
            BitmapLruCache.getInstance().put(url, bitmap);
        } catch (IOException ex) {

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (isCancelled()) {
            return;
        }
        if (imgRef.get() != null) {
            if (bitmap != null) {
                imgRef.get().setImageBitmap(bitmap);
            }
        }
    }

}