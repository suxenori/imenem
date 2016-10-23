package com.menemi.dbfactory.stream.messages;

import com.menemi.dbfactory.DBHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ui-Developer on 30.08.2016.
 */
public class ReadMessage extends  StreamMessage{
    private ArrayList<Integer> ids;
    private int dialogID;
    private int profileID;

    public ReadMessage() {
        setCommand(ConnectorCommands.ZCMD_READ_MESSAGE);
    }

    public ReadMessage(int profileID, int dialogID, ArrayList<Integer> ids) {
        this.ids = ids;
        this.dialogID = dialogID;
        this.profileID = profileID;
        setCommand(ConnectorCommands.ZCMD_READ_MESSAGE);
    }
    public ReadMessage(RecievedMessage recievedMessage) {
        ArrayList<Integer> id = new ArrayList<>();
        id.add(recievedMessage.getMessageId());
        this.ids = id;
        this.dialogID = recievedMessage.getDialogID();
        this.profileID = DBHandler.getInstance().getUserId();
        setCommand(ConnectorCommands.ZCMD_READ_MESSAGE);
    }
    public ArrayList<Integer> getIds() {
        return ids;
    }

    public void setIds(ArrayList<Integer> ids) {
        this.ids = ids;
    }

    public int getDialogID() {
        return dialogID;
    }

    public void setDialogID(int dialogID) {
        this.dialogID = dialogID;
    }

    public int getProfileID() {
        return profileID;
    }

    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();
        JSONArray idsJSON = new JSONArray();
        for (int i = 0; i < ids.size(); i++) {
            idsJSON.put(ids.get(i));
        }

        obj.put("ids", idsJSON);
        obj.put("profile_id", "" + profileID);
        obj.put("dialog_id", "" + dialogID);

        return obj;
    }
}
