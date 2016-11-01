package com.menemi.dbfactory;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.menemi.PersonPage;
import com.menemi.R;
import com.menemi.dbfactory.rest.Loader;
import com.menemi.dbfactory.rest.PictureLoader;
import com.menemi.personobject.PersonalGift;

import org.json.JSONException;
import org.json.JSONObject;

public  class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String INTENT_FILTER = "INTENT_FILTER";
    public static final String ACTION = "action";
    public static final String ACTION_GIFT = "gift";
    public static final String TAG = "MyFirebaseMsgService";
    public static final String DATA = "data";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */


    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            try {
                JSONObject data = new JSONObject(remoteMessage.getData());
                if(data.getString(ACTION).equals(ACTION_GIFT)) {
                    Log.d(TAG, "gift recieved");
                    PersonalGift personalGift = Loader.parceGift(data);

                }
                if(data.getString(ACTION).equals(ACTION_GIFT)){
                    Log.d(TAG, "gift recieved");
                    PersonalGift personalGift = Loader.parceGift(data);
                    sendNotification(personalGift);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(PersonalGift personalGift) {
    /*    Intent intent = new Intent(this, PersonPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        new PictureLoader(personalGift.getAvatarUrl(), (bitmap)->{

            Intent intent = new Intent(INTENT_FILTER);
            intent.putExtra(ACTION, ACTION_GIFT);
            intent.putExtra(DATA, personalGift);
            sendBroadcast(intent);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

                    NotificationCompat.Builder notificationBuilder =  new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.icon)
                            .setContentTitle(getString(R.string.new_gift_notification))
                            .setContentText(getString(R.string.who_sended, personalGift.getPersonName()))
                            .setLargeIcon(bitmap)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        });






        //{"action":"profile_viewed","gift_id":"2","from_avatar_url":"http:\/\/minemi.ironexus.com\/system\/profile_pictures\/pictures\/000\/000\/058\/thumb\/file.png?1476471371","from_profile_id":"1","name":"a"}
        //{"action":"profile_added_to_favorite","profile_id":"1", "name":"ivan","avatar_url":"http:\"}
        //{"action":"profile_message_sent","dialog_id":"1", "profile_id":"2","last_message":"bla bla","last_message_date":"30.30.30","unread_msgs_count":"1","name":"Ivan","status":"online"}
        //{"action":"profile_message_sent","dialog_id":"1", "profile_id":"2","last_message":"bla bla","last_message_date":"30.30.30","unread_msgs_count":"1","name":"Ivan","status":"online"}
        //{"action":"profile_out_of_credits"}
        //{"action":"profile_reward_received", "reward_url":"http:"}
        ///
    }

}