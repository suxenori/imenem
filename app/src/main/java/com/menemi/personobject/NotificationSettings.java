package com.menemi.personobject;

import com.menemi.dbfactory.Fields;

import java.util.ArrayList;

/**
 * Created by Ui-Developer on 08.08.2016.
 */
public class NotificationSettings {
    private int id;
    private int profile_id;
    private Setting messages = new Setting();
    private Setting mutual_likes = new Setting();
    private Setting their_likes = new Setting();
    private Setting nearby = new Setting();
    private Setting visitors = new Setting();
    private Setting favorites = new Setting();
    private Setting gifts = new Setting();
    private Setting other = new Setting();

    public NotificationSettings(int id, int profile_id) {
        this.id = id;
        this.profile_id = profile_id;
    }

    public String getMessages() {
        return messages.toString();
    }

    public String getMutual_likes() {
        return mutual_likes.toString();
    }

    public String getTheir_likes() {
        return their_likes.toString();
    }

    public String getNearby() {
        return nearby.toString();
    }

    public String getVisitors() {
        return visitors.toString();
    }

    public String getFavorites() {
        return favorites.toString();
    }

    public String getGifts() {
        return gifts.toString();
    }

    public String getOther() {
        return other.toString();
    }

    public boolean getMessages(SETTINGTYPE settingtype) {

        return messages.getValueOf(settingtype);
    }

    public void setMessages(String messages) {
        this.messages = prepareSettingFromString(messages);
    }
    public void revertMessages(SETTINGTYPE settingtype) {
       messages.setValue(settingtype, !messages.getValueOf(settingtype));
    }

    public void revertMutual_likes(SETTINGTYPE settingtype ) {
        mutual_likes.setValue(settingtype,!mutual_likes.getValueOf(settingtype));
    }

    public void revertTheir_likes(SETTINGTYPE settingtype) {
        their_likes.setValue(settingtype,!their_likes.getValueOf(settingtype));
    }

    public void revertNearby(SETTINGTYPE settingtype) {
        nearby.setValue(settingtype,!nearby.getValueOf(settingtype));
    }

    public void revertVisitors(SETTINGTYPE settingtype) {
        visitors.setValue(settingtype,!visitors.getValueOf(settingtype));
    }

    public void revertFavorites(SETTINGTYPE settingtype) {
        favorites.setValue(settingtype,!favorites.getValueOf(settingtype));
    }

    public void revertGifts(SETTINGTYPE settingtype) {
        gifts.setValue(settingtype,!gifts.getValueOf(settingtype));
    }

    public void revertOther(SETTINGTYPE settingtype) {
        other.setValue(settingtype,!other.getValueOf(settingtype));
    }

    public boolean getMutual_likes(SETTINGTYPE settingtype) {
        return mutual_likes.getValueOf(settingtype);
    }

    public void setMutual_likes(String mutual_likes) {
        this.mutual_likes = prepareSettingFromString(mutual_likes);
    }

    public boolean getTheir_likes(SETTINGTYPE settingtype) {
        return their_likes.getValueOf(settingtype);
    }

    public void setTheir_likes(String their_likes) {
        this.their_likes = prepareSettingFromString(their_likes);
    }

    public boolean getNearby(SETTINGTYPE settingtype) {
        return nearby.getValueOf(settingtype);
    }

    public void setNearby(String nearby) {
        this.nearby = prepareSettingFromString(nearby);
    }

    public boolean getVisitors(SETTINGTYPE settingtype) {
        return visitors.getValueOf(settingtype);
    }

    public void setVisitors(String visitors) {
        this.visitors = prepareSettingFromString(visitors);
    }

    public boolean getFavorites(SETTINGTYPE settingtype) {
        return favorites.getValueOf(settingtype);
    }

    public void setFavorites(String favorites) {
        this.favorites = prepareSettingFromString(favorites);
    }

    public boolean getGifts(SETTINGTYPE settingtype) {
        return gifts.getValueOf(settingtype);
    }

    public void setGifts(String gifts) {
        this.gifts = prepareSettingFromString(gifts);
    }

    public boolean getOther(SETTINGTYPE settingtype) {
        return other.getValueOf(settingtype);
    }

    public void setOther(String other) {
        this.other = prepareSettingFromString(other);
    }

    public Setting prepareSettingFromString(String initial) {
        if (initial != null && !initial.equals("")) {
            boolean email = false;
            boolean push = false;
            boolean inApp = false;
            if (initial.contains(Fields.N_EMAIL)) {
                email = true;
            }
            if (initial.contains(Fields.N_PUSH)) {
                push = true;
            }
            if (initial.contains(Fields.N_INAPP)) {
                inApp = true;
            }
            return new Setting(email, push, inApp);
        } else {
            return new Setting(false, false, false);
        }
    }

    class Setting {
        private boolean email = false;
        private boolean push = false;
        private boolean inApp = false;
        public Setting() {

        }
        public Setting(boolean email, boolean push, boolean inApp) {
            this.email = email;
            this.push = push;
            this.inApp = inApp;
        }
        public boolean getValueOf(SETTINGTYPE settingtype){
            switch (settingtype){
                case EMAIL:
                    return email;
                case PUSH:
                    return push;
                case INAPP:
                    return inApp;
            }
            return false;
        }
        public boolean isEmail() {
            return email;
        }

        public void setEmail(boolean email) {
            this.email = email;
        }

        public boolean isPush() {
            return push;
        }

        public void setPush(boolean push) {
            this.push = push;
        }

        public boolean isInApp() {
            return inApp;
        }

        public void setInApp(boolean inApp) {
            this.inApp = inApp;
        }

        public void setValue(SETTINGTYPE settingtype, boolean value){
            switch (settingtype){
                case EMAIL:
                    email = value;
                    break;
                case PUSH:
                     push = value;
                    break;
                case INAPP:
                    inApp = value;
                    break;
            }
        }
        @Override
        public String toString() {
            String result = "";
            ArrayList<String> results = new ArrayList<>();
            if(isEmail()){
                results.add(Fields.N_EMAIL);
            }
            if(isPush()){
                results.add(Fields.N_PUSH);
            }
            if(isInApp()){
                results.add(Fields.N_INAPP);
            }
            for (int i = 0; i < results.size(); i++) {
                result += results.get(i);
                if(i < results.size() - 1){
                    result += "|";
                }
            }

            return result;
        }
    }
    public enum SETTINGTYPE{
        EMAIL,
        PUSH,
        INAPP
    }
}
