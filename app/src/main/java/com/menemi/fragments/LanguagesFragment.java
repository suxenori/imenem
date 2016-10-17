package com.menemi.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.menemi.R;
import com.menemi.personobject.PersonObject;

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
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.languages_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        if(purpose != PersonDataFragment.Purpose.MY_PROFILE){
            TextView changeButton = (TextView)rootView.findViewById(R.id.changeButton);
            changeButton.setText("");
        }
        return rootView;
    }

    private PersonDataFragment.Purpose purpose = PersonDataFragment.Purpose.LIKE;
    public void setPurpose(PersonDataFragment.Purpose purpose) {
        this.purpose = purpose;
    }
}
