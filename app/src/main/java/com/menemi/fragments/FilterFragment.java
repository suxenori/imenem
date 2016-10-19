package com.menemi.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.menemi.PersonPage;
import com.menemi.R;
import com.menemi.SearchCity;
import com.menemi.dbfactory.DBHandler;
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
    private final int MALE_VAR = 1;
    private final int FEMALE_VAR = 0;
    private final int ANY_GENDER_VAR = 2;
    private final int FIND_FRIENDS = 0;
    private final int CHAT = 1;
    private final int ONLINE = 1;
    private final int OFLINE = 2;
    private final int ANY = 0;
    private final int SEX = 2;
    private PersonObject personObject = null;
    private CheckBox chatCB;
    private CheckBox sexCB;
    private CheckBox findFriendsCB;
    private PlaceModel city = new PlaceModel("", "", "", "", "", "", "");
    private FilterObject filterObject;
    private PersonObject.InterestGender genderSelected;
    private PersonObject.UserStatus statusSelected;
    private TextView choisedPlaceTextView;



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
        Button submitButton = (Button) rootView.findViewById(R.id.submitButton);
        Button cancelButton = (Button) rootView.findViewById(R.id.cancelButtonFilter);


        ImageView manRadioButton = (ImageView) rootView.findViewById(R.id.maleRadioButton);
        manRadioButton.setOnClickListener((v) -> {
            setCheckedGender(PersonObject.InterestGender.MAN);
        });

        ImageView womanRadioButton = (ImageView) rootView.findViewById(R.id.femaleRadioButton);
        womanRadioButton.setOnClickListener((v) -> {
            setCheckedGender(PersonObject.InterestGender.WOMAN);
        });
        ImageView anyGenderRadioButton = (ImageView) rootView.findViewById(R.id.anyGenderRadioButton);
        anyGenderRadioButton.setOnClickListener((v) -> {
            setCheckedGender(PersonObject.InterestGender.ANY_GENDER);
        });
        ImageView onlineRadioButton = (ImageView) rootView.findViewById(R.id.onlineRadioButton);
        onlineRadioButton.setOnClickListener((v) -> {
            setUserStatus(PersonObject.UserStatus.ONLINE);
        });
        ImageView offlineRadioButton = (ImageView) rootView.findViewById(R.id.offlineRadioButton);
        offlineRadioButton.setOnClickListener((v) -> {
            setUserStatus(PersonObject.UserStatus.OFFLINE);
        });
        ImageView anyNetworkStatusRadioButton = (ImageView) rootView.findViewById(R.id.anyStatusRadioButton);
        anyNetworkStatusRadioButton.setOnClickListener((v) -> {
            setUserStatus(PersonObject.UserStatus.ANY);
        });


        RangeSeekBar seekBar = (RangeSeekBar) rootView.findViewById(R.id.rangeSeekBarTextColorWithCode);
        findFriendsCB = (CheckBox) rootView.findViewById(R.id.findFriendsCheckBox);
        chatCB = (CheckBox) rootView.findViewById(R.id.chatCheckBox);
        sexCB = (CheckBox) rootView.findViewById(R.id.sexCheckBox);
        choisedPlaceTextView = (TextView) rootView.findViewById(R.id.choisedPlace);

        RelativeLayout textViewContainer = (RelativeLayout) rootView.findViewById(R.id.spinnerContainer);
        textViewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchCity.setChoiseListener(new SearchCity.ChoiceListener() {
                    @Override
                    public void changePlace(PlaceModel placeModel, PlaceModel detailPlaceModel) {
                        choisedPlaceTextView.setText(placeModel.getCityName());
                        city = detailPlaceModel;


                    }
                });
                Intent openSearchCity = new Intent(getActivity(), SearchCity.class);
                startActivity(openSearchCity);
            }
        });
        findFriendsCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sexCB.setChecked(false);
                chatCB.setChecked(false);
                findFriendsCB.setChecked(true);
            }
        });

        chatCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findFriendsCB.setChecked(false);
                sexCB.setChecked(false);
                chatCB.setChecked(true);
            }
        });

        sexCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatCB.setChecked(false);
                findFriendsCB.setChecked(false);
                sexCB.setChecked(true);
            }
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

        setCheckedGender(personObject.getInterestGender());
        setUserStatus(personObject.getUserStatus());

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
        shadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonPage.isFilterVisible = false;
                ViewGroup parent = (ViewGroup) rootView.getParent();
                parent.removeView(rootView);
                parent.removeAllViews();
            }
        });
        seekBar.setRangeValues(0, 100);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///(:locale_handle)/settings/savefilterinfo/(:i_want_val)/(:im_interested_val)/(:min_age)/(:max_age)/(:online)/(:center_coord)/(:/edge_coord)/(:requesting_profile_id)
                PersonPage.isFilterVisible = false;
                filterObject = new FilterObject(genderSelected.ordinal(), getIAmHereTo().ordinal(), minAge, maxAge, DBHandler.getInstance().getUserId(), statusSelected.ordinal(), city);
                personObject.setFilterObject(filterObject);

                DBHandler.getInstance().uploadFilterSettings(filterObject, new DBHandler.ResultListener()

                {
                    @Override
                    public void onFinish(Object object) {

                    }
                });

                ViewGroup parent = (ViewGroup) rootView.getParent();
                parent.removeView(rootView);
                parent.removeAllViews();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonPage.isFilterVisible = false;
                ViewGroup parent = (ViewGroup) rootView.getParent();
                parent.removeView(rootView);
                parent.removeAllViews();
            }
        });


        return rootView;
    }

    private void setCheckedGender(PersonObject.InterestGender pos) {
        ImageView manRadioButton = (ImageView) rootView.findViewById(R.id.maleRadioButton);
        ImageView womanRadioButton = (ImageView) rootView.findViewById(R.id.femaleRadioButton);
        ImageView anyGenderRadioButton = (ImageView) rootView.findViewById(R.id.anyGenderRadioButton);
        genderSelected = pos;
        if (pos == PersonObject.InterestGender.MAN) {
            manRadioButton.setImageResource(R.drawable.all_on);
            womanRadioButton.setImageResource(R.drawable.all_of);
            anyGenderRadioButton.setImageResource(R.drawable.all_of);
        } else if (pos == PersonObject.InterestGender.WOMAN) {
            manRadioButton.setImageResource(R.drawable.all_of);
            womanRadioButton.setImageResource(R.drawable.all_on);
            anyGenderRadioButton.setImageResource(R.drawable.all_of);
        } else {
            manRadioButton.setImageResource(R.drawable.all_of);
            womanRadioButton.setImageResource(R.drawable.all_of);
            anyGenderRadioButton.setImageResource(R.drawable.all_on);
        }
    }

    private void setUserStatus(PersonObject.UserStatus pos) {
        ImageView onlineRadioButton = (ImageView) rootView.findViewById(R.id.onlineRadioButton);
        ImageView offlineRadioButton = (ImageView) rootView.findViewById(R.id.offlineRadioButton);
        ImageView anyNetworkStatusRadioButton = (ImageView) rootView.findViewById(R.id.anyStatusRadioButton);
        statusSelected = pos;
        if (pos == PersonObject.UserStatus.ONLINE) {
            onlineRadioButton.setImageResource(R.drawable.all_on);
            offlineRadioButton.setImageResource(R.drawable.all_of);
            anyNetworkStatusRadioButton.setImageResource(R.drawable.all_of);
        } else if (pos == PersonObject.UserStatus.OFFLINE) {
            onlineRadioButton.setImageResource(R.drawable.all_of);
            offlineRadioButton.setImageResource(R.drawable.all_on);
            anyNetworkStatusRadioButton.setImageResource(R.drawable.all_of);
        } else {
            onlineRadioButton.setImageResource(R.drawable.all_of);
            offlineRadioButton.setImageResource(R.drawable.all_of);
            anyNetworkStatusRadioButton.setImageResource(R.drawable.all_on);
        }
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

    class SeekBarListener implements RangeSeekBar.OnRangeSeekBarChangeListener {
        @Override
        public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
            TextView minValueText = (TextView) rootView.findViewById(R.id.minValue);
            TextView maxValueText = (TextView) rootView.findViewById(R.id.maxValue);
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

}
