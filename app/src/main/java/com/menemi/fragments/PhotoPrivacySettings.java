package com.menemi.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.menemi.R;
import com.menemi.personobject.PhotoSetting;
import com.menemi.personobject.PhotoTemplate;

import java.util.ArrayList;

/**
 * Created by Ui-Developer on 07.07.2016.
 */
public class PhotoPrivacySettings extends Fragment implements TextWatcher {
    private View rootView = null;
    PhotoSetting photoSetting = null;
    ArrayList<PhotoTemplate> photoTemplates;
    public void setPhotoSetting(PhotoSetting photoSetting) {
        this.photoSetting = photoSetting;
    }

    public void setPhotoTemplates(ArrayList<PhotoTemplate> photoTemplates) {
        this.photoTemplates = photoTemplates;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.private_photo_setting_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }


        EditText setPriceCustom = (EditText) rootView.findViewById(R.id.setPriceCustom);
        setPriceCustom.addTextChangedListener(this);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        for (int i = 0; i < photoTemplates.size() + photoTemplates.size()%4; i+=4) {
            PhotoTemplateSet photoTemplateSet = new PhotoTemplateSet();
            photoTemplateSet.setPhotoTemplates(photoTemplates, i);
            photoTemplateSet.setOnArray(photoSetting.getTemplateIds());
            ft.add(R.id.templateContainer, photoTemplateSet);
        }
        ft.commit();
        prepare();

        return rootView;
    }
    private void prepare() {


        //LinearLayout templateContainer = (LinearLayout)rootView.findViewById(R.id.templateContainer);


        LinearLayout bkgr50 = (LinearLayout)rootView.findViewById(R.id.textBkgr50);
        LinearLayout bkgr100 = (LinearLayout)rootView.findViewById(R.id.textBkgr100);
        LinearLayout bkgr200 = (LinearLayout)rootView.findViewById(R.id.textBkgr200);

        Switch autoPriceSwitch = (Switch)rootView.findViewById(R.id.autoPriceSwitch);
        autoPriceSwitch.setOnCheckedChangeListener(new AutoPriseSwichListener());
        bkgr50.setOnClickListener(new TextClickListener());
        bkgr100.setOnClickListener(new TextClickListener());
        bkgr200.setOnClickListener(new TextClickListener());

        EditText setPriceCustom = (EditText) rootView.findViewById(R.id.setPriceCustom);
        switchOffAll();

        if (photoSetting.getPrice() == 50) {
            switchOn(1);
        } else if (photoSetting.getPrice() == 100) {
            switchOn(2);
        } else if (photoSetting.getPrice() == 200) {
            switchOn(3);
        } else {
            setPriceCustom.setText("" + photoSetting.getPrice());
        }
        if(photoSetting.isAutoprice()){

            autoPriceSwitch.setChecked(true);
            setPriceCustom.setText("");

        }

    }

    class AutoPriseSwichListener implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            photoSetting.setAutoprice(checked);
            EditText setPriceCustom = (EditText) rootView.findViewById(R.id.setPriceCustom);
            if(checked == true){
                switchOffAll();

                setPriceCustom.setText("");
            } else {
                prepare();
            }
        }
    }
private void switchOn(int priceButtonNumber){
    switchOffAll();
    TextView setPrice50 = (TextView) rootView.findViewById(R.id.setPrice50);
    TextView setPrice100 = (TextView) rootView.findViewById(R.id.setPrice100);
    TextView setPrice200 = (TextView) rootView.findViewById(R.id.setPrice200);
    LinearLayout bkgr50 = (LinearLayout)rootView.findViewById(R.id.textBkgr50);
    LinearLayout bkgr100 = (LinearLayout)rootView.findViewById(R.id.textBkgr100);
    LinearLayout bkgr200 = (LinearLayout)rootView.findViewById(R.id.textBkgr200);
    ImageView coins50 = (ImageView)rootView.findViewById(R.id.coins50);
    ImageView coins100 = (ImageView)rootView.findViewById(R.id.coins100);
    ImageView coins200 = (ImageView)rootView.findViewById(R.id.coins200);

    if(priceButtonNumber == 1){
        bkgr50.setBackgroundResource(R.drawable.price_button);
        setPrice50.setTextColor(getResources().getColor(R.color.actionbar_text));
        coins50.setImageResource(R.drawable.white_coins);
    } else if (priceButtonNumber == 2) {
        bkgr100.setBackgroundResource(R.drawable.price_button);
        setPrice100.setTextColor(getResources().getColor(R.color.actionbar_text));
        coins100.setImageResource(R.drawable.white_coins);
    } else if (priceButtonNumber == 3) {
        bkgr200.setBackgroundResource(R.drawable.price_button);
        setPrice200.setTextColor(getResources().getColor(R.color.actionbar_text));
        coins200.setImageResource(R.drawable.white_coins);
    }
}
private void switchOffAll(){

    TextView setPrice50 = (TextView) rootView.findViewById(R.id.setPrice50);
    TextView setPrice100 = (TextView) rootView.findViewById(R.id.setPrice100);
    TextView setPrice200 = (TextView) rootView.findViewById(R.id.setPrice200);
    LinearLayout bkgr50 = (LinearLayout)rootView.findViewById(R.id.textBkgr50);
    LinearLayout bkgr100 = (LinearLayout)rootView.findViewById(R.id.textBkgr100);
    LinearLayout bkgr200 = (LinearLayout)rootView.findViewById(R.id.textBkgr200);
    ImageView coins50 = (ImageView)rootView.findViewById(R.id.coins50);
    ImageView coins100 = (ImageView)rootView.findViewById(R.id.coins100);
    ImageView coins200 = (ImageView)rootView.findViewById(R.id.coins200);
    bkgr50.setBackgroundColor(getResources().getColor(R.color.actionbar_text));
    setPrice50.setTextColor(getResources().getColor(R.color.text_main));
    coins50.setImageResource(R.drawable.ic_coins);

    bkgr100.setBackgroundColor(getResources().getColor(R.color.actionbar_text));
    setPrice100.setTextColor(getResources().getColor(R.color.text_main));
    coins100.setImageResource(R.drawable.ic_coins);

    bkgr200.setBackgroundColor(getResources().getColor(R.color.actionbar_text));
    setPrice200.setTextColor(getResources().getColor(R.color.text_main));
    coins200.setImageResource(R.drawable.ic_coins);



}


    class TextClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            Switch autoPriceSwitch = (Switch)rootView.findViewById(R.id.autoPriceSwitch);
            autoPriceSwitch.setChecked(false);
            switchOffAll();
            EditText setPriceCustom = (EditText) rootView.findViewById(R.id.setPriceCustom);

            if (view.getId() == R.id.textBkgr50) {
                switchOn(1);
                setPriceCustom.setText("");
                photoSetting.setPrice(50);
            }
            if (view.getId() == R.id.textBkgr100) {
                switchOn(2);
                setPriceCustom.setText("");
                photoSetting.setPrice(100);
            }
            if (view.getId() == R.id.textBkgr200) {
                switchOn(3);
                setPriceCustom.setText("");
                photoSetting.setPrice(200);
            }
        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        EditText setPriceCustom = (EditText) rootView.findViewById(R.id.setPriceCustom);
        if (!setPriceCustom.getText().toString().equals("")) {
            photoSetting.setPrice(Integer.parseInt(setPriceCustom.getText().toString()));
          switchOffAll();
            Switch autoPriceSwitch = (Switch)rootView.findViewById(R.id.autoPriceSwitch);
            autoPriceSwitch.setChecked(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
