package com.menemi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.menemi.dbfactory.DBHandler;
import com.menemi.fragments.IntPickerDialog;
import com.menemi.fragments.ListRadioButtonDialog;
import com.menemi.personobject.PersonObject;

import java.util.ArrayList;

/**
 * Created by tester03 on 19.08.2016.
 */
public class EditPersonalInfo extends AppCompatActivity
{
    private static Toolbar toolbar;
    private ImageButton submitButton;
    private PersonObject profile;

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
        setSupportActionBar(toolbar);
        configureToolbar();
        profile = DBHandler.getInstance().getMyProfile();
        EditText editAboutPerson = (EditText)findViewById(com.menemi.R.id.about_edit_text);
        RelativeLayout heightPicker = (RelativeLayout)findViewById(com.menemi.R.id.height);
        RelativeLayout bodyType = (RelativeLayout) findViewById(com.menemi.R.id.bodyType);
        RelativeLayout eyeColor = (RelativeLayout) findViewById(com.menemi.R.id.eye_color);
        RelativeLayout hairColor = (RelativeLayout) findViewById(com.menemi.R.id.hair_color);
        RelativeLayout liveWith = (RelativeLayout) findViewById(com.menemi.R.id.live_in);
        RelativeLayout smoking = (RelativeLayout) findViewById(com.menemi.R.id.smoking);
        editAboutPerson.setText(profile.getAboutPersonInfo());

        //Change bodyType params
        bodyType.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ArrayList<String> items = new ArrayList<String>();
                items.add("Среднее");
                items.add("Пара лишних кило");
                items.add("Атлетическое");
                items.add("Крепкое");
                items.add("Стройное");
                items.add("Приятная полнота");
                ListRadioButtonDialog dialog = new ListRadioButtonDialog();
                dialog.setItemsArray(items);
                dialog.setTitle("Телосложение");
                dialog.setStyle(DialogFragment.STYLE_NORMAL, com.menemi.R.style.CustomDialog);
                dialog.show(getSupportFragmentManager(),"bt");
            }
        });
        //Change person parameter height
        heightPicker.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                IntPickerDialog dialog = new IntPickerDialog();
                dialog.show(getSupportFragmentManager(),"c");
            }
        });
        //Change eye color
        eyeColor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ArrayList<String> items = new ArrayList<String>();
                items.add("Карие");
                items.add("Серые");
                items.add("Зеленые");
                items.add("Голубые");
                items.add("Светло-карие");
                items.add("Другое");
                ListRadioButtonDialog dialog = new ListRadioButtonDialog();
                dialog.setItemsArray(items);
                dialog.setTitle("Цвет глаз");


                dialog.setStyle(DialogFragment.STYLE_NORMAL, com.menemi.R.style.CustomDialog);
                dialog.show(getSupportFragmentManager(),"eye color");
            }
        });
        // change hair color
        hairColor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ArrayList<String> items = new ArrayList<String>();
                items.add("Черные");
                items.add("Каштановые");
                items.add("Рыжие");
                items.add("Светлые");
                items.add("Немного седины");
                items.add("Седые");
                items.add("Сбриты");
                items.add("Окрашены");
                items.add("Окрашенные");
                items.add("Лысый");
                ListRadioButtonDialog dialog = new ListRadioButtonDialog();
                dialog.setItemsArray(items);
                dialog.setTitle("Цвет волос");
                dialog.setStyle(DialogFragment.STYLE_NORMAL, com.menemi.R.style.CustomDialog);
                dialog.show(getSupportFragmentManager(),"hair color");
            }
        });

        // change living place
        liveWith.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ArrayList<String> items = new ArrayList<String>();
                items.add("С родителями");
                items.add("С соседями");
                items.add("В общежитии");
                items.add("Светлые");
                items.add("Со второй половинкой");
                items.add("Один");
                ListRadioButtonDialog dialog = new ListRadioButtonDialog();
                dialog.setItemsArray(items);
                dialog.setTitle("Я живу");
                dialog.setStyle(DialogFragment.STYLE_NORMAL, com.menemi.R.style.CustomDialog);
                dialog.show(getSupportFragmentManager(),"living_with");
            }
        });

        smoking.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ArrayList<String> items = new ArrayList<String>();
                items.add("Не курю");
                items.add("Против курения");
                items.add("Курю");
                items.add("Курю за компанию");
                items.add("Курю одну за другой");
                ListRadioButtonDialog dialog = new ListRadioButtonDialog();
                dialog.setItemsArray(items);
                dialog.setTitle("Курение");
                dialog.setStyle(DialogFragment.STYLE_NORMAL, com.menemi.R.style.CustomDialog);
                dialog.show(getSupportFragmentManager(),"living_with");
            }
        });

    }
    private void configureToolbar()
    {
        Toolbar toolbar = EditPersonalInfo.getToolbar();
        LinearLayout toolbarContainer = (LinearLayout) toolbar.findViewById(com.menemi.R.id.toolbarContainer);
        toolbarContainer.removeAllViews();
        toolbarContainer.addView(View.inflate(this, com.menemi.R.layout.choice_interest_toolbar, null));
        submitButton = (ImageButton) toolbarContainer.findViewById(com.menemi.R.id.submButton);
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });

        ImageView menuButton = (ImageView) toolbarContainer.findViewById(com.menemi.R.id.menuButton);
        menuButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getFragmentManager().popBackStack();
            }
        });

        TextView title = (TextView) toolbarContainer.findViewById(com.menemi.R.id.screenTitle);
        title.setText(getString(com.menemi.R.string.edit_personal_info));
    }
}
