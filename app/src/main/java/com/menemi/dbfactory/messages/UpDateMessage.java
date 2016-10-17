package com.menemi.dbfactory.messages;

import com.menemi.dbfactory.ComandChange;

import java.util.LinkedList;

/**
 * Created by irondev on 08.06.16.
 */
public class UpDateMessage extends SendableMessage {


    public UpDateMessage(int userId, int currentVersion, LinkedList<ComandChange> changesList) {
        super(userId);
        setMessageBody("MisterFreeman");
    }
}
