package com.menemi.personobject;

public class Interests
{
    public Interests(String interests, int interestId, int groupId)
    {
        this.interestId = interestId;
        this.interest = interests;
        this.groupId = groupId;

    }

    private int interestId;
    private int groupId;
    private String interest;
    private boolean selected = false;

    public int getInterestId()
    {
        return interestId;
    }

    public void setInterestId(int interestId)
    {
        this.interestId = interestId;
    }

    public String getInterest()
    {
        return interest;
    }

    public void setInterest(String interest)
    {
        this.interest = interest;
    }

    public int getGroupId()
    {
        return groupId;
    }

    public void setGroupId(int groupId)
    {
        this.groupId = groupId;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}



