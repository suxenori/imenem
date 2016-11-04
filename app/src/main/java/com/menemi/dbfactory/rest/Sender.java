package com.menemi.dbfactory.rest;

import android.util.Log;

import com.menemi.dbfactory.Fields;
import com.menemi.edit_personal_Info.PersonalAppearanceSettingsModel;
import com.menemi.personobject.Configurations;
import com.menemi.personobject.CreditsInfo;
import com.menemi.personobject.LanguagesSet;
import com.menemi.personobject.PersonObject;
import com.menemi.personobject.PhotoSetting;
import com.menemi.personobject.PostField;
import com.menemi.social_network.SocialProfile;
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

    private static final String urlForAPIRegister = "/profile/register";
    private static final String urlForAPIAuthorise = "/profile/authorize";
    private static final String urlForAPIAddPhoto = "/profile/addphoto";
    private static final String urlForAPISetConfigurations = "/settings/setconfigurations";//(:requesting_profile_id)
    private static final String urlForAPISetINFO = "/settings/saveprofileinfo/";
    private static final String urlForAPISetLanguages = "/profile/setlanguages";
    private static final String urlForAPIAddCredits = "/profile/credits_added";
    private static final String urlFBRegister = "/profile/registerfb";



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
        messageTypesParcer.put(RestCommands.SET_PERSON_APPEARANCE, new JSONParcer()
        {
            @Override
            public String parce(Object object) throws JSONException
            {
                PersonalAppearanceSettingsModel apperanceModel = (PersonalAppearanceSettingsModel) object;
                JSONObject dataJSON = new JSONObject();

                dataJSON.put(USER_ID, apperanceModel.getId());

                JSONObject obj = new JSONObject();

                obj.put(Fields.GROWTH, apperanceModel.getHeight());
                obj.put(Fields.WEIGHT, apperanceModel.getWeigt());
                obj.put(Fields.ABOUT, apperanceModel.getAbout());
                obj.put(Fields.RELATIONSHIP_INT, apperanceModel.getRelationshipIndex());
                obj.put(Fields.ORIENTATION_INT, apperanceModel.getSexualityIndex());
                obj.put(Fields.BODY_TYPE, apperanceModel.getBodyTypeIndex());
                obj.put(Fields.EYE_COLOR, apperanceModel.getEyeColorIndex());
                obj.put(Fields.HAIR_COLOR, apperanceModel.getHairColorIndex());
                obj.put(Fields.LIVING_WITH_INT, apperanceModel.getLivingWithIndex());
                obj.put(Fields.KIDS, apperanceModel.getKidsIndex());
                obj.put(Fields.SMOKING_INT, apperanceModel.getSmokingIndex());
                obj.put(Fields.DRINKING_INT, apperanceModel.getAlcoholIndex());
                dataJSON.put("values", obj);
                return dataJSON.toString();
            }
        });
        messageTypesParcer.put(RestCommands.SET_FIELD, new JSONParcer() {
            @Override
            public String parce(Object object) throws JSONException {
                //Log.d("object", obj.toString());
                PostField data = (PostField) object;
                JSONObject dataJSON = new JSONObject();

                JSONObject obj = new JSONObject();
                //requesting_profile_id, name, gender (в виде 1 или 0), и birth в формате "%Y-%m-%d"
                dataJSON.put(USER_ID, data.getId());
                obj.put(data.getField(), data.getData());

                dataJSON.put("values", obj);
                Log.d("object", dataJSON.toString());
                return dataJSON.toString();
            }
        });
        messageTypesParcer.put(RestCommands.SET_LANGUAGES, new JSONParcer() {
            @Override
            public String parce(Object object) throws JSONException {
                //Log.d("object", obj.toString());
                LanguagesSet data = (LanguagesSet) object;
                JSONObject dataJSON = new JSONObject();

                JSONArray languages = new JSONArray();
                //requesting_profile_id, name, gender (в виде 1 или 0), и birth в формате "%Y-%m-%d"
                dataJSON.put(USER_ID, data.getId());
                for (int i = 0; i < data.getLanguages().size(); i++) {
                    JSONArray language = new JSONArray();
                    language.put(0,data.getLanguages().get(i).getLanguagesId());
                    language.put(1,data.getLanguages().get(i).getLanguageLevel());
                    languages.put(i, language);
                }


                dataJSON.put("languages", (Object)languages);
                Log.d("object", dataJSON.toString());

                return dataJSON.toString().replace("\"[", "[").replace("]\"", "]");
            }
        });

        messageTypesParcer.put(RestCommands.ADD_CREDITS, new JSONParcer()
        {
            @Override
            public String parce(Object object) throws JSONException
            {
                //Log.d("object", obj.toString());
                CreditsInfo data = (CreditsInfo)object;
                JSONObject dataJSON = new JSONObject();

                JSONObject obj = new JSONObject();
                //requesting_profile_id, name, gender (в виде 1 или 0), и birth в формате "%Y-%m-%d"
                dataJSON.put(USER_ID, data.getId());
                obj.put(Fields.CREDITS_ADDED_TOKEN, data.getToken());
                obj.put(Fields.CREDITS_ADDED_AMOUNT, data.getCreditsToAdd());


                dataJSON.put("values", obj);
                Log.d("object", dataJSON.toString());
                return dataJSON.toString();
            }
        });
        messageTypesParcer.put(RestCommands.REGISTER_FACEBOOK, new JSONParcer() {
            @Override
            public String parce(Object object) throws JSONException {

                /* {"fbid"=>"1073647636064268", "name"=>"Anton", "gender"=>1, "locale_handle"=>"en", "profile"=>{"gender"=>true, "name"=>"Anton", "fbid"=>"1073647636064268"}}*/
                //Log.d("object", obj.toString());
                SocialProfile profile = (SocialProfile) object;

                JSONObject dataJSON = new JSONObject();

                //requesting_profile_id, name, gender (в виде 1 или 0), и birth в формате "%Y-%m-%d"
                dataJSON.put("fbid",profile.getId());
                dataJSON.put(Fields.NAME,profile.getFullName());
                dataJSON.put(Fields.IS_MALE, Utils.getFacebookGender(profile.getGender()));



                Log.d("object", dataJSON.toString());

                return dataJSON.toString().replace("\"[", "[").replace("]\"", "]");
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
                url = Fields.URL_FOR_SERVER + urlForAPIRegister;
                break;
            case AUTHORISE:
                url =  Fields.URL_FOR_SERVER + urlForAPIAuthorise;
                break;
            case ADD_PHOTO:
                url = Fields.URL_FOR_SERVER + "/"+ Fields.PREFIX + urlForAPIAddPhoto;
                break;
            case SET_CONFIGURATIONS:
                url = Fields.URL_FOR_SERVER + "/"+ Fields.PREFIX + urlForAPISetConfigurations;
                break;
            case SET_INFO:
                url = Fields.URL_FOR_SERVER + "/"+ Fields.PREFIX + urlForAPISetINFO;
                break;
            case SET_LANGUAGES:
                url = Fields.URL_FOR_SERVER + "/"+ Fields.PREFIX + urlForAPISetLanguages;
                break;
            case SET_FIELD:
            case SET_PERSON_APPEARANCE:
                url = Fields.URL_FOR_SERVER + "/"+ Fields.PREFIX + urlForAPISetINFO;
                break;
            case ADD_CREDITS:
                url = Fields.URL_FOR_SERVER + "/"+ Fields.PREFIX + urlForAPIAddCredits;
                break;
            case REGISTER_FACEBOOK:
                url = Fields.URL_FOR_SERVER + "/"+ Fields.PREFIX + urlFBRegister;
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
        SET_INFO,
        SET_LANGUAGES,
        SET_PERSON_APPEARANCE,
        SET_FIELD,
        ADD_CREDITS,
        REGISTER_FACEBOOK
    }


    interface JSONParcer {
        String parce(Object obj) throws JSONException;
    }

    interface OnUploadListener extends OnUploadFinishListener {

    }
}
