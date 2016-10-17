package com.menemi.dbfactory.stream.messages;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ui-Developer on 23.08.2016.
 */
public class DialogSendMessage extends StreamMessage {
    private static final int RANDOMIZER_MIN = 100_000_000;
    private static final int RANDOMIZER_MAX = 899_999_998;

    private int profileID;
    private String messageBody;
    private int rnd1;
    private int rnd2;

    int dialogId;
    public DialogSendMessage(int dialogId, int profileID, String messageBody) {
        this.profileID = profileID;
        this.messageBody = messageBody;
        this.dialogId = dialogId;
        setCommand(ConnectorCommands.ZCMD_SEND_MESSAGE);
        rnd1 = getRND();
        rnd2 = getRND();
    }

    public String getMessageBody() {
        return messageBody;
    }

    public int getRnd1() {
        return rnd1;
    }

    public int getRnd2() {
        return rnd2;
    }

    private int getRND(){
    return (  (int)(Math.random()*RANDOMIZER_MAX ) + RANDOMIZER_MIN );

}



    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("message_body", "" + messageBody);
        obj.put("command", "" + getCommand());
        obj.put("profile_id", "" + profileID);
        obj.put("dialog_id", "" + dialogId);
        obj.put("rnd1", rnd1);
        obj.put("rnd2", rnd2);

        return obj;

    }


    //"dialog_id":"1","message_body":"Hello","command":"ZCMD_SEND_MESSAGE","profile_id":"1"
}
