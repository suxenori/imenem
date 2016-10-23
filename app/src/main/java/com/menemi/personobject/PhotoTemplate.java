package com.menemi.personobject;

import android.graphics.Bitmap;

import com.menemi.dbfactory.DBHandler;
import com.menemi.dbfactory.rest.PictureLoader;

/**
 * Created by Ui-Developer on 09.09.2016.
 */
public class PhotoTemplate {
    private int templateID;
    private Bitmap templatePicture;
    private String url;
    private boolean isOn = false;

    public PhotoTemplate(int templateID, Bitmap templatePicture) {
        this.templateID = templateID;
        this.templatePicture = templatePicture;
    }
    public PhotoTemplate(int templateID, String templatePictureURL) {
        this.templateID = templateID;
        this.url = templatePictureURL;
        new PictureLoader(templatePictureURL, (Bitmap obj) ->{
            this.templatePicture =  obj;
            DBHandler.getInstance().setTemplateToDB(this);

        });

    }
    public int getTemplateID() {
        return templateID;
    }

    public String getUrl() {
        return url;
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
