package com.menemi.dbfactory.stream.messages;

import com.menemi.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by Ui-Developer on 23.08.2016.
 */
public class TypingMessage extends StreamMessage {
    private int profileID;
    private static LinkedList<OnRecieveListener> onRecieveListener = new LinkedList<OnRecieveListener>();
    private int isTyping;
    private int fromProfileID;
    public int getProfileID() {
        return profileID;
    }

    public boolean getIsTyping() {
        return Utils.intToBool(isTyping);
    }

    public int getFromProfileID() {
        return fromProfileID;
    }

    public TypingMessage(int profileID, boolean isTyping) {
        this.profileID = profileID;

        this.isTyping = Utils.boolToInt(isTyping);
        setCommand(ConnectorCommands.ZCMD_NOTYFICATION_TYPING);
    }

    public TypingMessage(JSONObject jsonObject) {
        try {
            this.profileID = jsonObject.getInt("profile_id");

            this.isTyping = jsonObject.getInt("value");
            this.fromProfileID = jsonObject.getInt("from_profile_id");
            setCommand(ConnectorCommands.ZCMD_NOTYFICATION_TYPING);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("value", isTyping);
        obj.put("command", "" + getCommand());
        obj.put("profile_id", "" + profileID);

        return obj;

    }
}
