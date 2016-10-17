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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
public class FilterFragment extends Fragment
{
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
    private CheckBox sexCB ;
    private CheckBox findFriendsCB;
    private PlaceModel city  = new PlaceModel("","","","","","","");
    private FilterObject filterObject;

    private TextView choisedPlaceTextView;

    public FilterFragment()
    {
    }


    public void setPersonObject(PersonObject personObject)
    {
        this.personObject = personObject;
    }

    View rootView = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.filter_layout, container, false);
        } else
        {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
            {
                parent.removeView(rootView);
            }
        }
        Log.d("FilterFragment", "FilterFragment");
        Button submitButton = (Button) rootView.findViewById(R.id.submitButton);
        Button cancelButton = (Button) rootView.findViewById(R.id.cancelButtonFilter);
        RadioButton manRadioButton = (RadioButton) rootView.findViewById(R.id.maleRadioButton);
        RadioButton womanRadioButton = (RadioButton) rootView.findViewById(R.id.femaleRadioButton);

        RadioButton anyGenderRadioButton = (RadioButton) rootView.findViewById(R.id.anyGenderRadioButton);
        RadioButton onlineRadioButton = (RadioButton)rootView.findViewById(R.id.onlineRadioButton);
        RadioButton offlineRadioButton = (RadioButton)rootView.findViewById(R.id.offlineRadioButton);
        RadioButton anyNetworkStatusRadioButton = (RadioButton)rootView.findViewById(R.id.anyStatusRadioButton);
        RangeSeekBar seekBar = (RangeSeekBar) rootView.findViewById(R.id.rangeSeekBarTextColorWithCode);
        findFriendsCB = (CheckBox)rootView.findViewById(R.id.findFriendsCheckBox);
        chatCB = (CheckBox)rootView.findViewById(R.id.chatCheckBox);
        sexCB = (CheckBox)rootView.findViewById(R.id.sexCheckBox);
        choisedPlaceTextView = (TextView)rootView.findViewById(R.id.choisedPlace);
        RelativeLayout textViewContainer = (RelativeLayout)rootView.findViewById(R.id.spinnerContainer);
        textViewContainer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SearchCity.setChoiseListener(new SearchCity.ChoiceListener()
                {
                    @Override
                    public void changePlace(PlaceModel placeModel,PlaceModel detailPlaceModel)
                    {
                        choisedPlaceTextView.setText(placeModel.getCityName());
                        city = detailPlaceModel;



                    }
                });
                Intent openSearchCity = new Intent(getActivity(),SearchCity.class);
                startActivity(openSearchCity);
            }
        });
        findFriendsCB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                sexCB.setChecked(false);
                chatCB.setChecked(false);
                findFriendsCB.setChecked(true);
            }
        });

        chatCB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                findFriendsCB.setChecked(false);
                sexCB.setChecked(false);
                chatCB.setChecked(true);
            }
        });

        sexCB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                chatCB.setChecked(false);
                findFriendsCB.setChecked(false);
                sexCB.setChecked(true);
            }
        });
        Log.d("FilterFragment", "personObject" + personObject.getInterestGender());
        Log.d("FilterFragment", "personObject" + personObject.getInterestGender());


        if (personObject.getiAmHereTo() == PersonObject.IamHereTo.CHAT){
            chatCB.setChecked(true);
        }
        if (personObject.getiAmHereTo() == PersonObject.IamHereTo.DATE){
            sexCB.setChecked(true);
        }
        if (personObject.getiAmHereTo() == PersonObject.IamHereTo.MAKE_NEW_FRIEND){
            findFriendsCB.setChecked(true);
        }
        if (personObject.getInterestGender() == PersonObject.InterestGender.MAN)
        {
            manRadioButton.setChecked(true);
        }
        if (personObject.getInterestGender() == PersonObject.InterestGender.WOMAN)
        {
            womanRadioButton.setChecked(true);
        }
        if (personObject.getInterestGender() == PersonObject.InterestGender.ANY_GENDER)
        {
            anyGenderRadioButton.setChecked(true);
        }
        if (personObject.getUserStatus() == PersonObject.UserStatus.ONLINE)
        {
            onlineRadioButton.setChecked(true);
        }
        if (personObject.getUserStatus() == PersonObject.UserStatus.OFFLINE)
        {
            offlineRadioButton.setChecked(true);
        }
        if (personObject.getUserStatus() == PersonObject.UserStatus.ONLINE)
        {
            anyGenderRadioButton.setChecked(true);
        }

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
        shadow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                ViewGroup parent = (ViewGroup) rootView.getParent();
                parent.removeView(rootView);
                parent.removeAllViews();
            }
        });
        seekBar.setRangeValues(0, 100);


        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ///(:locale_handle)/settings/savefilterinfo/(:i_want_val)/(:im_interested_val)/(:min_age)/(:max_age)/(:online)/(:center_coord)/(:/edge_coord)/(:requesting_profile_id)
                filterObject = new FilterObject(getIAmHereTo(), getSelectedGender(), minAge, maxAge, DBHandler.getInstance().getUserId(),getOnlineStatus(),city);
                Log.d("FilterFragment", filterObject.toString());
                DBHandler.getInstance().uploadFilterSettings(filterObject, new DBHandler.ResultListener()

                {
                    @Override
                    public void onFinish(Object object)
                    {
                        personObject.setFilterObject(filterObject);
                    }
                });

                ViewGroup parent = (ViewGroup) rootView.getParent();
                parent.removeView(rootView);
                parent.removeAllViews();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ViewGroup parent = (ViewGroup) rootView.getParent();
                parent.removeView(rootView);
                parent.removeAllViews();
            }
        });


        return rootView;
    }

    private int getSelectedGender()
    {
        RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);

        if (radioGroup.getCheckedRadioButtonId() == R.id.maleRadioButton)
        {
            return MALE_VAR;
        }
        if (radioGroup.getCheckedRadioButtonId() == R.id.femaleRadioButton)
        {
            return FEMALE_VAR;
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.anyGenderRadioButton)
        {
            return ANY_GENDER_VAR;
        }
        return 0;
    }

    private int getOnlineStatus()
    {
        RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroupStatus);

        if (radioGroup.getCheckedRadioButtonId() == R.id.onlineRadioButton)
        {
            return ONLINE;
        }
        if (radioGroup.getCheckedRadioButtonId() == R.id.offlineRadioButton)
        {
            return OFLINE;
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.anyStatusRadioButton)
        {
            return ANY;
        }
        return 0;
    }

    private int getIAmHereTo(){
        if (findFriendsCB.isChecked()){
            return FIND_FRIENDS;
        }
        if (chatCB.isChecked()){
            return CHAT;
        }
        if (sexCB.isChecked()){
            return SEX;
        }
        return 0;
    }
    class SeekBarListener implements RangeSeekBar.OnRangeSeekBarChangeListener
    {
        @Override
        public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue)
        {
            TextView minValueText = (TextView) rootView.findViewById(R.id.minValue);
            TextView maxValueText = (TextView) rootView.findViewById(R.id.maxValue);
            maxAge = getMaxValue(maxValue);
            minAge = getMinValue(minValue);
            minValueText.setText("" + getMinValue(minValue));
            maxValueText.setText("" + getMaxValue(maxValue));


        }
    }

    public int getMinValue(Number minValue)
    {
        return ((int) minValue);
    }

    public int getMaxValue(Number maxValue)
    {
        return ((int) maxValue);
    }

}
