package com.framgia.lupx.frss.rssparser;

import android.text.Html;
import android.util.Xml;

import com.framgia.lupx.frss.models.RSSCategory;
import com.framgia.lupx.frss.models.RSSItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * Created by FRAMGIA\pham.xuan.lu on 27/07/2015.
 */
public class RSSParser {

    public RSSCategory parse(String xml) throws XmlPullParserException, IOException {
        RSSCategory cat = new RSSCategory();
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            parser.setInput(is, null);
            parser.nextTag();
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, null, "channel");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals("item")) {
                    cat.items.add(parseItem(parser));
                } else {
                    skip(parser);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return cat;
    }

    private RSSItem parseItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        RSSItem item = new RSSItem();
        parser.require(XmlPullParser.START_TAG, null, "item");
        String title = null;
        String link = null;
        String description = null;
        String enclosure = null;
        String pubDate = null;
        String author = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("link")) {
                link = readLink(parser);
            } else if (name.equals("description")) {
                description = readDescription(parser);
            } else if (name.equals("enclosure")) {
                enclosure = readEnclosure(parser);
            } else if (name.equals("pubDate")) {
                pubDate = readPubDate(parser);
            } else if (name.equals("author")) {
                author = readAuthor(parser);
            } else {
                skip(parser);
            }
        }
        item.title = title;
        item.link = link;
        item.description = description;
        item.enclosure = enclosure;
        item.pubDate = pubDate;
        item.author = author;
        return item;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private String readAuthor(XmlPullParser parser) throws IOException, XmlPullParserException {
        String author = "";
        parser.require(XmlPullParser.START_TAG, null, "author");
        author = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "author");
        return author;
    }

    private String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        String pubDate = "";
        parser.require(XmlPullParser.START_TAG, null, "pubDate");
        pubDate = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "pubDate");
        return pubDate;
    }

    private String readEnclosure(XmlPullParser parser) throws IOException, XmlPullParserException {
        String en = "";
        parser.require(XmlPullParser.START_TAG, null, "enclosure");
        String tag = parser.getName();
        if (tag.equals("enclosure")) {
            en = parser.getAttributeValue(null, "url");
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, null, "enclosure");
        return en;
    }

    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        String des = "";
        parser.require(XmlPullParser.START_TAG, null, "description");
        des = readText(parser);
        int index = des.indexOf("<br");
        des = des.substring(0, index);
        parser.require(XmlPullParser.END_TAG, null, "description");
        return des;
    }

    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = "";
        parser.require(XmlPullParser.START_TAG, null, "link");
        link = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "link");
        return link;
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "title");
        return title;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

}