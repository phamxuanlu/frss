package com.framgia.lupx.frss.asynctasks;

import android.os.AsyncTask;

import com.framgia.lupx.frss.models.RSSCategory;
import com.framgia.lupx.frss.rssparser.RSSParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by FRAMGIA\pham.xuan.lu on 27/07/2015.
 */
public class RSSParsingAsyncTask extends AsyncTask<String, Void, Void> {

    private RSSCategory category;

    public interface RSSParserCompletedListener {
        void onParsed(RSSCategory category);
    }

    private RSSParserCompletedListener listener;

    public void setOnParsedListener(RSSParserCompletedListener listener) {
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(String... params) {
        String xml = params[0];
        RSSParser parser = new RSSParser();
        try {
            category = parser.parse(xml);

        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (listener != null) {
            listener.onParsed(category);
        }
    }

}