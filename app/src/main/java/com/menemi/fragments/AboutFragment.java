package com.menemi.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.menemi.R;
import com.menemi.edit_personal_Info.EditPersonalInfo;
import com.menemi.edit_personal_Info.PersonalAppearanceDataSource;
import com.menemi.edit_personal_Info.PersonalAppearanceSettingsModel;
import com.menemi.personobject.PersonObject;

import java.util.Arrays;

/**
 * Created by Ui-Developer on 07.07.2016.
 */
public class AboutFragment extends Fragment{
    private View rootView = null;
    private PersonObject personObject = null;

    public void setPersonObject(PersonObject personObject) {
        this.personObject = personObject;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.about_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        TextView editButton = (TextView)rootView.findViewById(R.id.changeButton);
        LinearLayout[] containers = prepareContainers();
        TextView[] labels = prepareLabels();
        TextView[] dataFields = prepareDataFields();
        String[] data = prepareData();
        prepareDataOnScreen(containers, dataFields, data);
        if(purpose != PersonDataFragment.Purpose.MY_PROFILE){
            TextView changeButton = (TextView)rootView.findViewById(R.id.changeButton);
            changeButton.setText("");
        }

        editButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent openChangeInfo = new Intent(getActivity(), EditPersonalInfo.class);
                openChangeInfo.putExtra(EditPersonalInfo.SCROLL_DOWN_TAG, false);
                startActivity(openChangeInfo);
            }
        });
        return rootView;
    }

    private LinearLayout[] prepareContainers() {
        LinearLayout[] containers = new LinearLayout[6];

        containers[0] =  (LinearLayout) rootView.findViewById(R.id.containerForField0);
        containers[1] =  (LinearLayout) rootView.findViewById(R.id.containerForField1);
        containers[2] =  (LinearLayout) rootView.findViewById(R.id.containerForField2);
        containers[3] =  (LinearLayout) rootView.findViewById(R.id.containerForField3);
        containers[4] =  (LinearLayout) rootView.findViewById(R.id.containerForField4);
        containers[5] =  (LinearLayout) rootView.findViewById(R.id.containerForField5);

        return containers;
    }

    private void prepareDataOnScreen(LinearLayout[] containers, TextView[] dataFields, String[] data){
        for (int i = 0; i < containers.length; i++) {
            Log.d("AboutFragment", "data = " + data[i]);
            if(data[i] != null && !data[i].equals("") ){
                dataFields[i].setText(data[i]);
            } else{
                containers[i].removeAllViews();
            }
        }

    }

    private String[] prepareData(){
        String[] data = new String[6];
        PersonalAppearanceDataSource appearanceDataSource = new PersonalAppearanceDataSource(getActivity());

        PersonalAppearanceSettingsModel appearance = personObject.getPersonalAppearance();
        appearanceDataSource.getBodyType(appearance.getBodyTypeIndex());
        ;
        String appearanceString = (appearance.getHairColorIndex() != 0 ? appearanceDataSource.getHairColor(appearance.getHairColorIndex())  + ", " : "") + (appearance.getEyeColorIndex() != 0 ? appearanceDataSource.getEyeColor(appearance.getEyeColorIndex())  + ", " : "") + (appearance.getBodyTypeIndex() != 0 ? appearanceDataSource.getBodyType(appearance.getBodyTypeIndex())  + ", " : "") + (appearance.getHeight() != 0? getString(R.string.sm, "" + appearance.getHeight())+", " : "") + (appearance.getWeigt() != 0?   getString(R.string.kg, ""+appearance.getWeigt())+", " : "");
        data[0] = appearance.getAbout();
        data[1] = Arrays.asList(getResources().getStringArray(R.array.relationship)).get(appearance.getRelationshipIndex());
        data[2] =  Arrays.asList(getResources().getStringArray(R.array.orientation)).get(appearance.getSexualityIndex());
        data[3] = appearanceString;
        data[4] = appearanceDataSource.getSmoking(appearance.getSmokingIndex());
        data[5] = Arrays.asList(getResources().getStringArray(R.array.alco)).get(appearance.getAlcoholIndex());
        return data;
    }
    private TextView[] prepareLabels(){
        TextView[] labels = new TextView[6];
        labels[0] = (TextView) rootView.findViewById(R.id.mainText0);
        labels[1] = (TextView) rootView.findViewById(R.id.mainText1);
        labels[2] = (TextView) rootView.findViewById(R.id.mainText2);
        labels[3] = (TextView) rootView.findViewById(R.id.mainText3);
        labels[4] = (TextView) rootView.findViewById(R.id.mainText4);
        labels[5] = (TextView) rootView.findViewById(R.id.mainText5);

        return labels;
    }
    private TextView[] prepareDataFields(){
        TextView[] dataFields = new TextView[6];
        dataFields[0] = (TextView) rootView.findViewById(R.id.dataText0);
        dataFields[1] = (TextView) rootView.findViewById(R.id.dataText1);
        dataFields[2] = (TextView) rootView.findViewById(R.id.dataText2);
        dataFields[3] = (TextView) rootView.findViewById(R.id.dataText3);
        dataFields[4] = (TextView) rootView.findViewById(R.id.dataText4);
        dataFields[5] = (TextView) rootView.findViewById(R.id.dataText5);


        return dataFields;
    }
    private PersonDataFragment.Purpose purpose = PersonDataFragment.Purpose.LIKE;
    public void setPurpose(PersonDataFragment.Purpose purpose) {
        this.purpose = purpose;
    }
}
