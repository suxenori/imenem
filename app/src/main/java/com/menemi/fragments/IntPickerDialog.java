package com.menemi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.menemi.R;

import java.util.ArrayList;

/**
 * Created by tester03 on 19.08.2016.
 */
public class IntPickerDialog extends DialogFragment
{
    private View rootView;
    private NumberPicker picker;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        rootView = inflater.inflate(R.layout.int_picker_dialog,container,false);
        picker = (NumberPicker)rootView.findViewById(R.id.number_picker);
        ArrayList<String> s = new ArrayList<>();
        for (int i = 139; i <= 220 ; i++)
        {
            if (i == 139){
                s.add("меньше" + " " + i + " см");
            } else if (i == 220){
                s.add(i + " см");
                s.add("больше" + " " + i + " см");
                break;
            }
            s.add(i + " см");
        }
        String[] v = s.toArray(new String[s.size()]);
        picker.setMaxValue(222);
        picker.setMinValue(139);
        picker.setDisplayedValues(v);
        picker.setVerticalScrollbarPosition(180);
        picker.setWrapSelectorWheel(false);
        picker.setValue(180);
        return rootView;
    }
}
