package com.menemi.dbfactory;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.menemi.LoadingActivity;
import com.menemi.R;
import com.menemi.dbfactory.rest.Loader;
import com.menemi.dbfactory.rest.PictureLoader;
import com.menemi.personobject.DialogInfo;
import com.menemi.personobject.PersonObject;
import com.menemi.personobject.PersonalGift;

import org.json.JSONException;
import org.json.JSONObject;

public  class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String INTENT_FILTER = "INTENT_FILTER";
    public static final String ACTION = "action";
    public static final String ACTION_FAVORITE = "profile_added_to_favorite";
    public static final String ACTION_GIFT = "gift";
    public static final String TAG = "MyFirebaseMsgService";
    public static final String DATA = "data";
    public static final String ACTION_MESSAGE = "profile_message_sent";
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
                    sendNotification(personalGift);

                }

                if(data.getString(ACTION).equals(ACTION_FAVORITE)){
                    Log.d(TAG, "favorite recieved");
                    PersonObject person = new PersonObject(PersonObject.EMPTY, PersonObject.EMPTY);
                    person.setPersonId(data.getInt(Fields.PROFILE_ID_2));
                    person.setPersonName(data.getString(Fields.NAME));
                    person.setPersonAvatarURL(data.getString(Fields.AVATAR_URL));
                    sendNotification(person, ACTION_FAVORITE);

                }
                if(data.getString(ACTION).equals(ACTION_MESSAGE )){
                    Log.d(TAG, "message recieved");
                    DialogInfo dialogInfo = new DialogInfo();
                    dialogInfo.setDialogID(data.getInt(Fields.DIALOG_ID));
                    dialogInfo.setProfileId(data.getInt(Fields.PROFILE_ID_2));
                    dialogInfo.setLastMessage(data.getString(Fields.LAST_MESSAGE));
                    dialogInfo.setLastMessageDate(data.getString(Fields.LAST_MESSAGE_AT));
                    dialogInfo.setNewMessagesCount(data.getInt(Fields.UNREAD_COUNT));
                    dialogInfo.setContactName(data.getString(Fields.NAME));
                    dialogInfo.setOnline(data.getString(Fields.STATUS));

                    sendNotification(dialogInfo, ACTION_FAVORITE);


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


    private void sendNotification(PersonalGift personalGift) {
    /*    Intent intent = new Intent(this, PersonPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        new PictureLoader(personalGift.getAvatarUrl(), (bitmap) -> {

            Intent intent = new Intent(INTENT_FILTER);

            intent.putExtra(ACTION, ACTION_GIFT);
            intent.putExtra(DATA, personalGift);


                intent.setClass(this, LoadingActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.icon)
                    .setContentTitle(getString(R.string.new_gift_notification))
                    .setContentText(getString(R.string.who_sended, personalGift.getPersonName()))
                    .setLargeIcon(bitmap)
                    .setSound(defaultSoundUri)
                    .setAutoCancel(true)

                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());

        });
    }

    private void sendNotification(PersonObject person, String action) {
    /*    Intent intent = new Intent(this, PersonPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        new PictureLoader(person.getPersonAvatarURL(), (bitmap)->{

            Intent intent = new Intent(INTENT_FILTER);
            intent.putExtra(ACTION, action);
            intent.putExtra(DATA, person);
            intent.setClass(this, LoadingActivity.class);
          //  sendBroadcast(intent);


                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);
                NotificationCompat.Builder notificationBuilder = null;
                if (action == ACTION_FAVORITE) {
                    notificationBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.icon)
                            .setContentTitle(getString(R.string.new_favorite_notification))
                            .setContentText(getString(R.string.who_favorited, person.getPersonName()))
                            .setLargeIcon(bitmap)
                            .setSound(defaultSoundUri)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent);
                }


                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());


        });
    }

    private void sendNotification(DialogInfo dialogInfo, String action) {
    /*    Intent intent = new Intent(this, PersonPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        DBHandler.getInstance().getAvatar(dialogInfo.getProfileId(), (picture)->{
            Bitmap bitmap = (Bitmap)picture;
            Intent intent = new Intent(INTENT_FILTER);
            intent.putExtra(ACTION, action);
            intent.putExtra(DATA, dialogInfo);
            intent.setClass(this, LoadingActivity.class);
            //  sendBroadcast(intent);


            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder notificationBuilder = null;

                notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.icon)
                        .setContentTitle(getString(R.string.new_message_notification, dialogInfo.getContactName()))
                        .setContentText(dialogInfo.getLastMessage())
                        .setLargeIcon(bitmap)
                        .setSound(defaultSoundUri)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);



            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());


        });
    }

        //{"action":"profile_viewed","gift_id":"2","from_avatar_url":"http:\/\/minemi.ironexus.com\/system\/profile_pictures\/pictures\/000\/000\/058\/thumb\/file.png?1476471371","from_profile_id":"1","name":"a"}
        //{"action":"profile_added_to_favorite","profile_id":"1", "name":"ivan","avatar_url":"http:\"}
        //{"action":"profile_message_sent","dialog_id":"1", "profile_id":"2","last_message":"bla bla","last_message_date":"30.30.30","unread_msgs_count":"1","name":"Ivan","status":"online"}
        //{"action":"profile_out_of_credits"}
        //{"action":"profile_reward_received", "reward_url":"http:"}
        ///


}