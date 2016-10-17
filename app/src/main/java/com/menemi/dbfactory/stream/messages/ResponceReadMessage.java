package com.menemi.dbfactory.stream.messages;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ui-Developer on 05.09.2016.
 */
public class ResponceReadMessage extends StreamMessage{
    public ResponceReadMessage(JSONObject jsonObject) {
        super(jsonObject);
        setCommand(ConnectorCommands.ZCMD_READ_MESSAGE_RESPONSE);
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        return null;
    }
}
