package com.menemi.dbfactory;

import android.graphics.Bitmap;

import com.menemi.personobject.PersonObject;
import com.menemi.social_network.SocialProfile;

/**
 * Created by tester03 on 10.06.2016.
 */
public interface DBInterface
{
    String getName(int userID);

    int getAge(int userID);

    String getWork(int userID);

    String getCuurentLocation(int userID);

    String getEducation(int userID);

    int getScore(int userID);

    String getAbout(int userID);

    String getHereTo(int userID);

    int getKids(int userID);

    int getCredits(int userID);

    int getPopularity(int userID);

    int getSuperpower(int userID);

    Bitmap getPhoto(int userID);

    int getIsMale(int userID);

    int getIsVIP(int userID);

    int getUserId();

    String getPassword(int userID);

    String getEmail(int userID);

    int getBDay(int userID);

    int getInterestGender(int userID);

    String getTwitterAccount(int userID);

    String getInstagramAccount(int userID);

    String getLinkedinAccount(int userID);

    String getOdnoclassnikiAccount(int userID);

    String getFacebookAccount(int userID);

    String getVkAccount(int userID);

    String getGPlusAccount(int userID);

    int getSearchMinAge(int userID);

    int getSearchMaxAge(int userID);

    int getWeight(int userID);

    int getGrowht(int userID);

    int getEyeColor(int userID);

    int getBodyType(int userID);

    int getHairColor(int userID);

    int getLivingWith(int userID);

    String getLivingCity(int userID);

    int getDrinking(int userID);

    int getSmoking(int userID);

    int getSexuality(int userID);

    int getLevelLanguage(int userID);

    String getLanguage(int userID);

    String getAwards(int userID);

    String getInterest(int userID);

    String getCategory(int userID);

    int getSuperPower(int userID);

    int getPopulaity(int userID);

    int getRelationship(int userID);

    void setPersonalInfo(PersonObject personObject);

    void updatePassword(int userID, String password);

    void updateEmail(int userID, String email);

    void updateBDay(int userID, String bday);

    void updateInterestGender(int userID, String interestGender);

    void updateTwitterAccount(int userID, String twitterAccount);

    void updateInstagramAccount(int userID, String instagramAccount);

    void updateLinkedinAccount(int userID, String linkedinAccount);

    void updateOkAccount(int userID, String okAccount);

    void updateFacebookAccount(int userID, String facebookAccount);

    void updateVkAccount(int userID, String vkAccount);

    void updateGplusAccount(int userID, String gplusAccount);

    void updateSearchMinAge(int userID, int searchMinAge);

    void updateSearchMaxAge(int userID, int searchMaxAge);

    void updateWeight(int userID, int weight);

    void updateGrowth(int userID, int growth);

    void updateEyeColor(int userID, int eyeColor);

    void updateBodyType(int userID, int bodyType);

    void updateLivingWith(int userID, int livingWith);

    void updateLivingCity(int userID, int livingCity);

    void updateDrinking(int userID, int drinking);

    void updateSmoking(int userID, int smoking);

    void updateSexuality(int userID, int sexuality);

    void updateLevelLanguage(int userID, String levelLanguage);

    void updateLanguage(int userID, String language);

    void updateAwords(int userID, String awards);

    void updateInterest(int userID, String interest);

    void updateCategory(int userID, String category);

    void updatePopularity(int userID, String popularity);

    void updateName(int userID, String name);

    void updateCurrentLocation(int userID, String currentLocation);

    void updateAge(int userID, String age);

    void updateWork(int userID, String work);

    void updateEducation(int userID, String education);

    void updateVip(int userID, String isVip);

    void updateScore(int userID, String score);

    void updateAbout(int userID, String about);

    void updateHereTo(int userId, String hereTo);

    void updateIsMale(int userID, int isMale);

    void updateRelationship(int userID, int relationship);

    void updateKids(int userID, int kids);

    void updateCredits(int userID, int credits);

    void updatePopularity(int userID, int popularity);

    void updateSuperPower(int userID, int superpower);

    void updatePhoto(int userID, String photo);



    int getIdOnLoginData(PersonObject personObject);


    void saveLastId(int userID);


    void setSocialVK(SocialProfile profile,String socNetwork);
    void setSocialOK(SocialProfile profile,String socNetwork);
    void setSocialINSTA(SocialProfile profile,String socNetwork);
    void setSocialFB(SocialProfile profile,String socNetwork);

    int loadLastId();
}


