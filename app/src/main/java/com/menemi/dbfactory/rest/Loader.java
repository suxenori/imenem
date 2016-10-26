package com.menemi.dbfactory.rest;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.menemi.dbfactory.DBHandler;
import com.menemi.dbfactory.Fields;
import com.menemi.filter.FilterObject;
import com.menemi.interests_classes.InterestsGroup;
import com.menemi.models.PlaceModel;
import com.menemi.personobject.Configurations;
import com.menemi.personobject.DialogInfo;
import com.menemi.personobject.DialogMessage;
import com.menemi.personobject.Gift;
import com.menemi.personobject.Interests;
import com.menemi.personobject.NotificationSettings;
import com.menemi.personobject.PayPlan;
import com.menemi.personobject.PersonFavorite;
import com.menemi.personobject.PersonLike;
import com.menemi.personobject.PersonObject;
import com.menemi.personobject.PersonVisitor;
import com.menemi.personobject.PersonalGift;
import com.menemi.personobject.PhotoSetting;
import com.menemi.personobject.PhotoTemplate;
import com.menemi.personobject.Reward;
import com.menemi.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import static com.menemi.utils.Utils.constructStartURL;

/**
 * Created by kostya on 05.05.2016.
 */
public class Loader extends JSONLoader {



    private final static HashMap<RestCommands, JSONParcer> messageTypesParcer;

    static final String G_MY_PROFILE = "/profile/myinfo/";
    static final String G_PROFILE = "/profile/info/";
    static final String G_RANDOM_PROFILE_TO_LIKE = "/profile/tolike/";//5
    static final String G_USERS_NEAR = "/profile/near/";
    static final String G_NOTIFY_POSITION = "/profile/setlatlng/";
    static final String G_AVATAR = "/profile/avatar/";
    static final String G_PHOTO = "/profile/photo/";//(:profile_id)/(:offset)/(:count)/(:quality)/(:requesting_profile_id)
    static final String G_GET_PICTURES_URLS =  "/profile/photos_urls/";//(:profile_id)/(:quality)/(:is_private)/(:requesting_profile_id)
    static final String G_LIKES = "/profile/likes/";
    static final String G_DIS_LIKE = "/profile/like/";
    static final String G_VISITORS = "/profile/visitors/";
    static final String G_FAVORITES = "/profile/favorites/";//(:profile_id)
    static final String G_UPLOAD_FILTER_SETTINGS = "/settings/savefilterinfo/";
    static final String G_DOWNLOAD_FILTER_SETTINGS = "/settings/settings_interests/";
    static final String G_ADD_FAVORITE = "/profile/addfavorite/";
    static final String G_GET_CONFIGURATIONS = "/settings/getconfigurations/";
    static final String G_DELETE_PHOTO = "/profile/deletephoto/";
    static final String G_GET_INTERESTS_GROUP = "/profile/interest_groups/";
    static final String G_GET_INTERESTS_FROM_GROUP = "/profile/group_interests/";
    static final String G_GET_PROFILE_INTERESTS = "/profile/profile_interests/";
    static final String G_DELETE_INTERESTS = "/profile/removeinterest/";
    static final String G_GET_NOTIFICATIONS = "/settings/getnotifications/";//(:requesting_profile_id)
    static final String G_GET_PLACES_LIST = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=";
    static final String G_GOOGLE_PACES_INFO = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
    static final String G_SET_NOTIFICATIONS = "/settings/setnotifications/"; //(:handle)/(:settings)/(:requesting_profile_id)
    static final String G_GET_PAY_PLANS = "/main/plans/";//(:requesting_profile_id)
    static final String G_ADD_INTERESTS = "/profile/addinterest/";
    static final String G_FIND_INTERESTS = "/profile/searchinterests/";
    static final String G_GET_DIALOGS = "/messages/dialogs/";//(:requesting_profile_id)
    static final String G_GET_CREATE_DIALOG = "/messages/create_dialog/";//(:profile_id)/(:requesting_profile_id)
    static final String G_GET_PHOTO_BY_ID = "/profile/getphoto/";//(:photo_id)/(:quality)/(:requesting_profile_id)', to: 'profile#getphoto', via: [:get]
    static final String G_GET_MESSAGES = "/messages/messages/";//(:profile_id)/(:count)/(:offset)/(:requesting_profile_id)
    static final String G_GET_PHOTO_PROFITS = "/profile/photosprofits/";//(:requesting_profile_id)
    static final String G_GET_PHOTO_TEMPLATES = "/profile/phototemplates/";//(:requesting_profile_id)'
    static final String G_GET_GIFTS = "/profile/rewardsandgiftsthumbs/"; //(:gift_ids)/(:reward_ids)/(:requesting_profile_id)
    static final String G_SET_NOTIFICATION_TOKEN =  "/profile/report_token/";//(:is_ios)/(:token)/(:requesting_profile_id)
    static final String G_GET_UNLOCK_PHOTO = "/profile/unlock_photo/"; ///(:photo_id)/(:requesting_profile_id)
    static final String G_GET_ALL_GIFTS = "/profile/allgifts/"; // (:requesting_profile_id)
    static final String G_BUY_GIFT = "/profile/buygift/"; //(:gift_id)/(:to_profile_id)/(:requesting_profile_id)
    static final String G_SET_AVATAR = "/profile/setavatar/";//(:picture_id)/(:requesting_profile_id)

    //TODO: add friends add interests add gifts

    private static final String PICTURE_COUNT = "pictures_count";


    private static final String MUTUAL_LIKES = "new_likers_arr";
    private static final String LIKES = "mutual_likers_arr";
    private static final String PROFILES = "profiles";
    private static final String RESULT = "result";
    private static final String SUCCESS = "success";
    private static final String FAIL = "failed";
    private static final String AVATAR = "avatar";
    private static final String PROFILE_ID = "profile_id";
    private static final String NOT_EXISTS = "not_exists";
    private static final String THEIR_FAVORITES = "their_favorite_arr";
    private static final String MY_FAVORITES = "my_favorite_arr";
    private static final String I_AM_HERE_TO = "i_want";
    private static final String GENDERS = "genders";
    private static final String FRIENDS = "friends_ids";
    private static final String INTERESTS_GROUPS = "groups";
    private static final String INTERESTS_FROM_GROUPS = "interests";
    private static final String PROFILE_NOTIFICATIONS = "profile_notifications";

    private static final String PLANS = "plans";

    private static final String COINS = "coins";
    private static final String IS_POPULAR = "is_popular";
    private static final String DIALOGS = "dialogs";
    private static final String LIKED = "liked";



    static final String G_GET_PLACES_LIST_TARGET_VALUE = "&types=(cities)&language=";
    static final String G_GET_PLACES_LIST_KEY_VALUE = "&key=";
    static final String G_GOOGLE_PACES_API_KEY = "AIzaSyCAiADf4VQjnUfTxxeF8AQYYJMzTFnfWAY";//"AIzaSyA6TYMX3_c33BUyqwyeqpBRZtbu86-ASI0";


    static {
        messageTypesParcer = new HashMap<>();
        messageTypesParcer.put(RestCommands.CHECK_REST_SERVICE, (String jsonString) -> {

                JSONObject mainObject = new JSONObject(jsonString);
                if (mainObject.getString(RESULT).equals(NOT_EXISTS)) {
                    Log.v("loader", "check rest true" );
                    return true;
                }
                Log.v("loader", "check rest false" );
                return false;

        });
        messageTypesParcer.put(RestCommands.GET_MY_PROFILE_AUTH, (String jsonString) -> {

                Log.v("loader", requestNumber + "my profile" + jsonString);
                JSONObject mainObject = new JSONObject(jsonString);
                if (mainObject.getString(RESULT).equals(SUCCESS)) {
                    JSONObject profileObject = mainObject.getJSONObject(Fields.PROFILE);
                    return profileParce(profileObject);
                } else return null;

        });
        messageTypesParcer.put(RestCommands.GET_MY_PROFILE,  (String jsonString) -> {

                Log.v("loader", requestNumber + "my profile" + jsonString);
                JSONObject mainObject = new JSONObject(jsonString);
                if (mainObject.getString(RESULT).equals(SUCCESS)) {
                    return profileParceFull(mainObject);
                } else return null;


        });
        messageTypesParcer.put(RestCommands.ADD_INTERESTS, (String jsonString) -> {

                Log.v("loader", requestNumber + jsonString);
                JSONObject mainObject = new JSONObject(jsonString);
                if (mainObject.getString(RESULT).equals(SUCCESS)) {
                    return profileParceFull(mainObject);
                } else return null;


        });


        messageTypesParcer.put(RestCommands.GET_PROFILE, (String jsonString) -> {

                Log.v("loader", requestNumber + jsonString);
                JSONObject mainObject = new JSONObject(jsonString);
                if (mainObject.getString(RESULT).equals(SUCCESS)) {
                    return profileParceFull(mainObject);
                } else return null;


        });
        ///////////////////////////////////////////////
        messageTypesParcer.put(RestCommands.GET_RANDOM_PROFILE_TO_LIKE,(String jsonString) -> {

                Log.v("loader", requestNumber + jsonString);
                JSONObject mainObject = new JSONObject(jsonString);
                if (mainObject.getString(RESULT).equals(SUCCESS)) {


                    JSONObject profileObject = mainObject.getJSONObject(Fields.PROFILE);
                    return profileParce(profileObject);
                } else return null;


        });
        messageTypesParcer.put(RestCommands.DIS_LIKE, (String jsonString) -> {
                Log.v("loader", requestNumber + jsonString);
                JSONObject mainObject = new JSONObject(jsonString);
                if (mainObject.getString(RESULT).equals(SUCCESS)) {
                    return true;
                }
                return false;

        });
        messageTypesParcer.put(RestCommands.GET_LIKES,(String jsonString) -> {
                ArrayList<PersonLike> likes = new ArrayList<>();
                Log.v("loader", requestNumber + jsonString);
                JSONObject mainObject = new JSONObject(jsonString);
                if (mainObject.getString(RESULT).equals(SUCCESS)) {
                    JSONArray profileArray = mainObject.getJSONArray(LIKES);
                    for (int i = 0; i < profileArray.length(); i++) {
                        PersonLike personLike = new PersonLike(
                                profileArray.getJSONObject(i).getInt(PROFILE_ID),
                                profileArray.getJSONObject(i).getString(Fields.NAME),
                                profileArray.getJSONObject(i).getString(Fields.STATUS),
                                profileArray.getJSONObject(i).getString(Fields.CURRENT_LOCATION),
                                profileArray.getJSONObject(i).getInt(Fields.AGE),
                                profileArray.getJSONObject(i).getString(Fields.LIKED_AT),
                                Utils.intToBool(profileArray.getJSONObject(i).getInt(Fields.IS_MUTUAL)),
                                profileArray.getJSONObject(i).getInt(Fields.VAL)
                        );

                        likes.add(personLike);
                    }
                }

                return likes;

        });
        messageTypesParcer.put(RestCommands.GET_MUTUAL_LIKES,(String jsonString) -> {
                Log.v("loader", requestNumber + jsonString);
                ArrayList<PersonLike> likes = new ArrayList<>();
                JSONObject mainObject = new JSONObject(jsonString);
                if (mainObject.getString(RESULT).equals(SUCCESS)) {
                    JSONArray profileArray = mainObject.getJSONArray(MUTUAL_LIKES);
                    for (int i = 0; i < profileArray.length(); i++) {
                        PersonLike personLike = new PersonLike(
                                profileArray.getJSONObject(i).getInt(PROFILE_ID),
                                profileArray.getJSONObject(i).getString(Fields.NAME),
                                profileArray.getJSONObject(i).getString(Fields.STATUS),
                                profileArray.getJSONObject(i).getString(Fields.CURRENT_LOCATION),
                                profileArray.getJSONObject(i).getInt(Fields.AGE),
                                profileArray.getJSONObject(i).getString(Fields.LIKED_AT),
                                Utils.intToBool(profileArray.getJSONObject(i).getInt(Fields.IS_MUTUAL)),
                                profileArray.getJSONObject(i).getInt(Fields.VAL)
                        );

                        likes.add(personLike);
                    }
                }

                return likes;

        });
        messageTypesParcer.put(RestCommands.GET_VISITORS, (String jsonString) -> {
                Log.v("loader", requestNumber + jsonString);
                ArrayList<PersonVisitor> visitors = new ArrayList<>();

                JSONObject mainObject = new JSONObject(jsonString);
                if (mainObject.getString(RESULT).equals(SUCCESS)) {
                    JSONArray profileArray = mainObject.getJSONArray(PROFILES);
                    for (int i = 0; i < profileArray.length(); i++) {
                        PersonVisitor personVisitor = new PersonVisitor(
                                profileArray.getJSONObject(i).getInt(PROFILE_ID),
                                profileArray.getJSONObject(i).getString(Fields.NAME),
                                profileArray.getJSONObject(i).getString(Fields.STATUS),
                                profileArray.getJSONObject(i).getString(Fields.CURRENT_LOCATION),
                                profileArray.getJSONObject(i).getInt(Fields.AGE),
                                profileArray.getJSONObject(i).getString(Fields.VISITED_AT));

                        visitors.add(personVisitor);
                    }
                }

                return visitors;

        });


        messageTypesParcer.put(RestCommands.GET_USERS_NEAR,(String jsonString) -> {
                Log.v("loader", requestNumber + jsonString);
                ArrayList<PersonObject> personsNear = new ArrayList<>();

                JSONObject mainObject = new JSONObject(jsonString);
                JSONArray profiles = mainObject.getJSONArray(PROFILES);

                for (int i = 0; i < profiles.length(); i++) {
                    personsNear.add(profileParce(profiles.getJSONObject(i)));
                }
                return personsNear;

        });
        messageTypesParcer.put(RestCommands.DOWNLOAD_FILTER_SETTINGS,(String jsonString) -> {


                JSONObject mainObject = new JSONObject(jsonString);
                JSONArray iWantArray = mainObject.getJSONArray("i_want");
                JSONArray findFriendArray = iWantArray.getJSONArray(0);
                int iWant = 0;
                PersonObject.InterestGender interestGender = PersonObject.InterestGender.ANY_GENDER;

                if (findFriendArray.getInt(2) == 1) {
                    iWant = 0;
                }
                JSONArray chatArray = iWantArray.getJSONArray(1);
                if (chatArray.getInt(2) == 1) {
                    iWant = 1;

                }
                JSONArray dateArray = iWantArray.getJSONArray(2);
                if (dateArray.getInt(2) == 1) {
                    iWant = 2;
                }
                JSONArray gendersArray = mainObject.getJSONArray("genders");
                JSONArray maleArray = gendersArray.getJSONArray(1);
                if (maleArray.getInt(2) == 1) {
                    interestGender = PersonObject.InterestGender.MAN;

                }
                JSONArray femaleArray = gendersArray.getJSONArray(0);
                if (femaleArray.getInt(2) == 1) {
                    interestGender = PersonObject.InterestGender.WOMAN;
                }
                JSONArray anyGendreArray = gendersArray.getJSONArray(2);
                if (anyGendreArray.getInt(2) == 1) {
                    interestGender = PersonObject.InterestGender.ANY_GENDER;
                }
            JSONArray statusArray = mainObject.getJSONArray("status");
            JSONArray onlineArray = statusArray.getJSONArray(0);
            PersonObject.UserStatus userStatus = PersonObject.UserStatus.ANY;
            if (onlineArray.getInt(2) == 1) {
                userStatus = PersonObject.UserStatus.ONLINE;

            }
            JSONArray offlineArray = statusArray.getJSONArray(1);
            if (offlineArray.getInt(2) == 1) {
                userStatus = PersonObject.UserStatus.OFFLINE;
            }
            JSONArray anyStatusArray = statusArray.getJSONArray(2);
            if (anyStatusArray.getInt(2) == 1) {
                userStatus = PersonObject.UserStatus.ANY;
            }

                FilterObject filterObj = new FilterObject(iWant, interestGender.ordinal(), mainObject.getInt("min_age")
                        , mainObject.getInt("max_age"), DBHandler.getInstance().getUserId(), userStatus.ordinal(), null);     //TODO change value from REST, if this API will working correct
                Log.i("gender from Loader", interestGender + "");
                Log.i("gender from filterObj", filterObj.getiAmHereTo() + "");
                Log.i("min age from loader", mainObject.getInt("min_age") + "");
                Log.i("min age from filterObj", filterObj.getMinAge() + "");
                Log.i("max age from loader", mainObject.getInt("max_age") + "");
                Log.i("max age from filterObj", filterObj.getMaxAge() + "");
                return filterObj;

        });

        messageTypesParcer.put(RestCommands.GET_AVATAR, (String jsonString) -> {
                Log.v("loader", requestNumber + jsonString);
                String pictureURL = null;
                JSONObject mainObject = new JSONObject(jsonString);
                if (mainObject.getString(RESULT).equals(SUCCESS)) {
                    JSONArray photoArray = mainObject.getJSONArray("res_arr");
                    pictureURL = photoArray.getJSONObject(0).getString(AVATAR);
                }
                return pictureURL;


        });
        messageTypesParcer.put(RestCommands.GET_PHOTO,(String jsonString) -> {
                ArrayList<PhotoSetting> pictures = new ArrayList<>();
                JSONObject mainObject = null;
                try {
                    mainObject = new JSONObject(jsonString);
                } catch (OutOfMemoryError error) {
                    System.gc();
                        return messageTypesParcer.get(RestCommands.GET_PHOTO).parce(jsonString);
                }
                if (mainObject.getString(RESULT).equals(SUCCESS)) {

                    JSONArray photoArray = mainObject.getJSONArray(Fields.PICTURES);

                    for (int i = 0; i < photoArray.length(); i++) {
                        try {
                            String url = photoArray.getJSONObject(i).getString(Fields.PICTURE);
                            int id = photoArray.getJSONObject(i).getInt(Fields.ID);
                            Log.d("id", "" + id);

                                pictures.add(new PhotoSetting(id, url, true));


                        } catch (OutOfMemoryError error) {
                            System.gc();
                            i--;
                        }
                    }
                    // Log.d("HashMap",hashMaps.size())
                }

                return pictures;


        });
        messageTypesParcer.put(RestCommands.GET_UNLOCK_PHOTO, (String jsonString) -> {

            return true;
        });
        messageTypesParcer.put(RestCommands.GET_PICTURES_URLS, (String jsonString) -> {
                ArrayList<PhotoSetting> pictures = new ArrayList<>();

                JSONObject mainObject = new JSONObject(jsonString);

                if (mainObject.getString(RESULT).equals(SUCCESS)) {

                    JSONArray photoArray = mainObject.getJSONArray(Fields.PICTURES);


                    for (int i = 0; i < photoArray.length(); i++) {
                        JSONObject photosettingJSON = photoArray.getJSONObject(i);
                        PhotoSetting photoSetting = getPhotoSettingFromJSON(photosettingJSON);

                        pictures.add(photoSetting);
                           Log.v("PictureUrlLoading",pictures.get(i).getPhotoUrl()) ;

                    }
                    // Log.d("HashMap",hashMaps.size())
                }

                return pictures;


        });
        messageTypesParcer.put(RestCommands.GET_MULTIPLE_PERSONS_PHOTO, (String jsonString) -> {
                Log.v("loader", requestNumber + jsonString);
                HashMap<Integer, String> pictures = new HashMap<>();
                JSONObject mainObject = new JSONObject(jsonString);
                if (mainObject.getString(RESULT).equals(SUCCESS)) {

                    JSONArray photoArray = mainObject.getJSONArray("res_arr");

                    for (int i = 0; i < photoArray.length(); i++) {

                        pictures.put(photoArray.getJSONObject(i).getInt(PROFILE_ID), photoArray.getJSONObject(i).getString(AVATAR));
                    }

                    return pictures;
                } else return pictures;


        });
        messageTypesParcer.put(RestCommands.NOTIFY_POSITION, (String jsonString) -> {
                Log.v("loader", requestNumber + jsonString);
                JSONObject mainObject = new JSONObject(jsonString);
                return mainObject.getString("result").equals("success");
            });

        messageTypesParcer.put(RestCommands.DELETE_INTEREST,(String jsonString) -> {
                Log.v("loader", requestNumber + jsonString);
                JSONObject mainObject = new JSONObject(jsonString);
                return mainObject.getString("result").equals("success");
        });

        messageTypesParcer.put(RestCommands.DELETE_PHOTO,  (String jsonString) -> {
                Log.v("loader", requestNumber + jsonString);
                JSONObject mainObject = new JSONObject(jsonString);
                return mainObject.getString("result").equals("success");

        });

        messageTypesParcer.put(RestCommands.ADD_FAVORITES, (String jsonString) -> {
                Log.v("loader", requestNumber + jsonString);
                JSONObject mainObject = new JSONObject(jsonString);
                return mainObject.getString("result").equals("success");

        });


        messageTypesParcer.put(RestCommands.DELETE_PHOTO, (String jsonString) -> {
                Log.v("loader", requestNumber + jsonString);
                JSONObject mainObject = new JSONObject(jsonString);
                return mainObject.getString("result").equals("success");

        });
        messageTypesParcer.put(RestCommands.SET_AVATAR, (String jsonString) -> {
            Log.v("loader", requestNumber + jsonString);
            JSONObject mainObject = new JSONObject(jsonString);
            return mainObject.getString("result").equals("success");

        });
        messageTypesParcer.put(RestCommands.UPLOAD_FILTER_SETTINGS,(String jsonString) -> {
                Log.v("loader", requestNumber + jsonString);
                JSONObject mainObject = new JSONObject(jsonString);
                return mainObject.getString("result").equals("success");

        });

        messageTypesParcer.put(RestCommands.GET_MY_FAVORITES, (String jsonString) -> {
                Log.v("loader", requestNumber + "favorites " + jsonString);
                ArrayList<PersonFavorite> favorites = new ArrayList<>();
                JSONObject mainObject = new JSONObject(jsonString);
                if (mainObject.getString(RESULT).equals(SUCCESS)) {
                    JSONArray favoritesJSONArray = mainObject.getJSONArray(MY_FAVORITES);
                    for (int i = 0; i < favoritesJSONArray.length(); i++) {
                        int id = favoritesJSONArray.getJSONObject(i).getInt(PROFILE_ID);
                        String name = favoritesJSONArray.getJSONObject(i).getString(Fields.NAME);
//                        Bitmap bitmap = pictureParce(favoritesJSONArray.getJSONObject(i), Fields.PHOTO);
                        String added = favoritesJSONArray.getJSONObject(i).getString(Fields.ADDED_AT);

                        favorites.add(new PersonFavorite(id, name, null, added));
                        Log.i("Favorites", favorites.size() + "");
                    }

                }
                return favorites;

        });

        messageTypesParcer.put(RestCommands.GET_INTERESTS_GROUP, (String jsonString) -> {
                JSONObject mainObject = new JSONObject(jsonString);
                ArrayList<InterestsGroup> interestsGroup = new ArrayList<>();
                JSONArray mainGroups = mainObject.getJSONArray(INTERESTS_GROUPS);
                for (int i = 0; i < mainGroups.length(); i++) {
                    JSONArray groups = mainGroups.getJSONArray(i);
                    int index = groups.getInt(1);
                    String nameGroup = groups.getString(0);
                    byte[] decodedString = Base64.decode(groups.getString(2), Base64.DEFAULT);
                    Bitmap categoryIcon = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    interestsGroup.add(new InterestsGroup(index, nameGroup, categoryIcon));
                }

                return interestsGroup;

        });
        messageTypesParcer.put(RestCommands.GET_PLACES_LIST,(String jsonString) -> {

                JSONObject mainObject = new JSONObject(jsonString);
            Log.e("PLACES", jsonString);
                JSONArray placesJsonArray = mainObject.getJSONArray("predictions");
                ArrayList<PlaceModel> placesArraylist = new ArrayList();

                for (int i = 0; i < placesJsonArray.length(); i++) {
                    JSONObject place = placesJsonArray.getJSONObject(i);
                    String cityName = place.getString("description");
                    String placeId = place.getString("place_id");
                    placesArraylist.add(new PlaceModel(cityName, placeId));
                }
                return placesArraylist;

        });
        messageTypesParcer.put(RestCommands.GET_USERS_PLACE_INFO, (String jsonString) -> {

                JSONObject mainObject = new JSONObject(jsonString);
                JSONObject result = mainObject.getJSONObject("result");
                String address = result.getString("formatted_address");
                JSONObject geometry = result.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                JSONObject viewport = geometry.getJSONObject("viewport");

                //  location lat for place (must be add to detail places constructor)
                String locationLat = location.getString("lat");

                //  location lng for place (must be add to detail places constructor)
                String locationLng = location.getString("lng");

                JSONObject northeast = viewport.getJSONObject("northeast");
                JSONObject southwest = viewport.getJSONObject("southwest");

                // location northeast lng,lat for place (must be add to detail places constructor)
                String northeastLat = northeast.getString("lat");
                String northeastLng = northeast.getString("lng");

                // location southwest lng,lat for place (must be add to detail places constructor)
                String southwestLat = southwest.getString("lat");
                String southwestLng = southwest.getString("lng");

                PlaceModel detailsPlace = new PlaceModel(address, locationLat,
                        locationLng, northeastLat, northeastLng, southwestLat, southwestLng);

                Log.i("address", address);
                Log.i("locationLat", locationLat);
                Log.i("locationLng", locationLng);
                Log.i("northeastLat", northeastLat);
                Log.i("northeastLng", northeastLng);
                Log.i("southwestLat", southwestLat);
                Log.i("southwestLng", southwestLng);

                return detailsPlace;

        });
        messageTypesParcer.put(RestCommands.GET_NOTIFICATIONS,(String jsonString) -> {
                JSONObject mainObject = new JSONObject(jsonString);
                if (mainObject.getString(RESULT).equals(SUCCESS)) {
                    JSONObject notificationObject = mainObject.getJSONObject(PROFILE_NOTIFICATIONS);
                    NotificationSettings notificationSettings = new NotificationSettings(notificationObject.getInt(Fields.ID), notificationObject.getInt(Fields.PROFILE_ID_2));
                    notificationSettings.setMessages(notificationObject.getString(Fields.MESSAGES));
                    notificationSettings.setMutual_likes(notificationObject.getString(Fields.MUT_LIKES));
                    notificationSettings.setTheir_likes(notificationObject.getString(Fields.THEIR_LIKES));
                    notificationSettings.setNearby(notificationObject.getString(Fields.NEARBY));
                    notificationSettings.setVisitors(notificationObject.getString(Fields.VISITORS));
                    notificationSettings.setFavorites(notificationObject.getString(Fields.FAVORITES));
                    notificationSettings.setGifts(notificationObject.getString(Fields.GIFTS));
                    notificationSettings.setOther(notificationObject.getString(Fields.OTHER));
                    return notificationSettings;
                }
                return null;

        });

        messageTypesParcer.put(RestCommands.GET_INTERESTS_FROM_GROUP,(String jsonString) -> {
                ArrayList<Interests> interestsArray = new ArrayList<>();
                JSONObject mainObject = new JSONObject(jsonString);
                JSONArray interstsItem = mainObject.getJSONArray(INTERESTS_FROM_GROUPS);
                for (int i = 0; i < interstsItem.length(); i++) {
                    JSONArray interests = interstsItem.getJSONArray(i);
                    int interestId = interests.getInt(1);
                    int groupId = interests.getInt(2);
                    String interest = interests.getString(0);
                    interestsArray.add(new Interests(interest, interestId, groupId));
                }
                return interestsArray;

        });

        messageTypesParcer.put(RestCommands.GET_PROFILE_INTERESTS,(String jsonString) -> {
                JSONObject mainObject = new JSONObject(jsonString);
                JSONArray interestsArray = mainObject.getJSONArray("interests");
                Log.d("intersts_array", interestsArray.length() + "");

                ArrayList<Interests> personInterestsArray = new ArrayList<Interests>();
                for (int i = 0; i < interestsArray.length(); i++) {
                    JSONArray interest = interestsArray.getJSONArray(i);
                    personInterestsArray.add(new Interests(interest.getString(0), interest.getInt(1),
                            interest.getInt(2)));
                }
                Log.d("personInterestsArray", personInterestsArray.size() + "");
                return personInterestsArray;

        });

        messageTypesParcer.put(RestCommands.GET_FIND_INTERESTS, (String jsonString) -> {
                ArrayList<Interests> result = new ArrayList<Interests>();
                JSONObject mainObject = new JSONObject(jsonString);
                JSONArray interestsArray = mainObject.getJSONArray("interests");
                for (int i = 0; i < interestsArray.length(); i++) {
                    JSONArray interest = interestsArray.getJSONArray(i);
                    result.add(new Interests(interest.getString(1), interest.getInt(0), interest.getInt(2)));
                }
                return result;

        });

        messageTypesParcer.put(RestCommands.GET_OTHER_FAVORITES, (String jsonString) -> {
                Log.v("loader", requestNumber + jsonString);
                ArrayList<PersonFavorite> favorites = new ArrayList<>();
                JSONObject mainObject = new JSONObject(jsonString);
                if (mainObject.getString(RESULT).equals(SUCCESS)) {

                    JSONArray favoritesJSONArray = mainObject.getJSONArray(THEIR_FAVORITES);
                    for (int i = 0; i < favoritesJSONArray.length(); i++) {
                        int id = favoritesJSONArray.getJSONObject(i).getInt(PROFILE_ID);
                        String name = favoritesJSONArray.getJSONObject(i).getString(Fields.NAME);
                        Bitmap bitmap = pictureParce(favoritesJSONArray.getJSONObject(i), Fields.PHOTO);
                        String added = favoritesJSONArray.getJSONObject(i).getString(Fields.ADDED_AT);

                        favorites.add(new PersonFavorite(id, name, bitmap, added));
                    }

                }
                return favorites;

        });
        messageTypesParcer.put(RestCommands.GET_CONFIGURATION_SETTINGS, (String jsonString) -> {
                Log.v("loader", requestNumber + jsonString);
                Configurations configurations = new Configurations();

                JSONObject mainObject = new JSONObject(jsonString);
                if (mainObject.getString(RESULT).equals(SUCCESS)) {

                    JSONObject configsJSON = mainObject.getJSONObject(Fields.CONFIGURATIONS);
                    configurations.setId(configsJSON.getInt(Fields.ID));
                    configurations.setProfileId(configsJSON.getInt(Fields.PROFILE_ID_2));
                    configurations.setShowDistance(configsJSON.getBoolean(Fields.SHOW_DISTANCE));
                    configurations.setHideOnlineStatus(configsJSON.getBoolean(Fields.HIDE_ONLINE_STATUS));
                    configurations.setPublicSearch(configsJSON.getBoolean(Fields.PUBLIC_SEARCH));
                    configurations.setLimitProfile(configsJSON.getBoolean(Fields.LIMIT_PROFILE));
                    configurations.setShareProfile(configsJSON.getBoolean(Fields.SHARE_PROFILE));
                    configurations.setFindByEmail(configsJSON.getBoolean(Fields.FIND_BY_EMAIL));
                    configurations.setAlmostInvisible(configsJSON.getBoolean(Fields.HALF_INVISIBLE));
                    configurations.setInvisibleCloacked(configsJSON.getBoolean(Fields.INVISBLE_HEAT));
                    configurations.setHideVipStatus(configsJSON.getBoolean(Fields.HIDE_VIP_STATUS));
                    configurations.setLimitMessages(configsJSON.getBoolean(Fields.LIMIT_MESSAGES));
                    configurations.setShowNearby(configsJSON.getBoolean(Fields.SHOW_NEARBY));

                    configurations.setHideMyVerifications(configsJSON.getBoolean(Fields.HIDE_MY_VERIFICATIONS));
                    configurations.setHideProfileAsDeleted(configsJSON.getBoolean(Fields.HIDE_PROFILE_AS_DELETED));
                }
                return configurations;

        });
        messageTypesParcer.put(RestCommands.SET_NOTIFICATIONS,(String jsonString) -> {
                JSONObject mainObject = new JSONObject(jsonString);
                if (mainObject.getString(RESULT).equals(SUCCESS)) {

                    return true;
                }
                return false;

        });
        messageTypesParcer.put(RestCommands.GET_PAY_PLANS, (String jsonString) -> {
                ArrayList<PayPlan> plans = new ArrayList<>();
                JSONObject mainObject = new JSONObject(jsonString);
                if (mainObject.getString(RESULT).equals(SUCCESS)) {
                    JSONArray plansArray = mainObject.getJSONArray(PLANS);
                    for (int i = 0; i < plansArray.length(); i++) {
                        JSONObject planJSON = plansArray.getJSONObject(i);
                        plans.add(new PayPlan(planJSON.getInt(Fields.ID),
                                planJSON.getInt(Fields.PRICE),
                                planJSON.getInt(COINS),
                                planJSON.getString(IS_POPULAR)));
                    }

                    return plans;
                }
                return null;

        });
        messageTypesParcer.put(RestCommands.GET_DIALOGS,(String jsonString) -> {
                ArrayList<DialogInfo> dialogInfos = new ArrayList<>();
                JSONObject mainObject = new JSONObject(jsonString);

                JSONArray dialogsArrayJ = mainObject.getJSONArray(DIALOGS);
                for (int i = 0; i < dialogsArrayJ.length(); i++) {
                    JSONObject dialogJSON = dialogsArrayJ.getJSONObject(i);
                    DialogInfo dialogInfo = new DialogInfo();
                    dialogInfo.setDialogID(dialogJSON.getInt(Fields.DIALOG_ID));
                    dialogInfo.setProfileId(dialogJSON.getInt(Fields.PROFILE_ID_2));
                    dialogInfo.setLastMessage(dialogJSON.getString(Fields.LAST_MESSAGE));
                    dialogInfo.setLastMessageDate(dialogJSON.getString(Fields.LAST_MESSAGE_AT));
                    dialogInfo.setNewMessagesCount(dialogJSON.getInt(Fields.UNREAD_COUNT));
                    dialogInfo.setContactName(dialogJSON.getString(Fields.NAME));
                    dialogInfo.setOnline(dialogJSON.getString(Fields.STATUS));

                    dialogInfos.add(dialogInfo);
                }

                return dialogInfos;



        });
        messageTypesParcer.put(RestCommands.GET_MESSAGES, (String jsonString) -> {
                ArrayList<DialogMessage> dialogMessages = new ArrayList<>();
                JSONObject mainObject = new JSONObject(jsonString);

                JSONArray messagesArrayJ = mainObject.getJSONArray(Fields.MESSAGES);
                for (int i = 0; i < messagesArrayJ.length(); i++) {
                    JSONObject messageJSON = messagesArrayJ.getJSONObject(i);
                    DialogMessage dialogMessage = new DialogMessage(messageJSON.getInt(Fields.PROFILE_ID_2));

                    dialogMessage.setMessageId(messageJSON.getInt(Fields.ID));
                    dialogMessage.setMessageBody(messageJSON.getString(Fields.BODY));
                    dialogMessage.setSortId(messageJSON.getInt(Fields.SORT_ID));
                    dialogMessage.setPictureURL(messageJSON.getString(Fields.PICTURE));
                    dialogMessage.setDate(messageJSON.getString(Fields.DATE));

                    dialogMessages.add(dialogMessage);
                }

                return dialogMessages;



        });
        messageTypesParcer.put(RestCommands.GET_PHOTO_PROFITS, (String jsonString) -> {
                Log.v("loader", requestNumber + jsonString);
                Configurations configurations = new Configurations();

                JSONObject mainObject = new JSONObject(jsonString);
                if (mainObject.getString(RESULT).equals(SUCCESS)) {

                    JSONArray picturesStats = mainObject.getJSONArray(Fields.PICTURES);
                    JSONArray templates = mainObject.getJSONArray(Fields.TEMPLATES);
                    //(int photoId, boolean isPrivate, boolean autoprice, int price, int[] templateIds, Bitmap photo)
                    for (int i = 0; i < picturesStats.length(); i++) {
                        new PhotoSetting(picturesStats.getJSONObject(i).getInt(Fields.ID),
                                true,
                                picturesStats.getJSONObject(i).getBoolean(Fields.IS_AUTO_PRICE),
                                picturesStats.getJSONObject(i).getInt(Fields.PRICE),
                                Utils.JSONArrayToIntArray(templates.getJSONArray(i)),
                                null);
                    }


                }
                return configurations;

        });
        messageTypesParcer.put(RestCommands.GET_CREATE_DIALOG,(String jsonString) -> {
                Log.v("loader", requestNumber + jsonString);
                JSONObject mainObject = new JSONObject(jsonString);
                if (mainObject.getString(RESULT).equals(SUCCESS)) {
                    return mainObject.getInt(Fields.DIALOG_ID);
                }
                return -1;

        });
        messageTypesParcer.put(RestCommands.GET_PHOTO_BY_ID,(String jsonString) -> {
                Log.v("loader", requestNumber + jsonString);

                JSONObject mainObject = new JSONObject(jsonString);
                if (mainObject.getString(RESULT).equals(SUCCESS)) {

                    JSONObject pictureJSON = mainObject.getJSONObject(Fields.PICTURE);
                    PhotoSetting photoSetting = getPhotoSettingFromJSON(pictureJSON);



                  /*  PhotoSetting photoSetting = new PhotoSetting(pictureJSON.getInt(Fields.ID),
                            Utils.stringToBool(pictureJSON.getString(IS_PRIVATE)),
                            Utils.stringToBool(pictureJSON.getString(IS_AUTO_PRICE)),
                            pictureJSON.getInt(PRICE),
                            Utils.JSONArrayToIntArray(pictureJSON.getJSONArray(TEMPLATE_IDS)),
                            pictureJSON.getString(PICTURE), false);*/
                    return photoSetting;
                }

                return null;

        });
        messageTypesParcer.put(RestCommands.GET_PHOTO_TEMPLATES, (String jsonString) -> {
                Log.v("loader", requestNumber + jsonString);
                Configurations configurations = new Configurations();

                JSONObject mainObject = new JSONObject(jsonString);
                ArrayList<PhotoTemplate> templates = new ArrayList<>();
                if (mainObject.getString(RESULT).equals(SUCCESS)) {


                    JSONArray templatesJSON = mainObject.getJSONArray(Fields.PHOTO_TEMPLATES);
                    //(int photoId, boolean isPrivate, boolean autoprice, int price, int[] templateIds, Bitmap photo)

                    for (int i = 0; i < templatesJSON.length(); i++) {
                        templates.add(new PhotoTemplate(templatesJSON.getJSONObject(i).getInt(Fields.ID),
                                templatesJSON.getJSONObject(i).getString(Fields.PICTURE)));
                    }
                }
                return templates;

        });
        messageTypesParcer.put(RestCommands.GET_ALL_GIFTS, (String jsonString) -> {
            Log.v("loader", requestNumber + jsonString);
            ArrayList<Gift> gifts = new ArrayList<>();

            JSONObject mainObject = new JSONObject(jsonString);


                JSONArray templatesJSON = mainObject.getJSONArray(Fields.GIFTS);
                //(int photoId, boolean isPrivate, boolean autoprice, int price, int[] templateIds, Bitmap photo)

                for (int i = 0; i < templatesJSON.length(); i++) {
                    JSONObject jsonObject = templatesJSON.getJSONObject(i);

                    String url = jsonObject.getString(Fields.PICTURE);
                    int id = jsonObject.getInt(Fields.ID);
                    String name =  jsonObject.getString(Fields.NAME);
                    int price = jsonObject.getInt(Fields.PRICE);


                        gifts.add(new Gift(url,id ,name, price));



                }

            return gifts;

        });
        messageTypesParcer.put(RestCommands.SET_NOTIFICATION_TOKEN, (String jsonString) -> {
                Log.v("loader", requestNumber + jsonString);
                JSONObject mainObject = new JSONObject(jsonString);
                return mainObject.getString("result").equals("success");

        });
        messageTypesParcer.put(RestCommands.BUY_GIFT, (String jsonString) -> {
            Log.v("loader", requestNumber + jsonString);
            JSONObject mainObject = new JSONObject(jsonString);
            return mainObject.getString("result").equals("success");

        });

        //TODO: add actions for all other types
    }

    @NonNull
    private static PhotoSetting getPhotoSettingFromJSON(JSONObject photosettingJSON) throws JSONException {
        PhotoSetting photoSetting = new PhotoSetting(photosettingJSON.getString(Fields.URLS), false);
        photoSetting.setPrivate(photosettingJSON.getBoolean(Fields.IS_PRIVATE));// is_private : true
        photoSetting.setAutoprice(photosettingJSON.getBoolean(Fields.IS_AUTO_PRICE));
        photoSetting.setPrice(photosettingJSON.getInt(Fields.PRICE));
        photoSetting.setPhotoId(photosettingJSON.getInt(Fields.ID));
        photoSetting.setProfit(photosettingJSON.getDouble(Fields.TOTAL_PROFIT));
        photoSetting.setViews(photosettingJSON.getInt(Fields.TOTAL_VIEWS));
        photoSetting.setUnlocked(photosettingJSON.getBoolean(Fields.IS_UNLOCKED));

        JSONArray templatesJSON = photosettingJSON.getJSONArray(Fields.TEMPLATE_IDS);
        int[] templates = new int[templatesJSON.length()];
        for (int j = 0; j < templatesJSON.length(); j++) {
            templates[j] = templatesJSON.getInt(j);
        }
        photoSetting.setTemplateIds(templates);
        return photoSetting;
    }

    private RestCommands command;

    /**
     * @param command              action to be taken
     * @param personId
     * @param onLoadFinishListener implement this interface to get results in place
     *                             where you create object of this class
     */
    //(:profile_id)
    public Loader(RestCommands command, int personId, @NonNull OnLoadFinishListener onLoadFinishListener) {
        super(getURL(command) + personId, onLoadFinishListener);
        this.command = command;

    }

    //(:profile_id)/(:latitude)/(:longitude)
    public Loader(RestCommands command, int personId, String latitude, String longitude, @NonNull OnLoadFinishListener onLoadFinishListener) {
        super(getURL(command) + personId + "/" + latitude + "/" + longitude, onLoadFinishListener);
        this.command = command;

    }
    public Loader(RestCommands command, int id, String isThumbnail, int isPrivate, int ownerID, @NonNull OnLoadFinishListener onLoadFinishListener) {
        super(getURL(command) + id + "/" + isThumbnail + "/" + isPrivate + "/" + ownerID, onLoadFinishListener);
        this.command = command;

    }
    // (:dialog_id)/(:count)/(:offset)/(:requesting_profile_id)
    public Loader(RestCommands command, int contactID, int count, int offset, int ownerID, @NonNull OnLoadFinishListener onLoadFinishListener) {
        super(getURL(command) + contactID + "/" + count + "/" + offset + "/" + ownerID, onLoadFinishListener);
        this.command = command;

    }

    // (:profile_id)/(:miles)
    public Loader(RestCommands command, int personId, int secondData, @NonNull OnLoadFinishListener onLoadFinishListener) {
        super(getURL(command) + personId + "/" + secondData, onLoadFinishListener);
        this.command = command;

    }

    public Loader(RestCommands command, int personId, int requestedId, int photoNumber, int count, String quality, @NonNull OnLoadFinishListener onLoadFinishListener) {
        super(getURL(command) + requestedId + "/" + photoNumber + "/" + count + "/" + quality + "/" + personId, onLoadFinishListener);
        this.command = command;
    }
    public Loader(RestCommands command, int requestingID, int photoID, String quality, @NonNull OnLoadFinishListener onLoadFinishListener) {
        super(getURL(command) + photoID + "/" + quality + "/" + requestingID, onLoadFinishListener);
        this.command = command;
    }
    public Loader(RestCommands command, String quality, ArrayList<Integer> photosRequested, @NonNull OnLoadFinishListener onLoadFinishListener) {
        super(getURL(command) + quality + "/" + prepareIds(photosRequested), onLoadFinishListener);
        this.command = command;

    }

    /**
     * Used for loading pictures
     *
     * @param command
     * @param personId
     * @param isThumbnail          true if avatar quality is neaded
     * @param onLoadFinishListener
     */
    public Loader(RestCommands command, int personId, String isThumbnail, @NonNull OnLoadFinishListener onLoadFinishListener) {
        super(getURL(command) + isThumbnail + "/" + personId, onLoadFinishListener);
        this.command = command;
    }

    /*public Loader(RestCommands command, String interestId,int personId, @NonNull OnLoadFinishListener onLoadFinishListener) {
        super(getURL(command) + personId + "/" + interestId, onLoadFinishListener);
        this.command = command;
    }*/

    //(:handle)/(:settings)/(:requesting_profile_id)
    public Loader(RestCommands command, String value1, String value2, int personId, @NonNull OnLoadFinishListener onLoadFinishListener) {
        super(getURL(command) + value1 + "/" + value2 + "/" + personId, onLoadFinishListener);
        this.command = command;

    }

    public Loader(RestCommands command, String token, int personId,  @NonNull OnLoadFinishListener onLoadFinishListener) {
        super(getURL(command) + 0 +  "/" + token + "/" + personId, onLoadFinishListener);
        this.command = command;
    }
    //(:profile_id)/(:offset)/(:count)
    public Loader(RestCommands command, int personId, int photoNumber, int count, @NonNull OnLoadFinishListener onLoadFinishListener) {
        super(getURL(command) + personId + "/" + photoNumber + "/" + count, onLoadFinishListener);
        this.command = command;
    }

    public Loader(@NonNull OnLoadFinishListener onLoadFinishListener) {
        super(getURL(RestCommands.CHECK_REST_SERVICE), onLoadFinishListener);
        this.command = RestCommands.CHECK_REST_SERVICE;

    }

    //Like + Dislike
    public Loader(RestCommands command, int personId, int likedId, boolean isLike, @NonNull OnLoadFinishListener onLoadFinishListener) {
        super(getURL(command) + personId + "/" + likedId + "/" + Utils.boolToInt(isLike), onLoadFinishListener);
        this.command = command;

    }
    // /(:quality)/(:profile_ids)

    public Loader(HttpURLConnection urlConnection, @NonNull OnLoadFinishListener onLoadFinishListener) {
        super(urlConnection, onLoadFinishListener);

    }

    public Loader(RestCommands command, FilterObject filterObject, @NonNull OnLoadFinishListener onLoadFinishListener) {
        super(getURL(command) + parceFilterObject(filterObject), onLoadFinishListener);
        this.command = command;
    }

    public Loader(RestCommands command, String placeName, String localPref, String keyValue, OnLoadFinishListener onLoadFinishListener) {
        super(getURL(command) + placeName + localPref + keyValue, onLoadFinishListener);
        this.command = command;
    }

    public Loader(RestCommands command, String placeId, OnLoadFinishListener onLoadFinishListener) {
        super(getURL(command) + placeId, onLoadFinishListener);
        this.command = command;
    }

    public Loader(RestCommands command, int personId, ArrayList<Integer> interestId, OnLoadFinishListener onLoadFinishListener) {
        super(getURL(command) + personId + "/" + prepareIds(interestId), onLoadFinishListener);
        this.command = command;
    }



    private static String prepareIds(ArrayList<Integer> photosRequested) {

        String result = "";
        if(photosRequested != null) {
            for (int i = 0; i < photosRequested.size(); i++) {
                if (i != 0) {
                    result += "-";
                }
                result += photosRequested.get(i);
            }
        }
        Log.v("Loader", requestNumber + "multiple person photo ids = /" + result);
        return result;
    }


    private static Bitmap pictureParce(JSONObject jsonObject, String photoTag) throws JSONException {
        byte[] decodedString = Base64.decode(jsonObject.getString(photoTag), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    private static String parceFilterObject(FilterObject filterObject) {
        return filterObject.getiWantValue() + "/" + filterObject.getiAmHereTo() + "/" +
                filterObject.getIsOnline() + "/"+
                filterObject.getMinAge() + "/" + filterObject.getMaxAge() + "/" + filterObject.getIsOnline() + "/"
                + filterObject.getPlaceModel().getLocationLat() + ","
                + filterObject.getPlaceModel().getLocationLng() + "/" + filterObject.getPlaceModel().getNortheastLat()
                + "," + filterObject.getPlaceModel().getNortheastLng() + "/"
                + DBHandler.getInstance().getUserId();
        //(:i_want_val)/(:im_interested_val)/(:im_interested_status_val)/(:min_age)/(:max_age)/(:online)/(:center_coord)/(:/edge_coord)/(:requesting_profile_id)
    }

    private static PersonObject profileParceFull(JSONObject mainObject) throws JSONException {
        JSONObject profileObject = mainObject.getJSONObject(Fields.PROFILE);

        PersonObject personObject = profileParce(profileObject);
        personObject.setLivingWith(mainObject.getString(Fields.LIVING_WITH));

        personObject.setCanChat(mainObject.getBoolean(Fields.CAN_MESSAGE));
        personObject.setPersonVIP(Utils.intToBool(mainObject.getInt(Fields.VIP)));

        personObject.setOnline(mainObject.getString(Fields.STATUS));
        personObject.setPersonAge(mainObject.getInt(Fields.AGE));
        personObject.setPersonSexuality(mainObject.getString(Fields.ORIENTATION));
        personObject.setGifts( parceGifts(mainObject));
        personObject.setRewards((ArrayList<Reward>) mapParce(mainObject, Fields.REWARDS));
        personObject.setInterests(interestsParce(mainObject));
        personObject.setSmokingPerson(mainObject.getString(Fields.SMOKING));
        personObject.setDrinkingPerson(mainObject.getString(Fields.DRINKING));
        personObject.setPersonRelationship(mainObject.getString(Fields.RELATIONSHIP));

        personObject.setLikeStatus(PersonObject.LikeStatus.valueOf(mainObject.getString(LIKED)));;
        ArrayList<Integer> friendsList = new ArrayList<>();
        Log.d("LOG", "friendsList.size()" + friendsList.size());
        for (int i = 0; i < mainObject.getJSONArray(FRIENDS).length(); i++) {
            friendsList.add(mainObject.getJSONArray(FRIENDS).getInt(i));
            Log.d("LOG", "mainObject.getJSONArray(FRIENDS).getInt(i)" + mainObject.getJSONArray(FRIENDS).getInt(i));
            Log.d("mainObject_example", "mainObject - " + mainObject);
        }
        Log.d("LOG", "friendsList.size()" + friendsList.size());
        personObject.setPersonFriends(friendsList);

        return personObject;
    }

    private static PersonObject profileParce(JSONObject profileObject) throws JSONException {


        PersonObject personObject = new PersonObject(Utils.intToBool(profileObject.getInt(Fields.IS_MALE)),
                PersonObject.IamHereTo.values()[profileObject.getInt(Fields.HERE_TO)],
                PersonObject.InterestGender.values()[profileObject.getInt(Fields.INTEREST_GENDER)],
                profileObject.getString(Fields.EMAIL),
                profileObject.getString(Fields.NAME),
                Utils.getDateFromString(profileObject.getString(Fields.BIRTH_DAY)),
                profileObject.getString(Fields.PASSWORD));
        personObject.setVipUntil(profileObject.getString(Fields.VIP_STATUS));
        personObject.setPersonId(profileObject.getInt(Fields.ID));
        personObject.setAboutPersonInfo(profileObject.getString(Fields.ABOUT));
        personObject.setBodyType(profileObject.getInt(Fields.BODY_TYPE));

        personObject.setPersonKids(profileObject.getInt(Fields.KIDS));
        personObject.setPersonCredits(profileObject.getInt(Fields.CREDITS));
        personObject.setPersonCurrLocation(profileObject.getString(Fields.CURRENT_LOCATION));
//        personObject.setPersonSexuality(profileObject.getInt(Fields.SEXUALITY));
        personObject.setSearchAgeMin(profileObject.getInt(Fields.SEARCH_AGE_MIN));
        personObject.setSearchAgeMax(profileObject.getInt(Fields.SEARCH_AGE_MAX));
        personObject.setRating(profileObject.getInt(Fields.RATING));



        if (!profileObject.getString(Fields.POS_LAT).equals("null")) {
            personObject.setPositionLatitude(profileObject.getDouble(Fields.POS_LAT));
            personObject.setPositionLongitude(profileObject.getDouble(Fields.POS_LON));
        }
        return personObject;
    }



    private static ArrayList<Interests> interestsParce(JSONObject mainObject) throws JSONException {
        JSONArray interests = mainObject.getJSONArray(Fields.INTERESTS);
        ArrayList<Interests> preparedInterests = new ArrayList<>();

        for (int i = 0; i < interests.length(); i++) {

            preparedInterests.add(new Interests( interests.getJSONObject(i).getInt(Fields.INTEREST_ID),
                    interests.getJSONObject(i).getString(Fields.NAME),
                    interests.getJSONObject(i).getString(Fields.ICON_URL),
                    interests.getJSONObject(i).getBoolean(Fields.IS_MUTUAL) ));
        }
        return preparedInterests;
    }
private static ArrayList<PersonalGift> parceGifts(JSONObject mainObject)throws JSONException {

    JSONArray gifts = mainObject.getJSONArray(Fields.GIFTS);

    ArrayList<PersonalGift> preparedGifts = new ArrayList<>();

    for (int i = 0; i < gifts.length(); i++) {

        preparedGifts.add(parceGift(gifts.getJSONObject(i)));


    }
    return preparedGifts;
}

    public static PersonalGift parceGift(JSONObject jsonObject) throws JSONException{
        String giftName = jsonObject.getString(Fields.NAME);
        int profileId = jsonObject.getInt("from_profile_id");
        int giftId = jsonObject.getInt("gift_id");
        String personName = jsonObject.getString("from_name");
        String avatarUrl = jsonObject.getString("from_avatar_url");

        String date =  jsonObject.getString("date");
        return new PersonalGift(profileId, giftId, personName, giftName, avatarUrl, date);

    }
    public static PersonalGift parceGift(Bundle extras){
        String giftName = extras.getString(Fields.NAME);
        int profileId = extras.getInt("from_profile_id");
        int giftId = extras.getInt("gift_id");
        String personName = extras.getString("from_name");
        String avatarUrl = extras.getString("from_avatar_url");
        String date =  extras.getString("date");
        return new PersonalGift(profileId, giftId, personName, giftName, avatarUrl, date);

    }

    private static ArrayList<?> mapParce(JSONObject mainObject, String field) throws JSONException {
        JSONArray gifts = mainObject.getJSONArray(field);
        ArrayList<Reward> preparedGifts = new ArrayList<>();

        for (int i = 0; i < gifts.length(); i++) {
            int id = gifts.getJSONObject(i).getInt(Fields.ID);
            String giftName = gifts.getJSONObject(i).getString(Fields.NAME);
            preparedGifts.add(new Reward(id, giftName));
        }
        return preparedGifts;
    }



    public static String getURL(RestCommands command) {
        String url = "";
        switch (command) {
            case CHECK_REST_SERVICE:
                url = constructStartURL() + G_MY_PROFILE;
                break;
            case GET_MY_PROFILE:
                url = constructStartURL() + G_PROFILE;
                break;
            case GET_PROFILE:
                url = constructStartURL() + G_PROFILE;
                break;
            case GET_RANDOM_PROFILE_TO_LIKE:
                url = constructStartURL() + G_RANDOM_PROFILE_TO_LIKE;
                break;
            case GET_LIKES:
                url = constructStartURL() + G_LIKES;
                break;
            case GET_MUTUAL_LIKES:
                url = constructStartURL() + G_LIKES;
                break;
            case GET_VISITORS:
                url = constructStartURL() + G_VISITORS;
                break;
            case DIS_LIKE:
                url = constructStartURL() + G_DIS_LIKE;
                break;
            case GET_USERS_NEAR:
                url = constructStartURL() + G_USERS_NEAR;
                break;
            case NOTIFY_POSITION:
                url = constructStartURL() + G_NOTIFY_POSITION;
                break;
            case GET_PHOTO:
                url = constructStartURL() + G_PHOTO;
                break;
            case GET_MULTIPLE_PERSONS_PHOTO:
                url = constructStartURL() + G_AVATAR;
                break;
            case GET_AVATAR:
                url = constructStartURL() + G_AVATAR;
                break;
            case GET_OTHER_FAVORITES://Same url as GET_MY_FAVORITES
            case GET_MY_FAVORITES:
                url = constructStartURL() + G_FAVORITES;
                break;
            case UPLOAD_FILTER_SETTINGS:
                url = constructStartURL() + G_UPLOAD_FILTER_SETTINGS;
                break;
            case DOWNLOAD_FILTER_SETTINGS:
                url = constructStartURL() + G_DOWNLOAD_FILTER_SETTINGS;
                break;
            case ADD_FAVORITES:
                url = constructStartURL() + G_ADD_FAVORITE;
                break;
            case GET_CONFIGURATION_SETTINGS:
                url = constructStartURL() + G_GET_CONFIGURATIONS;
                break;
            case DELETE_PHOTO:
                url = constructStartURL() + G_DELETE_PHOTO;
                break;
            case GET_INTERESTS_GROUP:
                url = constructStartURL() + G_GET_INTERESTS_GROUP;
                break;
            case GET_INTERESTS_FROM_GROUP:
                url = constructStartURL() + G_GET_INTERESTS_FROM_GROUP;
                break;
            case GET_PROFILE_INTERESTS:
                url = constructStartURL() + G_GET_PROFILE_INTERESTS;
                break;
            case DELETE_INTEREST:
                url = constructStartURL() + G_DELETE_INTERESTS;
                break;
            case GET_NOTIFICATIONS:
                url = constructStartURL() + G_GET_NOTIFICATIONS;
                break;
            case GET_PLACES_LIST:
                url = G_GET_PLACES_LIST;
                break;
            case GET_USERS_PLACE_INFO:
                url = G_GOOGLE_PACES_INFO;
                break;
            case SET_NOTIFICATIONS:
                url = constructStartURL() + G_SET_NOTIFICATIONS;
                break;
            case GET_PAY_PLANS:
                url = constructStartURL() + G_GET_PAY_PLANS;
                break;
            case ADD_INTERESTS:
                url = constructStartURL() + G_ADD_INTERESTS;
                break;
            case GET_FIND_INTERESTS:
                url = constructStartURL() + G_FIND_INTERESTS;
                break;
            case GET_DIALOGS:
                url = constructStartURL() + G_GET_DIALOGS;
                break;
            case GET_MESSAGES:
                url = constructStartURL() + G_GET_MESSAGES;
                break;
            case GET_PHOTO_PROFITS:
                url = constructStartURL() + G_GET_PHOTO_PROFITS;
                break;
            case GET_CREATE_DIALOG:
                url = constructStartURL() + G_GET_CREATE_DIALOG;
                break;
            case GET_PHOTO_BY_ID:
                url = constructStartURL() + G_GET_PHOTO_BY_ID;
                break;
            case GET_PHOTO_TEMPLATES:
                url = constructStartURL() + G_GET_PHOTO_TEMPLATES;
                break;
            case SET_NOTIFICATION_TOKEN:
                url = constructStartURL() + G_SET_NOTIFICATION_TOKEN;
                break;
            case GET_PICTURES_URLS:
                url = constructStartURL() + G_GET_PICTURES_URLS;
                break;
            case GET_UNLOCK_PHOTO:
                url = constructStartURL() + G_GET_UNLOCK_PHOTO;
                break;
            case GET_ALL_GIFTS:
                url = constructStartURL() + G_GET_ALL_GIFTS;
                break;
            case BUY_GIFT:
                url = constructStartURL() + G_BUY_GIFT;
                break;
            case SET_AVATAR:
                url = constructStartURL() + G_SET_AVATAR;
                break;

        }
        return url;
    }


    static void parcing(RestCommands command, String jsonString, OnLoadFinishListener onLoadFinishListener) {
        try {
            Log.v("loader", requestNumber + jsonString);
            onLoadFinishListener.onFinish(messageTypesParcer.get(command).parce(jsonString));
        } catch (JSONException e) {
            e.printStackTrace();
            onLoadFinishListener.onFinish(null);
        }

    }

    /**
     * @param jsonString string with JSON code from server
     */
    @Override
    protected void parcing(String jsonString) {
        try {
            OnLoadFinishListener onLoadFinishListener = getOnLoadFinishListener();
            Log.d("JSON", "COMAND " + command);
            Log.d("JSON", "PARCER " + messageTypesParcer.get(command));
            JSONParcer parcer = messageTypesParcer.get(command);
            Object obj = parcer.parce(jsonString);
            onLoadFinishListener.onFinish(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    /*from_profile_id - идентификатор оценщика профиля
        to_profile_id - идентификатор профиля, который оценили
        did_like - 1 или 0: соответствует значениям "нравится" или "не нравится"*/
    public enum RestCommands {
        CHECK_REST_SERVICE,
        GET_MY_PROFILE,
        GET_MY_PROFILE_AUTH,
        GET_PROFILE,
        GET_RANDOM_PROFILE_TO_LIKE,
        DIS_LIKE,
        GET_MUTUAL_LIKES,
        GET_VISITORS,
        GET_LIKES,
        GET_USERS_NEAR,
        NOTIFY_POSITION,
        GET_AVATAR,
        GET_PICTURES_URLS,
        GET_UNLOCK_PHOTO,
        GET_PHOTO,
        GET_MULTIPLE_PERSONS_PHOTO,
        GET_OTHER_FAVORITES,
        GET_INTERESTS,
        GET_MY_FAVORITES,
        UPLOAD_FILTER_SETTINGS,
        DOWNLOAD_FILTER_SETTINGS,
        ADD_FAVORITES,
        GET_CONFIGURATION_SETTINGS,
        DELETE_PHOTO,
        GET_INTERESTS_GROUP,
        GET_INTERESTS_FROM_GROUP,
        GET_PROFILE_INTERESTS,
        DELETE_INTEREST,
        GET_PLACES_LIST,
        GET_USERS_PLACE_INFO,
        GET_NOTIFICATIONS,
        SET_NOTIFICATIONS,
        GET_PAY_PLANS,
        ADD_INTERESTS,
        GET_FIND_INTERESTS,
        GET_DIALOGS,
        GET_MESSAGES,
        GET_PHOTO_PROFITS,
        GET_CREATE_DIALOG,
        GET_PHOTO_BY_ID,
        GET_PHOTO_TEMPLATES,
        GET_ALL_GIFTS,
        BUY_GIFT,
        SET_AVATAR,
        SET_NOTIFICATION_TOKEN
    }

    interface JSONParcer {
        Object parce(String jsonString) throws JSONException;
    }

    interface InputData {
        Object parce(HttpURLConnection connection) throws JSONException;
    }
}
/*++match '/profile/register', to: 'profile#register', via: [:post]
 ++ match '/profile/authorize', to: 'profile#authorize', via: [:post]
 ++ match '/(:locale_handle)/profile/myinfo/(:requesting_profile_id)', to: 'profile#myinfo', via: [:get]
 ++ match '/(:locale_handle)/profile/info/(:requesting_profile_id)/(:requested_profile_id)', to: 'profile#info', via: [:get]
  match '/(:locale_handle)/profile/visitrecord/(:requesting_profile_id)/(:to_profile_id)', to: 'profile#visitrecord', via: [:get]
 ++ match '/(:locale_handle)/profile/tolike/(:requesting_profile_id)', to: 'profile#tolike', via: [:get]
  ++match '/(:locale_handle)/profile/like/(:requesting_profile_id)/(:to_profile_id)/(:did_like)', to: 'profile#like', via: [:get]
 ++ match '/(:locale_handle)/profile/visitors/(:requesting_profile_id)', to: 'profile#visitors', via: [:get]
  match '/(:locale_handle)/profile/top/(:requesting_profile_id)', to: 'profile#top', via: [:get]
 ++ match '/(:locale_handle)/profile/likes/(:requesting_profile_id)', to: 'profile#likes', via: [:get]
++  match '/(:locale_handle)/profile/favorites/(:requesting_profile_id)', to: 'profile#favorites', via: [:get]
++  match '/(:locale_handle)/profile/setlatlng/(:requesting_profile_id)/(:latitude)/(:longitude)', to: 'profile#setlatlng', via: [:get]
 ++ match '/(:locale_handle)/profile/near/(:rqeuesting_profile_id)/(:miles)', to: 'profile#near', via: [:get]
++  match '/(:locale_handle)/profile/addfavorite/(:requesting_profile_id)/(:slave_profile_id)/(:is_added)', to: 'profile#addfavorite', via: [:get]
 ++ match '/(:locale_handle)/profile/avatar/(:quality)/(:profile_ids)', to: 'profile#avatar', via: [:get]

  ++match '/(:locale_handle)/profile/photo/(:profile_id)/(:offset)/(:count)/(:quality)', to: 'profile#photo', via: [:get]

  ++match '/(:locale_handle)/profile/profile_interests/(:requesting_profile_id)/(:slave_profile_id)', to: 'profile#profile_interests', via: [:get]

  ++match '/(:locale_handle)/profile/interest_groups/(:requesting_profile_id)', to: 'profile#interest_groups', via: [:get]

  ++match '/(:locale_handle)/profile/group_interests/(:requesting_profile_id)/(:group_id)', to: 'profile#group_interests', via: [:get]

  ++match '/(:locale_handle)/profile/addinterest/(:requesting_profile_id)/(:interest_id)', to: 'profile#addinterest', via: [:get]
 ++ match '/(:locale_handle)/profile/removeinterest/(:requesting_profile_id)/(:interest_id)', to: 'profile#removeinterest', via: [:get]


  match '/(:locale_handle)/profile/rewardsandgiftsthumbs/(:gift_ids)/(:reward_ids)/(:requesting_profile_id)', to: 'profile#rewardsandgiftsthumbs', via: [:get]
  match '/(:locale_handle)/profile/phototemplates/(:requesting_profile_id)', to: 'profile#phototemplates', via: [:get]
  ++match '/(:locale_handle)/profile/addphoto', to: 'profile#addphoto', via: [:post]
    match '/(:locale_handle)/messages/dialogs/(:requesting_profile_id) via: [:get]
  #TODO: you can expand the profile/info method to indicate what kind of info you need. simple pass additional parameter like 'all', 'general' or 'notification_settings' etc

  match '/(:locale_handle)/settings/settings_interests/(:requesting_profile_id)', to: 'settings#settings_interests', via: [:get]
 ++ match '/(:locale_handle)/settings/savefilterinfo/(:i_want_val)/(:im_interested_val)/(:min_age)/(:max_age)/(:requesting_profile_id)', to: 'settings#savefilterinfo', via: [:get]
  match '/(:locale_handle)/settings/savegeneralinfo/(:requesting_profile_id)', to: 'settings#savegeneralinfo', via: [:post]
  match '/(:locale_handle)/settings/getnotifications/(:requesting_profile_id)', to: 'settings#getnotifications', via: [:get]
  match '/(:locale_handle)/settings/setnotifications/(:handle)/(:settings)/(:requesting_profile_id)', to: 'settings#setnotifications', via: [:get]
 ++ match '/(:locale_handle)/settings/getconfigurations/(:requesting_profile_id)', to: 'settings#getconfigurations', via: [:get]
 ++ match '/(:locale_handle)/settings/setconfigurations/(:requesting_profile_id)', to: 'settings#setconfigurations', via: [:post]


  match '/(:locale_handle)/profile/test', to: 'profile#test', via: [:get]*/