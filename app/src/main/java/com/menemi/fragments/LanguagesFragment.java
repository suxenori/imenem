package com.menemi.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.edit_personal_Info.EditPersonalInfo;
import com.menemi.personobject.Language;
import com.menemi.personobject.PersonObject;

import java.util.ArrayList;

/**
 * Created by Ui-Developer on 07.07.2016.
 */
public class LanguagesFragment extends Fragment{
    private View rootView = null;
    PersonObject personObject = null;

    public void setPersonObject(PersonObject personObject) {
        this.personObject = personObject;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(personObject.getPersonId() != DBHandler.getInstance().getMyProfile().getPersonId() && (personObject.getPersonLanguages() == null || personObject.getPersonLanguages().size() == 0) ){
            return null;
        }
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.languages_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        ArrayList<Language> languages = personObject.getPersonLanguages();
        String languageString = "";
        for (int i = 0; i < languages.size(); i++) {
            languageString += languages.get(i).getLanguageName();
            languageString += " (";

            languageString+= getResources().getStringArray(R.array.language_level)[languages.get(i).getLanguageLevel()];

            if (i<languages.size()-1){
            languageString += "), ";} else{
                languageString += ")";
            }
        }
        TextView languageList = (TextView)rootView.findViewById(R.id.languageList);
        languageList.setText(languageString);
        if(purpose != PersonDataFragment.Purpose.MY_PROFILE){

            TextView changeButton = (TextView)rootView.findViewById(R.id.changeButton);
            changeButton.setText("");
        }
        TextView changeButton = (TextView)rootView.findViewById(R.id.changeButton);
        changeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent openChangeInfo = new Intent(getActivity(), EditPersonalInfo.class);
                openChangeInfo.putExtra(EditPersonalInfo.SCROLL_DOWN_TAG, true);
                startActivity(openChangeInfo);
            }
        });
        return rootView;
    }

    private PersonDataFragment.Purpose purpose = PersonDataFragment.Purpose.LIKE;
    public void setPurpose(PersonDataFragment.Purpose purpose) {
        this.purpose = purpose;
    }
}
