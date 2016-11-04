package com.menemi.edit_personal_Info;

import android.view.View;
import android.view.ViewParent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tester03 on 27.10.2016.
 */

public class GRadioGroup {

    private List<RadioButton> radios = new ArrayList<>();

    private int index;

    /**
     * Constructor, which allows you to pass number of RadioButton instances,
     * making a group.
     *
     * @param radios One RadioButton or more.
     */
    public GRadioGroup(RadioButton... radios) {
        super();

        for (RadioButton rb : radios) {
            this.radios.add(rb);
            rb.setOnClickListener(onClick);
        }
    }

    /**
     * Constructor, which allows you to pass number of RadioButtons
     * represented by resource IDs, making a group.
     *
     * @param activity  Current View (or Activity) to which those RadioButtons
     *                  belong.
     * @param radiosIDs One RadioButton or more.
     */
    public GRadioGroup(View activity, int... radiosIDs) {
        super();

        for (int radioButtonID : radiosIDs) {
            RadioButton rb = (RadioButton) activity.findViewById(radioButtonID);
            if (rb != null) {
                this.radios.add(rb);
                rb.setOnClickListener(onClick);
            }
        }
    }

    /**
     * This occurs everytime when one of RadioButtons is clicked,
     * and deselects all others in the group.
     */
    View.OnClickListener onClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            // let's deselect all radios in group
            for (RadioButton rb : radios) {

                ViewParent p = rb.getParent();
                if (p.getClass().equals(RadioGroup.class)) {
                    // if RadioButton belongs to RadioGroup,
                    // then deselect all radios in it
                    RadioGroup rg = (RadioGroup) p;
                    rg.clearCheck();
                } else {
                    // if RadioButton DOES NOT belong to RadioGroup,
                    // just deselect it
                    rb.setChecked(false);
                }
            }

            // now let's select currently clicked RadioButton
            RadioButton rb = (RadioButton) v;
            rb.setChecked(true);
            int index = selectedIndex(radios, rb);
            if (onCheckChange != null) {
                onCheckChange.OnChange(index);
            }
        }
    };


    public int selectedIndex(List<RadioButton> buttons, RadioButton rb) {
        index = buttons.indexOf(rb);
        return index;
    }

    public int getIndex() {
        return index;
    }

    public void setCheckedRadioButton(int index) {
        radios.get(index).setChecked(true);
        this.index = index;
    }

    public void setOnCheckChange(OnCheckChange onCheckChange) {
        this.onCheckChange = onCheckChange;
    }

    private OnCheckChange onCheckChange = null;

    public interface OnCheckChange {
        void OnChange(int checkedIndex);
    }
}
