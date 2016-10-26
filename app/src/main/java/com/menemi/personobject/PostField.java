package com.menemi.personobject;

import com.menemi.dbfactory.DBHandler;

/**
 * Created by Ui-Developer on 26.10.2016.
 */

public class PostField {
    int Id;
    String field;
    String data;

    public PostField(String field, String data) {
        this.Id = DBHandler.getInstance().getMyProfile().getPersonId();
        this.field = field;
        this.data = data;
    }

    public int getId() {
        return Id;
    }

    public String getField() {
        return field;
    }

    public String getData() {
        return data;
    }
}
