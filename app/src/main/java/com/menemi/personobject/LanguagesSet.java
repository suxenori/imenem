package com.menemi.personobject;

import java.util.ArrayList;

/**
 * Created by Ui-Developer on 29.10.2016.
 */

public class LanguagesSet {
    private ArrayList<Language> languages = new ArrayList<>();
    private int id;
    public LanguagesSet(ArrayList<Language> languages) {
        this.languages = languages;
    }

    public ArrayList<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(ArrayList<Language> languages) {
        this.languages = languages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
