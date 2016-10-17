package com.menemi.dbfactory.stream.messages;

import com.menemi.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;

/**
 * Created by Ui-Developer on 25.08.2016.
 */
public class DialogResponceMessage extends StreamMessage{

    int messageID;
            Date sendAt;
    int dialogID;
    int sortID;
    int rnd1;
    int rnd2;

    public DialogResponceMessage(JSONObject jsonObject) {
        try {
            this.messageID = jsonObject.getInt("server_message_id");
            this.sendAt = Utils.getDateFromServer(jsonObject.getString("created_at"));
            this.dialogID =      jsonObject.getInt("dialog_id");;
            this.sortID =      jsonObject.getInt("sort_id");;
            this.rnd1 =      jsonObject.getInt("rnd1");
            this.rnd2 =      jsonObject.getInt("rnd2");
            setCommand(ConnectorCommands.ZCMD_SEND_MESSAGE_RESPONSE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getMessageID() {
        return messageID;
    }

    public Date getSendAt() {
        return sendAt;
    }

    public int getDialogID() {
        return dialogID;
    }

    public int getSortID() {
        return sortID;
    }

    public int[] getRnd() {
        return new int[] {rnd1, rnd2};
    }



    @Override
    public JSONObject toJSON() throws JSONException {
        return null;
    }

    //server_message_id
     //       sort_id
    //server_dialog_id
}
