package com.menemi.dbfactory;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.menemi.dbfactory.rest.DBRest;
import com.menemi.dbfactory.rest.PictureLoader;
import com.menemi.dbfactory.stream.StreamController;
import com.menemi.dbfactory.stream.messages.DialogSendMessage;
import com.menemi.dbfactory.stream.messages.PictureMessage;
import com.menemi.dbfactory.stream.messages.ReadMessage;
import com.menemi.dbfactory.stream.messages.TypingMessage;
import com.menemi.filter.FilterObject;
import com.menemi.interests_classes.InterestsGroup;
import com.menemi.personobject.Configurations;
import com.menemi.personobject.DialogInfo;
import com.menemi.personobject.Gift;
import com.menemi.personobject.Interests;
import com.menemi.personobject.NotificationSettings;
import com.menemi.personobject.PersonFavorite;
import com.menemi.personobject.PersonObject;
import com.menemi.personobject.PhotoSetting;
import com.menemi.personobject.PhotoTemplate;
import com.menemi.social_network.SocialProfile;
import com.menemi.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

public class DBHandler {
    protected static Context ctx;

    private static DBHandler instance = null;
    private static int currentVersion = 0;
    private static LinkedList<InternetConnectionListener> restSubscribers = new LinkedList<>();
    InternetConnectionListener internetConnectionListener = new InternetConnectionListener() {
        @Override
        public void internetON() {
            if(!wasAvailableLastTime) {
                for (int i = 0; i < restSubscribers.size(); i++) {
                    restSubscribers.get(i).internetON();
                }
                wasAvailableLastTime=true;
            }
        }

        @Override
        public void internetOFF() {
            if(wasAvailableLastTime) {
                for (int i = 0; i < restSubscribers.size(); i++) {
                    restSubscribers.get(i).internetOFF();
                }
                wasAvailableLastTime=false;
            }
        }
    };
    private boolean wasAvailableLastTime = false;
    private PersonObject myProfile = null;
    private DBSQLite dbSQLite = null;
    private DBRest dbRest = null;
    private StreamController stream = null;
    private ArrayList<PhotoTemplate> photoTemplates;
    public ArrayList<InterestsGroup> interestsGroupArray = new ArrayList<>();
    private ArrayList<Interests> profileInterests;
    private ArrayList<Gift> gifts;
    private Configurations configurations;
    private NotificationSettings notificationSettings;





    public ArrayList<Interests> getProfileInterests() {

        return profileInterests;
    }


    public PersonObject getMyProfile() {
        return myProfile;
    }





    protected DBHandler() {

    }

    public static void setUP(Context ctx) {
        if (DBHandler.ctx == null) {
            DBHandler.ctx = ctx;
            Locale current = ctx.getResources().getConfiguration().locale;
            Fields.PREFIX = current.getLanguage();

        }
    }


    public void lunchStreams() {
        stream = new StreamController(getUserId(), internetConnectionListener);
    }





    public static
    @NonNull
    DBHandler getInstance() {  //Issue with current version
        // DBHandler.currentVersion = currentVersion;
        try {
            if (ctx == null) {
                throw new ContextNotSetException();
            }
            if (instance == null) {
                Log.d("ssss", "null");
                instance = new DBHandler();
                instance.prepareDB();

            }

            return instance;

        } catch (ContextNotSetException ce) {
            ce.getMessage();
        }
        return instance;
    }

    public DialogSendMessage sendTextMessage(int dialogId, int profileID, String messageBody) {
        DialogSendMessage dialogMessage = new DialogSendMessage(dialogId, profileID, messageBody);
        stream.sendMessage(dialogMessage);
        return dialogMessage;

    }

    public void sendReadMessage(int dialogId, ArrayList<Integer> msgIds) {

        ReadMessage readMessage = new ReadMessage(getUserId(), dialogId, msgIds);
        stream.sendMessage(readMessage);


    }

    public void sendTypingMessage(int profileID, boolean isTyping) {
        stream.sendMessage(new TypingMessage(profileID, isTyping));
    }

    public void sendPictureMessage(int profileID, Bitmap picture) {
        stream.sendMessage(new PictureMessage(profileID, picture));
    }

    public static boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public void isRESTAvailable(final ResultListener resultListener) {
        if (isOnline()) {
            new DBRest().checkIsRestAvailable(new ResultListener() {
                @Override
                public void onFinish(Object object) {
                    if((boolean)object) {
                        internetConnectionListener.internetON();
                    } else{
                        internetConnectionListener.internetOFF();
                    }
                    resultListener.onFinish(object);
                }
            });
        } else {
            internetConnectionListener.internetOFF();
            resultListener.onFinish(false);
        }

    }

    public void authorise(final int personId, final ResultListener resultListener) {

        PersonObject personObject = dbSQLite.getProfile(personId);
        authorise(personObject, resultListener);

    }
    /**
     * @param personObject with filled email and password
     * @param resultListener
     */

    public void authorise(final PersonObject personObject, final ResultListener resultListener) {

        isRESTAvailable(new ResultListener() {
            @Override
            public void onFinish(Object object) {
                if ((boolean) object == true) {
                    if (dbRest == null || dbSQLite == null) {
                        prepareDB();
                    }

                    Log.v("DBHandler", "authorise rest" + dbRest);
                      dbRest.authorise(personObject, new DBRest.OnDataRecieveListener() {
                        @Override
                        public void onFinish(Object object) {
                            if (object != null) {

                                myProfile = (PersonObject) object;
                                dbSQLite.setPersonalInfo(myProfile);
                                dbSQLite.saveLastId(myProfile.getPersonId());
                                prepareSettingsForProfile();
                            }
                            if(photoTemplates == null || gifts == null){
                                prepareSettings(() -> {
                                    resultListener.onFinish(object);
                                });
                            } else {
                                resultListener.onFinish(object);
                            }


                        }
                    });
                } else {
                    subscribeToRest(new InternetConnectionListener() {
                        @Override
                        public void internetON() {
                            authorise(personObject, (Object obj)->{});

                        }@Override public void internetOFF() {}});

                    if (dbSQLite.getIdOnLoginData(personObject) != -1) {
                        Log.v("DBHandler", "id != -1");
                        myProfile = dbSQLite.getProfile(personObject.getPersonId());
                        resultListener.onFinish(myProfile);
                    } else {
                        Log.v("DBHandler", "id = -1");
                        resultListener.onFinish(null);
                    }
                }
            }
        });


    }
public void prepareSettings(Runnable runnable){
    preparePhotoTemplates((Object obj) ->{
        prepareGifts((Object object) ->{
            runnable.run();
        });
    });


}
    private void prepareSettingsForProfile(){
        prepareConfigurations(myProfile.getPersonId());
        getMyFavorites(new ResultListener() {
            @Override
            public void onFinish(Object object) {
                myProfile.setFavorites((ArrayList<PersonFavorite>) object);
            }
        });
    }
    public void addPhoto(final PhotoSetting photoSetting, final ResultListener resultListener) {
        isRESTAvailable(new ResultListener() {
            @Override
            public void onFinish(Object object) {
                if ((boolean) object == true) {
                    dbRest.addPhoto(photoSetting, resultListener);
                    myProfile.setPhotoCount(photoSetting.isPrivate(), myProfile.getPhotoCount(photoSetting.isPrivate()) + 1);
                }
            }
        });
    }


    public void deletePhoto(final PhotoSetting photoSetting, final int personId, final ResultListener resultListener) {
        isRESTAvailable(new ResultListener() {
            @Override
            public void onFinish(Object object) {
                if ((boolean) object == true) {
                    dbRest.deletePhoto(photoSetting.getPhotoId(), personId, resultListener);
                    myProfile.setPhotoCount(photoSetting.isPrivate(), myProfile.getPhotoCount(photoSetting.isPrivate()) - 1);
                }
            }
        });
    }

    public void uploadFilterSettings(final FilterObject filterObject, final ResultListener resultListener) {
        isRESTAvailable(new ResultListener() {
            @Override
            public void onFinish(Object object) {
                if ((boolean) object == true) {
                    dbRest.uploadFilterSettings(filterObject, resultListener);
                }
            }
        });
    }

    public void getInterestsGroup(final int personId, final ResultListener resultListener) {
        isRESTAvailable(new ResultListener() {
            @Override
            public void onFinish(Object object) {
                if ((boolean) object == true) {
                    if (interestsGroupArray.size() == 0)
                        dbRest.getInterestsGroup(personId, resultListener);
                }
            }
        });
    }

    public void getInterestProfile(final int personId, final int slavePersonId, final ResultListener resultListener) {
        isRESTAvailable(new ResultListener() {
            @Override
            public void onFinish(Object object) {
                if ((boolean) object == true) {
                    dbRest.getInterestsProfile(personId, slavePersonId, resultListener);
                }
            }
        });
    }

    public void deleteInterest(final int personId, final ArrayList<Integer> interestId, final ResultListener resultListener) {
        isRESTAvailable(new ResultListener() {
            @Override
            public void onFinish(Object object) {
                if ((boolean) object == true) {
                    dbRest.deleteInterest(personId, interestId, resultListener);
                }
            }
        });
    }

    public void findInterests(final String partWord, final int personId, final ResultListener resultListener) {
        isRESTAvailable(new ResultListener() {
            @Override
            public void onFinish(Object object) {
                if ((boolean) object == true) {
                    dbRest.findInterests(partWord, personId, resultListener);
                }
            }
        });
    }

    public void addInterests(final int personId, final ArrayList<Integer> interestId, final ResultListener resultListener) {
        isRESTAvailable(new ResultListener() {
            @Override
            public void onFinish(Object object) {
                if ((boolean) object == true) {
                    dbRest.addInterest(personId, interestId, resultListener);
                }
            }
        });
    }

    public NotificationSettings getNotifications() {
       return notificationSettings;
    }

    public void downloadFilterSettings(final int personId, final ResultListener resultListener) {
        isRESTAvailable(new ResultListener() {
            @Override
            public void onFinish(Object object) {
                if ((boolean) object == true) {
                    dbRest.downloadFilterSettings(personId, resultListener);

                }
            }
        });
    }



    private void startRestMonitor() {

        new Thread(new Runnable() {
            boolean isRestAvailable = false;

            @Override
            public void run() {

                while (true) {



                            if (wasAvailableLastTime != isRestAvailable) {
                                isRestAvailable = wasAvailableLastTime;

                                for (int i = 0; i < restSubscribers.size(); i++) {
                                    if (isRestAvailable) {
                                        restSubscribers.get(i).internetON();
                                    } else{
                                        restSubscribers.get(i).internetOFF();
                                    }
                                }

                            }


                    try {
                        Thread.sleep(5000l);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                }
            }
        ).start();
    }

    public void subscribeToRest(InternetConnectionListener connectionListener) {
        if (wasAvailableLastTime) {
            connectionListener.internetON();
        } else {
            connectionListener.internetOFF();
        }
        restSubscribers.add(connectionListener);
    }

    public void unsubscribeFromRest(InternetConnectionListener connectionListener){
        restSubscribers.remove(connectionListener);
    }

    private void prepareDB() {
        //TODO: change and update db instance if necessary

        dbRest = new DBRest();
        dbSQLite = new DBSQLite(ctx);

        getInstance().prepareSettings(() -> {});
        instance.startRestMonitor();


    }

    /**
     * @param personId       id of picture owner
     * @param resultListener will return Bitmap photo = (Bitmap)object;
     */
    public void getAvatar(final int personId, final ResultListener resultListener) {

        isRESTAvailable(new ResultListener() {
            @Override
            public void onFinish(Object object) {
                if ((boolean) object == true) {
                    dbRest.getPicture(personId, Utils.PICTURE_QUALITY_THUMBNAIL, new ResultListener() {
                        @Override
                        public void onFinish(Object object) {
                            String url = (String) object;
                            dbSQLite.saveAvatarURL(personId, url);
                            new PictureLoader(url, (Bitmap picture) ->{
                                resultListener.onFinish(picture);
                            });
                        }
                    });
                } else {
                    String avatarUrl = dbSQLite.getAvatarURL(personId);
                    if(avatarUrl != null){
                        new PictureLoader(avatarUrl, (Bitmap picture) ->{
                            resultListener.onFinish(picture);
                        });
                    } else {
                        resultListener.onFinish(null);
                    }


                }
            }
        });


    }

    /**
     *
     * @param id
     * @param isThumbnail
     * @param isPrivate
     * @param resultListener  ArrayList<PhotoSetting> pictures = (ArrayList<PhotoSetting>)object;
     */
    public void getPhotoUrls(int id, String isThumbnail, boolean isPrivate, final ResultListener resultListener) {

        isRESTAvailable( (Object object) -> {
                if ((boolean) object == true) {
                    dbRest.getPhotoUrls(id, isThumbnail, Utils.boolToInt(isPrivate), getUserId(), (Object obj) ->{
                        ArrayList<PhotoSetting> pictures = (ArrayList<PhotoSetting>)obj;
                        for (int i = 0; i < pictures.size(); i++) {
                        dbSQLite.savePicture(id, isThumbnail,pictures.get(i));
                        }
                        resultListener.onFinish(pictures);
                    });

                } else {

                    resultListener.onFinish(dbSQLite.getPhotoUrls(id, isThumbnail, isPrivate)); // instead of null should be dbSQLite.getPhoto(personId)
                }

        });

    }
    public Bitmap getBitmapFromDB(String url){
        return dbSQLite.getBitmap(url);
    }
    public void saveBitmapToDB(String url, Bitmap photo) {
        dbSQLite.saveBitmap(url, photo);
    }
    public void logout(Context ctx) {
Log.d("logout", "logout1");
        restSubscribers = new LinkedList<>();
        myProfile = null;
        wasAvailableLastTime = false;

        stream.disconnect();
        stream=null;
        instance = new DBHandler();
        instance.setUP(ctx);
        instance.prepareDB();
        dbSQLite.clearDataBase();
        dbSQLite.saveLastId(-1);

        Utils.clearCashe(ctx);
        Log.d("logout", "logout2");
    }

    /**
     * @param resultListener ArrayList<PersonLike> likes
     */
    public void getLikes(ResultListener resultListener) {
        dbRest.getLikes(getUserId(), resultListener);
    }

    /**
     * @param resultListener ArrayList<PersonLike> likes
     */
    public void getMutualLikes(ResultListener resultListener) {
        dbRest.getMutualLikes(getUserId(), resultListener);
    }
//private String notification
    public void setFireBaseToken(String token){
        dbSQLite.setFireBaseToken(token);
    }
    public void sendFirebaseTokentToServer() {
        String firebaseToken =  dbSQLite.getFireBaseToken();
        dbRest.setNotificationToken(getUserId(),firebaseToken, new ResultListener() {
            @Override
            public void onFinish(Object object) {

            }
        });
    }

    public void getNextRandomProfile(final ResultListener resultListener) {
        isRESTAvailable(new ResultListener() {
            @Override
            public void onFinish(Object object) {
                if ((boolean) object == true) {
                    dbRest.getNextRandomProfile(getUserId(), resultListener);
                } else {
                    resultListener.onFinish(null);
                }
            }
        });

    }

/*
    */


    public void addFavorites(int personId, int slavePersonId, boolean isAdded, ResultListener resultListener) {
        dbRest.addFavorites(personId, slavePersonId, isAdded, resultListener);
    }

    public Configurations getConfigurations() {
        return configurations;
    }

    public void prepareConfigurations(int id) {
        dbRest.getConfigurations(id, new ResultListener() {
            @Override
            public void onFinish(Object object) {
                configurations = (Configurations) object;
            }
        });
        dbRest.getNotifications(id, (Object object)->{
            notificationSettings = (NotificationSettings)object;
        });

    }

    public void setConfigurations(Configurations configurations, ResultListener resultListener) {
        dbRest.setConfigurations(configurations, resultListener);
    }

    public void getMyProfile(final ResultListener resultListener) {
        if (myProfile == null) {

            isRESTAvailable(new ResultListener() {
                @Override
                public void onFinish(Object object) {
                    if ((boolean) object) {
                        dbRest.getProfile(getUserId(), new ResultListener() {
                            @Override
                            public void onFinish(Object object) {
                                myProfile = (PersonObject) object;
                                dbSQLite.setPersonalInfo((PersonObject) object);
                                resultListener.onFinish(object);

                            }
                        });

                    } else {
                        myProfile = dbSQLite.getProfile(getUserId());
                        resultListener.onFinish(myProfile);
                    }
                }
            });
        } else {
            resultListener.onFinish(myProfile);
        }

    }

    public void register(final PersonObject personObject, final ResultListener resultListener) {

        isRESTAvailable(new ResultListener() {
            @Override
            public void onFinish(Object object) {
                if ((boolean) object == true) {
                    if (dbRest == null || dbSQLite == null) {
                        prepareDB();
                    }
                    dbRest.register(personObject, new DBRest.OnDataRecieveListener() {
                        @Override
                        public void onFinish(Object object) {
                            Log.v("DBHandler", "register rest finish listener");
                            Log.v("DBHandler", "reg " + object);

                            if (object != null) {
                                PersonObject personObject = (PersonObject) object;
                                myProfile = personObject;
                                dbSQLite.setPersonalInfo(personObject);
                                dbSQLite.saveLastId(personObject.getPersonId());

                            }


                            resultListener.onFinish(object);
                        }
                    });

                }
            }
        });


    }

    /**
     * @param otherPersonId
     * @param resultListener PersonObject
     */
    public void getOtherProfile(int otherPersonId, ResultListener resultListener) {
        dbRest.getOtherProfile(getUserId(), otherPersonId, resultListener);
    }

    public void getInterestsFromGroup(int personId, int interestsGroup, ResultListener resultListener) {
        dbRest.getInterestsFromGroup(personId, interestsGroup, resultListener);
    }

    /**
     * @param resultListener ArrayList<PersonFavorite>
     */
    public void getMyFavorites(ResultListener resultListener) {
        dbRest.getMyFavorites(getUserId(), resultListener);
    }


    /**
     * @param resultListener returns ArrayList<PersonVisitor> visitors
     */
    public void getVisitors(ResultListener resultListener) {
        dbRest.getVisitors(getUserId(), resultListener);
    }




    public SocialProfile getSocialProfile(String socNetwork) {
        return dbSQLite.getSocial(socNetwork);
    }

    public boolean isEmtyTable(String tableName) {
        return dbSQLite.isEmtyTable(tableName);
    }



    public int getIdOnLoginData(PersonObject personObject) {
        return dbSQLite.getIdOnLoginData(personObject);
    }


    public void saveLastId(int userID) {
        dbSQLite.saveLastId(userID);
    }

    public void clearTable(String s) {
        dbSQLite.clearTable(s);
    }

    public void setSocialVK(SocialProfile profile, String socNetwork) {
        dbSQLite.setSocialVK(profile, socNetwork);
    }



    public void setSocialOK(SocialProfile profile, String socNetwork) {
        dbSQLite.setSocialOK(profile, socNetwork);
    }


    public void setSocialINSTA(SocialProfile profile, String socNetwork) {
        dbSQLite.setSocialINSTA(profile, socNetwork);
    }

    public void setSocialFB(SocialProfile profile, String socNetwork) {
        dbSQLite.setSocialFB(profile, socNetwork);
    }


    public int loadLastId() {
        return dbSQLite.loadLastId();
    }


    public void disLike(int likedId, boolean isLiked, ResultListener resultListener) {
        dbRest.disLike(getUserId(), likedId, isLiked, resultListener);
    }

    /**
     * @param resultListener ArrayList<PayPlan> plans = ( ArrayList<PayPlan>) object;
     */
    public void getPayPlans(ResultListener resultListener) {
        dbRest.getPayPlans(getUserId(), resultListener);
    }



     /**
      *
      * @param count
      * @param offset
      * @param requestedId
      * @param resultListener   ArrayList<DialogMessage> dialogMessages = (ArrayList<DialogMessage>) object;
      */
    public void getMessagesList(int count, int offset, int requestedId, ResultListener resultListener) {
        dbRest.getMessagesList(requestedId, count, offset, getUserId(), resultListener);
    }

    public int getUserId() {
        if (myProfile != null) {
            return myProfile.getPersonId();
        } else {
            return dbSQLite.getUserId();
        }
    }




    /**
     * @param quality
     * @param photosRequested
     * @param resultListener  object must be casted to HashMap<Integer, Bitmap> where Integer is person's ID
     */
    public void getMultiplePeoplePictures(String quality, ArrayList<Integer> photosRequested, ResultListener resultListener) {
        dbRest.getMultiplePeoplePictures(quality, photosRequested, resultListener);
    }

    public void unlockPhoto(PhotoSetting photoSetting, ResultListener resultListener) {
        dbRest.unlockPhoto(photoSetting.getPhotoId(), getUserId(), (Object obj)->{
            photoSetting.setUnlocked(true);
            resultListener.onFinish(photoSetting);
        });
    }

    /**
     * @param milesRadius
     * @param resultListener object must be casted to ArrayList<PersonObject>
     */
    public void getUsersAround(int milesRadius, ResultListener resultListener) {
        dbRest.getUsersAround(getUserId(), milesRadius, resultListener);
    }

    public void setPersonPosition(LatLng latLng, ResultListener resultListener) {
        myProfile.setPositionLatitude(latLng.latitude);
        myProfile.setPositionLongitude(latLng.longitude);
        dbRest.setUserPosition(getUserId(), latLng.latitude, latLng.longitude, resultListener);
    }


    public void getPlacesList(String placeName, ResultListener resultListener) {
        dbRest.getPlacesList(placeName, resultListener);
    }

    public void getUsersPlaceInfo(String placeId, ResultListener resultListener) {
        dbRest.getUsersPlaceInfo(placeId, resultListener);
    }


    public void setNotifications(String fieldName, String values, ResultListener resultListener) {
        dbRest.setNotifications(getUserId(), fieldName, values, resultListener);

    }






    public void setInterestsGroupArray(ArrayList<InterestsGroup> interestsGroupArray) {
        this.interestsGroupArray = interestsGroupArray;
    }

    public ArrayList<InterestsGroup> getInterestsGroupArray() {
        return interestsGroupArray;
    }

    public void setProfileInterests(ArrayList<Interests> profileInterests) {
        this.profileInterests = profileInterests;
    }


    public void getDialogsList(ResultListener resultListener) {
        isRESTAvailable(new ResultListener() {
            @Override
            public void onFinish(Object object) {
                if((boolean) object) {
                    dbRest.getDialogsList(getUserId(), new ResultListener() {
                        @Override
                        public void onFinish(Object object) {
                            ArrayList<DialogInfo> dialogInfos = (ArrayList<DialogInfo>)object;
                            resultListener.onFinish(dialogInfos);
                            for (int i = 0; i < dialogInfos.size(); i++) {
                                dbSQLite.setDialog(dialogInfos.get(i));
                            }

                        }
                    });
                } else{
                    resultListener.onFinish(dbSQLite.getDialogs());

                }
            }
        });


    }

    public void getDialog(final DialogInfo dialogInfo, final ResultListener resultListener) {
        dbRest.getDialogId(getUserId(), dialogInfo.getProfileId(), new ResultListener() {
            @Override
            public void onFinish(Object object) {
                dialogInfo.setDialogID((int) object);
                getAvatar(dialogInfo.getProfileId(), new ResultListener() {
                    @Override
                    public void onFinish(Object object) {
                        Bitmap photo = (Bitmap) object;
                        dialogInfo.setConatactAvatar(photo);
                        resultListener.onFinish(dialogInfo);
                    }
                });

            }
        });

    }

    /**
     * @param photoID
     * @param quality
     * @param resultListener PhotoSetting photoSetting = (PhotoSetting) object;
     */


    public void getPhotoByID(int photoID, String quality, ResultListener resultListener) {
        dbRest.getPhotoByID(getUserId(), photoID, quality, resultListener);
    }


    public ArrayList<PhotoTemplate> getPhotoTemplates() {
        if(photoTemplates== null)
        {
            return null;
        }
            ArrayList<PhotoTemplate> templates = (ArrayList<PhotoTemplate>) photoTemplates.clone();

        return templates;
    }
    public Bitmap getPhotoTemplatePictureById(int id){
        for (int i = 0; i < photoTemplates.size(); i++) {
            if(photoTemplates.get(i).getTemplateID() == id){
                return  photoTemplates.get(i).getTemplatePicture();
            }
        }
        return null;
    }


    public int getGiftsCount() {

        return gifts.size();
    }

    public Bitmap getGiftById(int id){
        for (int i = 0; i < gifts.size(); i++) {
            if(gifts.get(i).getGiftId() == id){
                return  gifts.get(i).getImage();
            }
        }
        return null;
    }
    private void preparePhotoTemplates(ResultListener resultListener) {

           isRESTAvailable((Object isAvailable) ->{
            if((boolean)isAvailable){
                dbRest.getPhotoTemplates(1, (Object object) ->{
                    photoTemplates = (ArrayList<PhotoTemplate>) object;

                    resultListener.onFinish(null);
                });
            } else{
                photoTemplates = dbSQLite.getTemplates();
                if(photoTemplates.size() == 0){
                    subscribeToRest(new InternetConnectionListener() {
                        @Override
                        public void internetON() {
                            dbRest.getPhotoTemplates(1, (Object object) ->{
                                photoTemplates = (ArrayList<PhotoTemplate>) object;
                            });
                        }

                        @Override
                        public void internetOFF() {

                        }
                    });
                }

            }



        });
    }

    public void setGiftToDB(Gift gift) {
       dbSQLite.setGift(gift);
    }
    public void setTemplateToDB(PhotoTemplate template) {
        dbSQLite.setTemplate(template);
    }
    private void prepareGifts(ResultListener resultListener) {
        isRESTAvailable((Object isAvailable) ->{
            if((boolean)isAvailable){
                dbRest.prepareGifts(1, (Object object) ->{
                    gifts = (ArrayList<Gift>) object;

                    resultListener.onFinish(null);
                });
            } else{
                gifts = dbSQLite.getGifts();
                if(gifts.size() == 0){
                    subscribeToRest(new InternetConnectionListener() {
                        @Override
                        public void internetON() {
                            dbRest.prepareGifts(1, (Object object) ->{
                                gifts = (ArrayList<Gift>) object;

                            });
                        }

                        @Override
                        public void internetOFF() {

                        }
                    });
                }

            }



        });

    }

    public ArrayList<Gift> getGifts() {
        return gifts;
    }

    public void buyGift(int giftID, int personID, ResultListener resultListener) {
        dbRest.buyGift(getUserId(), personID, giftID, resultListener);
    }

    public interface ResultListener extends DBRest.OnDataRecieveListener {

    }

    static class ContextNotSetException extends Exception {
        @Override
        public String getMessage() {
            return "ERROR CONTEXT IS NOT SET " + super.getMessage();
        }
    }
}
