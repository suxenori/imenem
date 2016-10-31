package com.menemi.edit_personal_Info;


import android.content.Context;

import com.menemi.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tester03 on 26.10.2016.
 */

public class PersonalAppearanceDataSource

{
    public PersonalAppearanceDataSource(Context context){
        this.context = context;
    }
    private  Context context;

    public String getBodyType(int index)
    {

        List<String> bodyType = Arrays.asList(context.getResources().getStringArray(R.array.bodyType));

        return bodyType.get(index);
    }

    public List<String> getBodyType()
    {

        List<String> bodyType =  Arrays.asList(context.getResources().getStringArray(R.array.bodyType));

        return bodyType;
    }

    public String getEyeColor(int index)
    {
        List<String> eyeColor = Arrays.asList(context.getResources().getStringArray(R.array.eyeColor));
        return eyeColor.get(index);
    }

    public List<String> getEyeColor()
    {
        List<String> eyeColor = Arrays.asList(context.getResources().getStringArray(R.array.eyeColor));
        return eyeColor;
    }

    public String getHairColor(int index)
    {
        List<String> hairColor = Arrays.asList(context.getResources().getStringArray(R.array.hairColor));
        return hairColor.get(index);
    }

    public List<String> getHairColor()
    {
        List<String> hairColor = Arrays.asList(context.getResources().getStringArray(R.array.hairColor));
        return hairColor;
    }

    public String getLivingWith(int index)
    {
        List<String> livingWith = Arrays.asList(context.getResources().getStringArray(R.array.livingWith));
        return livingWith.get(index);
    }

    public List<String> getLivingWith()
    {
        List<String> livingWith = Arrays.asList(context.getResources().getStringArray(R.array.livingWith));
        return livingWith;
    }

    public String getSmoking(int index)
    {
        List<String> smoking = Arrays.asList(context.getResources().getStringArray(R.array.smoking));
        return smoking.get(index);
    }

    public List<String> getSmoking()
    {
        List<String> smoking = Arrays.asList(context.getResources().getStringArray(R.array.smoking));
        return smoking;
    }


   /* public ArrayList<String> getItems()
    {
        return items;
    }

    ArrayList<String> items = new ArrayList<String>();
    items.add("Среднее");
    items.add("Пара лишних кило");
    items.add("Атлетическое");
    items.add("Крепкое");
    items.add("Стройное");
    items.add("Приятная полнота");*/
   /* private enum Relationship {
        WITHOUT_RELATIONSHIP_INFO,
        ALONE_RELATIONSHIP,
        IN_RELATIONSHIP,
        IN_FREE_RELATIONS
    }

    private enum Sexuality {
        WITHOUT_INFO,
        GAY,
        OPEN_MINDED,
        GETERO,
        BISEXUAL
    }

    private enum BodyType {
        WITHOUT_BODY_TYPE,
        AVERAGE_BODY_TYPE,
        FEW_EXTRA_KG,
        ATHLETIC_BODY_TYPE,
        STRONG_BODY_TYPE,
        SLIM_BODY_TYPE,
        OBESITY_BODY_TYPE
    }

    private enum EyeColor {
        WITHOUT_EYE_COLOR,
        BROWN_EYE_COLOR,
        GRAY_EYE_COLOR,
        GREEN_EYE_COLOR,
        BLUE_EYE_COLOR,
        LIGHT_BROWN_EYE_COLOR
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


    public enum Kids {
        WITHOUT_KIDS_PERSON,
        NEVER_MAKE_KIDS,
        SOMETIME_MAKE_KIDS,
        ALREADY_MADE_KIDS,
        ADULT_KIDS
    }

    public enum Smoking
    {
        WITHOUT_INFO_ABOUT_SMOKING,
        IS_SMOKING_PERSON,
        IS_NO_SMOKING_PERSON,
        ANTI_SMOKING_PERSON,
        SMOKING_IN_COMPANY_PERSON,
        SMOKE_ONE_AFTER_THE_OTHER_PERSON
    }

    public enum Alcohol {
        WITHOUT_INFO,
        DRINKING_PERSON,
        NO_DRINKING_PERSON,
        IN_COMPANY_DRINKING_PERSON,
        HIGHLY_DRINKING_PERSON
    }*/

}
