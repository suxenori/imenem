package com.menemi.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.menemi.PersonPage;
import com.menemi.R;
import com.menemi.SearchCity;
import com.menemi.dbfactory.DBHandler;
import com.menemi.edit_personal_Info.GRadioGroup;
import com.menemi.filter.FilterObject;
import com.menemi.models.PlaceModel;
import com.menemi.personobject.PersonObject;
import com.menemi.utils.RangeSeekBar;

/**
 * Created by tester03 on 04.07.2016.
 */
public class FilterFragment extends Fragment {
    private int minAge;
    private int maxAge;
    private PersonObject personObject = null;
    private CheckBox chatCB;
    private CheckBox sexCB;
    private CheckBox findFriendsCB;
    private PlaceModel city = new PlaceModel("", "", "", "", "", "", "");
    private FilterObject filterObject;
    private PersonObject.InterestGender genderSelected;
    private PersonObject.UserStatus statusSelected;
    private TextView choisedPlaceTextView;
    private boolean isDraggedRangeBar = false;
    private FilterType filterType;
    private boolean isCanOpen;

    public void setCanOpen(boolean canOpen)
    {
        isCanOpen = canOpen;
    }

    public void setPersonObject(PersonObject personObject) {
        this.personObject = personObject;
    }

    View rootView = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.filter_layout, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }

        Log.d("FilterFragment", "FilterFragment");
       // blurBackground(isCanOpen);
        Button submitButton = (Button) rootView.findViewById(R.id.submitButton);
        Button cancelButton = (Button) rootView.findViewById(R.id.cancelButtonFilter);

        RadioButton onlineRadioButton = (RadioButton) rootView.findViewById(R.id.onlineRadioButton);
        RadioButton offlineRadioButton = (RadioButton) rootView.findViewById(R.id.offlineRadioButton);
        RadioButton anyNetworkStatusRadioButton = (RadioButton) rootView.findViewById(R.id.anyStatusRadioButton);

        GRadioGroup statusRadioGroup = new GRadioGroup(onlineRadioButton, offlineRadioButton,anyNetworkStatusRadioButton);
        statusRadioGroup.setCheckedRadioButton(personObject.getUserStatus().ordinal());
        statusSelected = personObject.getUserStatus();
        statusRadioGroup.setOnCheckChange((index)->{
            statusSelected = PersonObject.UserStatus.values()[index];
        });


        RadioButton manRadioButton = (RadioButton) rootView.findViewById(R.id.maleRadioButton);

        RadioButton womanRadioButton = (RadioButton) rootView.findViewById(R.id.femaleRadioButton);

        RadioButton anyGenderRadioButton = (RadioButton) rootView.findViewById(R.id.anyGenderRadioButton);


        GRadioGroup genderRadioGroup = new GRadioGroup(manRadioButton, womanRadioButton,anyGenderRadioButton);
        genderRadioGroup.setCheckedRadioButton(personObject.getInterestGender().ordinal());
        genderSelected = personObject.getInterestGender();
        genderRadioGroup.setOnCheckChange((index)->{
            genderSelected = PersonObject.InterestGender.values()[index];
        });

        RangeSeekBar seekBar = (RangeSeekBar) rootView.findViewById(R.id.rangeSeekBarTextColorWithCode);
        findFriendsCB = (CheckBox) rootView.findViewById(R.id.findFriendsCheckBox);
        chatCB = (CheckBox) rootView.findViewById(R.id.chatCheckBox);
        sexCB = (CheckBox) rootView.findViewById(R.id.sexCheckBox);
        choisedPlaceTextView = (TextView) rootView.findViewById(R.id.choisedPlace);

        if (personObject.getFilterObject().getPlaceModel()== null){
            choisedPlaceTextView.setText(R.string.nearby);
        } else {
            choisedPlaceTextView.setText(personObject.getFilterObject().getPlaceModel().getCityName());
        }
        RelativeLayout textViewContainer = (RelativeLayout) rootView.findViewById(R.id.spinnerContainer);
        TextView whereSearch = (TextView)rootView.findViewById(R.id.whereSearch);
        if (filterType == FilterType.FILTER_FROM_ENCOUNTERS){
            textViewContainer.setVisibility(View.INVISIBLE);
            whereSearch.setVisibility(View.INVISIBLE);
        }

        textViewContainer.setOnClickListener(view -> {
            SearchCity.setChoiseListener((placeModel, detailPlaceModel) -> {
                choisedPlaceTextView.setText(placeModel.getCityName());
                city = detailPlaceModel;
            });
            Intent openSearchCity = new Intent(getActivity(), SearchCity.class);
            startActivity(openSearchCity);
        });
        findFriendsCB.setOnClickListener(view -> {
            sexCB.setChecked(false);
            chatCB.setChecked(false);
            findFriendsCB.setChecked(true);
        });

        chatCB.setOnClickListener(view -> {
            findFriendsCB.setChecked(false);
            sexCB.setChecked(false);
            chatCB.setChecked(true);
        });

        sexCB.setOnClickListener(view -> {
            chatCB.setChecked(false);
            findFriendsCB.setChecked(false);
            sexCB.setChecked(true);
        });
        Log.d("FilterFragment", "personObject " + personObject.getInterestGender());
        Log.d("FilterFragment", "personObject " + personObject.getUserStatus());


        if (personObject.getiAmHereTo() == PersonObject.IamHereTo.CHAT) {
            chatCB.setChecked(true);
        }
        if (personObject.getiAmHereTo() == PersonObject.IamHereTo.DATE) {
            sexCB.setChecked(true);
        }
        if (personObject.getiAmHereTo() == PersonObject.IamHereTo.MAKE_NEW_FRIEND) {
            findFriendsCB.setChecked(true);
        }

      //setCheckedGender();
//        setUserStatus();

        Log.d("FilterFragment", "personObject.getSearchAgeMin" + personObject.getSearchAgeMin());
        Log.d("FilterFragment", "personObject.getSearchAgeMax" + personObject.getSearchAgeMax());


        seekBar.setSelectedMinValue(personObject.getSearchAgeMin());
        seekBar.setSelectedMaxValue(personObject.getSearchAgeMax());
        TextView minValue = (TextView) rootView.findViewById(R.id.minValue);
        TextView maxValue = (TextView) rootView.findViewById(R.id.maxValue);
        minValue.setText("" + (personObject.getSearchAgeMin()));
        maxValue.setText("" + (personObject.getSearchAgeMax()));


        seekBar.setOnRangeSeekBarChangeListener(new SeekBarListener());
        RelativeLayout shadow = (RelativeLayout) rootView.findViewById(R.id.layoutShadow);
        shadow.setOnClickListener(v -> {
            PersonPage.isFilterVisible = false;
            ViewGroup parent = (ViewGroup) rootView.getParent();
            parent.removeView(rootView);
            parent.removeAllViews();
        });
        seekBar.setRangeValues(0, 100);


        submitButton.setOnClickListener(view -> {
         // /(:locale_handle)/settings/savefilterinfo/(:i_want_val)/(:im_interested_val)/(:im_interested_status_val)/(:min_age)/(:max_age)/(:center_coord)/(:edge_coord)/(:requesting_profile_id)
            PersonPage.isFilterVisible = false;
            if (!isDraggedRangeBar){
                minAge = personObject.getSearchAgeMin();
                maxAge = personObject.getSearchAgeMax();
            }
            filterObject = new FilterObject(genderSelected.ordinal(), getIAmHereTo().ordinal(), statusSelected.ordinal(), minAge, maxAge, DBHandler.getInstance().getUserId(),city);
            personObject.setFilterObject(filterObject);

            DBHandler.getInstance().uploadFilterSettings(filterObject, object -> {

            });

            ViewGroup parent = (ViewGroup) rootView.getParent();
            parent.removeView(rootView);
            parent.removeAllViews();

        });

        cancelButton.setOnClickListener(view -> {
            PersonPage.isFilterVisible = false;
            ViewGroup parent = (ViewGroup) rootView.getParent();
            parent.removeView(rootView);
            parent.removeAllViews();
        });


        return rootView;
    }
    private PersonObject.IamHereTo getIAmHereTo() {
        if (findFriendsCB.isChecked()) {
            return PersonObject.IamHereTo.MAKE_NEW_FRIEND;
        }
        if (chatCB.isChecked()) {
            return PersonObject.IamHereTo.CHAT;
        } else  {
            return PersonObject.IamHereTo.DATE;
        }

    }

    public void setFilterType(FilterType filterType)
    {
        this.filterType = filterType;
    }

    class SeekBarListener implements RangeSeekBar.OnRangeSeekBarChangeListener {
        @Override
        public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
            TextView minValueText = (TextView) rootView.findViewById(R.id.minValue);
            TextView maxValueText = (TextView) rootView.findViewById(R.id.maxValue);
            isDraggedRangeBar = true;
            maxAge = getMaxValue(maxValue);
            minAge = getMinValue(minValue);
            minValueText.setText("" + getMinValue(minValue));
            maxValueText.setText("" + getMaxValue(maxValue));


        }
    }

    public int getMinValue(Number minValue) {
        return ((int) minValue);
    }

    public int getMaxValue(Number maxValue) {
        return ((int) maxValue);
    }

    public enum FilterType{
        FILTER_FROM_ENCOUNTERS,
        FILTER_FROM_NEAR
    }
    public void blurBackground(boolean isCanOpen){
        RelativeLayout relativeLayout = (RelativeLayout)rootView.findViewById(R.id.layoutShadow);
        if (isCanOpen){
            ColorDrawable[] color = {new ColorDrawable(R.color.no_color), new ColorDrawable(R.color.filterShadow_pressed)};
            TransitionDrawable trans = new TransitionDrawable(color);
            relativeLayout.setBackground(trans);
            trans.startTransition(300);
        } else {
            ColorDrawable[] color = {new ColorDrawable(R.color.filterShadow_pressed), new ColorDrawable(R.color.no_color)};
            TransitionDrawable trans = new TransitionDrawable(color);
            relativeLayout.setBackground(trans);
            trans.reverseTransition(10);
        }
    }


}
