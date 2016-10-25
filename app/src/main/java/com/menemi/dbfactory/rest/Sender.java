package com.menemi.dbfactory.rest;

import android.util.Log;

import com.menemi.dbfactory.Fields;
import com.menemi.personobject.Configurations;
import com.menemi.personobject.PersonObject;
import com.menemi.personobject.PhotoSetting;
import com.menemi.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by irondev on 10.06.16.
 */
public class Sender extends JSONSender {
    static final String urlForAPIRegister = "http://minemi.ironexus.com/profile/register";
    static final String urlForAPIAuthorise = "http://minemi.ironexus.com/profile/authorize";
    static final String urlForAPIAddPhoto = "http://minemi.ironexus.com/ru/profile/addphoto";
    static final String urlForAPISetConfigurations = "http://minemi.ironexus.com/ru/settings/setconfigurations";//(:requesting_profile_id)
    static final String urlForAPISetINFO = "http://minemi.ironexus.com/ru/settings/savegeneralinfo/";



    private static final String USER_ID = "requesting_profile_id";
    private static HashMap<RestCommands, JSONParcer> messageTypesParcer;

    static {
        messageTypesParcer = new HashMap<RestCommands, JSONParcer>();
        messageTypesParcer.put(RestCommands.REGISTER, new JSONParcer() {

            @Override
            public String parce(Object object) throws JSONException {
                PersonObject personObject = (PersonObject) object;
                JSONObject obj = new JSONObject();

                obj.put("is_man", Utils.boolToInt(personObject.isMale()));
                obj.put("purpose", personObject.getiAmHereTo().ordinal());
                obj.put("interest_gender", personObject.getInterestGender().ordinal());
                obj.put("email", personObject.getEmail());
                obj.put("name", personObject.getPersonName());
                obj.put("birth_date", personObject.getBirthday());
                obj.put("password", personObject.getPassword());


                /*Передаем следующие параметры:
                (string ) is_man: "1" или "0" - пол нового пользователя
                (string) purpose: "friends", "chat",  или "date" - цель знакомств нового пользователя
                        (integer) interest_gender: 1 = man 0 = woman 2 = any - жрузей какого пола ищет новый пользователь
                        (string) email: почта нового пользователя
                        (string) name: имя нового пользователя
                        (string) birth_date: "2012-12-26" - дата рождения нового пользователя
                        (string) password: пароль нового пользователя*/

                return obj.toString();
            }
        });
        messageTypesParcer.put(RestCommands.AUTHORISE, new JSONParcer() {
            /*
            Передаем следующие параметры:
            (string) email: почта пользователя
            (string) password: пароль пользователя
        * */
            @Override
            public String parce(Object object) throws JSONException {
                PersonObject personObject = (PersonObject) object;
                JSONObject obj = new JSONObject();

                obj.put("email", personObject.getEmail());
                obj.put("password", personObject.getPassword());

                return obj.toString();
            }
        });
        messageTypesParcer.put(RestCommands.ADD_PHOTO, new JSONParcer()
        {
            @Override
            public String parce(Object object) throws JSONException
            {
                PhotoSetting photoSetting = (PhotoSetting) object;
                JSONObject obj = new JSONObject();
                obj.put("requesting_profile_id", photoSetting.getProfileId());
                obj.put("is_private",photoSetting.isPrivate());
                Log.d("is_private", "" + photoSetting.isPrivate());
                obj.put("is_auto_price",photoSetting.isAutoprice());
                obj.put("price",photoSetting.getPrice());
                ArrayList<Integer> templates = new ArrayList<Integer>();
                for (int i = 0; i < photoSetting.getTemplateIds().length; i++) {
                    templates.add(photoSetting.getTemplateIds()[i]);
                }
                JSONArray jsonArray = new JSONArray(templates);
                obj.put("template_ids",jsonArray);
                obj.put("photo", photoSetting.getPhotoBase64());
                return obj.toString();
            }
        });
        messageTypesParcer.put(RestCommands. SET_CONFIGURATIONS, new JSONParcer()
        {
            @Override
            public String parce(Object object) throws JSONException
            {
                Configurations configurations = (Configurations) object;
                JSONObject obj = new JSONObject(Fields.CONFIGURATIONS);
                obj.put(Fields.ID, configurations.getId());
                obj.put(Fields.PROFILE_ID_2, configurations.getProfileId());
                obj.put(Fields.SHOW_DISTANCE, configurations.isShowDistance());
                obj.put(Fields.HIDE_ONLINE_STATUS, configurations.isHideOnlineStatus());
                obj.put(Fields.PUBLIC_SEARCH, configurations.isPublicSearch());
                obj.put(Fields.SHOW_NEARBY, configurations.isShowNearby());
                obj.put(Fields.LIMIT_PROFILE, configurations.isLimitProfile());
                obj.put(Fields.SHARE_PROFILE, configurations.isShareProfile());
                obj.put(Fields.FIND_BY_EMAIL, configurations.isFindByEmail());
                obj.put(Fields.HALF_INVISIBLE, configurations.isAlmostInvisible());
                obj.put(Fields.INVISBLE_HEAT, configurations.isInvisibleCloacked());
                obj.put(Fields.HIDE_VIP_STATUS, configurations.isHideVipStatus());
                obj.put(Fields.LIMIT_MESSAGES, configurations.isLimitMessages());
                obj.put(Fields.HIDE_MY_VERIFICATIONS, configurations.isHideMyVerifications());
                obj.put(Fields.HIDE_PROFILE_AS_DELETED, configurations.isHideProfileAsDeleted());


                return obj.toString();
            }
        });
        messageTypesParcer.put(RestCommands.SET_INFO, new JSONParcer()
        {
            @Override
            public String parce(Object object) throws JSONException
            {
                PersonObject personData = (PersonObject)object;

                JSONObject obj = new JSONObject();
                //requesting_profile_id, name, gender (в виде 1 или 0), и birth в формате "%Y-%m-%d"

                obj.put(Fields.NAME, personData.getPersonName());
                obj.put(Fields.IS_MALE, Utils.boolToInt(personData.isMale()));
                obj.put(Fields.BIRTH_DAY, Utils.getStringFromDate(personData.getBirthday()));
                obj.put(USER_ID, personData.getPersonId());

                return obj.toString();
            }
        });
        //TODO: add actions for all other types
    }


    Object object = null;
    RestCommands command;


    public Sender(RestCommands command, Object obj, OnUploadListener onUploadListener) {
        super(getURL(command), onUploadListener);
        this.command = command;
        this.object = obj;
    }

    public static String getURL(RestCommands command) {
        String url = "";
        switch (command) {
            case REGISTER:
                url = urlForAPIRegister;
                break;
            case AUTHORISE:
                url = urlForAPIAuthorise;
                break;
            case ADD_PHOTO:
                url = urlForAPIAddPhoto;
                break;
            case SET_CONFIGURATIONS:
                url = urlForAPISetConfigurations;
                break;
            case SET_INFO:
                url = urlForAPISetINFO;
                break;
        }
        return url;
    }

    @Override
    protected String parcing() throws JSONException {
        return messageTypesParcer.get(command).parce(object);
    }


    public enum RestCommands {
        REGISTER,
        AUTHORISE,
        ADD_PHOTO,
        SET_CONFIGURATIONS,
        SET_INFO
    }


    interface JSONParcer {
        String parce(Object obj) throws JSONException;
    }

    interface OnUploadListener extends OnUploadFinishListener {

    }
}
