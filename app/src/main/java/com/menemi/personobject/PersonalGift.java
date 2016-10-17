package com.menemi.personobject;

import java.io.Serializable;

/**
 * Created by Ui-Developer on 26.07.2016.
 */
public class PersonalGift implements Serializable{
    private int personId;
    private int giftId;
    private String personName;
    private String giftName;
    private int personAge;
    private String avatarUrl;
    private String sendDate = "30.04.1987";


    public PersonalGift(int personId, int giftId, String personName, String giftName, int personAge, String avatarUrl, String sendDate) {
        this.personId = personId;
        this.giftId = giftId;
        this.personName = personName;
        this.giftName = giftName;
        this.personAge = personAge;
        this.avatarUrl = avatarUrl;
        this.sendDate = sendDate;
    }

    public int getPersonId() {
        return personId;
    }

    public int getGiftId() {
        return giftId;
    }

    public String getPersonName() {
        return personName;
    }

    public int getPersonAge() {
        return personAge;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getSendDate() {
        return sendDate;
    }
}
