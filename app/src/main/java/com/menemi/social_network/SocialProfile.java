package com.menemi.social_network;

import android.graphics.Bitmap;

/**
 * Created by tester03 on 05.09.2016.
 */
public class SocialProfile
{
    private String id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String fullName;
    private Bitmap image;
    private String gender;
    private String photoUrl;

    public SocialProfile (String id, String fullName, String gender){
        this.id = id;
        this.fullName = fullName;
        this.gender = gender;
    }

    public SocialProfile (){

    }

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getMiddleName()
    {
        return middleName;
    }

    public void setMiddleName(String middleName)
    {
        this.middleName = middleName;
    }

    public Bitmap getImage()
    {
        return image;
    }

    public void setImage(Bitmap image)
    {
        this.image = image;
    }

    public String getPhotoUrl()
    {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl)
    {
        this.photoUrl = photoUrl;
    }
}
