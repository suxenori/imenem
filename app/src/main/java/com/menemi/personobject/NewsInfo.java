package com.menemi.personobject;

import java.sql.Date;

/**
 * Created by Ui-Developer on 16.11.2016.
 */

public class NewsInfo {
    public NewsInfo(String name, String action, int type, String imageUrl, Date date, int id) {
        this.name = name;
        this.action = action;
        this.type = NEWS_TYPE.values()[type-1];
        this.date = date;
        this.id = id;
        this.imageNewsUrl = imageUrl;
    }

    private String name;
    private String action;
    private NEWS_TYPE type;
    private Date date;
    private int id;
    private String imageNewsUrl;

    public String getImageNewsUrl()
    {
        return imageNewsUrl;
    }

    public String getName() {
        return name;
    }

    public String getAction() {
        return action;
    }

    public NEWS_TYPE getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public enum NEWS_TYPE{
        LIKE,
        PHOTO,
        FAVORITE
    }
}
