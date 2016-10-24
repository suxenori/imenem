package com.menemi.personobject;

import android.graphics.Bitmap;

import com.menemi.dbfactory.DBHandler;
import com.menemi.filter.FilterObject;
import com.menemi.utils.Utils;
import com.google.android.gms.maps.model.LatLng;

import java.sql.Date;
import java.util.ArrayList;

public class PersonObject {

    public static final double INCORRECT_LAT_LONG = 666;
    public static final String EMPTY = "";
    private int personId;
    private String personSexuality;
    private String smokingPerson;
    private String drinkingPerson;
    private PersonKids personKids = PersonKids.WITHOUT_KIDS_PERSON;
    private String personName;
    private int personAge;
    private UserStatus userStatus = UserStatus.ANY;
    private String personWork = "";
    private String personEducation;
    private boolean isPersonVIP;
    private ArrayList<Integer> personFriends;
    private String personCurrLocation;


    private int personScore;
    private boolean isMale;
    private String personLivingCity;
    private ArrayList<Language> personLanguages;
    private int personCredits;
    private int personPopularity;
    private Bitmap personAvatar = null;
    private String personRelationship;
    private String livingWith = "";
    private HairColor hairColor = HairColor.BROWN_HAIR_COLOR;
    private BodyType bodyType = BodyType.WITHOUT_BODY_TYPE;
    private EyeColor eyeColor = EyeColor.WITHOUT_EYE_COLOR;
    private int growth;
    private int weight;
    private String aboutPersonInfo;
    private String personGPlus;
    private String personVKontakte;
    private String personFBook;
    private String personOK;
    private String personLinkedin;
    private String personInstagram;
    private String personTwitter;
    private String appearance;
    private Date birthday;
    private Date vipUntil;
    private String password;
    private ArrayList<Interests> interests;
    private String email;
    private int photoCountPublic;
    private int photoCountPrivate;
    private ArrayList<PersonalGift> gifts = new ArrayList<>();
    private ArrayList<Reward> rewards = new ArrayList<>();
    private double positionLatitude = INCORRECT_LAT_LONG;
    private double positionLongitude = INCORRECT_LAT_LONG;
    private boolean isOnline = false;
    private ArrayList<PersonFavorite> favorites = new ArrayList<>();
    private FilterObject filterObject = new FilterObject();
    private int searchAgeMax;
    private ArrayList<PhotoSetting> pictureUrlsPrivate = new ArrayList<>();
    private ArrayList<PhotoSetting> pictureUrlsPublic = new ArrayList<>();
    private LikeStatus likeStatus = LikeStatus.none;
    private int rating;

private boolean canChat;

    public PersonObject(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public PersonObject(boolean isMale, IamHereTo iAmHereTo, InterestGender interestGender, String email, String personName, Date birthday, String password) {
        this.isMale = isMale;
        setInterestGender(interestGender);
        setiAmHereTo(iAmHereTo);
        this.email = email;
        this.personName = personName;
        this.birthday = birthday;
        this.password = password;
    }

    public PersonObject(PersonObject personObject) {
        this.personId = personObject.personId;
        this.personSexuality = personObject.personSexuality;
        this.smokingPerson = personObject.smokingPerson;
        this.drinkingPerson = personObject.drinkingPerson;
        this.personKids = personObject.personKids;
        this.personName = personObject.personName;
        this.personAge = personObject.personAge;
        this.personWork = personObject.personWork;
        this.personEducation = personObject.personEducation;
        this.isPersonVIP = personObject.isPersonVIP;
        this.personFriends = personObject.personFriends;
        this.personCurrLocation = personObject.personCurrLocation;

        this.personScore = personObject.personScore;
        this.isMale = personObject.isMale;
        this.personLivingCity = personObject.personLivingCity;
        this.personLanguages = personObject.personLanguages;
        this.personCredits = personObject.personCredits;
        this.personPopularity = personObject.personPopularity;
        this.personAvatar = personObject.personAvatar;
        this.personRelationship = personObject.personRelationship;
        this.livingWith = personObject.livingWith;
        this.hairColor = personObject.hairColor;
        this.bodyType = personObject.bodyType;
        this.eyeColor = personObject.eyeColor;
        this.growth = personObject.growth;
        this.weight = personObject.weight;
        this.aboutPersonInfo = personObject.aboutPersonInfo;
        this.personGPlus = personObject.personGPlus;
        this.personVKontakte = personObject.personVKontakte;
        this.personFBook = personObject.personFBook;
        this.personOK = personObject.personOK;
        this.personLinkedin = personObject.personLinkedin;
        this.personInstagram = personObject.personInstagram;
        this.personTwitter = personObject.personTwitter;
        this.appearance = personObject.appearance;
        this.birthday = personObject.birthday;
        this.vipUntil = personObject.vipUntil;
        this.password = personObject.password;
        this.interests = personObject.interests;
        this.email = personObject.email;
        this.photoCountPublic = personObject.photoCountPublic;
        this.photoCountPrivate = personObject.photoCountPrivate;
        this.gifts = personObject.gifts;
        this.rewards = personObject.rewards;
        this.positionLatitude = personObject.positionLatitude;
        this.positionLongitude = personObject.positionLongitude;
        this.isOnline = personObject.isOnline;
        this.favorites = personObject.favorites;
        this.filterObject = personObject.filterObject;
        this.searchAgeMax = personObject.searchAgeMax;
        this.pictureUrlsPrivate = personObject.pictureUrlsPrivate;
        this.pictureUrlsPublic = personObject.pictureUrlsPublic;
    }

    public UserStatus getUserStatus()
    {
        return UserStatus.values()[filterObject.getIsOnline()];
    }

    public void setUserStatus(UserStatus userStatus)
    {
        this.filterObject.setIsOnline(userStatus.ordinal());
    }

    public int getPhotoCountPublic() {
        return photoCountPublic;
    }

    public void setPhotoCountPublic(int photoCountPublic) {
        this.photoCountPublic = photoCountPublic;
    }

    public int getPhotoCountPrivate() {
        return photoCountPrivate;
    }
    public void setPhotoCount(boolean isPrivate, int count){
        if(isPrivate){
            this.photoCountPrivate = count;
        } else{
            this.photoCountPublic = count;
        }
    }
    public int getPhotoCount(boolean isPrivate) {
        if(isPrivate) {
            return photoCountPrivate;
        } else{
            return photoCountPublic;
        }
    }
    public void setPhotoCountPrivate(int photoCountPrivate) {
        this.photoCountPrivate = photoCountPrivate;
    }

    public ArrayList<Interests> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<Interests> interests) {
        this.interests = interests;
    }

    public boolean isInterestMatches(String interest){
        for (int i = 0; i < interests.size(); i++) {
            if(interests.get(i).getInterest().equals(interest)){
                return true;
            }
        }
        return false;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = Utils.getDateFromString(birthday);
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public InterestGender getInterestGender() {
        return InterestGender.values()[filterObject.getiWantValue()];
    }

    public void setInterestGender(int value) {
        this.filterObject.setiAmHereTo(value);
    }

    public void setInterestGender(InterestGender interestGender) {
        this.filterObject.setiWantValue(interestGender.ordinal());

    }

    public void setiAmHereTo(IamHereTo iAmHereTo) {
        this.filterObject.setiWantValue(iAmHereTo.ordinal());
    }
    
    public ArrayList<Integer> getPersonFriends() {
        return personFriends;
    }

    public void setPersonFriends(ArrayList<Integer> personFriends) {
        this.personFriends = personFriends;
    }

    public String getPersonGPlus() {
        return personGPlus;
    }

    public void setPersonGPlus(String personGPlus) {
        this.personGPlus = personGPlus;
    }

    public String getPersonVKontakte() {
        return personVKontakte;
    }

    public void setPersonVKontakte(String personVKontakte) {
        this.personVKontakte = personVKontakte;
    }

    public String getPersonFBook() {
        return personFBook;
    }
   public void setPersonFBook(String personFBook) {
        this.personFBook = personFBook;
    }

    public String getPersonOK() {
        return personOK;
    }

    public void setPersonOK(String personOK) {
        this.personOK = personOK;
    }

    public String getPersonLinkedin() {
        return personLinkedin;
    }

    public void setPersonLinkedin(String personLinkedin) {
        this.personLinkedin = personLinkedin;
    }

    public String getPersonInstagram() {
        return personInstagram;
    }

    public void setPersonInstagram(String personInstagram) {
        this.personInstagram = personInstagram;
    }
    public String getPersonTwitter() {
        return personTwitter;
    }

    public void setPersonTwitter(String personTwitter) {
        this.personTwitter = personTwitter;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getEmail() {
        return email;
    }
//Getters and Setters for growth

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGrowth() {
        return growth;
    }

    public void setGrowth(int growth) {
        this.growth = growth;
    }

    public String getAboutPersonInfo() {
        return aboutPersonInfo;
    }

    public void setAboutPersonInfo(String aboutPersonInfo) {
        this.aboutPersonInfo = aboutPersonInfo;
    }

    public String getPersonRelationship() {
        return personRelationship;
    }

    public void setPersonRelationship(String personRelationship) {
        this.personRelationship = personRelationship;
    }

  public void prepaparePictureUrls(Runnable callback){

      DBHandler.getInstance().getPhotoUrls(personId,Utils.PICTURE_QUALITY_THUMBNAIL,false,(Object obj)->{
          this.pictureUrlsPublic = (ArrayList<PhotoSetting>)obj;
          DBHandler.getInstance().getPhotoUrls(personId,Utils.PICTURE_QUALITY_THUMBNAIL, true,(Object object)->{
              this.pictureUrlsPrivate = (ArrayList<PhotoSetting>)object;
              callback.run();
          });
      });

  }

    public ArrayList<PhotoSetting> getPictureUrlsPrivate() {
        return pictureUrlsPrivate;
    }

    public ArrayList<PhotoSetting> getPictureUrlsPublic() {
        return pictureUrlsPublic;
    }

    public String getLivingWith() {
        return livingWith;
    }

    public void setLivingWith(String livingWith) {
        this.livingWith = livingWith;
    }

    public HairColor getHairColor() {
        return hairColor;
    }

    public void setHairColor(int value) {
        this.hairColor = HairColor.values()[value];
    }

    public void setHairColor(HairColor hairColor) {
        this.hairColor = hairColor;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(int value) {
        this.bodyType = BodyType.values()[value];
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public EyeColor getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(int value) {
        this.eyeColor = EyeColor.values()[value];
    }

    public void setEyeColor(EyeColor eyeColor) {
        this.eyeColor = eyeColor;
    }

    public int getPersonId() {
        return personId;
    }

//Getters and Setters for person items

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public Bitmap getPersonAvatar() {
        return personAvatar;
    }

    public void setPersonAvatar(Bitmap personAvatar) {
        this.personAvatar = personAvatar;
    }




   public int getPersonPopularity() {
        return personPopularity;
    }

    public void setPersonPopularity(int personPopularity) {
        this.personPopularity = personPopularity;
    }

    public String getAppearance() {
        return appearance;
    }

    public void setAppearance(String appearance) {
        this.appearance = appearance;
    }

    public int getPersonCredits() {
        return personCredits;
    }

    public void setPersonCredits(int personCredits) {
        this.personCredits = personCredits;
    }

    public ArrayList<Language> getPersonLanguages() {
        return personLanguages;
    }

    public void setPersonLanguages(ArrayList<Language> personLanguages) {
        this.personLanguages = personLanguages;
    }

    public String getPersonLivingCity() {
        return personLivingCity;
    }

    public void setPersonLivingCity(String personLivingCity) {
        this.personLivingCity = personLivingCity;
    }

    public LikeStatus getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(LikeStatus likeStatus) {
        this.likeStatus = likeStatus;
    }

    public IamHereTo getiAmHereTo() {
        return IamHereTo.values()[filterObject.getiAmHereTo()];
    }

    /*public void setiAmHereTo(int value) {
        this.iAmHereTo = IamHereTo.values()[value];
    }

    public void setiAmHereTo(IamHereTo iAmHereTo) {
        this.iAmHereTo = iAmHereTo;
    }*/

    public int getPersonScore() {
        return personScore;
    }

    public void setPersonScore(int personScore) {
        this.personScore = personScore;
    }

    public boolean isPersonVIP() {
        return isPersonVIP;
    }

    public void setPersonVIP(boolean personVIP) {
        isPersonVIP = personVIP;
    }

    public String getPersonEducation() {
        return personEducation;
    }

    public void setPersonEducation(String personEducation) {
        this.personEducation = personEducation;
    }

    public String getPersonWork() {
        return personWork;
    }

    public void setPersonWork(String personWork) {
        this.personWork = personWork;
    }

    public int getPersonAge() {
        return personAge;
    }

    public void setPersonAge(int personAge) {
        this.personAge = personAge;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonSexuality() {
        return personSexuality;
    }

    public void setPersonSexuality(String value) {
        this.personSexuality = value;
    }


    public String getSmokingPerson() {
        return smokingPerson;
    }

    public void setSmokingPerson(String smokingPerson) {
        this.smokingPerson = smokingPerson;
    }

    public int getSearchAgeMin() {
        return filterObject.getMinAge();
    }

    public void setSearchAgeMin(int searchAgeMin) {
        this.filterObject.setMinAge(searchAgeMin);
    }

    public int getSearchAgeMax() {
        return filterObject.getMaxAge();
    }

    public void setSearchAgeMax(int searchAgeMax) {
        this.filterObject.setMaxAge(searchAgeMax);
    }

    public String getDrinkingPerson() {
        return drinkingPerson;
    }

    public void setDrinkingPerson(String drinkingPerson) {
        this.drinkingPerson = drinkingPerson;
    }

    public void setVipUntil(String vipUntil) {
        this.vipUntil =  Utils.getDateFromServer(vipUntil);
    }

    public PersonKids getPersonKids() {
        return personKids;
    }

    public void setPersonKids(PersonKids personKids) {
        this.personKids = personKids;
    }

    public void setPersonKids(int value) {
        this.personKids = PersonKids.values()[value];
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public void setMale(int value) {
        isMale = Utils.intToBool(value);
    }

    public double getPositionLatitude() {
        return positionLatitude;
    }
    public LatLng getPosition(){
        return new LatLng(getPositionLatitude(), getPositionLongitude());
    }

    public void setPositionLatitude(double positionLatitude) {
        this.positionLatitude = positionLatitude;
    }

    public double getPositionLongitude() {
        return positionLongitude;
    }

    public void setPositionLongitude(double positionLongitude) {
        this.positionLongitude = positionLongitude;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
    public void setOnline(String value) {
        if(value.equals("online")){
            isOnline = true;
        }
    }
    public FilterObject getFilterObject(){
        return  filterObject;
    }
    public ArrayList<PersonalGift> getGifts() {
        return gifts;
    }

    public void setGifts(ArrayList<PersonalGift> gifts) {
        this.gifts = gifts;
    }

    public ArrayList<Reward> getRewards() {
        return rewards;
    }

    public void setRewards(ArrayList<Reward> rewards) {
        this.rewards = rewards;
    }

    public void setFilterObject(FilterObject filterObject)
    {
        this.filterObject = filterObject;
    }

    public void setiAmHereTo(int iAmHereTo)
    {
        filterObject.setiAmHereTo(iAmHereTo);
    }

    public String getPersonCurrLocation() {
        return personCurrLocation;
    }
   /* public void setPersonCurrLocation(String personCurrLocation) {
        filterObject.getPlaceModel().getCityName() = personCurrLocation;
    }*/

    public ArrayList<PersonFavorite> getFavorites() {
        return favorites;
    }
    public boolean isAddedAsFavorite(PersonObject personObject){
        for (int i = 0; i < favorites.size(); i++) {
            if(favorites.get(i).getPersonId() == personObject.getPersonId()){
                return true;
            }
        }
        return false;
    }
    public void setAddedAsFavorite(PersonObject personObject, boolean isAdded){
        if(isAdded){
            favorites.add(new PersonFavorite(personObject));
        } else{
            for (int i = 0; i < favorites.size(); i++) {
                if(favorites.get(i).getPersonId() == personObject.getPersonId()){
                    favorites.remove(i);
                }
            }
        }

    }
    public void setFavorites(ArrayList<PersonFavorite> favorites) {
        this.favorites = favorites;
    }

    public void setPersonCurrLocation(String personCurrLocation)
    {
        this.personCurrLocation = personCurrLocation;
    }

    public boolean canChat() {
        return canChat;
    }

    public void setCanChat(boolean canChat) {
        this.canChat = canChat;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    /*   public enum PersonSexuality {
        WITHOUT_INFO,
        GAY,
        OPEN_MINDED,
        STRAIGHT,
        BISEXUAL
    }*/

    /*public enum SmokingPerson {
        WITHOUT_INFO_ABOUT_SMOKING,
        IS_SMOKING_PERSON,
        IS_NO_SMOKING_PERSON,
        ANTI_SMOKING_PERSON,
        SMOKING_IN_COMPANY_PERSON,
        SMOKE_ONE_AFTER_THE_OTHER_PERSON
    }*/

   /* public enum DrinkingPerson {
        WITHOUT_INFO_ABOUT_DRINKING,
        DRINKING_PERSON,
        NO_DRINKING_PERSON,
        IN_COMPANY_DRINKING_PERSON,
        HIGHLY_DRINKING_PERSON
    }
*/
    public enum PersonKids {
        WITHOUT_KIDS_PERSON,
        NEVER_MAKE_KIDS,
        SOMETIME_MAKE_KIDS,
        ALREADY_MADE_KIDS,
        ADULT_KIDS
    }

    public enum IamHereTo {
        MAKE_NEW_FRIEND,
        CHAT,
        DATE

    }

    public enum EyeColor {
        WITHOUT_EYE_COLOR,
        BROWN_EYE_COLOR,
        GRAY_EYE_COLOR,
        GREEN_EYE_COLOR,
        BLUE_EYE_COLOR,
        LIGHT_BROWN_EYE_COLOR
    }

    public enum BodyType {
        WITHOUT_BODY_TYPE,
        AVERAGE_BODY_TYPE,
        FEW_EXTRA_KG,
        ATHLETIC_BODY_TYPE,
        STRONG_BODY_TYPE,
        SLIM_BODY_TYPE,
        OBESITY_BODY_TYPE


    }

    public enum HairColor {
        WITHOUT_HAIR_COLOR,
        BROWN_HAIR_COLOR,
        RED_HAIR_COLOR,
        LIGHT_HAIR_COLOR,
        FEW_GRAY_HAIR_COLOR,
        GRIZZLE_HAIR_COLOR,
        SHAVE_HAIR,
        PAINTED_HAIR,
        HAIRLESS

    }

    public enum LivingWith {
        WITHOUT_LIVING_WITH_INFO,
        LIVING_WITH_PARENTS,
        LIVING_WITH_NEIGHBORS,
        LIVING_IN_DORMITORY,
        LIVING_WITH_SECOND_HALF,
        LIVING_ALONE
    }

    public enum PersonRelationship {
        WITHOUT_RELATIONSHIP_INFO,
        ALONE_RELATIONSHIP,
        BUSY_RELATIONSHIP,
        IN_FREE_RELATION
    }

    public static enum InterestGender {
        WOMAN,
        MAN,
        ANY_GENDER
    }

    public enum UserStatus{
        ONLINE,
        OFFLINE,
        ANY,
    }
    public enum LikeStatus{
        none,// - ни одна сторона не поставила лайк друг другу
        mutual_like,// - друг другу уже проставили лайк
        liked_me,// - профиль залайкал меня
        like_him// - я залайкал уже просматриваемый профиль
    }
}
