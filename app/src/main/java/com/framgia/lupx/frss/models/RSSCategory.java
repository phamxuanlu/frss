package com.framgia.lupx.frss.models;

import com.framgia.lupx.frss.database.DBField;
import com.framgia.lupx.frss.database.DBKey;
import com.framgia.lupx.frss.database.DBTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FRAMGIA\pham.xuan.lu on 24/07/2015.
 */
@DBTable(tableName = "RSSCategory")
public class RSSCategory {
    @DBField(fieldName = "id", type = "integer")
    @DBKey(keyName = "id")
    public int id;

    @DBField(fieldName = "url", type = "text")
    public String url;

    @DBField(fieldName = "name", type = "text")
    public String name;

    public List<RSSItem> items = new ArrayList<>();

    public RSSCategory(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public RSSCategory() {
        super();
    }

}