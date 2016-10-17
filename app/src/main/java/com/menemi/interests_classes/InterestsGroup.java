package com.menemi.interests_classes;

import android.graphics.Bitmap;

/**
 * Created by tester03 on 03.08.2016.
 */
public class InterestsGroup
{
    private int index;
    private String name;
    private Bitmap categoryIcon;

    public int getIndex()
    {
        return index;
    }

    public Bitmap getCategoryIcon()
    {
        return categoryIcon;
    }

    public void setCategoryIcon(Bitmap categoryIcon)
    {
        this.categoryIcon = categoryIcon;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public String getNameGroup()
    {
        return name;
    }

    public void setValue(String value)
    {
        this.name = value;
    }

    public InterestsGroup (int index, String value, Bitmap bitmap){
        this.index = index;
        this.name = value;
        this.categoryIcon = bitmap;
    }
}
