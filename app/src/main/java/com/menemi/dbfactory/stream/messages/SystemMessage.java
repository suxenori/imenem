package com.menemi.dbfactory.stream.messages;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by irondev on 09.06.16.
 */
public class SystemMessage extends StreamMessage {
    private static LinkedList<OnRecieveListener> onRecieveListener = new LinkedList<OnRecieveListener>();


    private boolean isOut = false;
    private int profileID = -1;
    private int random;
    public SystemMessage(int profileID, boolean isOut, int random) {
        this.isOut = isOut;
        this.profileID = profileID;
        this.random = random;
    }

    /**
     * Should be called, whenever Messeg is send to SERVER
     * @return json looking, string representation of the data, contained by object
     */
    @Override
    public JSONObject toJSON()  throws JSONException {
        JSONObject obj = new JSONObject();

            if(isOut) {
                obj.put("socket_type", "OUT:" + profileID + ":" + random);
            }else {
                obj.put("socket_type", "IN:" + profileID + ":" + random);
            }
            obj.put("command", "" + getCommand());

        return obj;

    }

}
