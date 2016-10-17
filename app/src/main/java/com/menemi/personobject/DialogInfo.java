package com.menemi.personobject;

import android.graphics.Bitmap;

import com.menemi.utils.Utils;

import java.sql.Date;

/**
 * Created by Ui-Developer on 11.08.2016.
 */
public class DialogInfo {

    private int profileId;
    private String lastMessage;// : "Everything is find for now, just thinking if we can meet each other closer by sitting at the caffe somewhere and speaking about us. We can also speak about autos and politics if you wish."
    private Date lastMessageDate;// : "2016-08-11T13:02:51.000Z"
    private int newMessagesCount = 5;
    private String contactName = "BOB";
    private boolean isOnline = false;
    private int dialogID = -1;
    private Bitmap conatactAvatar = null;

    public int getDialogID() {
        return dialogID;
    }

    public void setDialogID(int dialogID) {
        this.dialogID = dialogID;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Date getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(String lastMessageDate) {
        this.lastMessageDate = Utils.getDateFromServer2(lastMessageDate);
    }

    public int getNewMessagesCount() {
        return newMessagesCount;
    }

    public void setNewMessagesCount(int newMessagesCount) {
        this.newMessagesCount = newMessagesCount;
    }

    public void setLastMessageDate(Date lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    public Bitmap getConatactAvatar() {
        return conatactAvatar;
    }

    public void setConatactAvatar(Bitmap conatactAvatar) {
        this.conatactAvatar = conatactAvatar;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(String value) {
        if(value.equals("online")){
            isOnline = true;
        }

    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
