package com.menemi.dbfactory.stream;

import android.util.Log;

import com.menemi.dbfactory.Fields;
import com.menemi.dbfactory.InternetConnectionListener;
import com.menemi.dbfactory.stream.messages.StreamMessage;
import com.menemi.dbfactory.stream.messages.SystemMessage;

import org.json.JSONException;

import java.util.LinkedList;

/**
 * Created by irondev on 08.06.16.
 */
public class StreamController implements StreamTunnel.CommunicationDelegate {
    static final String MESSAGE_BEGIN = "__MESSAGE__BORDER__";
    static final String MESSAGE_END = "__REDROB__EGASSEM__";
    private static int userId = -1;
    StreamTunnel incommingTunnel = null;
    StreamTunnel outgoingTunnel = null;
    LinkedList<StreamMessage> incomingMessagesQueue = new LinkedList<>();
    LinkedList<StreamMessage> outgoingMessagesQueue = new LinkedList<>();
    private InternetConnectionListener internetConnectionListener;
    private String ipAdress = Fields.URL_FOR_SERVER;
    private int port = 55_555;

    public StreamController(int userId, InternetConnectionListener internetConnectionListener) {
        this.userId = userId;
        startMessageSender();

        connect();
        this.internetConnectionListener = internetConnectionListener;
    }



    public void startMessageSender() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100l);
                        StreamController.this.sendQueuedMessages();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    public void startMessageReciever() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("COMMAND", "start");
                incommingTunnel.startReadData();

            }
        }).start();
    }

    public static void setUserId(int userId) {
        StreamController.userId = userId;
    }

    @Override
    public void didReceivedMessage(StreamTunnel sender, StreamMessage message) {
        Log.d("COMMAND", "didReceivedMessage " + !message.isCorrupted());
        // if (!message.isCorrupted()) {
        message.setSourceConnector(this);
        incomingMessagesQueue.add(message);
        recievedMessages++;
        Log.d("MessageTest", "recieve " + recievedMessages);
        message.notifyAllListeners();
        //}

    }

    int random = (int) (Math.random() * 89_000_000) + 10_000_000;

    @Override
    public void didConnectToHost() {
        Log.d("COMMAND", "didConnectToHost");
        //////// ??? /////////
        if (outgoingTunnel.isConnected() && (incommingTunnel == null || !incommingTunnel.isConnected())) {
            //outgoing connected - send socket description
            //we need to do that manually because sending through common SYNC:sendMessage will just use the outgoping tunnel
            SystemMessage message = new SystemMessage(userId, true, random);
            message.setCommand(StreamMessage.ConnectorCommands.ZCMD_SYSTEM);

            //message.setData(new Pair<String, String>("socket_type", "OUT:" + userId));
            try {
                String strJSON = message.toJSON().toString();
                String formatedStr = prepareMessage(strJSON);
                //print(+formatedStr)
                Log.v("didConnectToHost", "Writing to socket: " + formatedStr);
                outgoingTunnel.writeData(formatedStr);

                incommingTunnel = new StreamTunnel();
                incommingTunnel.initWithIP(ipAdress, port);
                incommingTunnel.delegate = this;
                incommingTunnel.connect();
                Log.v("didConnectToHost", "Web connected mOutgoingTunnel");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (outgoingTunnel.isConnected() && incommingTunnel.isConnected()) {
            //outgoing connected - send socket description
            //we need to do that manually because sending through common SYNC:sendMessage will just use the outgoping tunnel
            SystemMessage message = new SystemMessage(userId, false, random);
            message.setCommand(StreamMessage.ConnectorCommands.ZCMD_SYSTEM);
            try {
                //message.setData(new android.util.Pair<String, String>("socket_type", "IN:" + userId));
                String strJSON = message.toJSON().toString();
                String formatedStr = prepareMessage(strJSON);
                Log.v("didConnectToHost", "Writing to socket: " + formatedStr);
                incommingTunnel.writeData(formatedStr);
                startMessageReciever();
                Log.v("didConnectToHost", "Web connected mIncommingTunnel");
                internetConnectionListener.internetON();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String prepareMessage(String message) {
        return MESSAGE_BEGIN + message + MESSAGE_END;
    }

    @Override
    public void didDisconnectFromHost() {

        //print("DISCONNECTED! Reconnecting both tunnels")
        try {
            internetConnectionListener.internetOFF();
            Thread.sleep(5000l);
            reconnect();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    int sendedMessages = 0;
    int recievedMessages = 0;

    void sendQueuedMessages() {


        for (int i = 0; i < outgoingMessagesQueue.size(); i++) {
            Log.d("SEND", "outgoingMessagesQueue.size()" + outgoingMessagesQueue.size());

            try {
                StreamMessage queuedMessage = outgoingMessagesQueue.getFirst();
                String strMessage = prepareMessage(queuedMessage.toJSON().toString());
                outgoingTunnel.writeData(strMessage);
                sendedMessages++;
                Log.d("MessageTest", "sended " + sendedMessages);
                outgoingMessagesQueue.removeFirst();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        //print("Writing to socket: "+formatedStr)


        if (incommingTunnel != null) {
            incommingTunnel.checkIncomingData();
        }
    }

    void reconnect() {
        disconnect();
        connect();
    }

    void connect() {
        outgoingTunnel = new StreamTunnel();
        outgoingTunnel.initWithIP(ipAdress, port);
        outgoingTunnel.delegate = this;
        outgoingTunnel.connect();

    }

    public void disconnect() {
        if (outgoingTunnel != null) {
            outgoingTunnel.disconnect();
            outgoingTunnel = null;
        }

        if (incommingTunnel != null) {
            incommingTunnel.disconnect();
            incommingTunnel = null;
        }
    }

    boolean isConnected() {
        return outgoingTunnel.isConnected() && incommingTunnel.isConnected();
    }

    int messagesCount() {
        return incomingMessagesQueue.size();
    }

    public void sendMessage(StreamMessage message) {
        outgoingMessagesQueue.add(message);
    }

    StreamMessage popMessageFromQueue() {
        StreamMessage message = null;
        if (incomingMessagesQueue.size() > 0) {
            message = incomingMessagesQueue.getFirst();
            incomingMessagesQueue.removeFirst();
        }
        return message;
    }

    class UserIdNotSetExeption extends Exception {
        @Override
        public String getMessage() {
            return "id is not set, call setUserId(int) " + super.getMessage();
        }
    }

}

   /* StreamMessage processGeneralMessageReceived(StreamMessage message, message_type: NSInteger, message_body: String)
    {
        let response_message = message.messageData["response_message"] as! NSMutableDictionary
        let new_message = self.processaMsgBasePart(message_body,
                attachementType: message_type,//0 - text only, 1 - picture, 2 - video, 3 - smile
            fromZamZamStr: (message.mMessageData["from_zamzam_id"] as? String)!,
            rnd1: (message.mMessageData["rand1"] as? String)!,
            rnd2: (message.mMessageData["rand2"] as? String)!,
            serverMsgId: (response_message["server_message_id"] as! NSNumber?)!,
            sortId: (response_message["sort_id"] as! NSNumber?)!)
        //--------------------------------------------------------------------------------
        let dialog = self.processaMsgDialogPart(response_message["server_dialog_id"] as! NSNumber, from_zamzam_id_str: (message.mMessageData["from_zamzam_id"] as? String)!)
        new_message.dialog = dialog
        dialog.last_message = new_message.message_text
        DBManager.Instance().saveEntity(dialog)
        //--------------------------------------------------------------------------------

        //--------------------------------------------------------------------------------
        self.processaMsgContactNumberPart(dialog.to_zamzam_id!, contact_info: message.mMessageData["contact_info"] as! NSMutableDictionary)
        //--------------------------------------------------------------------------------

        DBManager.Instance().saveEntity(new_message)

        print("NewMessageFrom"+String(new_message.from_zamzam_id!))

        return new_message
    }*/
    /*Dialog processaMsgDialogPart(server_dialog_id: NSNumber, from_zamzam_id_str: String)
    {
        var dialog = DBManager.Instance().GetDialogByServerId(server_dialog_id.integerValue)
        if dialog == nil
        {
            dialog = DBManager.Instance().createEntity("Dialog") as? Dialog
            dialog?.server_dialog_id = server_dialog_id

            NSNotificationCenter.defaultCenter().postNotificationName("NewDialogCreated", object: nil, userInfo: nil)

            let from_zamzam_id_str = from_zamzam_id_str
            dialog?.to_zamzam_id = NSNumber(integer: Int(from_zamzam_id_str)!)
            print("RECEIVED from zamzam id: "+String(dialog!.to_zamzam_id))
        }

        return dialog!
    }
*/





/*

        func ProcessIncomingQueuedMessages() -> Void
        {
            var nextTimout = 2.0;
            if(self.MessagesCount() > 0)
            {
                for _ in 0...10  {

                if(self.MessagesCount() == 0)
                {
                    break
                }

                let message = self.PopMessageFromQueue()
                if message == nil
                {
                    break
                }

                if message!.mType == e_ZConnectorCommands.ZCMD_SYSTEM
                {
                    self.processCommandSystem(message!)
                }
                else if message!.mType == e_ZConnectorCommands.ZCMD_WHO_HAS_ZAMZAM_RESPONSE{
                    self.processCommandWhoHasZamZamResponse(message!)
                }
                else if message!.mType == e_ZConnectorCommands.ZCMD_SEND_MESSAGE_RESPONSE{
                    self.processCommandSendMessageResponse(message!)
                }
                else if message!.mType == e_ZConnectorCommands.ZCMD_RECEIVED_MESSAGE{
                    self.processCommandReceivedMessage(message!)
                }
                else if message!.mType == e_ZConnectorCommands.ZCMD_RECEIVED_PICTURE{
                    self.processCommandReceivedPicture(message!)
                }
                else if message!.mType == e_ZConnectorCommands.ZCMD_RECEIVED_VIDEO{
                    self.processCommandReceivedVideo(message!)
                }
                else if message!.mType == e_ZConnectorCommands.ZCMD_NOTYFICATION_TYPING{
                    self.processCommandNotifyTyping(message!)
                }
                else if message!.mType == e_ZConnectorCommands.ZCMD_CONTACT_VERSION_UPDATED{
                    self.processCommandContactVersionUpdated(message!)
                }

                if message!.mMessageData["um_id"] != nil
                {
                    let um_message = ZMessage();
                    um_message.mType = e_ZConnectorCommands.ZCMD_CONFIRM_LATE_MESSAGE
                    um_message.mMessageData = ["um_id" : (message!.mMessageData["um_id"] as? NSNumber)!]
                    SendMessage(um_message)
                }
            }

                nextTimout = 0.5;
            }

            NSTimer.scheduledTimerWithTimeInterval(nextTimout, target: self, selector: "ProcessIncomingQueuedMessages", userInfo: nil, repeats: false)
        }

        func processCommandContactVersionUpdated(message: ZMessage)
        {
            ...
        }

        func processCommandNotifyTyping(message: ZMessage)
        {
            ...
        }


        func processaMsgContactNumberPart(to_zamzam_id: NSNumber, contact_info: NSMutableDictionary) -> Void
        {
            let contactNumber = DBManager.Instance().PhoneNumberByZamZamId(to_zamzam_id)
            if contactNumber == nil
            {
                let new_contact = DBManager.Instance().createEntity("PhoneContact") as! PhoneContact
                let fullNameArr = (contact_info["full_name"] as! String).characters.split{$0 == " "}.map(String.init)
                new_contact.first_name = fullNameArr[0]
                new_contact.family_name = fullNameArr.count > 1 ? fullNameArr[1] : ""
                new_contact.identifier = ""

                var new_number : PhoneNumber
                    new_number = DBManager.Instance().createEntity("PhoneNumber") as! PhoneNumber
                new_number.phone_number = "+"+String(contact_info["country_code"] as! NSNumber)+String(contact_info["phone_number"] as! NSNumber)
                print("phone: "+new_number.phone_number!)
                new_number.contact = new_contact

                new_number.server_contact_id = contact_info["server_zamzam_id"] as? NSNumber


                new_contact.addPhoneNumber(new_number)

                DBManager.Instance().saveEntity(new_contact)
                DBManager.Instance().saveEntity(new_number)

                print("phone: "+String(new_number.server_contact_id!))
            }
        }

        func processGeneralMessageReceived(message: ZMessage, message_type: NSInteger, message_body: String) -> Message
        {
            let response_message = message.mMessageData["response_message"] as! NSMutableDictionary
            let new_message = self.processaMsgBasePart(message_body,
                    attachementType: message_type,//0 - text only, 1 - picture, 2 - video, 3 - smile
                fromZamZamStr: (message.mMessageData["from_zamzam_id"] as? String)!,
                rnd1: (message.mMessageData["rand1"] as? String)!,
                rnd2: (message.mMessageData["rand2"] as? String)!,
                serverMsgId: (response_message["server_message_id"] as! NSNumber?)!,
                sortId: (response_message["sort_id"] as! NSNumber?)!)
            //--------------------------------------------------------------------------------
            let dialog = self.processaMsgDialogPart(response_message["server_dialog_id"] as! NSNumber, from_zamzam_id_str: (message.mMessageData["from_zamzam_id"] as? String)!)
            new_message.dialog = dialog
            dialog.last_message = new_message.message_text
            DBManager.Instance().saveEntity(dialog)
            //--------------------------------------------------------------------------------

            //--------------------------------------------------------------------------------
            self.processaMsgContactNumberPart(dialog.to_zamzam_id!, contact_info: message.mMessageData["contact_info"] as! NSMutableDictionary)
            //--------------------------------------------------------------------------------

            DBManager.Instance().saveEntity(new_message)

            print("NewMessageFrom"+String(new_message.from_zamzam_id!))

            return new_message
        }

        func processCommandReceivedVideo(message: ZMessage)
        {
            ...
        }

        func processCommandReceivedPicture(message: ZMessage)
        {
            ...
        }

        func processCommandReceivedMessage(message: ZMessage)
        {
            ...
        }

        func processCommandSendMessageResponse(message: ZMessage)
        {
            ...
        }

        func processCommandSystem(message: ZMessage)
        {
            ...
        }

        func processCommandWhoHasZamZamResponse(message: ZMessage)
        {
            ...
        }

    }

}
*/