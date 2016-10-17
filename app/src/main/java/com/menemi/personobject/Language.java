package com.menemi.personobject;
public class Language
{
    public Language(int languageID, int languageLevel)
    {
        this.LanguagesId = languageID;
        this.languageLevel = LanguageLevel.values()[languageID];
    }


    public enum LanguageLevel
    {
        WITHOUT_LEVEL,
        LOW,
        AVERAGE,
        FLUENT,
        NATIVE
    }

    private LanguageLevel languageLevel;
    private int LanguagesId;

    public int  getLanguageLevel()
    {
        return languageLevel.ordinal();
    }

    public void setLanguageLevel(int languageLevel)
    {
        this.languageLevel = LanguageLevel.values()[languageLevel];
    }

    public int getLanguagesId()
    {
        return LanguagesId;
    }

    public void setLanguagesId(int languagesId)
    {
        LanguagesId = languagesId;
    }
}

