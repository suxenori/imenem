package com.menemi.models;

/**
 * Created by tester03 on 09.08.2016.
 */
public class PlaceModel
{
    private String locationLat;
    private String locationLng;
    private String northeastLat;
    private String northeastLng;
    private String southwestLat;
    private String southwestLng;
    private String placeId;
    private String cityName = "люди рядом";

    public PlaceModel(String cityName, String locationLat, String locationLng, String northeastLat, String northeastLng, String southwestLat, String southwestLng)
    {
        this.locationLat = locationLat;
        this.locationLng = locationLng;
        this.northeastLat = northeastLat;
        this.northeastLng = northeastLng;
        this.southwestLat = southwestLat;
        this.southwestLng = southwestLng;
        this.cityName = cityName;
    }

    public PlaceModel(String cityName, String placeId){
        this.cityName = cityName;
        this.placeId = placeId;
    }

    public String getLocationLat()
    {
        return locationLat;
    }

    public String getLocationLng()
    {
        return locationLng;
    }

    public String getNortheastLat()
    {
        return northeastLat;
    }

    public String getNortheastLng()
    {
        return northeastLng;
    }

    public String getSouthwestLat()
    {
        return southwestLat;
    }

    public String getSouthwestLng()
    {
        return southwestLng;
    }

    public String getCityName()
    {
        return cityName;
    }

    public String getPlaceId()
    {
        return placeId;
    }

}
