package com.menemi.personobject;

import android.graphics.Bitmap;

import com.menemi.dbfactory.rest.PictureLoader;

/**
 * Created by Ui-Developer on 09.09.2016.
 */
public class PhotoTemplate {
    private int templateID;
    private Bitmap templatePicture;
    private boolean isOn = false;

    public PhotoTemplate(int templateID, Bitmap templatePicture) {
        this.templateID = templateID;
        this.templatePicture = templatePicture;
    }
    public PhotoTemplate(int templateID, String templatePictureURL) {
        this.templateID = templateID;
        new PictureLoader(templatePictureURL, (Object obj) ->{
            this.templatePicture = (Bitmap) obj;
        });

    }
    public int getTemplateID() {
        return templateID;
    }

    public Bitmap getTemplatePicture() {
        return templatePicture;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }
}
