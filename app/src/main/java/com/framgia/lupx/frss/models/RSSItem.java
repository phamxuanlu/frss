package com.framgia.lupx.frss.models;

import com.framgia.lupx.frss.database.DBField;
import com.framgia.lupx.frss.database.DBKey;
import com.framgia.lupx.frss.database.DBTable;

/**
 * Created by FRAMGIA\pham.xuan.lu on 24/07/2015.
 */
@DBTable(tableName = "RSSItem")
public class RSSItem {
    @DBField(fieldName = "id", type = "integer")
    @DBKey(keyName = "id")
    public int id;

    @DBField(fieldName = "htmlContent", type = "text")
    public String htmlContent;

    @DBField(fieldName = "catId", type = "integer")
    public int catId;

    @DBField(fieldName = "title", type = "text")
    public String title;

    @DBField(fieldName = "link", type = "text")
    public String link;

    @DBField(fieldName = "description", type = "text")
    public String description;

    @DBField(fieldName = "thumbnail", type = "text")
    public String thumbnail;

    @DBField(fieldName = "pubDate", type = "text")
    public String pubDate;

    @DBField(fieldName = "author", type = "text")
    public String author;
}