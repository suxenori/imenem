package com.menemi.edit_personal_Info;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.menemi.R;
import com.menemi.customviews.LanguagesChooseDialog;
import com.menemi.dbfactory.DBHandler;
import com.menemi.fragments.IntPickerDialog;
import com.menemi.fragments.ListRadioButtonDialog;
import com.menemi.personobject.Language;
import com.menemi.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by tester03 on 19.08.2016.
 */
public class EditPersonalInfo extends AppCompatActivity
{
    public static final String SCROLL_DOWN_TAG = "ScrollDown";
    private static Toolbar toolbar;


    private GRadioGroup relationshipRadioGroup;
    private GRadioGroup orientationRadioGroup;
    private GRadioGroup childRadioGroup;
    private GRadioGroup alcoRadioGraoup;
    private TextView heightTextView;
    private TextView weightTextView;
    private TextView bodyTypeTextView;
    private TextView eyeColorTextView;
    private TextView hairColorTextView;
    private TextView livigWithTextView;
    private TextView smokingTextView;
    private PersonalAppearanceSettingsModel personalAppearanceSettingsModelFromStorage;
    private PersonalAppearanceDataSource personalAppearanceDataSource;
    private ArrayList<Language> languages;
    private EditText editAboutPerson;
    private int minHeightValue = 135;
    private int maxHeightValue = 221;
    private int minWeightValue = 40;
    private int maxWeightValue = 151;

    public static Toolbar getToolbar()
    {
        return toolbar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(com.menemi.R.layout.edit_info_container);


        toolbar = (Toolbar) findViewById(com.menemi.R.id.toolbar_with_checkBox);

        smokingTextView = (TextView) findViewById(R.id.smokingTextView);
        livigWithTextView = (TextView) findViewById(R.id.livigWithTextView);
        bodyTypeTextView = (TextView) findViewById(R.id.bodyTypeTextView);
        eyeColorTextView = (TextView) findViewById(R.id.eyeColorTextView);
        hairColorTextView = (TextView) findViewById(R.id.hair_colorTextview);
        weightTextView = (TextView) findViewById(R.id.weightTextView);
        heightTextView = (TextView) findViewById(R.id.heightTextView);

        setSupportActionBar(toolbar);
        configureToolbar();

        editAboutPerson = (EditText)findViewById(com.menemi.R.id.about_edit_text);
        RelativeLayout heightPicker = (RelativeLayout)findViewById(com.menemi.R.id.height);
        RelativeLayout weightPicker = (RelativeLayout)findViewById(R.id.weight);
        RelativeLayout bodyType = (RelativeLayout) findViewById(com.menemi.R.id.bodyType);
        RelativeLayout eyeColor = (RelativeLayout) findViewById(com.menemi.R.id.eye_color);
        RelativeLayout hairColor = (RelativeLayout) findViewById(com.menemi.R.id.hair_color);
        RelativeLayout liveWith = (RelativeLayout) findViewById(com.menemi.R.id.live_in);
        RelativeLayout smoking = (RelativeLayout) findViewById(com.menemi.R.id.smoking);


        relationshipRadioGroup = new GRadioGroup(findViewById(R.id.relationshipRadioGroup),R.id.empty,R.id.alone,R.id.in_relationship,R.id.in_free_relationship);
        orientationRadioGroup = new GRadioGroup(findViewById(R.id.orientationRadioGroup),R.id.emptyOrientationRB,R.id.gayOrientedRB,R.id.freeOrientedRB,R.id.geteroOrientedRB,R.id.biOrientedRB);
        childRadioGroup = new GRadioGroup(findViewById(R.id.kidsRadioGroup),R.id.emptyChildRB,R.id.neverChildRB,R.id.oldChildRB,R.id.alredyHaveRB,R.id.nextTimeRB);
        alcoRadioGraoup = new GRadioGroup(findViewById(R.id.alcoRadioGroup),R.id.emptyAlco,R.id.neverAlcoRB,R.id.readyToAlcoRB,R.id.noAlcoRB,R.id.inCompanyAlco);

        personalAppearanceDataSource = new PersonalAppearanceDataSource(getApplicationContext());
        if (DBHandler.getInstance().getMyProfile().getPersonalAppearance() != null)
        {
            personalAppearanceSettingsModelFromStorage = DBHandler.getInstance().getMyProfile().getPersonalAppearance();
            setPersonalAppearanceCurrentStateInUI(personalAppearanceSettingsModelFromStorage,personalAppearanceDataSource);
        } else {

            personalAppearanceSettingsModelFromStorage = new PersonalAppearanceSettingsModel();
        }




        //Change bodyType params
        ListRadioButtonDialog bodyTypeDialog = new ListRadioButtonDialog();
        bodyType.setOnClickListener(view -> {
            bodyTypeDialog.setEditDialogListener(new EditDialogBodyType());
            bodyTypeDialog.setItems(personalAppearanceDataSource.getBodyType());
            bodyTypeDialog.setSelectedRadioButton(personalAppearanceSettingsModelFromStorage.getBodyTypeIndex());
            bodyTypeDialog.setTitle(getString(R.string.body_type));
            bodyTypeDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
            bodyTypeDialog.show(getSupportFragmentManager(),"bt");
        });

        //Change person parameter height
        heightPicker.setOnClickListener(view -> {
            IntPickerDialog intPickerDialog = new IntPickerDialog();

            intPickerDialog.setMaxValue(maxHeightValue);
            intPickerDialog.setMinValue(minHeightValue);
            intPickerDialog.setUnit(getString(R.string.unit_sm));
            intPickerDialog.setCurrentValue(personalAppearanceSettingsModelFromStorage.getHeight());
            intPickerDialog.setEditDialogListener(new EditDialogHeight());
            intPickerDialog.show(getSupportFragmentManager(),"height");
        });

        //Change person parameter weight
        weightPicker.setOnClickListener(v -> {
            IntPickerDialog intPickerDialog = new IntPickerDialog();
            intPickerDialog.setMaxValue(maxWeightValue);
            intPickerDialog.setMinValue(minWeightValue);
            intPickerDialog.setUnit(getString(R.string.unit_kg));
            intPickerDialog.setCurrentValue(personalAppearanceSettingsModelFromStorage.getWeigt());
            intPickerDialog.setEditDialogListener(new EditDialogWeight());
            intPickerDialog.show(getSupportFragmentManager(),"weight");

        });

        //Change eye color
        ListRadioButtonDialog eyeColorDialog = new ListRadioButtonDialog();
        eyeColor.setOnClickListener(view -> {
            eyeColorDialog.setEditDialogListener(new EditDialogEyeColor());
            eyeColorDialog.setItems(personalAppearanceDataSource.getEyeColor());
            eyeColorDialog.setSelectedRadioButton(personalAppearanceSettingsModelFromStorage.getEyeColorIndex());
            eyeColorDialog.setTitle(getString(R.string.eye_color));
            eyeColorDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
            eyeColorDialog.show(getSupportFragmentManager(),"eye color");
        });


        // change hair color
        ListRadioButtonDialog hairColorDialog = new ListRadioButtonDialog();
        hairColor.setOnClickListener(view -> {
            hairColorDialog.setEditDialogListener(new EditDialogHairColor());
            hairColorDialog.setItems(personalAppearanceDataSource.getHairColor());
            hairColorDialog.setSelectedRadioButton(personalAppearanceSettingsModelFromStorage.getHairColorIndex());
            hairColorDialog.setTitle("Цвет волос");
            hairColorDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
            hairColorDialog.show(getSupportFragmentManager(),"hair color");
        });

        // change living place
        ListRadioButtonDialog liveWithDialog = new ListRadioButtonDialog();
        liveWith.setOnClickListener(view -> {
            liveWithDialog.setEditDialogListener(new EditDialogLivingWith());
            liveWithDialog.setItems(personalAppearanceDataSource.getLivingWith());
            liveWithDialog.setSelectedRadioButton(personalAppearanceSettingsModelFromStorage.getLivingWithIndex());
            liveWithDialog.setTitle(getString(R.string.living_with));
            liveWithDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
            liveWithDialog.show(getSupportFragmentManager(),"living_with");
        });
        ListRadioButtonDialog smokingDialog = new ListRadioButtonDialog();
        smoking.setOnClickListener(view -> {
            smokingDialog.setEditDialogListener(new EditDialogSmoking());
            smokingDialog.setItems(personalAppearanceDataSource.getSmoking());
            smokingDialog.setSelectedRadioButton(personalAppearanceSettingsModelFromStorage.getSmokingIndex());
            smokingDialog.setTitle(getString(R.string.smoking));
            smokingDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
            smokingDialog.show(getSupportFragmentManager(),"living_with");
        });
        try {
            languages = cloneList(DBHandler.getInstance().getMyProfile().getPersonLanguages());
        } catch (CloneNotSupportedException cnse){
            return;
        }
        prepareLanguages();
        if( getIntent().getExtras().getBoolean(SCROLL_DOWN_TAG, false)){
            ScrollView scrollContainer = (ScrollView) findViewById(R.id.scrollContainer);
            scrollContainer.post(new Runnable() {
                @Override
                public void run() {
                    scrollContainer.fullScroll(View.FOCUS_DOWN);
                }
            });

        }
    }
    private void prepareLanguages(){

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout languagesContainer = (LinearLayout) findViewById(R.id.languagesContainer);
        languagesContainer.removeAllViews();
        for (int i = 0; i < languages.size(); i++) {
            View languageViewItem = inflater.inflate(R.layout.language_item,null);
            CheckBox languageCheckBox = (CheckBox) languageViewItem.findViewById(R.id.languageCheckBox);
            languageCheckBox.setChecked(true);
            int finalI = i;
            languageCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                Language language = languages.get(finalI);
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        languages.add(language);
                    } else {
                        languages.remove(language);

                    }
                }
            });

            TextView languageLevel = (TextView) languageViewItem.findViewById(R.id.languageLevel);
            languageLevel.setText(getResources().getStringArray(R.array.language_level)[languages.get(i).getLanguageLevel()]);

            TextView languageName = (TextView) languageViewItem.findViewById(R.id.languageName);
            languageName.setText(languages.get(i).getLanguageName());
            languagesContainer.addView(languageViewItem);

            ListRadioButtonDialog languageDialog = new ListRadioButtonDialog();

            languageViewItem.setOnClickListener(view -> {

                languageDialog.setItems(Arrays.asList(getResources().getStringArray(R.array.language_level)));
                languageDialog.setSelectedRadioButton(languages.get(finalI).getLanguageLevel());
                languageDialog.setTitle(getString(R.string.chooseLanguageLevel));
                languageDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                languageDialog.show(getSupportFragmentManager(),"living_with");

                languageDialog.setEditDialogListener(new EditLanguageListener(languageLevel,finalI));
            });

        }

        LinearLayout otherLanguagesButton = (LinearLayout) findViewById(R.id.otherLanguagesButton);
        otherLanguagesButton.setOnClickListener((v) ->{
            Log.d("LANGUAGES", "click");
            LanguagesChooseDialog languagesChooseDialog = new LanguagesChooseDialog();
            languagesChooseDialog.setItems(DBHandler.getInstance().getAllLanguages());
            languagesChooseDialog.setChoosenItems(languages);
            languagesChooseDialog.setTitle(getString(R.string.title_languages));
            languagesChooseDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
            languagesChooseDialog.show(getSupportFragmentManager(),"living_with");
            languagesChooseDialog.setEditDialogListener(new EditLanguageListener2());

          //  languagesChooseDialog.setEditDialogListener(new EditLanguageListener(languageLevel,finalI));
        });




    }
    public static ArrayList<Language> cloneList(ArrayList<Language> list) throws CloneNotSupportedException {
        ArrayList<Language> clone = new ArrayList<>(list.size());
        for (Language item : list) {
            clone.add(item.clone());
        }
        return clone;
    }
    public void setPersonalAppearanceCurrentStateInUI(PersonalAppearanceSettingsModel personalAppearanceSettingsModel, PersonalAppearanceDataSource personalAppearanceDataSource)
    {
        if (personalAppearanceSettingsModel.getHeight() < minHeightValue){
            heightTextView.setText(getString(R.string.less, minHeightValue + ""));
        } else if (personalAppearanceSettingsModel.getHeight() > maxHeightValue){
            heightTextView.setText(getString(R.string.more, maxHeightValue + ""));
        } else {
            heightTextView.setText(getString(R.string.sm,String.valueOf(personalAppearanceSettingsModel.getHeight())));
        }

        if (personalAppearanceSettingsModel.getWeigt() < minWeightValue){
            weightTextView.setText(getString(R.string.less_kg, minWeightValue + ""));
        } else if (personalAppearanceSettingsModel.getHeight() > maxWeightValue){
            weightTextView.setText(getString(R.string.more_kg, maxWeightValue + ""));
        } else {
            weightTextView.setText(getString(R.string.kg,String.valueOf(personalAppearanceSettingsModel.getWeigt())));
        }


        editAboutPerson.setText(personalAppearanceSettingsModelFromStorage.getAbout());
        relationshipRadioGroup.setCheckedRadioButton(personalAppearanceSettingsModel.getRelationshipIndex());
        orientationRadioGroup.setCheckedRadioButton(personalAppearanceSettingsModel.getSexualityIndex());
        childRadioGroup.setCheckedRadioButton(personalAppearanceSettingsModel.getKidsIndex());
        alcoRadioGraoup.setCheckedRadioButton(personalAppearanceSettingsModel.getAlcoholIndex());
        bodyTypeTextView.setText(personalAppearanceDataSource.getBodyType(personalAppearanceSettingsModel.getBodyTypeIndex()));
        eyeColorTextView.setText(personalAppearanceDataSource.getEyeColor(personalAppearanceSettingsModel.getEyeColorIndex()));
        hairColorTextView.setText(personalAppearanceDataSource.getHairColor(personalAppearanceSettingsModel.getHairColorIndex()));
        livigWithTextView.setText(personalAppearanceDataSource.getLivingWith(personalAppearanceSettingsModel.getLivingWithIndex()));
        smokingTextView.setText(personalAppearanceDataSource.getSmoking(personalAppearanceSettingsModel.getSmokingIndex()));
    }
    private void configureToolbar()
    {
        Toolbar toolbar = EditPersonalInfo.getToolbar();
        LinearLayout toolbarContainer = (LinearLayout) toolbar.findViewById(com.menemi.R.id.toolbarContainer);
        toolbarContainer.removeAllViews();
        toolbarContainer.addView(View.inflate(this, com.menemi.R.layout.choice_interest_toolbar, null));
       ImageButton submitButton = (ImageButton) toolbarContainer.findViewById(com.menemi.R.id.submButton);
        submitButton.setImageResource(R.drawable.check_on);
        submitButton.setOnClickListener(view -> {
            ProgressDialog progressDialog =  Utils.startLodingProgress(this, getString(R.string.update_info), (dialog)-> {});
            personalAppearanceSettingsModelFromStorage.setRelationshipIndex(relationshipRadioGroup.getIndex());
            personalAppearanceSettingsModelFromStorage.setKidsIndex(childRadioGroup.getIndex());
            personalAppearanceSettingsModelFromStorage.setSexualityIndex(orientationRadioGroup.getIndex());
            personalAppearanceSettingsModelFromStorage.setAlcoholIndex(alcoRadioGraoup.getIndex());
            personalAppearanceSettingsModelFromStorage.setAbout(editAboutPerson.getText().toString());
            Log.d("",personalAppearanceSettingsModelFromStorage + "");
            DBHandler.getInstance().setLanguages(languages, obj -> {
            DBHandler.getInstance().setAppearance(personalAppearanceSettingsModelFromStorage, object -> {
                boolean isUploaded = (boolean) object;
                if (!isUploaded){
                    Toast.makeText(getApplicationContext(), R.string.update_info_failed,Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.info_is_updated,Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
                finish();


            });});


        });

        ImageView menuButton = (ImageView) toolbarContainer.findViewById(com.menemi.R.id.menuButton);
        menuButton.setOnClickListener(view -> finish());

        TextView title = (TextView) toolbarContainer.findViewById(com.menemi.R.id.screenTitle);
        title.setText(getString(com.menemi.R.string.edit_personal_info));
    }


    class EditDialogHeight implements IntPickerDialog.EditDialogListener
    {
    @Override
    public void updateResult(String inputText)
    {

        if (personalAppearanceSettingsModelFromStorage != null)
        {
            if (Integer.parseInt(inputText) < minHeightValue){
                heightTextView.setText(getString(R.string.less, minHeightValue + ""));
                personalAppearanceSettingsModelFromStorage.setHeight(Integer.parseInt(inputText));

            } else if (Integer.parseInt(inputText) >= maxHeightValue) {
                heightTextView.setText(getString(R.string.more, maxHeightValue - 1 + ""));
                personalAppearanceSettingsModelFromStorage.setHeight(Integer.parseInt(inputText));
            } else {
                personalAppearanceSettingsModelFromStorage.setHeight(Integer.parseInt(inputText));
                heightTextView.setText(getString(R.string.sm,inputText));
            }

        }

    }
}


    class EditDialogWeight implements IntPickerDialog.EditDialogListener
    {
        @Override
        public void updateResult(String inputText)
        {

            if (personalAppearanceSettingsModelFromStorage != null)
            {
                if (Integer.parseInt(inputText) < minWeightValue){
                    weightTextView.setText(getString(R.string.less_kg, minWeightValue + ""));
                    personalAppearanceSettingsModelFromStorage.setWeigt(Integer.parseInt(inputText));

                } else if (Integer.parseInt(inputText) >= maxWeightValue) {
                    weightTextView.setText(getString(R.string.more_kg, maxWeightValue - 1 + ""));
                    personalAppearanceSettingsModelFromStorage.setWeigt(Integer.parseInt(inputText));
                } else {
                    personalAppearanceSettingsModelFromStorage.setWeigt(Integer.parseInt(inputText));
                    weightTextView.setText(getString(R.string.kg,inputText));
                }
            }

        }
    }

    class EditDialogBodyType implements ListRadioButtonDialog.EditDialogListener
    {
        @Override
        public void updateResult(String inputText, int index)
        {
            bodyTypeTextView.setText(inputText);
            if (personalAppearanceSettingsModelFromStorage != null)
            {
                personalAppearanceSettingsModelFromStorage.setBodyTypeIndex(index);
            }
        }
    }

    class EditDialogEyeColor implements ListRadioButtonDialog.EditDialogListener
    {
        @Override
        public void updateResult(String inputText, int index)
        {

            eyeColorTextView.setText(inputText);
            if (personalAppearanceSettingsModelFromStorage != null)
            {
                personalAppearanceSettingsModelFromStorage.setEyeColorIndex(index);
            }
        }
    }

    class EditDialogHairColor implements ListRadioButtonDialog.EditDialogListener
    {
        @Override
        public void updateResult(String inputText, int index)
        {

            hairColorTextView.setText(inputText);
            if (personalAppearanceSettingsModelFromStorage != null)
            {
                personalAppearanceSettingsModelFromStorage.setHairColorIndex(index);
            }
        }
    }

    class EditDialogLivingWith implements ListRadioButtonDialog.EditDialogListener
    {
        @Override
        public void updateResult(String inputText, int index)
        {

            livigWithTextView.setText(inputText);
            if (personalAppearanceSettingsModelFromStorage != null)
            {
                personalAppearanceSettingsModelFromStorage.setLivingWithIndex(index);
            }
        }
    }

    class EditDialogSmoking implements ListRadioButtonDialog.EditDialogListener
    {
        @Override
        public void updateResult(String inputText, int index)
        {
            smokingTextView.setText(inputText);
            if (personalAppearanceSettingsModelFromStorage != null)
            {
                personalAppearanceSettingsModelFromStorage.setSmokingIndex(index);
            }
        }
    }

    public class EditLanguageListener implements ListRadioButtonDialog.EditDialogListener
    {
        TextView languageLevel;
        int languageIndex;
        public EditLanguageListener(TextView languageLevel, int languageIndex) {
            this.languageLevel = languageLevel;
            this.languageIndex = languageIndex;
        }

        @Override
        public void updateResult(String inputText, int index)
        {

            languageLevel.setText(getResources().getStringArray(R.array.language_level)[index]);
            languages.get(languageIndex).setLanguageLevel(index);
        }
    }
    class EditLanguageListener2 implements ListRadioButtonDialog.EditDialogListener
    {
        @Override
        public void updateResult(String inputText, int index)
        {
            prepareLanguages();
        }
    }

}
