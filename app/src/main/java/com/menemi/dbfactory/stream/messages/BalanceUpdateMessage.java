package com.menemi.dbfactory.stream.messages;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ui-Developer on 17.11.2016.
 */

public class BalanceUpdateMessage extends StreamMessage{
public BalanceUpdateMessage(JSONObject jsonObject){
    setCommand(ConnectorCommands.ZCMD_BALANCE_UPDATED_NOTIFICATION);
}
    public BalanceUpdateMessage(){
        setCommand(ConnectorCommands.ZCMD_BALANCE_UPDATED_NOTIFICATION);
    }
    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("command", "" + getCommand());

        return obj;
    }
}
