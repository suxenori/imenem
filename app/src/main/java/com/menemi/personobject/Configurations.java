package com.menemi.personobject;

/**
 * Created by Ui-Developer on 30.07.2016.
 */
public class Configurations {
    int id;
    int profileId;
    boolean showDistance = true;
    boolean hideOnlineStatus = true;
    boolean publicSearch = true;
    boolean showNearby = true;
    boolean limitProfile = false;
    boolean shareProfile = true;
    boolean findByEmail = true;
    boolean halfInvisible = false;
    boolean invisibleHeat = false;
    boolean hideVipStatus = false;
    boolean limitMessages = false;
    boolean hideMyVerifications = false;
    boolean hideProfileAsDeleted = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public boolean isShowDistance() {
        return showDistance;
    }

    public void setShowDistance(boolean showDistance) {
        this.showDistance = showDistance;
    }

    public boolean isHideOnlineStatus() {
        return hideOnlineStatus;
    }

    public void setHideOnlineStatus(boolean hideOnlineStatus) {
        this.hideOnlineStatus = hideOnlineStatus;
    }

    public boolean isPublicSearch() {
        return publicSearch;
    }

    public void setPublicSearch(boolean publicSearch) {
        this.publicSearch = publicSearch;
    }

    public boolean isShowNearby() {
        return showNearby;
    }

    public void setShowNearby(boolean showNearby) {
        this.showNearby = showNearby;
    }

    public boolean isLimitProfile() {
        return limitProfile;
    }

    public void setLimitProfile(boolean limitProfile) {
        this.limitProfile = limitProfile;
    }

    public boolean isShareProfile() {
        return shareProfile;
    }

    public void setShareProfile(boolean shareProfile) {
        this.shareProfile = shareProfile;
    }

    public boolean isFindByEmail() {
        return findByEmail;
    }

    public void setFindByEmail(boolean findByEmail) {
        this.findByEmail = findByEmail;
    }

    public boolean isAlmostInvisible() {
        return halfInvisible;
    }

    public void setAlmostInvisible(boolean halfInvisible) {
        this.halfInvisible = halfInvisible;
    }

    public boolean isInvisibleCloacked() {
        return invisibleHeat;
    }

    public void setInvisibleCloacked(boolean invisibleHeat) {
        this.invisibleHeat = invisibleHeat;
    }

    public boolean isHideVipStatus() {
        return hideVipStatus;
    }

    public void setHideVipStatus(boolean hideVipStatus) {
        this.hideVipStatus = hideVipStatus;
    }

    public boolean isLimitMessages() {
        return limitMessages;
    }

    public void setLimitMessages(boolean limitMessages) {
        this.limitMessages = limitMessages;
    }

    public boolean isHideMyVerifications() {
        return hideMyVerifications;
    }

    public void setHideMyVerifications(boolean hideMyVerifications) {
        this.hideMyVerifications = hideMyVerifications;
    }

    public boolean isHideProfileAsDeleted() {
        return hideProfileAsDeleted;
    }

    public void setHideProfileAsDeleted(boolean hideProfileAsDeleted) {
        this.hideProfileAsDeleted = hideProfileAsDeleted;
    }
}
