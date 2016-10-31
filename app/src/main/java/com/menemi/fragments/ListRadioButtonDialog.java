package com.menemi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;

import com.menemi.R;
import com.menemi.adapter.ListRadioButtonAdapter;
import com.menemi.interests_classes.RowModel;

import java.util.List;

/**
 * Created by tester03 on 22.08.2016.
 */
public class ListRadioButtonDialog extends DialogFragment
{
    private View rootView;
    private List<String> itemsArray;
    private RowModel[] rowItemsModel;
    private String title;
    private EditDialogListener editDialogListener;
    private ListRadioButtonAdapter adapter;
    private int index;

    public void setItems(List<String> items) {
        this.itemsArray = items;
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
//        itemsArray.add(0,"Не выбрано");
        rowItemsModel = new RowModel[itemsArray.size()];
        for (int i = 0; i < itemsArray.size(); i++)
        {
            rowItemsModel[i] = new RowModel(itemsArray.get(i),i);
        }
        adapter = new ListRadioButtonAdapter(getActivity().getApplicationContext(), rowItemsModel);
        listView.setAdapter(adapter);
        adapter.setRadioButtonChecked(index);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            RadioButton radioButton = (RadioButton)view.findViewById(R.id.radioButton2);
            if (radioButton.isChecked()){
                radioButton.setChecked(false);
            } else {
                radioButton.setChecked(true);
            }
            editDialogListener.updateResult(itemsArray.get(i),i);
            dismiss();
        });
        return rootView;
    }

    public interface EditDialogListener {
        void updateResult(String inputText, int index);
    }

    public void setEditDialogListener(EditDialogListener editDialogListener)
    {
        this.editDialogListener = editDialogListener;
    }

    public void setSelectedRadioButton(int index){
        this.index = index;
           

    }
}
