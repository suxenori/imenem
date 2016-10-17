package com.menemi.personobject;

import com.menemi.utils.Utils;

import java.sql.Date;

/**
 * Created by Ui-Developer on 12.07.2016.
 */
public class PersonLike extends PersonObject{

    private Date likedAtDate;
    boolean isMutual = false;
    int val = 0; //TODO: WHAT THE F......
    public PersonLike(int id, String name, String isOnline, String currentLocation, int age, String likedAt, boolean isMutual, int val) {
        super("", "");
        setPersonId(id);
        setPersonName(name);
        setPersonCurrLocation(currentLocation);
        setPersonAge(age);
        setOnline(isOnline);
        setLikedAtDate(likedAt);
        setVal(val);
        setMutual(isMutual);
    }

    public Date getLikedAtDate() {
        return likedAtDate;
    }

    public void setLikedAtDate(String likedAtDate) {
        this.likedAtDate = Utils.getDateFromServer(likedAtDate);
    }

    public boolean isMutual() {
        return isMutual;
    }

    public void setMutual(boolean mutual) {
        isMutual = mutual;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

}
