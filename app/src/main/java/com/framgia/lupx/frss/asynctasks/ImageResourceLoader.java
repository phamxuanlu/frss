package com.framgia.lupx.frss.asynctasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.framgia.lupx.frss.AppConfigs;
import com.framgia.lupx.frss.cache.BitmapLruCache;
import com.framgia.lupx.frss.utils.BitmapUtils;

import java.lang.ref.WeakReference;

/**
 * Created by FRAMGIA\pham.xuan.lu on 06/08/2015.
 */
public class ImageResourceLoader extends AsyncTask<Integer, Void, Bitmap> {

    private WeakReference<ImageView> img;
    private Context context;
    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;

    public ImageResourceLoader(Context context, ImageView img) {
        this.context = context;
        this.img = new WeakReference<ImageView>(img);
    }

    @Override
    protected Bitmap doInBackground(Integer... params) {
        int res = params[0];
        Bitmap bm = BitmapLruCache.getInstance().get(String.valueOf(res));
        if (bm == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inScaled = false;
            BitmapFactory.decodeResource(context.getResources(), res, options);
            options.inSampleSize = BitmapUtils.inSampleSize(options, MAX_WIDTH, MAX_HEIGHT);
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeResource(context.getResources(), res, options);
            BitmapLruCache.getInstance().put(String.valueOf(res), bm);
        }
        return bm;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (img != null) {
            ImageView imgV = img.get();
            if (imgV != null) {
                imgV.setImageBitmap(bitmap);
            }
        }
    }

}