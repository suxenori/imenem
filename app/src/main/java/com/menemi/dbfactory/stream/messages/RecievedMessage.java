package com.menemi.dbfactory.stream.messages;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ui-Developer on 26.08.2016.
 */
public class RecievedMessage extends StreamMessage{
    private int messageId;

    private int dialogID;
    private int profileID;
    private int fromProfileID;
    private String messageBody;
//{"command":"ZCMD_RECEIVED_MESSAGE","message_body":"ÑÐ°Ð¿","rnd1":528295114,"rnd2":861760946,"profile_id":"2","dialog_id":"1","from_profile_id":1}

    public RecievedMessage(JSONObject jsonObject) {
        try {
            this.dialogID = jsonObject.getInt("dialog_id");
            this.profileID = jsonObject.getInt("profile_id");
            this.fromProfileID = jsonObject.getInt("from_profile_id");
            this.messageBody = jsonObject.getString("message_body");
            this.messageId = jsonObject.getInt("server_message_id");
            setCommand(ConnectorCommands.ZCMD_RECEIVED_MESSAGE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public int getMessageId() {
        return messageId;
    }

    public int getDialogID() {
        return dialogID;
    }

    public int getProfileID() {
        return profileID;
    }

    public int getFromProfileID() {
        return fromProfileID;
    }

    public String getMessageBody() {
        return messageBody;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        return null;
    }
}

