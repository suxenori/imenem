package com.menemi.filter;

import com.menemi.models.PlaceModel;

/**
 * Created by tester03 on 26.07.2016.
 */
public class FilterObject
{
    private int iWantValue;
    private int iAmHereTo;
    private int minAge;
    private int maxAge;
    private int personId;
    private int isOnline;
    private PlaceModel placeModel;
    public FilterObject(int iWantValue, int iAmHereTo, int minAge, int maxAge, int personId, int isOnline, PlaceModel placeModel)
    {
        this.iWantValue = iWantValue;
        this.iAmHereTo = iAmHereTo;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.personId = personId;
        this.placeModel = placeModel;
        this.isOnline = isOnline;
    }

    public FilterObject(int iWantValue, int iAmHereTo, int minAge, int maxAge, int personId, int isOnline)
    {
        this.iWantValue = iWantValue;
        this.iAmHereTo = iAmHereTo;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.personId = personId;
        this.isOnline = isOnline;
    }
    public FilterObject(FilterObject oldFilterObject)
    {
        this.iWantValue = oldFilterObject.iWantValue;
        this.iAmHereTo = oldFilterObject.iAmHereTo;
        this.minAge = oldFilterObject.minAge;
        this.maxAge = oldFilterObject.maxAge;
        this.personId = oldFilterObject.personId;
        this.placeModel = oldFilterObject.placeModel;
        this.isOnline = oldFilterObject.isOnline;
    }

    public FilterObject(){

    }

    public int getIsOnline()
    {
        return isOnline;
    }

    public void setIsOnline(int isOnline)
    {
        this.isOnline = isOnline;
    }

    public PlaceModel getPlaceModel()
    {
        return placeModel;
    }

    public void setPlaceModel(PlaceModel placeModel)
    {
        this.placeModel = placeModel;
    }

    public void setiAmHereTo(int iAmHereTo)
    {
        this.iAmHereTo = iAmHereTo;
    }

    public void setMinAge(int minAge)
    {
        this.minAge = minAge;
    }

    public void setMaxAge(int maxAge)
    {
        this.maxAge = maxAge;
    }

    public int getiWantValue()
    {
        return iWantValue;
    }

    public int getiAmHereTo()
    {
        return iAmHereTo;
    }

    public int getMinAge()
    {
        return minAge;
    }

    public int getMaxAge()
    {
        return maxAge;
    }

    public int getPersonId()
    {
        return personId;
    }

    public void setPersonId(int personId)
    {
        this.personId = personId;
    }

    public void setiWantValue(int iWantValue)
    {
        this.iWantValue = iWantValue;
    }

    @Override
    public String toString()
    {
        return "iWantValue = " + iWantValue + ", iAmHereTo = " + iAmHereTo + ", minAge = " + minAge + ", maxAge = " + maxAge + ", personId = " + personId;
    }
}
