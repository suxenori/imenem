package com.menemi.customviews;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.menemi.R;
import com.menemi.fragments.ListRadioButtonDialog;
import com.menemi.personobject.Language;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by tester03 on 22.08.2016.
 */
public class LanguagesChooseDialog extends DialogFragment
{
    private View rootView;
    private ArrayList<Language> allLanguages;
    private ArrayList<Language> choosenItems;
    private String title;

    private int index;
    private ListRadioButtonDialog.EditDialogListener editDialogListener;

    public void setItems(ArrayList<Language> items) {
        this.allLanguages = items;
    }

    public void setChoosenItems(ArrayList<Language> choosenItems) {
        this.choosenItems = choosenItems;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.list_radio_dialog_layout,container,false);
        ListView listView = (ListView)rootView.findViewById(R.id.editListItems);
        getDialog().setTitle(title);
         listView.setAdapter(new Adapter());


        return rootView;
    }

    public void setEditDialogListener(ListRadioButtonDialog.EditDialogListener editDialogListener)
    {
        this.editDialogListener = editDialogListener;
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
            choosenItems.get(languageIndex).setLanguageLevel(index);
        }
    }
    class Adapter extends BaseAdapter{


        @Override
        public int getCount() {
            return allLanguages.size();
        }

        @Override
        public Object getItem(int position) {
            return allLanguages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View languageViewItem = inflater.inflate(R.layout.language_item,null);

            TextView languageName = (TextView) languageViewItem.findViewById(R.id.languageName);
            languageName.setText(allLanguages.get(position).getLanguageName());

            CheckBox languageCheckBox = (CheckBox) languageViewItem.findViewById(R.id.languageCheckBox);
            TextView languageLevel = (TextView) languageViewItem.findViewById(R.id.languageLevel);
            for (int j = 0; j < choosenItems.size(); j++) {
                if(choosenItems.get(j).getLanguagesId() == allLanguages.get(position).getLanguagesId()){
                    languageCheckBox.setChecked(true);

                    languageLevel.setText(getResources().getStringArray(R.array.language_level)[choosenItems.get(j).getLanguageLevel()]);
                    ListRadioButtonDialog languageDialog = new ListRadioButtonDialog();
                    int finalJ = j;
                    languageViewItem.setOnClickListener(view -> {

                        languageDialog.setItems(Arrays.asList(getResources().getStringArray(R.array.language_level)));
                        languageDialog.setSelectedRadioButton(choosenItems.get(finalJ).getLanguageLevel());
                        languageDialog.setTitle(getString(R.string.chooseLanguageLevel));
                        languageDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                        languageDialog.setEditDialogListener(new EditLanguageListener(languageLevel, finalJ));
                        languageDialog.show(getFragmentManager(),"living_with");


                    });
                }
            }


            languageCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){

                        choosenItems.add(allLanguages.get(position));
                        int lastAdded = choosenItems.size()-1;
                        ListRadioButtonDialog languageDialog = new ListRadioButtonDialog();



                            languageDialog.setItems(Arrays.asList(getResources().getStringArray(R.array.language_level)));
                            languageDialog.setSelectedRadioButton(choosenItems.get(lastAdded).getLanguageLevel());
                            languageDialog.setTitle(getString(R.string.chooseLanguageLevel));
                            languageDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                            languageDialog.setEditDialogListener(new EditLanguageListener(languageLevel,lastAdded));
                            languageDialog.show(getFragmentManager(),"living_with");

                        languageViewItem.setOnClickListener(view -> {

                            languageDialog.setItems(Arrays.asList(getResources().getStringArray(R.array.language_level)));
                            languageDialog.setSelectedRadioButton(choosenItems.get(lastAdded).getLanguageLevel());
                            languageDialog.setTitle(getString(R.string.chooseLanguageLevel));
                            languageDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                            languageDialog.setEditDialogListener(new EditLanguageListener(languageLevel,lastAdded));
                            languageDialog.show(getFragmentManager(),"living_with");


                        });

                    } else {
                        choosenItems.remove(allLanguages.get(position));
                        languageViewItem.setOnClickListener(null);
                    }
                }
            });

            return languageViewItem;
        }


    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        editDialogListener.updateResult(null,0);
    }
}
