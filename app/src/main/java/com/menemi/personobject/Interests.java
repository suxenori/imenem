package com.menemi.personobject;

public class Interests
{
    public Interests(String interests, int interestId, int groupId)
    {
        this.interestId = interestId;
        this.interest = interests;
        this.groupId = groupId;

    }

    public Interests(int interestId, String interest, String groupIconUrl, boolean isMutual) {
        this.interestId = interestId;
        this.interest = interest;
        this.groupIconUrl = groupIconUrl;
        this.isMutual = isMutual;
    }

    private int interestId;
    private int groupId;
    private String interest;
    private boolean selected = false;
    private String groupIconUrl;// : "http://minemi.ironexus.com/system/interest_groups/icon_mutuals/000/000/012/original/data?1477400224"
    private boolean isMutual;// : true

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

    public String getGroupIconUrl() {
        return groupIconUrl;
    }

    public boolean isMutual() {
        return isMutual;
    }
}



