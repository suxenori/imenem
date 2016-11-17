package com.menemi.dbfactory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import com.menemi.filter.FilterObject;
import com.menemi.personobject.Configurations;
import com.menemi.personobject.DialogInfo;
import com.menemi.personobject.Gift;
import com.menemi.personobject.Language;
import com.menemi.personobject.NotificationSettings;
import com.menemi.personobject.PersonObject;
import com.menemi.personobject.PhotoSetting;
import com.menemi.personobject.PhotoTemplate;
import com.menemi.social_network.SocialNetworkHandler;
import com.menemi.social_network.SocialProfile;
import com.menemi.utils.Utils;

import java.util.ArrayList;

import static com.menemi.dbfactory.SQLiteEngine.TABLE_LANGUAGES;

class DBSQLite {
    SQLiteEngine sqliteInstance;
    SQLiteDatabase sqliteDB;

    protected DBSQLite(Context ctx) {
        Log.d("SQLite", "DBSQLite constructor");
        sqliteInstance = new SQLiteEngine(ctx);
        sqliteDB = sqliteInstance.getWritableDatabase();
    }

    private String getStringFromDB(String tableName, String field) {
        Cursor cursor = sqliteDB.query(tableName, new String[]{field},
                null, null, null, null, null);

        cursor.moveToFirst();

        String data = cursor.getString(cursor.getColumnIndex(field));

        cursor.close();
        return data;
    }

    private int getIntFromDB(String tableName, String field) {
        Cursor cursor = sqliteDB.query(tableName, new String[]{field},
                null, null, null, null, null);

        cursor.moveToFirst();


        int data = cursor.getInt(cursor.getColumnIndex(field));

        cursor.close();
        return data;
    }

    public boolean isEmtyTable(String tableName) {
        boolean empty = true;
        Cursor cur = sqliteDB.rawQuery("SELECT COUNT(*) FROM " + tableName, null);
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt(0) == 0);
        }
        cur.close();

        return empty;
    }

    public PersonObject getProfile(int id) {
        if (id == getUserId()) {
            Cursor cursor = sqliteDB.query(SQLiteEngine.TABLE_OWNER, null, Fields.ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);
            if (cursor != null)
                cursor.moveToFirst();

            PersonObject personObject = new PersonObject(cursor.getString(cursor.getColumnIndex(Fields.EMAIL)),
                    cursor.getString(cursor.getColumnIndex(Fields.PASSWORD)));
            personObject.setPersonId(id);//PersonId());
            personObject.setPersonName(cursor.getString(cursor.getColumnIndex(Fields.NAME)));//PersonName());
            personObject.setAboutPersonInfo(cursor.getString(cursor.getColumnIndex(Fields.ABOUT)));//AboutPersonInfo());
            personObject.setPersonAge(cursor.getInt(cursor.getColumnIndex(Fields.AGE)));//PersonAge());
            personObject.setMale(cursor.getInt(cursor.getColumnIndex(Fields.IS_MALE)));
            personObject.setPersonCredits(cursor.getInt(cursor.getColumnIndex(Fields.CREDITS)));//PersonCredits());
            personObject.setPersonEducation(cursor.getString(cursor.getColumnIndex(Fields.EDUCATION)));//PersonEducation());
            personObject.setPersonLivingCity(cursor.getString(cursor.getColumnIndex(Fields.LIVING_CITY)));//PersonLivingCity());
            personObject.setPersonCurrLocation(cursor.getString(cursor.getColumnIndex(Fields.CURRENT_LOCATION)));//PersonCurrLocation());
            personObject.setPersonPopularity(cursor.getInt(cursor.getColumnIndex(Fields.POPULARITY)));//PersonPopularity());
            personObject.setPersonScore(cursor.getInt(cursor.getColumnIndex(Fields.SCORE)));//PersonScore());
            personObject.setPersonWork(cursor.getString(cursor.getColumnIndex(Fields.WORK)));//PersonWork());
            personObject.setiAmHereTo(cursor.getInt(cursor.getColumnIndex(Fields.HERE_TO)));//iAmHereTo().toString());
            personObject.setLivingWith(cursor.getString(cursor.getColumnIndex(Fields.LIVING_WITH)));//LivingWith().ordinal());
            personObject.setBirthday(cursor.getString(cursor.getColumnIndex(Fields.BIRTH_DAY)));
    /*        personObject.setPersonTwitter(cursor.getString(cursor.getColumnIndex(Fields.TWITTER_ACCOUNT)));
            personObject.setPersonLinkedin(cursor.getString(cursor.getColumnIndex(Fields.LINKEDIN_ACCOUNT)));
            personObject.setPersonInstagram(cursor.getString(cursor.getColumnIndex(Fields.INSTAGRAM_ACCOUNT)));
            personObject.setPersonOK(cursor.getString(cursor.getColumnIndex(Fields.ODNOCLASSNIKI_ACCOUNT)));
            personObject.setPersonFBook(cursor.getString(cursor.getColumnIndex(Fields.FACEBOOK_ACCOUNT)));
            personObject.setPersonVKontakte(cursor.getString(cursor.getColumnIndex(Fields.VKONTAKTE_ACCOUNT)));
            personObject.setPersonGPlus(cursor.getString(cursor.getColumnIndex(Fields.Gplus_ACCOUNT)));*/
            personObject.setSearchAgeMax(cursor.getInt(cursor.getColumnIndex(Fields.SEARCH_AGE_MAX)));
            personObject.setSearchAgeMin(cursor.getInt(cursor.getColumnIndex(Fields.SEARCH_AGE_MIN)));
            personObject.setWeight(cursor.getInt(cursor.getColumnIndex(Fields.WEIGHT)));
            personObject.setGrowth(cursor.getInt(cursor.getColumnIndex(Fields.GROWTH)));
            personObject.setDrinkingPerson(cursor.getString(cursor.getColumnIndex(Fields.DRINKING)));
            personObject.setSmokingPerson(cursor.getString(cursor.getColumnIndex(Fields.SMOKING)));
            //  personObject.setPersonSexuality(cursor.getInt(cursor.getColumnIndex(Fields.SEXUALITY)));
            personObject.setPersonRelationship(cursor.getString(cursor.getColumnIndex(Fields.RELATIONSHIP)));

            // return contact

            return personObject;
        }

        return null;
    }

    public ArrayList<Gift> getGifts() {


        Cursor cursor = sqliteDB.query(SQLiteEngine.TABLE_GIFTS_BASE, null, null, null, null, null, null);
        ArrayList<Gift> gifts = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                Gift gift = new Gift(cursor.getString(cursor.getColumnIndex(Fields.PHOTO)),
                        cursor.getInt(cursor.getColumnIndex(Fields.ID)), cursor.getString(cursor.getColumnIndex(Fields.NAME)),
                        cursor.getInt(cursor.getColumnIndex(Fields.PRICE)));
                gifts.add(gift);
                cursor.moveToNext();
            }

        }
        return gifts;

    }

    public void setGift(Gift gift) {

        ContentValues values = new ContentValues();

        values.put(Fields.PHOTO, gift.getUrl());
        values.put(Fields.ID, gift.getGiftId());
        values.put(Fields.NAME, gift.getGiftName());
        values.put(Fields.PRICE, gift.getPrice());


        if (isFirstTime(SQLiteEngine.TABLE_GIFTS_BASE)) {
            sqliteDB.insert(SQLiteEngine.TABLE_GIFTS_BASE, null, values);
        } else {
            Cursor cursor = sqliteDB.query(SQLiteEngine.TABLE_GIFTS_BASE, null, Fields.ID + "=?",
                    new String[]{"" + gift.getGiftId()}, null, null, null, null);
            if (cursor != null && cursor.moveToFirst() == true) {
                sqliteDB.update(SQLiteEngine.TABLE_GIFTS_BASE, values,
                        Fields.ID + "=?", new String[]{"" + gift.getGiftId()});
            } else {
                sqliteDB.insert(SQLiteEngine.TABLE_GIFTS_BASE, null, values);
            }

        }

    }

    public void setLanguage(Language language) {

        ContentValues values = new ContentValues();

        values.put(Fields.ID, language.getLanguagesId());
        values.put(Fields.NAME, language.getLanguageName());


        if (isFirstTime(TABLE_LANGUAGES)) {
            sqliteDB.insert(TABLE_LANGUAGES, null, values);
        } else {
            Cursor cursor = sqliteDB.query(TABLE_LANGUAGES, null, Fields.ID + "=?",
                    new String[]{"" + language.getLanguagesId()}, null, null, null, null);
            if (cursor != null && cursor.moveToFirst() == true) {
                if (!cursor.getString(cursor.getColumnIndex(Fields.NAME)).equals(language.getLanguageName())) {


                    sqliteDB.update(TABLE_LANGUAGES, values,
                            Fields.ID + "=?", new String[]{"" + language.getLanguagesId()});
                }
            } else {
                sqliteDB.insert(TABLE_LANGUAGES, null, values);
            }
        }

    }

    public ArrayList<Language> getLanguages() {


        Cursor cursor = sqliteDB.query(TABLE_LANGUAGES, null, null, null, null, null, null);
        ArrayList<Language> languages = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                Language language = new Language(cursor.getString(cursor.getColumnIndex(Fields.NAME)),
                        cursor.getInt(cursor.getColumnIndex(Fields.ID)));
                languages.add(language);
                cursor.moveToNext();
            }

        }
        return languages;

    }
    public void deleteLanguages() {

            sqliteDB.delete(TABLE_LANGUAGES, null, null);


    }
    public void saveFilter(FilterObject filterObject) {
        ContentValues values = new ContentValues();
        values.put(Fields.SEARCH_AGE_MIN, filterObject.getMinAge());
        values.put(Fields.SEARCH_AGE_MAX, filterObject.getMaxAge());
        values.put(Fields.HERE_TO, filterObject.getiAmHereTo());
        values.put(Fields.STATUS, filterObject.getIsOnline());
        values.put(Fields.INTEREST_GENDER, filterObject.getiWantValue());
        if (isFirstTime(SQLiteEngine.TABLE_FILTER)) {
            sqliteDB.insert(SQLiteEngine.TABLE_FILTER, null, values);
        } else {
            Cursor cursor = sqliteDB.query(SQLiteEngine.TABLE_FILTER, null, null,
                    null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst() == true) {
                sqliteDB.update(SQLiteEngine.TABLE_GIFTS_BASE, values, null, null);
            } else {
                sqliteDB.insert(SQLiteEngine.TABLE_GIFTS_BASE, null, values);
            }

        }
    }

    public FilterObject getFilter() {
        FilterObject filterObject = null;
        Cursor cursor = sqliteDB.query(SQLiteEngine.TABLE_FILTER, null, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst() == true) {
            filterObject = new FilterObject();
            filterObject.setMinAge(cursor.getInt(cursor.getColumnIndex(Fields.SEARCH_AGE_MIN)));
            filterObject.setMaxAge(cursor.getInt(cursor.getColumnIndex(Fields.SEARCH_AGE_MAX)));
            filterObject.setiAmHereTo(cursor.getInt(cursor.getColumnIndex(Fields.HERE_TO)));
            filterObject.setIsOnline(cursor.getInt(cursor.getColumnIndex(Fields.STATUS)));
            filterObject.setiWantValue(cursor.getInt(cursor.getColumnIndex(Fields.INTEREST_GENDER)));
        }
        return filterObject;
    }

    public ArrayList<PhotoTemplate> getTemplates() {


        Cursor cursor = sqliteDB.query(SQLiteEngine.TABLE_TEMPLATES_BASE, null, null, null, null, null, null);
        ArrayList<PhotoTemplate> templates = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                PhotoTemplate template = new PhotoTemplate(cursor.getInt(cursor.getColumnIndex(Fields.ID)),
                        cursor.getString(cursor.getColumnIndex(Fields.PHOTO)));
                templates.add(template);
                cursor.moveToNext();
            }

        }
        return templates;

    }

    public ArrayList<PhotoSetting> getPhotoUrls(int id, String isThumbnail, boolean isPrivate) {
        Cursor cursor = sqliteDB.query(SQLiteEngine.TABLE_PHOTOS, null, Fields.PROFILE_ID_2 + "=?" + " AND " + Fields.QUALITY + "=?" + " AND " + Fields.IS_PRIVATE + "=?", new String[]{"" + id, isThumbnail, "" + Utils.boolToInt(isPrivate)}, null, null, null);
        ArrayList<PhotoSetting> photoSettings = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {

                PhotoSetting photoSetting = new PhotoSetting(cursor.getString(cursor.getColumnIndex(Fields.URLS)), false);
                photoSetting.setPrivate(Utils.intToBool(cursor.getInt(cursor.getColumnIndex(Fields.IS_PRIVATE))));// is_private : true
                photoSetting.setAutoprice(Utils.intToBool(cursor.getInt(cursor.getColumnIndex(Fields.IS_AUTO_PRICE))));
                photoSetting.setPrice(cursor.getInt(cursor.getColumnIndex(Fields.PRICE)));
                photoSetting.setPhotoId(cursor.getInt(cursor.getColumnIndex(Fields.ID)));
                photoSetting.setProfit(cursor.getDouble(cursor.getColumnIndex(Fields.TOTAL_PROFIT)));
                photoSetting.setViews(cursor.getInt(cursor.getColumnIndex(Fields.TOTAL_VIEWS)));
                photoSetting.setUnlocked(Utils.intToBool(cursor.getInt(cursor.getColumnIndex(Fields.IS_UNLOCKED))));

                int[] templates = Utils.convertStringToIntArray(cursor.getString(cursor.getColumnIndex(Fields.TEMPLATE_IDS)));
                photoSetting.setTemplateIds(templates);
                photoSettings.add(photoSetting);
                cursor.moveToNext();
            }

        }
        return photoSettings;

    }

    public Bitmap getBitmap(String url) {
if(url == null){
    return null;
}

        Cursor cursor = sqliteDB.query(SQLiteEngine.TABLE_PHOTOS, new String[]{Fields.PHOTO}, Fields.URLS + "=?", new String[]{url}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            return Utils.getBitmapFromStringBase64(cursor.getString(cursor.getColumnIndex(Fields.PHOTO)));
        }
        return null;
    }

    public void saveBitmap(String url, Bitmap photo) {
        ContentValues values = new ContentValues();
        values.put(Fields.PHOTO, Utils.getBitmapToBase64String(photo));
        if (sqliteDB.update(SQLiteEngine.TABLE_PHOTOS, values, Fields.URLS + "=?", new String[]{url}) == 0) {
            values.put(Fields.URLS, url);
            sqliteDB.insert(SQLiteEngine.TABLE_PHOTOS, null, values);
        }

    }

    public void saveAvatarURL(int userID, String url) {
        ContentValues values = new ContentValues();
        values.put(Fields.ID, userID);
        values.put(Fields.URLS, url);

        if (isFirstTime(SQLiteEngine.TABLE_AVATARS)) {
            sqliteDB.insert(SQLiteEngine.TABLE_AVATARS, null, values);
        } else {
            Cursor cursor = sqliteDB.query(SQLiteEngine.TABLE_AVATARS, null, Fields.ID + "=?",
                    new String[]{"" + userID}, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                sqliteDB.update(SQLiteEngine.TABLE_AVATARS, values,
                        Fields.ID + "=?", new String[]{"" + userID});
            } else {
                sqliteDB.insert(SQLiteEngine.TABLE_AVATARS, null, values);
            }
        }
    }

    public void saveSocialProfile(Fields.SOCIAL_NETWORKS socialNetwork, SocialProfile profile) {
        ContentValues values = new ContentValues();
        values.put(Fields.SOCIAL_ID, profile.getId());
        values.put(Fields.SOCIAL_PROFILE_IMAGE, Utils.getBitmapToBase64String(profile.getImage()));
        values.put(Fields.SOCIAL_PROFILE_FIRST_NAME, profile.getFirstName());
        values.put(Fields.SOCIAL_PROFILE_MIDDLE_NAME, profile.getMiddleName());
        values.put(Fields.SOCIAL_PROFILE_LAST_NAME, profile.getLastName());
        values.put(Fields.SOCIAL_NETWORK, socialNetwork.name());


        if (isFirstTime(SQLiteEngine.TABLE_SOCIAL)) {
            sqliteDB.insert(SQLiteEngine.TABLE_SOCIAL, null, values);
        } else {
            Cursor cursor = sqliteDB.query(SQLiteEngine.TABLE_SOCIAL, null, Fields.SOCIAL_NETWORK + "=?",
                    new String[]{"" + socialNetwork}, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                sqliteDB.update(SQLiteEngine.TABLE_SOCIAL, values,
                        Fields.SOCIAL_NETWORK + "=?", new String[]{"" + socialNetwork});
            } else {
                sqliteDB.insert(SQLiteEngine.TABLE_SOCIAL, null, values);
            }
        }
    }

    public SocialProfile getSocialProfile(Fields.SOCIAL_NETWORKS socialNetwork) {

        SocialProfile profile = null;

        Cursor cursor = sqliteDB.query(SQLiteEngine.TABLE_SOCIAL, null, Fields.SOCIAL_NETWORK + "=?", new String[]{socialNetwork.name()}, null, null, null);
        if (cursor != null &&  cursor.moveToFirst()) {
            profile = new SocialProfile();
            profile.setFirstName(cursor.getString(cursor.getColumnIndex(Fields.SOCIAL_PROFILE_FIRST_NAME)));
            profile.setMiddleName(cursor.getString(cursor.getColumnIndex(Fields.SOCIAL_PROFILE_MIDDLE_NAME)));
            profile.setLastName(cursor.getString(cursor.getColumnIndex(Fields.SOCIAL_PROFILE_LAST_NAME)));
            profile.setId(cursor.getString(cursor.getColumnIndex(Fields.SOCIAL_ID)));
            profile.setImage(Utils.getBitmapFromStringBase64(cursor.getString(cursor.getColumnIndex(Fields.SOCIAL_PROFILE_IMAGE))));
        }
        return profile;

    }

    /**
     * @param userID
     * @return null if nothig found
     */
    public String getAvatarURL(int userID) {


        Cursor cursor = sqliteDB.query(SQLiteEngine.TABLE_AVATARS, new String[]{Fields.URLS}, Fields.ID + "=?", new String[]{"" + userID}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(Fields.URLS));
        }
        return null;
    }

    public void savePicture(int userID, String quality, PhotoSetting photoSetting) {
        ContentValues values = new ContentValues();
        values.put(Fields.PROFILE_ID_2, userID);
        values.put(Fields.QUALITY, quality);
        values.put(Fields.URLS, photoSetting.getPhotoUrl());
        if (photoSetting.getPhoto() != null) {
            values.put(Fields.PHOTO, Utils.getBitmapToBase64String(photoSetting.getPhoto()));
        }
        values.put(Fields.IS_PRIVATE, photoSetting.isPrivate());
        values.put(Fields.ID, photoSetting.getPhotoId());
        values.put(Fields.IS_AUTO_PRICE, photoSetting.isAutoprice());
        values.put(Fields.PRICE, photoSetting.getPhotoId());
        values.put(Fields.TOTAL_PROFIT, photoSetting.getProfit());
        values.put(Fields.TOTAL_VIEWS, photoSetting.getViews());
        values.put(Fields.IS_UNLOCKED, photoSetting.isUnlocked());
        values.put(Fields.TEMPLATE_IDS, Utils.convertArrayToString(photoSetting.getTemplateIds()));


        if (isFirstTime(SQLiteEngine.TABLE_PHOTOS)) {
            sqliteDB.insert(SQLiteEngine.TABLE_PHOTOS, null, values);
        } else {
            Cursor cursor = sqliteDB.query(SQLiteEngine.TABLE_PHOTOS, null, Fields.ID + "=?",
                    new String[]{"" + photoSetting.getPhotoId()}, null, null, null, null);
            if (cursor != null && cursor.moveToFirst() == true) {
                sqliteDB.update(SQLiteEngine.TABLE_PHOTOS, values,
                        Fields.ID + "=?", new String[]{"" + photoSetting.getPhotoId()});
            } else {
                sqliteDB.insert(SQLiteEngine.TABLE_PHOTOS, null, values);
            }
        }
    }

    public void setTemplate(PhotoTemplate template) {


        ContentValues values = new ContentValues();

        values.put(Fields.PHOTO, template.getUrl());
        values.put(Fields.ID, template.getTemplateID());

        if (isFirstTime(SQLiteEngine.TABLE_TEMPLATES_BASE)) {
            sqliteDB.insert(SQLiteEngine.TABLE_TEMPLATES_BASE, null, values);
        } else {
            Cursor cursor = sqliteDB.query(SQLiteEngine.TABLE_TEMPLATES_BASE, null, Fields.ID + "=?",
                    new String[]{"" + template.getTemplateID()}, null, null, null, null);
            if (cursor != null && cursor.moveToFirst() == true) {
                sqliteDB.update(SQLiteEngine.TABLE_TEMPLATES_BASE, values,
                        Fields.ID + "=?", new String[]{"" + template.getTemplateID()});
            } else {
                sqliteDB.insert(SQLiteEngine.TABLE_TEMPLATES_BASE, null, values);
            }

        }


    }

    public void setNotifications(int userId, NotificationSettings notifications) {


        ContentValues values = new ContentValues();

        values.put(Fields.ID, userId);
        values.put(Fields.MESSAGES, notifications.getMessages());
        values.put(Fields.MUT_LIKES, notifications.getMutual_likes());
        values.put(Fields.THEIR_LIKES, notifications.getTheir_likes());
        values.put(Fields.NEARBY, notifications.getNearby());
        values.put(Fields.VISITORS, notifications.getVisitors());
        values.put(Fields.FAVORITES, notifications.getFavorites());
        values.put(Fields.GIFTS, notifications.getGifts());
        values.put(Fields.OTHER, notifications.getOther());



        if (isFirstTime(SQLiteEngine.TABLE_NOTIFICATIONS)) {
            sqliteDB.insert(SQLiteEngine.TABLE_NOTIFICATIONS, null, values);
        } else {
            Cursor cursor = sqliteDB.query(SQLiteEngine.TABLE_NOTIFICATIONS, null, Fields.ID + "=?",
                    new String[]{"" + userId}, null, null, null, null);
            if (cursor != null && cursor.moveToFirst() == true) {
                sqliteDB.update(SQLiteEngine.TABLE_NOTIFICATIONS, values,
                        Fields.ID + "=?", new String[]{"" + userId});
            } else {
                sqliteDB.insert(SQLiteEngine.TABLE_NOTIFICATIONS, null, values);
            }

        }

    }
    public NotificationSettings getNotifications(int userId) {

        NotificationSettings notificationSettings = null;

        Cursor cursor = sqliteDB.query(SQLiteEngine.TABLE_CONFIGURATIONS, null, Fields.ID + "=?", new String[]{""+userId}, null, null, null);
        if (cursor != null &&  cursor.moveToFirst()) {
            notificationSettings = new NotificationSettings(userId, userId);

            notificationSettings.setMessages(cursor.getString(cursor.getColumnIndex(Fields.MESSAGES)));
            notificationSettings.setMutual_likes(cursor.getString(cursor.getColumnIndex(Fields.MUT_LIKES)));
            notificationSettings.setTheir_likes(cursor.getString(cursor.getColumnIndex(Fields.THEIR_LIKES)));
            notificationSettings.setNearby(cursor.getString(cursor.getColumnIndex(Fields.NEARBY)));
            notificationSettings.setVisitors(cursor.getString(cursor.getColumnIndex(Fields.VISITORS)));
            notificationSettings.setFavorites(cursor.getString(cursor.getColumnIndex(Fields.FAVORITES)));
            notificationSettings.setGifts(cursor.getString(cursor.getColumnIndex(Fields.GIFTS)));
            notificationSettings.setOther(cursor.getString(cursor.getColumnIndex(Fields.OTHER)));

        }
        return notificationSettings;

    }
    public void setConfigurations(Configurations configuration) {


        ContentValues values = new ContentValues();

        values.put(Fields.ID, configuration.getProfileId());
        values.put(Fields.SHOW_DISTANCE, Utils.boolToInt(configuration.isShowDistance()));
        values.put(Fields.HIDE_ONLINE_STATUS, Utils.boolToInt(configuration.isHideOnlineStatus()));
        values.put(Fields.PUBLIC_SEARCH, Utils.boolToInt(configuration.isPublicSearch()));
        values.put(Fields.LIMIT_PROFILE, Utils.boolToInt(configuration.isLimitProfile()));
        values.put(Fields.SHARE_PROFILE, Utils.boolToInt(configuration.isShareProfile()));
        values.put(Fields.FIND_BY_EMAIL, Utils.boolToInt(configuration.isFindByEmail()));
        values.put(Fields.HALF_INVISIBLE, Utils.boolToInt(configuration.isAlmostInvisible()));
        values.put(Fields.INVISBLE_HEAT, Utils.boolToInt(configuration.isInvisibleCloacked()));
        values.put(Fields.HIDE_VIP_STATUS, Utils.boolToInt(configuration.isHideVipStatus()));
        values.put(Fields.LIMIT_MESSAGES, Utils.boolToInt(configuration.isLimitMessages()));
        values.put(Fields.SHOW_NEARBY, Utils.boolToInt(configuration.isShowNearby()));
        values.put(Fields.HIDE_MY_VERIFICATIONS, Utils.boolToInt(configuration.isHideMyVerifications()));
        values.put(Fields.HIDE_PROFILE_AS_DELETED, Utils.boolToInt(configuration.isHideProfileAsDeleted()));


        if (isFirstTime(SQLiteEngine.TABLE_CONFIGURATIONS)) {
            sqliteDB.insert(SQLiteEngine.TABLE_CONFIGURATIONS, null, values);
        } else {
            Cursor cursor = sqliteDB.query(SQLiteEngine.TABLE_CONFIGURATIONS, null, Fields.ID + "=?",
                    new String[]{"" + configuration.getProfileId()}, null, null, null, null);
            if (cursor != null && cursor.moveToFirst() == true) {
                sqliteDB.update(SQLiteEngine.TABLE_CONFIGURATIONS, values,
                        Fields.ID + "=?", new String[]{"" + configuration.getProfileId()});
            } else {
                sqliteDB.insert(SQLiteEngine.TABLE_CONFIGURATIONS, null, values);
            }

        }

    }
    public Configurations getConfigurations(int userId) {

        Configurations configurations = null;

        Cursor cursor = sqliteDB.query(SQLiteEngine.TABLE_CONFIGURATIONS, null, Fields.ID + "=?", new String[]{""+userId}, null, null, null);
        if (cursor != null &&  cursor.moveToFirst()) {
            configurations = new Configurations();
            configurations.setId(cursor.getInt(cursor.getColumnIndex(Fields.ID)));
            configurations.setProfileId(cursor.getInt(cursor.getColumnIndex(Fields.PROFILE_ID_2)));
            configurations.setShowDistance(Utils.intToBool(cursor.getInt(cursor.getColumnIndex(Fields.SHOW_DISTANCE))));
            configurations.setHideOnlineStatus(Utils.intToBool(cursor.getInt(cursor.getColumnIndex(Fields.HIDE_ONLINE_STATUS))));
            configurations.setPublicSearch(Utils.intToBool(cursor.getInt(cursor.getColumnIndex(Fields.PUBLIC_SEARCH))));
            configurations.setLimitProfile(Utils.intToBool(cursor.getInt(cursor.getColumnIndex(Fields.LIMIT_PROFILE))));
            configurations.setShareProfile(Utils.intToBool(cursor.getInt(cursor.getColumnIndex(Fields.SHARE_PROFILE))));
            configurations.setFindByEmail(Utils.intToBool(cursor.getInt(cursor.getColumnIndex(Fields.FIND_BY_EMAIL))));
            configurations.setAlmostInvisible(Utils.intToBool(cursor.getInt(cursor.getColumnIndex(Fields.HALF_INVISIBLE))));
            configurations.setInvisibleCloacked(Utils.intToBool(cursor.getInt(cursor.getColumnIndex(Fields.INVISBLE_HEAT))));
            configurations.setHideVipStatus(Utils.intToBool(cursor.getInt(cursor.getColumnIndex(Fields.HIDE_VIP_STATUS))));
            configurations.setLimitMessages(Utils.intToBool(cursor.getInt(cursor.getColumnIndex(Fields.LIMIT_MESSAGES))));
            configurations.setShowNearby(Utils.intToBool(cursor.getInt(cursor.getColumnIndex(Fields.SHOW_NEARBY))));

            configurations.setHideMyVerifications(Utils.intToBool(cursor.getInt(cursor.getColumnIndex(Fields.HIDE_MY_VERIFICATIONS))));
            configurations.setHideProfileAsDeleted(Utils.intToBool(cursor.getInt(cursor.getColumnIndex(Fields.HIDE_PROFILE_AS_DELETED))));

        }
        return configurations;

    }
    public ArrayList<DialogInfo> getDialogs() {

        Cursor cursor = sqliteDB.query(SQLiteEngine.TABLE_DIALOGS, null, null, null, null, null, null);
        ArrayList<DialogInfo> dialogs = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                DialogInfo dialogInfo = new DialogInfo();
                dialogInfo.setDialogID(cursor.getInt(cursor.getColumnIndex(Fields.DIALOG_ID)));
                dialogInfo.setProfileId(cursor.getInt(cursor.getColumnIndex(Fields.PROFILE_ID_2)));
                dialogInfo.setLastMessage(cursor.getString(cursor.getColumnIndex(Fields.LAST_MESSAGE)));
                dialogInfo.setLastMessageDate(cursor.getString(cursor.getColumnIndex(Fields.LAST_MESSAGE_AT)));
                dialogInfo.setNewMessagesCount(cursor.getInt(cursor.getColumnIndex(Fields.UNREAD_COUNT)));
                dialogInfo.setContactName(cursor.getString(cursor.getColumnIndex(Fields.NAME)));


                dialogs.add(dialogInfo);
                cursor.moveToNext();
            }

        }
        return dialogs;

    }

    public void setDialog(DialogInfo dialog) {
        ContentValues values = new ContentValues();
        values.put(Fields.DIALOG_ID, dialog.getDialogID());
        values.put(Fields.PROFILE_ID_2, dialog.getProfileId());
        values.put(Fields.LAST_MESSAGE, dialog.getLastMessage());
        values.put(Fields.LAST_MESSAGE_AT, Utils.getStringDateForDB(dialog.getLastMessageDate()));
        values.put(Fields.UNREAD_COUNT, dialog.getNewMessagesCount());
        values.put(Fields.NAME, dialog.getContactName());


        if (isFirstTime(SQLiteEngine.TABLE_DIALOGS)) {
            sqliteDB.insert(SQLiteEngine.TABLE_DIALOGS, null, values);
        } else {
            Cursor cursor = sqliteDB.query(SQLiteEngine.TABLE_DIALOGS, null, Fields.DIALOG_ID + "=?",
                    new String[]{"" + dialog.getDialogID()}, null, null, null, null);
            if (cursor != null && cursor.moveToFirst() == true) {
                sqliteDB.update(SQLiteEngine.TABLE_DIALOGS, values,
                        Fields.DIALOG_ID + "=?", new String[]{"" + dialog.getDialogID()});
            } else {
                sqliteDB.insert(SQLiteEngine.TABLE_DIALOGS, null, values);
            }
        }
    }


    public SocialProfile getSocial(String socNetwork) {
        SocialProfile profile = new SocialProfile();
        Cursor cursor = null;
        if (socNetwork.equals(SocialNetworkHandler.getInstance().VK_SOCIAL)) {
            cursor = sqliteDB.query(SQLiteEngine.TABLE_SOCIAL_VK, null, Fields.SOCIAL_ID + "=?",
                    new String[]{socNetwork}, null, null, null, null);
        } else if (socNetwork.equals(SocialNetworkHandler.getInstance().FB_SOCIAL)) {
            cursor = sqliteDB.query(SQLiteEngine.TABLE_SOCIAL_FB, null, Fields.SOCIAL_ID + "=?",
                    new String[]{socNetwork}, null, null, null, null);
        } else if (socNetwork.equals(SocialNetworkHandler.getInstance().OK_SOCIAL)) {
            cursor = sqliteDB.query(SQLiteEngine.TABLE_SOCIAL_OK, null, Fields.SOCIAL_ID + "=?",
                    new String[]{socNetwork}, null, null, null, null);
        } else if (socNetwork.equals(SocialNetworkHandler.getInstance().INSTA_SOCIAL)) {
            cursor = sqliteDB.query(SQLiteEngine.TABLE_SOCIAL_INSTA, null, Fields.SOCIAL_ID + "=?",
                    new String[]{socNetwork}, null, null, null, null);
        }
        if (cursor != null)
            cursor.moveToFirst();
        profile.setFirstName(cursor.getString(cursor.getColumnIndex(Fields.SOCIAL_PROFILE_FIRST_NAME)));
        profile.setMiddleName(cursor.getString(cursor.getColumnIndex(Fields.SOCIAL_PROFILE_MIDDLE_NAME)));
        profile.setLastName(cursor.getString(cursor.getColumnIndex(Fields.SOCIAL_PROFILE_LAST_NAME)));
        profile.setImage(Utils.stringToBitmap(cursor.getString(cursor.getColumnIndex(Fields.SOCIAL_PROFILE_IMAGE))));
        return profile;
    }

    public int getIdOnLoginData(PersonObject personObject) {
        int id = getIdOnEmail(personObject.getEmail());
        if (id == -1) {
            return -1;
        }
        String email = getEmail(id);
        String password = getPassword(id);
        if (personObject.getEmail().equals(email) && personObject.getPassword().equals(password)) {
            return id;
        } else {
            return -1;
        }

    }

    private int getIdOnEmail(String email) {
        Cursor cursor = sqliteDB.query(SQLiteEngine.TABLE_OWNER, null, Fields.EMAIL + "=?",
                new String[]{email}, null, null, null);

        if (cursor.getCount() == 0) {
            return -1;
        }
        cursor.moveToFirst();

        int data = cursor.getInt(cursor.getColumnIndex(Fields.ID));

        cursor.close();
        return data;
    }


    public Bitmap getPhoto(int userID) {
        if (userID == getUserId()) {
            return Utils.getBitmapFromStringBase64(getStringFromDB(SQLiteEngine.TABLE_OWNER, Fields.PHOTO));
        }
        return null;
    }


    public int getUserId() {
        return getIntFromDB(SQLiteEngine.TABLE_OWNER, Fields.ID);
    }

    public String getFireBaseToken() {
        if (!isFirstTime(SQLiteEngine.TABLE_FIRE_BASE)) {
            return getStringFromDB(SQLiteEngine.TABLE_FIRE_BASE, Fields.FIRE_BASE_TOKEN);
        } else return "";
    }

    public String getPassword(int userID) {
        if (userID == getUserId()) {
            return getStringFromDB(SQLiteEngine.TABLE_OWNER, Fields.PASSWORD);
        }
        return getStringFromDB(SQLiteEngine.TABLE_OWNER, Fields.PASSWORD);
    }


    public String getEmail(int userID) {
        if (userID == getUserId()) {
            return getStringFromDB(SQLiteEngine.TABLE_OWNER, Fields.EMAIL);
        }
        return getStringFromDB(SQLiteEngine.TABLE_OWNER, Fields.EMAIL);
    }

    private boolean isFirstTime(String table) {

            Cursor cursor = sqliteDB.query(table, null,
                null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            if (cursor.getInt(0) == 0) {
                cursor.close();
                return true;
            } else {                            // Review this code
                cursor.close();
                return false;
            }
        }
        return true;
    }

    private ContentValues getContentValues(PersonObject personObject) {
        ContentValues values = new ContentValues();

        values.put(Fields.ID, personObject.getPersonId());
        values.put(Fields.NAME, personObject.getPersonName());
        values.put(Fields.ABOUT, personObject.getAboutPersonInfo());
        values.put(Fields.AGE, personObject.getPersonAge());
        values.put(Fields.IS_MALE, Utils.boolToInt(personObject.isMale()));
        values.put(Fields.CREDITS, personObject.getPersonCredits());
        values.put(Fields.EDUCATION, personObject.getPersonEducation());
        values.put(Fields.LIVING_CITY, personObject.getPersonLivingCity());
        values.put(Fields.CURRENT_LOCATION, personObject.getPersonCurrLocation());
        values.put(Fields.POPULARITY, personObject.getPersonPopularity());
        values.put(Fields.SCORE, personObject.getPersonScore());
        values.put(Fields.WORK, personObject.getPersonWork());
        values.put(Fields.VIP, personObject.isPersonVIP());
        values.put(Fields.HERE_TO, personObject.getiAmHereTo().toString());
        values.put(Fields.LIVING_WITH, personObject.getLivingWith());
        values.put(Fields.PASSWORD, personObject.getPassword());
        values.put(Fields.EMAIL, personObject.getEmail());
        values.put(Fields.BIRTH_DAY, String.valueOf(personObject.getBirthday()));
        values.put(Fields.INTEREST_GENDER, personObject.getInterestGender().ordinal());
/*        values.put(Fields.TWITTER_ACCOUNT, personObject.getPersonTwitter());
        values.put(Fields.LINKEDIN_ACCOUNT, personObject.getPersonLinkedin());
        values.put(Fields.INSTAGRAM_ACCOUNT, personObject.getPersonInstagram());
        values.put(Fields.ODNOCLASSNIKI_ACCOUNT, personObject.getPersonOK());
        values.put(Fields.FACEBOOK_ACCOUNT, personObject.getPersonFBook());
        values.put(Fields.VKONTAKTE_ACCOUNT, personObject.getPersonVKontakte());
        values.put(Fields.Gplus_ACCOUNT, personObject.getPersonGPlus());*/
        values.put(Fields.SEARCH_AGE_MAX, personObject.getSearchAgeMax());
        values.put(Fields.SEARCH_AGE_MIN, personObject.getSearchAgeMin());
        values.put(Fields.WEIGHT, personObject.getWeight());
        values.put(Fields.GROWTH, personObject.getGrowth());
        values.put(Fields.DRINKING, personObject.getDrinkingPerson());
        values.put(Fields.SMOKING, personObject.getSmokingPerson());
        // values.put(Fields.SEXUALITY,personObject.getPersonSexuality().ordinal());
        //values.put(Fields.LANGUAGES, String.valueOf((personObject.getPersonLanguages())));//
        // values.put(Fields.LEVEL_LANGUAGES,personObject.getLevelLanguage());            //   <== how working with this array? IN ANOTHER SQL TABLE!
        //values.put(Fields.AWARDS, String.valueOf(personObject.getPersonAwards()));    //
        values.put(Fields.POPULARITY, personObject.getPersonPopularity());
        values.put(Fields.RELATIONSHIP, personObject.getPersonRelationship());

        return values;
    }


    public void setPersonalInfo(PersonObject personObject) {
        Log.d("DBHandler", "setPersonalInfo(), isFirstTime = " + isFirstTime(SQLiteEngine.TABLE_OWNER));

        if (isFirstTime(SQLiteEngine.TABLE_OWNER)) {
            sqliteDB.insert(SQLiteEngine.TABLE_OWNER, null, getContentValues(personObject));
        } else {
            sqliteDB.update(SQLiteEngine.TABLE_OWNER, getContentValues(personObject),
                    Fields.ID + "=?", new String[]{"" + personObject.getPersonId()});
        }

    }

    private void updateInDB(String tableName, int userId, String field, String data) {
        ContentValues values = new ContentValues();
        values.put(field, data);
        sqliteDB.update(tableName, values,
                Fields.ID + "=?", new String[]{"" + userId});

    }

    public void saveLastId(int userID) {
        ContentValues values = new ContentValues();
        values.put(Fields.ID, userID);
        sqliteDB.update(SQLiteEngine.TABLE_LAST_ID, values,
                null, null);
    }


    public void clearDataBase() {
        sqliteDB.execSQL("delete from " + SQLiteEngine.TABLE_OWNER);
        //sqliteDB.execSQL("delete from " + SQLiteEngine.TABLE_OWNER);
    }


    public ContentValues prepareSocProfile(SocialProfile profile, String soc_network) {

        ContentValues values = new ContentValues();
        values.put(Fields.SOCIAL_ID, soc_network);
        values.put(Fields.SOCIAL_PROFILE_FIRST_NAME, profile.getFirstName());
        values.put(Fields.SOCIAL_PROFILE_MIDDLE_NAME, profile.getMiddleName());
        values.put(Fields.SOCIAL_PROFILE_LAST_NAME, profile.getLastName());
        values.put(Fields.SOCIAL_PROFILE_IMAGE, Utils.getBitmapToBase64String(profile.getImage()));
        return values;


    }

    public void clearTable(String table) {
        sqliteDB.delete(table, null, null);
    }

    public void setSocialVK(SocialProfile profile, String soc_network) {

        sqliteDB.insert(SQLiteEngine.TABLE_SOCIAL_VK, null, prepareSocProfile(profile, soc_network));
    }

    public void setFireBaseToken(String token) {
        ContentValues values = new ContentValues();
        values.put(Fields.FIRE_BASE_TOKEN, token);
        if (isFirstTime(SQLiteEngine.TABLE_FIRE_BASE)) {
            sqliteDB.insert(SQLiteEngine.TABLE_FIRE_BASE, null, values);
        } else {
            sqliteDB.update(SQLiteEngine.TABLE_FIRE_BASE, values, null, null);
        }


    }


    public void setSocialOK(SocialProfile profile, String socNetwork) {
        sqliteDB.insert(SQLiteEngine.TABLE_SOCIAL_OK, null, prepareSocProfile(profile, socNetwork));
    }

    public void setSocialINSTA(SocialProfile profile, String socNetwork) {
        sqliteDB.insert(SQLiteEngine.TABLE_SOCIAL_INSTA, null, prepareSocProfile(profile, socNetwork));
    }


    public void setSocialFB(SocialProfile profile, String socNetwork) {
        sqliteDB.insert(SQLiteEngine.TABLE_SOCIAL_FB, null, prepareSocProfile(profile, socNetwork));
    }

    public int loadLastId() {
        return getIntFromDB(SQLiteEngine.TABLE_LAST_ID, Fields.ID);
    }

    public void updatePhoto(int userID, String photo) {
        updateInDB(SQLiteEngine.TABLE_OWNER, userID, Fields.PHOTO, photo);
    }


}
