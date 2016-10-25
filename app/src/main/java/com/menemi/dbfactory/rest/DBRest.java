package com.menemi.dbfactory.rest;

import android.util.Log;

import com.menemi.dbfactory.Fields;
import com.menemi.dbfactory.messages.SendableMessage;
import com.menemi.filter.FilterObject;
import com.menemi.personobject.Configurations;
import com.menemi.personobject.PersonObject;
import com.menemi.personobject.PhotoSetting;

import java.util.ArrayList;

/**
 * Created by irondev on 07.06.16.
 */
public class DBRest {


    public DBRest() {

    }

    public void sendMessageToRest(SendableMessage message) {
        message.getMessage();
    }

    public String getName(int userId) {
        String name = "";
      /*  new Loader("http://ellotv.bigdig.com.ua/api/home/video", new Loader.OnLoadFinishListener() {
            @Override
            public void onFinish() {

            }
        });

        return*/
        return "sdfg";
    }
  /*  public void register(PersonObject personObject) {

        new Sender(Sender.RestCommands.REGISTER, personObject).execute();

    }*/

    public void authorise(PersonObject personObject, final OnDataRecieveListener onDataRecieveListener) {

        new Sender(Sender.RestCommands.AUTHORISE, personObject, new Sender.OnUploadListener() {
            @Override
            public void onUploadFinish(String s) {
                Log.v("DBRest", "on upload finish listener");
                Loader.parcing(Loader.RestCommands.GET_MY_PROFILE, s, onDataRecieveListener);
            }
        }).execute();
    }

    public void addPhoto(PhotoSetting photoSetting,final OnDataRecieveListener onDataRecieveListener){
        new Sender(Sender.RestCommands.ADD_PHOTO,photoSetting, new Sender.OnUploadListener()
        {
            @Override
            public void onUploadFinish(String s)
            {
                if(s.equals("{\"result\":\"success\"}")){
                    onDataRecieveListener.onFinish(true);
                } else{onDataRecieveListener.onFinish(false);}
            }
        }).execute();
    }
    public void setInfo(PersonObject personObject, final OnDataRecieveListener onDataRecieveListener){
        new Sender(Sender.RestCommands.SET_INFO, personObject, new Sender.OnUploadListener()
        {
            @Override
            public void onUploadFinish(String s)
            {
                if(s.equals("{\"result\":\"success\"}")){
                    onDataRecieveListener.onFinish(true);
                } else{onDataRecieveListener.onFinish(false);}
            }
        }).execute();

    }

    public void uploadFilterSettings(FilterObject filterObject, final  OnDataRecieveListener onDataRecieveListener){
        new Loader(Loader.RestCommands.UPLOAD_FILTER_SETTINGS, filterObject,onDataRecieveListener).execute();
    }
    public void downloadFilterSettings(int personId, final OnDataRecieveListener onDataRecieveListener){
        new Loader(Loader.RestCommands.DOWNLOAD_FILTER_SETTINGS,personId,onDataRecieveListener).execute();
    }

    public void checkIsRestAvailable(OnDataRecieveListener onDataRecieveListener) {
        new Loader(onDataRecieveListener).execute();
    }
    public void getProfile(int id, OnDataRecieveListener onDataRecieveListener) {
        new Loader(Loader.RestCommands.GET_PROFILE, id, id, onDataRecieveListener).execute();
    }

    public void getInterestsGroup(int id, OnDataRecieveListener onDataRecieveListener){
            new Loader(Loader.RestCommands.GET_INTERESTS_GROUP,id,onDataRecieveListener).execute();
    }

    public void getInterestsProfile(int profileId, int slaveId, OnDataRecieveListener onDataRecieveListener){
        new Loader(Loader.RestCommands.GET_PROFILE_INTERESTS,profileId,slaveId,onDataRecieveListener).execute();
    }

    public void deleteInterest(int personId, ArrayList<Integer> interestId, OnDataRecieveListener onDataRecieveListener){
        new Loader(Loader.RestCommands.DELETE_INTEREST,personId,interestId,onDataRecieveListener).execute();
    }

    public void addInterest(int personId, ArrayList<Integer> interestId, OnDataRecieveListener onDataRecieveListener){
        new Loader(Loader.RestCommands.ADD_INTERESTS,personId,interestId,onDataRecieveListener).execute();
    }
    public void getNotifications(int id, OnDataRecieveListener onDataRecieveListener){
        new Loader(Loader.RestCommands.GET_NOTIFICATIONS,id,onDataRecieveListener).execute();
    }
    public void getOtherProfile(int id, int otherPersonId, OnDataRecieveListener onDataRecieveListener) {
        new Loader(Loader.RestCommands.GET_PROFILE, id, otherPersonId, onDataRecieveListener).execute();

    }

    public void getInterestsFromGroup(int personId, int groupId, OnDataRecieveListener onDataRecieveListener){
        new Loader(Loader.RestCommands.GET_INTERESTS_FROM_GROUP,personId,groupId, onDataRecieveListener).execute();
    }

    public void getNextRandomProfile(int id, OnDataRecieveListener onDataRecieveListener) {
        new Loader(Loader.RestCommands.GET_RANDOM_PROFILE_TO_LIKE, id, onDataRecieveListener).execute();
    }

    public void setNotificationToken(int id, String token, OnDataRecieveListener onDataRecieveListener) {
        new Loader(Loader.RestCommands.SET_NOTIFICATION_TOKEN, token, id, onDataRecieveListener).execute();
    }

    /**
     *
     * @param id
     * @param onDataRecieveListener   ArrayList<PersonVisitor> visitors
     */
    public void getVisitors(int id, OnDataRecieveListener onDataRecieveListener) {
        new Loader(Loader.RestCommands.GET_VISITORS, id, onDataRecieveListener).execute();
    }

    /**
     *
     * @param id
     * @param onDataRecieveListener ArrayList<PersonLike> likes
     */
    public void getLikes(int id, OnDataRecieveListener onDataRecieveListener) {
        new Loader(Loader.RestCommands.GET_LIKES, id, onDataRecieveListener).execute();
    }

    public void getMutualLikes(int id, OnDataRecieveListener onDataRecieveListener) {
        new Loader(Loader.RestCommands.GET_MUTUAL_LIKES, id, onDataRecieveListener).execute();
    }

    public void disLike(int id, int likedId, boolean isLiked, OnDataRecieveListener onDataRecieveListener) {
        new Loader(Loader.RestCommands.DIS_LIKE, id, likedId, isLiked, onDataRecieveListener).execute();
    }

    public void addFavorites(int id, int likedId, boolean isAdded, OnDataRecieveListener onDataRecieveListener) {
        new Loader(Loader.RestCommands.ADD_FAVORITES, id, likedId, isAdded, onDataRecieveListener).execute();
    }

    /**
     *
     * @param id
     * @param isThumbnail
     * @param onDataRecieveListener String url = (String) object;
     */
    public void getPicture(int id, String isThumbnail, OnDataRecieveListener onDataRecieveListener) {
        new Loader(Loader.RestCommands.GET_AVATAR, id, isThumbnail, onDataRecieveListener).execute();
    }

    public void getPhotos(int personId, int requestedId, int photoNumber, int count, String quality, OnDataRecieveListener onDataRecieveListener) {
        new Loader(Loader.RestCommands.GET_PHOTO, personId, requestedId, photoNumber, count, quality, onDataRecieveListener).execute();
    }

    public void deletePhoto(int photoNumber, int personId, OnDataRecieveListener onDataRecieveListener){
        new Loader(Loader.RestCommands.DELETE_PHOTO, photoNumber,personId,onDataRecieveListener).execute();
    }

    public void getMultiplePeoplePictures(String isThumbnail, ArrayList<Integer> photosRequested, OnDataRecieveListener onDataRecieveListener) {
        new Loader(Loader.RestCommands.GET_MULTIPLE_PERSONS_PHOTO, isThumbnail, photosRequested, onDataRecieveListener).execute();
    }

    public void unlockPhoto(int photoId, int ownerId, OnDataRecieveListener onDataRecieveListener) {
        new Loader(Loader.RestCommands.GET_UNLOCK_PHOTO, photoId, ownerId, onDataRecieveListener).execute();
    }


    public void setUserPosition(int id, double latitude, double longitude, OnDataRecieveListener onDataRecieveListener) {
        new Loader(Loader.RestCommands.NOTIFY_POSITION, id, ""+latitude, ""+longitude, onDataRecieveListener).execute();
    }

    public void getPlacesList(String placeName, OnDataRecieveListener onDataRecieveListener ){
        new Loader(Loader.RestCommands.GET_PLACES_LIST, placeName,Loader.G_GET_PLACES_LIST_TARGET_VALUE + Fields.PREFIX,
                Loader.G_GET_PLACES_LIST_KEY_VALUE + Loader.G_GOOGLE_PACES_API_KEY, onDataRecieveListener).execute();
    }
    public void findInterests(String partWord, int userId, OnDataRecieveListener onDataRecieveListener){
        new Loader(Loader.RestCommands.GET_FIND_INTERESTS, userId, partWord, onDataRecieveListener).execute();
    }
    public void getUsersPlaceInfo(String placeId, OnDataRecieveListener onDataRecieveListener){
        new Loader(Loader.RestCommands.GET_USERS_PLACE_INFO, placeId
                + Loader.G_GET_PLACES_LIST_KEY_VALUE + Loader.G_GOOGLE_PACES_API_KEY,onDataRecieveListener).execute();
    }

    public void getUsersAround(int id, int milesRadius, OnDataRecieveListener onDataRecieveListener) {
        new Loader(Loader.RestCommands.GET_USERS_NEAR, id, milesRadius, onDataRecieveListener).execute();
    }
    public void getMyFavorites(int id, OnDataRecieveListener onDataRecieveListener) {
        new Loader(Loader.RestCommands.GET_MY_FAVORITES, id, onDataRecieveListener).execute();
    }
    public void getOtherFavorites(int id, OnDataRecieveListener onDataRecieveListener) {
        new Loader(Loader.RestCommands.GET_OTHER_FAVORITES, id, onDataRecieveListener).execute();
    }
    public void getConfigurations(int id, OnDataRecieveListener onDataRecieveListener) {
        new Loader(Loader.RestCommands.GET_CONFIGURATION_SETTINGS, id, onDataRecieveListener).execute();
    }
    public void setConfigurations(Configurations configurations, final OnDataRecieveListener onDataRecieveListener) {
        new Sender(Sender.RestCommands.SET_CONFIGURATIONS, configurations, new Sender.OnUploadListener() {
            @Override
            public void onUploadFinish(String s) {
                Loader.parcing(Loader.RestCommands.GET_CONFIGURATION_SETTINGS, s, onDataRecieveListener);
            }
        }).execute();
    }

  /*  public void getPayPlans(int id, OnDataRecieveListener onDataRecieveListener) {
        new Loader(Loader.RestCommands.GET_PAY_PLANS, id, onDataRecieveListener).execute();
    }*/
    public void register(PersonObject personObject, final OnDataRecieveListener onDataRecieveListener) {
        new Sender(Sender.RestCommands.REGISTER, personObject, new Sender.OnUploadListener() {
            @Override
            public void onUploadFinish(String s) {
                Log.v("DBRest", "on upload finish listener");
                Loader.parcing(Loader.RestCommands.GET_MY_PROFILE, s, onDataRecieveListener);
            }
        }).execute();
    }
    public void getPayPlans(int id, OnDataRecieveListener onDataRecieveListener) {
        new Loader(Loader.RestCommands.GET_PAY_PLANS, id, onDataRecieveListener).execute();
    }
  public void getDialogsList(int id, OnDataRecieveListener onDataRecieveListener) {
        new Loader(Loader.RestCommands.GET_DIALOGS, id, onDataRecieveListener).execute();
    }
    // (:dialog_id)/(:count)/(:offset)/(:requesting_profile_id)
    public void getMessagesList(int requestedId, int count, int offset, int ownerID, OnDataRecieveListener onDataRecieveListener) {
        new Loader(Loader.RestCommands.GET_MESSAGES, requestedId, count,  offset,  ownerID, onDataRecieveListener).execute();
    }
    public void setNotifications(int id, String fieldName, String values,OnDataRecieveListener onDataRecieveListener) {
        new Loader(Loader.RestCommands.SET_NOTIFICATIONS, fieldName, values,id, onDataRecieveListener).execute();

    }
    public void getDialogId(int ownerID, int requestedId,OnDataRecieveListener onDataRecieveListener) {
        new Loader(Loader.RestCommands.GET_CREATE_DIALOG, requestedId, ownerID, onDataRecieveListener).execute();

    }
    public void prepareGifts(int requestingID, OnDataRecieveListener onDataRecieveListener){
        new Loader(Loader.RestCommands.GET_ALL_GIFTS,requestingID, onDataRecieveListener ).execute() ;
    }
    public void buyGift(int ownerID, int toID, int giftID, OnDataRecieveListener onDataRecieveListener){
        new Loader(Loader.RestCommands.BUY_GIFT,giftID,toID, ownerID, onDataRecieveListener ).execute() ;
    }
    public void getPhotoByID(int requestingID, int photoID, String quality, OnDataRecieveListener onDataRecieveListener){
        new Loader(Loader.RestCommands.GET_PHOTO_BY_ID,requestingID, photoID, quality,onDataRecieveListener ).execute() ;
    }
    public void getPhotoTemplates(int requestingID, OnDataRecieveListener onDataRecieveListener){
        new Loader(Loader.RestCommands.GET_PHOTO_TEMPLATES,requestingID, onDataRecieveListener ).execute() ;
    }
    public void getPhotoUrls(int id, String isThumbnail, int isPrivate, int ownerID, OnDataRecieveListener onDataRecieveListener){
        new Loader(Loader.RestCommands.GET_PICTURES_URLS,id,isThumbnail, isPrivate, ownerID, onDataRecieveListener ).execute() ;
    }
    public void setAvatar(int personId, int pictureId, OnDataRecieveListener onDataRecieveListener){
        new Loader(Loader.RestCommands.SET_AVATAR, pictureId, personId,onDataRecieveListener ).execute() ;
    }
    public interface OnDataRecieveListener extends JSONLoader.OnLoadFinishListener {
    }
}
