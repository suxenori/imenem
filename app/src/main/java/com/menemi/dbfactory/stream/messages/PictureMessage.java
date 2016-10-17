package com.menemi.dbfactory.stream.messages;

import android.graphics.Bitmap;

import com.menemi.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ui-Developer on 23.08.2016.
 */
public class PictureMessage extends StreamMessage{
    private int profileID;
    private Bitmap picture;

    public PictureMessage(int profileID, Bitmap picture) {
        this.profileID = profileID;

        this.picture = picture;
        setCommand(ConnectorCommands.ZCMD_SEND_PICTURE);
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("picture", "" + Utils.getBitmapToBase64String(picture));
        obj.put("command", "" + getCommand());
        obj.put("profile_id", "" + profileID);

        return obj;

    }
}
