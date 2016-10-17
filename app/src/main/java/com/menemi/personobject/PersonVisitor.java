package com.menemi.personobject;

import com.menemi.utils.Utils;

import java.sql.Date;

/**
 * Created by Ui-Developer on 11.07.2016.
 */
public class PersonVisitor extends PersonObject {

    private Date visitedDate;
    public PersonVisitor(int id, String name, String isOnline, String currentLocation, int age, String visitedAt) {
        super("", "");
        setPersonId(id);
        setPersonName(name);
        setPersonCurrLocation(currentLocation);
        setPersonAge(age);
        setOnline(isOnline);
        setVisitedDate(visitedAt);
    }

    public Date getVisitedDate() {
        return visitedDate;
    }

    public void setVisitedDate(String visitedDate) {
        this.visitedDate =  Utils.getDateFromServer(visitedDate);
    }
}
