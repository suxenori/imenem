package com.menemi.dbfactory.stream.messages;

import android.util.Log;

import com.menemi.dbfactory.stream.StreamController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by irondev on 08.06.16.
 */
public abstract class StreamMessage {

    private static HashMap<ConnectorCommands, OnRecieveListener> onRecieveListener = new HashMap<>();




    private String fullMessage;
    private ConnectorCommands command = null;
    private boolean isCorrupted = false;
    private StreamController sourceConnector = null;

    public String getCommand() {
        return command.name();
    }

    protected StreamMessage(JSONObject jsonObject) {
    }

    public StreamMessage() {
        isCorrupted = true;
    }

    public static StreamMessage fromJSON(String data) {
    StreamMessage mesage = null;
        boolean isParsed = true;
        String command = "";
        JSONObject jsonObject = null;
        Log.d("COMMAND", "fromJSON ! " + command);
        try {
            jsonObject = new JSONObject(data);
            command = jsonObject.getString("command");
            Log.d("COMMAND", "command ! " + command);
            Log.d("COMMAND", "command ! " + (ConnectorCommands.valueOf(command)== ConnectorCommands.ZCMD_SEND_MESSAGE_RESPONSE));


            if(ConnectorCommands.valueOf(command) == ConnectorCommands.ZCMD_SEND_MESSAGE_RESPONSE){
                Log.d("COMMAND", "RESPONSE ! " + data);
                return new DialogResponceMessage(jsonObject);
            }
            if(ConnectorCommands.valueOf(command) == ConnectorCommands.ZCMD_NOTYFICATION_TYPING){
                Log.d("COMMAND", "RESPONSE ! " + data);
                return new TypingMessage(jsonObject);
            }
            if(ConnectorCommands.valueOf(command) == ConnectorCommands.ZCMD_RECEIVED_MESSAGE){
                Log.d("COMMAND", "RESPONSE ! " + data);
                return new RecievedMessage(jsonObject);
            }
            if(ConnectorCommands.valueOf(command) == ConnectorCommands.ZCMD_READ_MESSAGE_RESPONSE){
                Log.d("COMMAND", "RESPONSE ! " + data);
                return new ResponceReadMessage(jsonObject);
            }
            if(ConnectorCommands.valueOf(command) == ConnectorCommands.ZCMD_BALANCE_UPDATED_NOTIFICATION){
                Log.d("COMMAND", "RESPONSE ! " + data);
                return new BalanceUpdateMessage(jsonObject);
            }


            //JSON -> JSONArray -> JSONArray.getString(«comand») -> connector_comands

        } catch (JSONException e) {
            e.printStackTrace();
            isParsed = false;
        }
        //message.mMessageData = NSMutableDictionary(dictionary: JSONDictionary!)
        if (isParsed) {


         //   this.command = ConnectorCommands.valueOf(command);
        //    messageTypesParcer.get(this.command).onJSONObjectRecieved(jsonObject).notifyAllListeners();
        } else {
           // this.isCorrupted = true;
        }
        return new StreamMessage() {
            @Override
            public JSONObject toJSON() throws JSONException {
                return null;
            }
        };

    }

    public static void addOnRecieveListener(ConnectorCommands command, OnRecieveListener listener) {

        onRecieveListener.put(command,listener);
    }




    public abstract JSONObject toJSON() throws JSONException;

    public boolean isCorrupted() {

        return isCorrupted;
    }



     public void notifyAllListeners() {
        Log.d("COMMAND", "listeners number" + onRecieveListener.size());
         if(command != null) {
             for (Map.Entry<ConnectorCommands, OnRecieveListener> listener : onRecieveListener.entrySet()) {
                 if (listener.getKey() == command) {

                     listener.getValue().onRecieve(this);
                 }
             }
         }
    }

    public StreamController getSourceConnector() {
        return sourceConnector;
    }

    public void setSourceConnector(StreamController sourceConnector) {
        this.sourceConnector = sourceConnector;
    }

    public void setCommand(ConnectorCommands command) {
        this.command = command;
    }

    public void setFullMessage(String fullMessage) {
        this.fullMessage = fullMessage;
    }


    public enum ConnectorCommands {
        ZCMD_SYSTEM,
        ZCMD_WHO_HAS_ZAMZAM,
        ZCMD_WHO_HAS_ZAMZAM_RESPONSE,
        ZCMD_SEND_MESSAGE,
        ZCMD_SEND_MESSAGE_RESPONSE,
        ZCMD_SEND_PICTURE,
        ZCMD_SEND_PICTURE_RESPONSE,
        ZCMD_SEND_VIDEO,
        ZCMD_SEND_VIDEO_RESPONSE,
        ZCMD_UPDATE_PROFILE_NAME,
        ZCMD_UPDATE_PROFILE_AVATAR,
        ZCMD_RECEIVED_MESSAGE,
        ZCMD_RECEIVED_PICTURE,
        ZCMD_RECEIVED_VIDEO,
        ZCMD_CONFIRM_LATE_MESSAGE,
        ZCMD_NOTYFICATION_TYPING,
        ZCMD_CONTACT_VERSION_UPDATED,
        ZCMD_READ_MESSAGE,
        ZCMD_READ_MESSAGE_RESPONSE,
        ZCMD_BALANCE_UPDATED_NOTIFICATION

    }



    public interface OnRecieveListener {
        void onRecieve(StreamMessage message);
    }
}
