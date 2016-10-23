package com.menemi.personobject;

import android.graphics.Bitmap;

import com.menemi.dbfactory.rest.PictureLoader;
import com.menemi.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Ui-Developer on 20.07.2016.
 */
public class PhotoSetting {
    private Bitmap photo = null;


    private int photoId = -1;
    private int price = 0;
    private boolean isPrivate = false;
    private boolean autoprice = true;
    private int profileId;
    private int views;
    private double profit;
    private boolean isUnlocked = false;
    private int[] templateIds = new int[0];
    private String photoUrl;

    public PhotoSetting(String photoUrl, boolean loadNow) {
        if(loadNow){
        new PictureLoader(photoUrl, (Object obj)->{
            photo = (Bitmap) obj;
            this.photoUrl = photoUrl;
        });
        }
        this.photoUrl = photoUrl;
    }
    public PhotoSetting(int photoId, String photoUrl, boolean loadNow) {
        if(loadNow){
            new PictureLoader(photoUrl, (Object obj)->{
                photo = (Bitmap) obj;
                this.photoUrl = photoUrl;
            });
        }
        this.photoId = photoId;
        this.photoUrl = photoUrl;
    }
    public PhotoSetting(int photoId, boolean isPrivate, boolean autoprice, int price, int[] templateIds, String photoUrl, boolean loadNow) {
        if(loadNow){
            new PictureLoader(photoUrl, (Object obj)->{
                photo = (Bitmap) obj;
                this.photoUrl = photoUrl;
            });
        }
        this.photoId = photoId;
        this.isPrivate = isPrivate;
        this.autoprice = autoprice;
        this.price = price;
        this.templateIds = templateIds;
        this.photoUrl = photoUrl;
    }
    public PhotoSetting(int photoId, boolean isPrivate, boolean autoprice, int price, int[] templateIds, Bitmap photo)
    {
        this.photoId = photoId;
        this.isPrivate = isPrivate;
        this.autoprice = autoprice;
        this.price = price;
        this.templateIds = templateIds;
        this.photo = photo;
    }

    public PhotoSetting(Bitmap photo) {
        this.photo = photo;
    }

    public PhotoSetting(PhotoSetting photoSetting){
        this.photo = photoSetting.photo;

        this.price = photoSetting.price;
        this.photoId = photoSetting.photoId;
        this.isPrivate = photoSetting.isPrivate;
        this.autoprice = photoSetting.autoprice;
        this.profileId = photoSetting.profileId;
        this.templateIds = photoSetting.templateIds;
        this.views = photoSetting.views;
        this.profit = photoSetting.profit;
        this.isUnlocked = photoSetting.isUnlocked;
        this.photoUrl = photoSetting.photoUrl;

    }

    public Bitmap getPhoto() {
        return photo;
    }
    public String getPhotoBase64(){
        return Utils.getBitmapToBase64String(photo);
    }

    public PhotoSetting(Bitmap photo, int photoId) {
        this.photo = photo;
        this.photoId = photoId;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public void setTemplateIds(ArrayList<PhotoTemplate> photoTemplates){
        ArrayList<PhotoTemplate> templates = new ArrayList<>();
        for (int i = 0; i < photoTemplates.size(); i++) {
            if(photoTemplates.get(i).isOn()) {
                templates.add(photoTemplates.get(i));
            }
        }
        int[] temps = new int[templates.size()];
        for (int i = 0; i < templates.size(); i++) {
            temps[i] = templates.get(i).getTemplateID();
        }
        templateIds = temps;
    }


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public boolean isAutoprice()
    {
        return autoprice;
    }

    public void setAutoprice(boolean autoprice)
    {
        this.autoprice = autoprice;
    }

    public int getProfileId()
    {
        return profileId;
    }

    public void setProfileId(int profileId)
    {
        this.profileId = profileId;
    }

    public boolean isPrivate()
    {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate)
    {
        isPrivate = aPrivate;
    }

    public int[] getTemplateIds()
    {
        return templateIds;
    }

    public void setTemplateIds(int[] templateIds)
    {
        this.templateIds = templateIds;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }
}
