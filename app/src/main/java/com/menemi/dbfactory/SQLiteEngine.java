package com.menemi.dbfactory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by irondev on 07.06.16.
 */
public class SQLiteEngine extends SQLiteOpenHelper implements BaseColumns {

    public static final String DATABASE_NAME = "menime1.db";
    public static final String TABLE_OWNER = "profiles";
    public static final String TABLE_LAST_ID = "last_id";
    public static final String TABLE_INTEREST = "personInterests";
    public static final String TABLE_SOCIAL_OK = "social_ok";
    public static final String TABLE_SOCIAL_FB = "social_fb";
    public static final String TABLE_SOCIAL_VK = "social_vk";
    public static final String TABLE_SOCIAL_INSTA = "social_insta";
    public static final String TABLE_AWARDS = "personAwards";
    public static final String TABLE_LANGUAGES = "person_languages";
    public static final String TABLE_FIRE_BASE = "fire_base";
    public static final String TABLE_GIFTS_BASE = "gifts_base";
    public static final String TABLE_TEMPLATES_BASE = "templates_base";
    public static final String TABLE_PHOTOS = "photos";
    public static final String TABLE_AVATARS = "avatars";
    public static final String TABLE_DIALOGS = "dialogs_base";

    private static final int DATABASE_VERSION = 1; // In case of any changes in database structure this variable should be incremented
    private static final String DATABASE_LAST_USER_CREATE_SCRIPT = "CREATE TABLE IF NOT EXISTS " +
            TABLE_LAST_ID + " (" + Fields.ID + " integer)";
    private static final String DATABASE_FIREBASE_TOKEN_CREATE_SCRIPT = "CREATE TABLE IF NOT EXISTS " +
            TABLE_FIRE_BASE + " (" + Fields.FIRE_BASE_TOKEN + " text)";

    private static final String DATABASE_CREATE_SCRIPT = "CREATE TABLE IF NOT EXISTS " +
            TABLE_OWNER + " (" + BaseColumns._ID +
            " integer primary key autoincrement, " +
            Fields.ID + " integer, " +
            Fields.NAME + " text not null, " +
            Fields.AGE + " integer, " +
            Fields.WORK + " text, " +
            Fields.EDUCATION + " text, " +
            Fields.VIP + " integer, " +
            Fields.CURRENT_LOCATION + " text, " +
            Fields.SCORE + " integer, " +
            Fields.ABOUT + " text, " +
            Fields.HERE_TO + " text, " +
            Fields.IS_MALE + " integer, " +
            Fields.RELATIONSHIP + " text, " +
            Fields.KIDS + " integer, " +
            Fields.CREDITS + " integer, " +
            Fields.POPULARITY + " integer, " +
            Fields.SUPERPOWER + " integer, " +
            Fields.DRINKING + " text, " +
            Fields.SMOKING + " text, " +
            Fields.SEXUALITY + " integer," +
            Fields.BIRTH_DAY + " int8, " +
            Fields.BODY_TYPE + " integer, " +
            Fields.EYE_COLOR + " integer, " +
            Fields.HAIR_COLOR + " integer, " +
            Fields.LIVING_CITY + " text, " +
            Fields.LIVING_WITH + " integer, " +
            Fields.SEARCH_AGE_MAX + " integer, " +
            Fields.SEARCH_AGE_MIN + " integer," +
            Fields.FRIENDS + " text, " +
            Fields.AWARDS + " text, " +
            Fields.LINKEDIN_ACCOUNT + " text, " +
            Fields.Gplus_ACCOUNT + " text, " +
            Fields.FACEBOOK_ACCOUNT + " text, " +
            Fields.VKONTAKTE_ACCOUNT + " text, " +
            Fields.TWITTER_ACCOUNT + " text, " +
            Fields.ODNOCLASSNIKI_ACCOUNT + " text, " +
            Fields.INSTAGRAM_ACCOUNT + " text, " +
            Fields.GROWTH + " integer, " +
            Fields.WEIGHT + " integer, " +
            Fields.INTEREST_GENDER + " integer, " +
            Fields.ORIENTATION + " integer, " +
            Fields.EMAIL + " email, " +
            Fields.PASSWORD + " text)";

    private static final String CREATE_TABLE_INTEREST = "CREATE TABLE IF NOT EXISTS " +
            TABLE_INTEREST + " (" + BaseColumns._ID +
            " integer primary key autoincrement, " +
            Fields.ID + " integer, " +
            Fields.CATEGORY + " integer, " +
            Fields.INTERESTS + " text)";

    private static final String CREATE_TABLE_GIFTS = "CREATE TABLE IF NOT EXISTS " +
            TABLE_GIFTS_BASE + " (" + BaseColumns._ID +
            " integer primary key autoincrement, " +
            Fields.PHOTO + " text, " +
            Fields.ID + " integer, " +
            Fields.NAME + " text, " +
            Fields.PRICE + " integer)";

    private static final String CREATE_TABLE_TEMPLATES = "CREATE TABLE IF NOT EXISTS " +
            TABLE_TEMPLATES_BASE + " (" + BaseColumns._ID +
            " integer primary key autoincrement, " +
            Fields.PHOTO + " text, " +
            Fields.ID + " integer)";

    private static final String CREATE_TABLE_AVATARS = "CREATE TABLE IF NOT EXISTS " +
            TABLE_AVATARS + " (" + BaseColumns._ID +
            " integer primary key autoincrement, " +
            Fields.URLS + " text, " +
            Fields.ID + " integer)";

    private static final String CREATE_TABLE_DIALOGS = "CREATE TABLE IF NOT EXISTS " +
            TABLE_DIALOGS + " (" + BaseColumns._ID +
            " integer primary key autoincrement, " +
            Fields.DIALOG_ID + " integer, " +
            Fields.PROFILE_ID_2 + " integer, " +
            Fields.LAST_MESSAGE + " text, " +
            Fields.LAST_MESSAGE_AT + " text, " +
            Fields.UNREAD_COUNT + " integer, " +
            Fields.NAME + " text)";



    private static final String CREATE_TABLE_SOCIAL_FB = "CREATE TABLE IF NOT EXISTS " +
            TABLE_SOCIAL_FB + " (" + BaseColumns._ID +
            " integer primary key autoincrement, " +
            Fields.SOCIAL_ID + " text, " +
            Fields.SOCIAL_PROFILE_IMAGE + " text, " +
            Fields.SOCIAL_PROFILE_FIRST_NAME + " text, " +
            Fields.SOCIAL_PROFILE_MIDDLE_NAME + " text, " +
            Fields.SOCIAL_PROFILE_LAST_NAME + " text, " +
            Fields.ID + " text)";
    private static final String CREATE_TABLE_SOCIAL_VK = "CREATE TABLE IF NOT EXISTS " +
            TABLE_SOCIAL_VK + " (" + BaseColumns._ID +
            " integer primary key autoincrement, " +
            Fields.SOCIAL_ID + " text, " +
            Fields.SOCIAL_PROFILE_IMAGE + " text, " +
            Fields.SOCIAL_PROFILE_FIRST_NAME + " text, " +
            Fields.SOCIAL_PROFILE_MIDDLE_NAME + " text, " +
            Fields.SOCIAL_PROFILE_LAST_NAME + " text, " +
            Fields.ID + " text)";
    private static final String CREATE_TABLE_SOCIAL_OK = "CREATE TABLE IF NOT EXISTS " +
            TABLE_SOCIAL_OK + " (" + BaseColumns._ID +
            " integer primary key autoincrement, " +
            Fields.SOCIAL_ID + " text, " +
            Fields.SOCIAL_PROFILE_IMAGE + " text, " +
            Fields.SOCIAL_PROFILE_FIRST_NAME + " text, " +
            Fields.SOCIAL_PROFILE_MIDDLE_NAME + " text, " +
            Fields.SOCIAL_PROFILE_LAST_NAME + " text, " +
            Fields.ID + " text)";
    private static final String CREATE_TABLE_SOCIAL_INSTA = "CREATE TABLE IF NOT EXISTS " +
            TABLE_SOCIAL_INSTA + " (" + BaseColumns._ID +
            " integer primary key autoincrement, " +
            Fields.SOCIAL_ID + " text, " +
            Fields.SOCIAL_PROFILE_IMAGE + " text, " +
            Fields.SOCIAL_PROFILE_FIRST_NAME + " text, " +
            Fields.SOCIAL_PROFILE_MIDDLE_NAME + " text, " +
            Fields.SOCIAL_PROFILE_LAST_NAME + " text, " +
            Fields.ID + " text)";
    /*private static final String CREATE_TABLE_SOCIAL_USER = "CREATE TABLE IF NOT EXISTS " +
            TABLE_SOCIAL + " (" + BaseColumns._ID +
            " integer primary key autoincrement, " +
            Fields.SOCIAL_ID + " text, " +
            Fields.SOCIAL_PROFILE_IMAGE + " text, " +
            Fields.SOCIAL_PROFILE_FIRST_NAME + " text, " +
            Fields.SOCIAL_PROFILE_MIDDLE_NAME + " text, " +
            Fields.SOCIAL_PROFILE_LAST_NAME + " text, " +
            Fields.ID + " text)";*/

    private static final String CREATE_TABLE_AWARDS = "CREATE TABLE IF NOT EXISTS " +
            TABLE_AWARDS + " (" + BaseColumns._ID +
            " integer primary key autoincrement, " +
            Fields.ID + " integer, " +
            Fields.AWARDS + " text)";

    private static final String CREATE_TABLE_PHOTOS = "CREATE TABLE IF NOT EXISTS " +
            TABLE_PHOTOS + " (" + BaseColumns._ID +
            " integer primary key autoincrement, " +
            Fields.PROFILE_ID_2 + " integer, " +
            Fields.PHOTO + " text, " +
            Fields.QUALITY + " text, " +
            Fields.URLS + " text, " +
            Fields.IS_PRIVATE + " integer, " +
            Fields.ID + " integer, " +
            Fields.IS_AUTO_PRICE + " integer, " +
            Fields.PRICE + " integer, " +
            Fields.TOTAL_PROFIT + " integer, " +
            Fields.TOTAL_VIEWS + " integer, " +
            Fields.IS_UNLOCKED + " integer, " +
            Fields.TEMPLATE_IDS + " text)";


    private static final String CREATE_TABLE_LANGUAGE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_LANGUAGES + " (" + BaseColumns._ID +
            " integer primary key autoincrement, " +
            Fields.ID + " integer, " +
            Fields.LANGUAGES + " text, " +
            Fields.LEVEL_LANGUAGES + " integer)";

    public SQLiteEngine(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v("SQLite", "onCreate");
        try {
            db.execSQL(DATABASE_CREATE_SCRIPT);
            db.execSQL(DATABASE_LAST_USER_CREATE_SCRIPT);
            db.execSQL(CREATE_TABLE_SOCIAL_OK);
            db.execSQL(CREATE_TABLE_SOCIAL_VK);
            db.execSQL(CREATE_TABLE_SOCIAL_FB);
            db.execSQL(CREATE_TABLE_SOCIAL_INSTA);
            db.execSQL(DATABASE_FIREBASE_TOKEN_CREATE_SCRIPT);
            db.execSQL(CREATE_TABLE_GIFTS);
            db.execSQL(CREATE_TABLE_TEMPLATES);
            db.execSQL(CREATE_TABLE_PHOTOS);
            db.execSQL(CREATE_TABLE_AVATARS);
            db.execSQL(CREATE_TABLE_DIALOGS);


            this.addDefaultId(db);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public void addDefaultId(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(Fields.ID, -1);
        db.insert(TABLE_LAST_ID, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w("SQLite", "Update from version " + oldVersion + " to version " + newVersion);


        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);

        //TODO: Copy data to new table code

        onCreate(db);
    }

    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[]{"mesage"};
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try {
            String maxQuery = Query;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[]{"Success"});

            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0, c);
                c.moveToFirst();

                return alc;
            }
            return alc;
        } catch (SQLException sqlEx) {
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + sqlEx.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        } catch (Exception ex) {

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + ex.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        }


    }
}
