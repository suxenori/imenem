package com.menemi.personobject;

import com.menemi.dbfactory.DBHandler;

/**
 * Created by Ui-Developer on 27.10.2016.
 */

public class CreditsInfo {
    String token;
    int creditsToAdd;
    int id;

    public CreditsInfo(String token, int creditsToAdd) {
        this.id = DBHandler.getInstance().getMyProfile().getPersonId();
        this.token = token;
        this.creditsToAdd = creditsToAdd;
    }

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public int getCreditsToAdd() {
        return creditsToAdd;
    }
}
