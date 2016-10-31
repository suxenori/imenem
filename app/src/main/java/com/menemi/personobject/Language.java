package com.menemi.personobject;

import com.menemi.dbfactory.DBHandler;

public class Language implements Cloneable
{
    private int id;
    private LanguageLevel languageLevel = LanguageLevel.WITHOUT_LEVEL;
    private int LanguagesId;
    private String languageName;

    public Language(String languageName, int languagesId) {
        this.languageName = languageName;
        this.LanguagesId = languagesId;
    }

    public Language(int languageID, int languageLevel)
    {
        this.LanguagesId = languageID;
        this.languageLevel = LanguageLevel.values()[languageLevel];
        this.languageName = DBHandler.getInstance().getLanguageName(languageID);
    }


    public enum LanguageLevel
    {
        WITHOUT_LEVEL,
        LOW,
        AVERAGE,
        FLUENT,
        NATIVE
    }



    public int  getLanguageLevel()
    {
        return languageLevel.ordinal();
    }

    public void setLanguageLevel(int languageLevel)
    {
        this.languageLevel = LanguageLevel.values()[languageLevel];
    }

    public void setLanguageLevel(LanguageLevel languageLevel) {
        this.languageLevel = languageLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public int getLanguagesId()
    {
        return LanguagesId;
    }

    public void setLanguagesId(int languagesId)
    {
        LanguagesId = languagesId;
    }

    @Override
    public Language clone() throws CloneNotSupportedException {
        Language clone = new Language(languageName, LanguagesId);
        clone.setLanguageLevel(languageLevel);
        return clone;
    }
}

