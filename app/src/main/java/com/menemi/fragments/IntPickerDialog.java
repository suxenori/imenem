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
    private int pickerValue;
    private int minValue;
    private  int maxValue;
    private int currentValue;
    private String unit;
    private EditDialogListener editDialogListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        rootView = inflater.inflate(R.layout.int_picker_dialog,container,false);
        picker = (NumberPicker)rootView.findViewById(R.id.number_picker);
        ArrayList<String> s = new ArrayList<>();

        for (int i = minValue; i <= maxValue ; i++)
        {
            if (i == minValue){
                s.add("меньше" + " " + i + " " + unit);
            } else if (i == maxValue){
                s.add(i + " " + unit);
                s.add("больше" + " " + i + " " + unit);
                break;
            }
            s.add(i + " " + unit);
        }
        String[] v = s.toArray(new String[s.size()]);
        picker.setMaxValue(maxValue);
        picker.setMinValue(minValue);
        picker.setDisplayedValues(v);
       // picker.setVerticalScrollbarPosition(180);
        picker.setWrapSelectorWheel(false);
        picker.setValue(currentValue + 1);
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
        {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal)
            {
                if (newVal == 0){
                    pickerValue = oldVal;
                } else {
                    pickerValue = newVal - 1;
                }

                editDialogListener.updateResult(pickerValue + "");
            }
        });
        return rootView;
    }

    public interface EditDialogListener {
        void updateResult(String inputText);
    }


    public void setMinValue(int minValue)
    {
        this.minValue = minValue;
    }

    public void setMaxValue(int maxValue)
    {
        this.maxValue = maxValue;
    }

    public void setCurrentValue(int currentValue)
    {
        this.currentValue = currentValue;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public void setEditDialogListener(EditDialogListener editDialogListener)
    {
        this.editDialogListener = editDialogListener;
    }
}
