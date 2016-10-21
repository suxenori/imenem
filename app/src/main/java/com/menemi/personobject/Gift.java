package com.menemi.personobject;

import android.graphics.Bitmap;

import com.menemi.dbfactory.DBHandler;
import com.menemi.dbfactory.rest.PictureLoader;

/**
 * Created by Ui-Developer on 06.10.2016.
 */

public class Gift {
    private Bitmap image;
    private int giftId;
    private String giftName;
    private int price;

    /*id : 1
    name : "Flowers"
    price : 1
    picture : "/system/gifts/pictures/000/000/001/original/open-uri20161004-32232-t2mr6f?1475588156"*/
    public Gift(String imageURL, int giftId, String giftName, int price) {
        this.giftId = giftId;
        this.giftName = giftName;
        this.price = price;
        new PictureLoader(imageURL, (Object bitmap) -> {
            this.image = (Bitmap) bitmap;
            DBHandler.getInstance().setGiftToDB(this);
        });
        //this.imageURL = imageURL;

    }

    public Gift(Bitmap image, int giftId, String giftName, int price) {

        this.image = image;
        this.giftId = giftId;
        this.giftName = giftName;
        this.price = price;
    }

    public Bitmap getImage() {
        return image;
    }


    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
