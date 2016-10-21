package com.menemi.dbfactory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import com.menemi.personobject.Gift;
import com.menemi.personobject.PersonObject;
import com.menemi.personobject.PhotoTemplate;
import com.menemi.social_network.SocialNetworkHandler;
import com.menemi.social_network.SocialProfile;
import com.menemi.utils.Utils;

import java.util.ArrayList;

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
            personObject.setPersonKids(cursor.getInt(cursor.getColumnIndex(Fields.KIDS)));//PersonKids().ordinal());
            personObject.setPersonLivingCity(cursor.getString(cursor.getColumnIndex(Fields.LIVING_CITY)));//PersonLivingCity());
            personObject.setPersonCurrLocation(cursor.getString(cursor.getColumnIndex(Fields.CURRENT_LOCATION)));//PersonCurrLocation());
            personObject.setPersonPopularity(cursor.getInt(cursor.getColumnIndex(Fields.POPULARITY)));//PersonPopularity());
            personObject.setPersonScore(cursor.getInt(cursor.getColumnIndex(Fields.SCORE)));//PersonScore());
            personObject.setPersonWork(cursor.getString(cursor.getColumnIndex(Fields.WORK)));//PersonWork());
            personObject.setiAmHereTo(cursor.getInt(cursor.getColumnIndex(Fields.HERE_TO)));//iAmHereTo().toString());
            personObject.setPersonSuperPower(cursor.getInt(cursor.getColumnIndex(Fields.SUPERPOWER)));//PersonSuperPower());
            personObject.setLivingWith(cursor.getString(cursor.getColumnIndex(Fields.LIVING_WITH)));//LivingWith().ordinal());
            personObject.setBirthday(cursor.getString(cursor.getColumnIndex(Fields.BIRTH_DAY)));
            personObject.setPersonTwitter(cursor.getString(cursor.getColumnIndex(Fields.TWITTER_ACCOUNT)));
            personObject.setPersonLinkedin(cursor.getString(cursor.getColumnIndex(Fields.LINKEDIN_ACCOUNT)));
            personObject.setPersonInstagram(cursor.getString(cursor.getColumnIndex(Fields.INSTAGRAM_ACCOUNT)));
            personObject.setPersonOK(cursor.getString(cursor.getColumnIndex(Fields.ODNOCLASSNIKI_ACCOUNT)));
            personObject.setPersonFBook(cursor.getString(cursor.getColumnIndex(Fields.FACEBOOK_ACCOUNT)));
            personObject.setPersonVKontakte(cursor.getString(cursor.getColumnIndex(Fields.VKONTAKTE_ACCOUNT)));
            personObject.setPersonGPlus(cursor.getString(cursor.getColumnIndex(Fields.Gplus_ACCOUNT)));
            personObject.setSearchAgeMax(cursor.getInt(cursor.getColumnIndex(Fields.SEARCH_AGE_MAX)));
            personObject.setSearchAgeMin(cursor.getInt(cursor.getColumnIndex(Fields.SEARCH_AGE_MIN)));
            personObject.setWeight(cursor.getInt(cursor.getColumnIndex(Fields.WEIGHT)));
            personObject.setGrowth(cursor.getInt(cursor.getColumnIndex(Fields.GROWTH)));
            personObject.setEyeColor(cursor.getInt(cursor.getColumnIndex(Fields.EYE_COLOR)));
            personObject.setBodyType(cursor.getInt(cursor.getColumnIndex(Fields.BODY_TYPE)));
            personObject.setHairColor(cursor.getInt(cursor.getColumnIndex(Fields.HAIR_COLOR)));
            personObject.setDrinkingPerson(cursor.getString(cursor.getColumnIndex(Fields.DRINKING)));
            personObject.setSmokingPerson(cursor.getString(cursor.getColumnIndex(Fields.SMOKING)));
            //  personObject.setPersonSexuality(cursor.getInt(cursor.getColumnIndex(Fields.SEXUALITY)));
            personObject.setPersonSuperPower(cursor.getInt(cursor.getColumnIndex(Fields.SUPERPOWER)));
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
                Gift gift = new Gift(Utils.getBitmapFromStringBase64(cursor.getString(cursor.getColumnIndex(Fields.PHOTO))),
                        cursor.getInt(cursor.getColumnIndex(Fields.ID)), cursor.getString(cursor.getColumnIndex(Fields.NAME)),
                        cursor.getInt(cursor.getColumnIndex(Fields.PRICE)));
                gifts.add(gift);
                cursor.moveToNext();
            }

        }
        return gifts;

    }
    public void setGift(Gift gift){

            ContentValues values = new ContentValues();

            values.put(Fields.PHOTO, Utils.getBitmapToBase64String(gift.getImage()));
            values.put(Fields.ID, gift.getGiftId());
            values.put(Fields.NAME, gift.getGiftName());
            values.put(Fields.PRICE, gift.getPrice());



            if (isFirstTime(SQLiteEngine.TABLE_GIFTS_BASE)) {
                sqliteDB.insert(SQLiteEngine.TABLE_GIFTS_BASE, null, values);
            } else {
                Cursor cursor = sqliteDB.query(SQLiteEngine.TABLE_GIFTS_BASE, null, Fields.ID + "=?",
                        new String[]{""+gift.getGiftId()}, null, null, null, null);
                if (cursor != null && cursor.moveToFirst() == true){
                    sqliteDB.update(SQLiteEngine.TABLE_GIFTS_BASE, values,
                            Fields.ID + "= ?", new String[]{"" + gift.getGiftId()});
                } else {
                    sqliteDB.insert(SQLiteEngine.TABLE_GIFTS_BASE, null, values);
                }

            }

        }



    public void setGifts(ArrayList<Gift> gifts){
        for (int i = 0; i < gifts.size(); i++) {
            ContentValues values = new ContentValues();

            values.put(Fields.PHOTO, Utils.getBitmapToBase64String(gifts.get(i).getImage()));
            values.put(Fields.ID, gifts.get(i).getGiftId());
            values.put(Fields.NAME, gifts.get(i).getGiftName());
            values.put(Fields.PRICE, gifts.get(i).getPrice());

            Log.d("DBHandler", "setPersonalInfo(), isFirstTime = " + isFirstTime(SQLiteEngine.TABLE_OWNER));

            if (isFirstTime(SQLiteEngine.TABLE_GIFTS_BASE)) {
                sqliteDB.insert(SQLiteEngine.TABLE_GIFTS_BASE, null, values);
            } else {
                Cursor cursor = sqliteDB.query(SQLiteEngine.TABLE_GIFTS_BASE, null, Fields.ID + "=?",
                        new String[]{""+gifts.get(i).getGiftId()}, null, null, null, null);
                if (cursor != null && cursor.moveToFirst() == true){
                    sqliteDB.update(SQLiteEngine.TABLE_GIFTS_BASE, values,
                            Fields.ID + "= ?", new String[]{"" + gifts.get(i).getGiftId()});
                } else {
                    sqliteDB.insert(SQLiteEngine.TABLE_GIFTS_BASE, null, values);
                }

            }

        }


    }
    public ArrayList<PhotoTemplate> getTemplates() {


        Cursor cursor = sqliteDB.query(SQLiteEngine.TABLE_TEMPLATES_BASE, null, null, null, null, null, null);
        ArrayList<PhotoTemplate> templates = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                PhotoTemplate template = new PhotoTemplate(cursor.getInt(cursor.getColumnIndex(Fields.ID)),
                        Utils.getBitmapFromStringBase64(cursor.getString(cursor.getColumnIndex(Fields.PHOTO))));
                templates.add(template);
                cursor.moveToNext();
            }

        }
        return templates;

    }

    public void setTemplates(ArrayList<PhotoTemplate> templates){
        for (int i = 0; i < templates.size(); i++) {
            ContentValues values = new ContentValues();

            values.put(Fields.PHOTO, Utils.getBitmapToBase64String(templates.get(i).getTemplatePicture()));
            values.put(Fields.ID, templates.get(i).getTemplateID());

            //Log.d("DBHandler", "setPersonalInfo(), isFirstTime = " + isFirstTime(SQLiteEngine.TABLE_OWNER));

            if (isFirstTime(SQLiteEngine.TABLE_TEMPLATES_BASE)) {
                sqliteDB.insert(SQLiteEngine.TABLE_TEMPLATES_BASE, null, values);
            } else {
                Cursor cursor = sqliteDB.query(SQLiteEngine.TABLE_TEMPLATES_BASE, null, Fields.ID + "=?",
                        new String[]{"" + templates.get(i).getTemplateID()}, null, null, null, null);
                if (cursor != null) {
                    sqliteDB.update(SQLiteEngine.TABLE_TEMPLATES_BASE, values,
                            Fields.ID + "= ?", new String[]{"" + templates.get(i).getTemplateID()});
                } else {
                    sqliteDB.insert(SQLiteEngine.TABLE_TEMPLATES_BASE, null, values);
                }

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

        return getStringFromDB(SQLiteEngine.TABLE_FIRE_BASE, Fields.FIRE_BASE_TOKEN);
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
        values.put(Fields.KIDS, personObject.getPersonKids().ordinal());
        values.put(Fields.LIVING_CITY, personObject.getPersonLivingCity());
        values.put(Fields.CURRENT_LOCATION, personObject.getPersonCurrLocation());
        values.put(Fields.POPULARITY, personObject.getPersonPopularity());
        values.put(Fields.SCORE, personObject.getPersonScore());
        values.put(Fields.WORK, personObject.getPersonWork());
        values.put(Fields.VIP, personObject.isPersonVIP());
        values.put(Fields.HERE_TO, personObject.getiAmHereTo().toString());
        values.put(Fields.SUPERPOWER, personObject.getPersonSuperPower());
        values.put(Fields.LIVING_WITH, personObject.getLivingWith());
        values.put(Fields.PASSWORD, personObject.getPassword());
        values.put(Fields.EMAIL, personObject.getEmail());
        values.put(Fields.BIRTH_DAY, String.valueOf(personObject.getBirthday()));
        values.put(Fields.INTEREST_GENDER, personObject.getInterestGender().ordinal());
        values.put(Fields.TWITTER_ACCOUNT, personObject.getPersonTwitter());
        values.put(Fields.LINKEDIN_ACCOUNT, personObject.getPersonLinkedin());
        values.put(Fields.INSTAGRAM_ACCOUNT, personObject.getPersonInstagram());
        values.put(Fields.ODNOCLASSNIKI_ACCOUNT, personObject.getPersonOK());
        values.put(Fields.FACEBOOK_ACCOUNT, personObject.getPersonFBook());
        values.put(Fields.VKONTAKTE_ACCOUNT, personObject.getPersonVKontakte());
        values.put(Fields.Gplus_ACCOUNT, personObject.getPersonGPlus());
        values.put(Fields.SEARCH_AGE_MAX, personObject.getSearchAgeMax());
        values.put(Fields.SEARCH_AGE_MIN, personObject.getSearchAgeMin());
        values.put(Fields.WEIGHT, personObject.getWeight());
        values.put(Fields.GROWTH, personObject.getGrowth());
        values.put(Fields.EYE_COLOR, personObject.getEyeColor().ordinal());
        values.put(Fields.BODY_TYPE, personObject.getBodyType().ordinal());
        values.put(Fields.HAIR_COLOR, personObject.getHairColor().ordinal());
        values.put(Fields.DRINKING, personObject.getDrinkingPerson());
        values.put(Fields.SMOKING, personObject.getSmokingPerson());
        // values.put(Fields.SEXUALITY,personObject.getPersonSexuality().ordinal());
        //values.put(Fields.LANGUAGES, String.valueOf((personObject.getPersonLanguages())));//
        // values.put(Fields.LEVEL_LANGUAGES,personObject.getLevelLanguage());            //   <== how working with this array? IN ANOTHER SQL TABLE!
        //values.put(Fields.AWARDS, String.valueOf(personObject.getPersonAwards()));    //
        values.put(Fields.SUPERPOWER, personObject.getPersonSuperPower());
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
                    Fields.ID + "= ?", new String[]{"" + personObject.getPersonId()});
        }

    }

    private void updateInDB(String tableName, int userId, String field, String data) {
        ContentValues values = new ContentValues();
        values.put(field, data);
        sqliteDB.update(tableName, values,
                Fields.ID + "= ?", new String[]{"" + userId});

    }

    public void saveLastId(int userID) {
        ContentValues values = new ContentValues();
        values.put(Fields.ID, userID);
        sqliteDB.update(SQLiteEngine.TABLE_LAST_ID, values,
                null, null);
    }


    public void clearDataBase() {
        sqliteDB.execSQL("delete from " + SQLiteEngine.TABLE_OWNER);
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
