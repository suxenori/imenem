package com.menemi.personobject;

/**
 * Created by Ui-Developer on 26.07.2016.
 */
public class Reward {
    int personId; String rewardName;

    public Reward(int personId, String rewardName) {
        this.personId = personId;
        this.rewardName = rewardName;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }
}
