package com.menemi.personobject;

import android.graphics.Bitmap;

import com.menemi.dbfactory.DBHandler;
import com.menemi.dbfactory.stream.messages.DialogSendMessage;
import com.menemi.dbfactory.stream.messages.RecievedMessage;
import com.menemi.utils.Utils;

import java.sql.Date;

/**
 * Created by Ui-Developer on 11.08.2016.
 */
public class DialogMessage {
    private int messageId;
    private String messageBody;
    private int sortId;
    private int profileId;
    private Bitmap picture;
    private String pictureURL;
    private Date date;
    private boolean isOwner;
    private int rnd1;
    private int rnd2;


    public DialogMessage(int profileId){
        isOwner = DBHandler.getInstance().getUserId() == profileId;
        this.profileId = profileId;
    }



    public DialogMessage(RecievedMessage recievedMessage) {

        this.messageBody = recievedMessage.getMessageBody();

        this.profileId = recievedMessage.getFromProfileID();
        this.setDate(new Date(System.currentTimeMillis()));
        isOwner = true;
    }

    public DialogMessage(DialogSendMessage dialogSendMessage) {
        this.messageBody = dialogSendMessage.getMessageBody();
        this.profileId = DBHandler.getInstance().getUserId();

        this.date = new Date(System.currentTimeMillis());

        rnd1 = dialogSendMessage.getRnd1();
        rnd2 = dialogSendMessage.getRnd2();

        isOwner = true;
    }
    public boolean comparRND(int[] rnd){
        if(rnd1 == rnd[0] && rnd2 == rnd[1]){
            return true;
        }
        return false;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public int getRnd1() {
        return rnd1;
    }

    public int getRnd2() {
        return rnd2;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }



    public void setDate(Date date) {
        this.date = date;
    }
    public Date getDate() {

        return date;
    }
    public void setDate(String date) {
        this.date =  Utils.getDateFromServer(date);
    }
}
