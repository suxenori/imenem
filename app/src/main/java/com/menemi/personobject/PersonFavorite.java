package com.menemi.personobject;

import android.graphics.Bitmap;

import com.menemi.utils.Utils;

import java.sql.Date;

/**
 * Created by irondev on 27.06.16.
 */
public class PersonFavorite extends PersonObject{
    private Date addedDate;
    public PersonFavorite(int id, String name, Bitmap avatar, String added) {
        super(EMPTY, EMPTY);
        setPersonId(id);
        setPersonName(name);
        setPersonAvatar(avatar);
        setAddedDate(added);
    }
    public PersonFavorite(PersonObject personObject, int id, String name, Bitmap avatar, String added) {
        super(personObject);
        setPersonId(id);
        setPersonName(name);
        setPersonAvatar(avatar);
        setAddedDate(added);
    }
    public PersonFavorite(PersonObject personObject) {
        super(personObject);
    }
    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }
    public void setAddedDate(String addedDate) {
        this.addedDate = Utils.getDateFromServer(addedDate);
    }

}