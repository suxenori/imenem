package com.menemi.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.menemi.PersonPage;
import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.Gift;
import com.menemi.personobject.PersonObject;

import java.util.ArrayList;

/**
 * Created by Ui-Developer on 07.07.2016.
 */
public class GiftsListFragment extends Fragment  {
    private View rootView = null;
    PersonObject personObject;

    public void setPersonObject(PersonObject personObject) {
        this.personObject = personObject;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.gifts_list_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        configureToolbar();
        LinearLayout giftsContainer = (LinearLayout) rootView.findViewById(R.id.giftsContainer);
        giftsContainer.removeAllViews();
        ArrayList<Gift> gifts = DBHandler.getInstance().getGifts();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        for (int i = 0; i < gifts.size() + gifts.size() % 4; i += 4) {
            GiftsSetFragment giftsSetFragment = new GiftsSetFragment();
            giftsSetFragment.setGifts(gifts, i);
            giftsSetFragment.setPersonObject(personObject);
            ft.add(R.id.giftsContainer, giftsSetFragment);
        }
        ft.commit();


        return rootView;
    }

    private void configureToolbar() {
        Toolbar toolbar = PersonPage.getToolbar();

        LinearLayout toolbarContainer = (LinearLayout) toolbar.findViewById(R.id.toolbarContainer);
        toolbarContainer.removeAllViews();
        toolbarContainer.addView(View.inflate(getActivity(), R.layout.ab_buy_gift, null)); // TODO insertt enother

        ImageView menuButton = (ImageView) toolbarContainer.findViewById(R.id.menuButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }}
        });

    }
}
