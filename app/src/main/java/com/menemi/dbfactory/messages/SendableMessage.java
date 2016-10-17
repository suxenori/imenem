package com.menemi.dbfactory.messages;

import android.util.Log;

/**
 * Created by irondev on 08.06.16.
 */
public class SendableMessage {
    private static OnRecieveListener onRecieveListener = new OnRecieveListener() {
        @Override
        public void onRecieve() {
            Log.e("Listener", "I is called, but appear to be empty");
        }
    };

    int userId;
    public static final String MESSAGE_START = "bla lba bla";
    public static final String MESSAGE_END = "bla lba bla";
    private String messageBody;

    public String getMessage(){
        return MESSAGE_START + messageBody + MESSAGE_END;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public SendableMessage(int userId){
        this.userId = userId;
    }


    public interface OnRecieveListener{
            void onRecieve();
    }
    public static void setOnRecieveListener(OnRecieveListener onRecieveListener) {
        SendableMessage.onRecieveListener = onRecieveListener;
    }

}
