package com.menemi.dbfactory;

import com.menemi.BuildConfig;

/**
 * Created by irondev on 14.06.16.
 */
public  class Fields {
    public static String URL_FOR_SERVER ;
    public static String URL_FOR_MESSAGING;
    static{
        if (BuildConfig.DEBUG) {
            //URL_FOR_SERVER = "http://menemidev.ironexus.com";
            //URL_FOR_MESSAGING = "menemidev.ironexus.com";
            URL_FOR_SERVER = "http://minemi.ironexus.com";
            URL_FOR_MESSAGING = "minemi.ironexus.com";
        } else {
            URL_FOR_SERVER = "http://minemi.ironexus.com";
            URL_FOR_MESSAGING = "minemi.ironexus.com";
        }
    }


    public static final String PROFILE = "profile";
    public static final String SOCIAL_PROFILE_IMAGE = "profile";
    public static final String SOCIAL_PROFILE_FIRST_NAME = "f_name";
    public static final String SOCIAL_PROFILE_MIDDLE_NAME = "m_name";
    public static final String SOCIAL_PROFILE_LAST_NAME = "l_name";
    public static final String ORIENTATION = "orientation_string";//orientation : "Bisexual"
    public static final String ORIENTATION_INT = "orientation";//orientation : "Bisexual"
    public static final String ID = "id";
    public static final String SOCIAL_ID = "s_id";
    public static final String NAME = "name";
    public static final String AGE = "age";
    public static final String NEWS = "news";

    public static final String EDUCATION = "education";
    public static final String VIP = "is_vip_on";
    public static final String CURRENT_LOCATION = "address";
    public static final String SCORE = "score";
    public static final String ABOUT = "about_me";
    public static final String HERE_TO = "purpose";
    public static final String IS_MALE = "gender";
    public static final String RELATIONSHIP = "relationship_status_string";
    public static final String RELATIONSHIP_INT = "relationship_status";
    public static final String KIDS = "childrens";
    public static final String CREDITS = "credits";
    public static final String POPULARITY = "popularity";
    public static final String SUPERPOWER = "super_power";
    public static final String PHOTO = "avatar";
    public static final String CATEGORY = "category";
    public static final String INTERESTS = "interests";
    public static final String AWARDS = "awards";
    public static final String LANGUAGES = "languages";
    public static final String LEVEL_LANGUAGES = "level_languages";
    public static final String SEXUALITY = "sexuality";
    public static final String SMOKING = "smoking_string";
    public static final String DRINKING = "drinking_string";
    public static final String SMOKING_INT = "smoking";
    public static final String DRINKING_INT = "drinking";
    public static final String WORK = "work";
    public static final String FRIENDS = "friends";

    public static final String LIVING_CITY = "living_city";
    public static final String LIVING_WITH = "living_with_string";
    public static final String LIVING_WITH_INT = "living_with";
    public static final String HAIR_COLOR = "hair_color";
    public static final String BODY_TYPE = "body_type";
    public static final String DISTANCE = "distance";
    public static final String EYE_COLOR = "eye_color";
    public static final String GROWTH = "height";
    public static final String WEIGHT = "wight";
    public static final String SEARCH_AGE_MAX = "search_age_max";
    public static final String SEARCH_AGE_MIN = "search_age_min";
    public static final String Gplus_ACCOUNT = "gplusid";
    public static final String VKONTAKTE_ACCOUNT = "vkid";
    public static final String FACEBOOK_ACCOUNT = "fbid";
    public static final String ODNOCLASSNIKI_ACCOUNT = "okid";
    public static final String LINKEDIN_ACCOUNT = "linkedin_account";
    public static final String TWITTER_ACCOUNT = "twitter_account";
    public static final String INSTAGRAM_ACCOUNT = "instagramid";
    public static final String INTEREST_GENDER = "interested_gender";
    public static final String BIRTH_DAY = "birth";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String POS_LAT = "latitude";
    public static final String POS_LON = "longitude";
    public static final String ADDED_AT = "added_at";
    public static final String VISITED_AT = "visited_at";
    public static final String STATUS = "status";
    public static final String LIKED_AT = "liked_at";
    public static final String VAL = "val";
    public static final String IS_MUTUAL = "is_mutual";
    public static final String VIP_STATUS = "vip_until";// : "2016-09-30T15:06:12.000Z"
    public static final String INTEREST_ID = "interest_id";
    public static final String ICON_URL = "icon_url";
    public static final String AVATAR_URL = "avatar_url";
    public static final String CREDITS_ADDED_AMOUNT = "added_amount";
    public static final String CREDITS_ADDED_TOKEN = "transaction_token";
    public static final String RATING = "profile_rating";

    public static final String PHOTO_COUNT = "pictures_count";
    public static final String PUBLIC_PHOTO_COUNT = "public_pictures_count";
    public static final String PRIVATE_PHOTO_COUNT = "private_pictures_count";


    public static final String JSON = "JSON";
    public static final String GIFTS = "gifts";
    public static final String REWARDS = "rewards";
    public static final String CAN_MESSAGE = "can_message";
    public static final String FIRE_BASE_TOKEN = "fire_base_token";
    public static final String PRIVATE_PICTURES_COUNT = "private_pictures_count";
    public static final String PUBLIC_PICTURES_COUNT = "public_pictures_count";

    public static final String CONFIGURATIONS= "configurations";
    public static final String PROFILE_ID_2 = "profile_id";
    public static final String SHOW_DISTANCE = "show_distance";
    public static final String HIDE_ONLINE_STATUS = "hide_online_status";
    public static final String PUBLIC_SEARCH = "public_search";
    public static final String SHOW_NEARBY = "show_nearby";
    public static final String LIMIT_PROFILE = "limit_profile";
    public static final String SHARE_PROFILE = "share_profile";
    public static final String FIND_BY_EMAIL = "find_by_email";
    public static final String HALF_INVISIBLE = "half_invisible";
    public static final String INVISBLE_HEAT = "invisible_heat";
    public static final String HIDE_VIP_STATUS = "hide_vip_status";
    public static final String LIMIT_MESSAGES = "limit_messages";
    public static final String HIDE_MY_VERIFICATIONS = "hide_my_verifications";
    public static final String HIDE_PROFILE_AS_DELETED = "hide_profile_as_deleted";

    public static final String MESSAGES = "messages";
    public static final String MUT_LIKES = "mutual_likes";
    public static final String THEIR_LIKES = "their_likes";
    public static final String NEARBY = "nearby";
    public static final String VISITORS = "visitors";
    public static final String FAVORITES = "favorites";

    public static final String OTHER = "other";

    public static final String N_EMAIL = "email";
    public static final String N_PUSH = "push";
    public static final String N_INAPP = "inapp";
    public static final String SOCIAL_NETWORK = "social_network";

    public static String PREFIX;

    //PhotoSettings
    public  static final String TEMPLATE_IDS = "template_ids";
    public  static final String TEMPLATES = "pictures_templates_ids";
    public  static final String PHOTO_TEMPLATES = "templates";
    public  static final String TOTAL_PROFIT= "total_profit";
    public  static final String IS_AUTO_PRICE = "is_auto_price";
    public  static final String TOTAL_VIEWS  = "total_views";
    public  static final String IS_UNLOCKED = "is_unlocked";
    public  static final String IS_PRIVATE = "is_private";
    public  static final String URLS = "url";
    public static final String PICTURES = "pictures";
    public static final String PICTURE = "picture";
    public static final String PRICE = "price";
    public static final String QUALITY = "quality";
    //TODO VIP UNTIL
    public static final String SORT_ID = "sort_id";
    public static final String BODY = "body";
    public static final String DATE = "date";
    public static final String DIALOG_ID = "dialog_id";
    public static final String LAST_MESSAGE = "last_message";
    public static final String LAST_MESSAGE_AT = "last_message_date";
    public static final String UNREAD_COUNT = "unread_msgs_count";
    public enum SOCIAL_NETWORKS{
        FACEBOOK,
        VKONTAKTE,
        INSTAGRAM,
        ODNOKLASNIKI,
        GOOGLE_PLUS

    }
}
