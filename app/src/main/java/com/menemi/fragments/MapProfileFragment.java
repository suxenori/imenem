package com.menemi.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.menemi.R;
import com.menemi.personobject.PersonObject;

/**
 * Created by Ui-Developer on 07.07.2016.
 */
public class MapProfileFragment extends Fragment{
    private View rootView = null;
    private PersonObject personToShow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.map_holder_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ShowMapFragment mapFragment = new ShowMapFragment();
        mapFragment.setPersonToShow(personToShow);
        mapFragment.setOwnPosition(false);
        fragmentTransaction.replace(R.id.mapHolder, mapFragment);
        fragmentTransaction.commitAllowingStateLoss();
        LinearLayout mapGlass = (LinearLayout)rootView.findViewById(R.id.mapGlass);
        mapGlass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        return rootView;
    }

    public void setPersonToShow(PersonObject personToShow) {
        this.personToShow = personToShow;
    }
}
