package com.menemi.fragments;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.menemi.PersonPage;
import com.menemi.PhotoSwipeFragment;
import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.PersonObject;


public class EncountersFragment extends Fragment
{

    private PersonObject person = null;
    private View rootView = null;
    private PersonDataFragment.Purpose purpose = PersonDataFragment.Purpose.LIKE;


    public void setPurpose(PersonDataFragment.Purpose purpose) {
        this.purpose = purpose;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.encounters_layout, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        if(person != null) {
            if(getActivity() == null || getFragmentManager() == null){
                return null;
            }
            FragmentManager fragmentManager = getActivity().getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            PhotoSwipeFragment photoSwipeFragment = new PhotoSwipeFragment();
            photoSwipeFragment.setUrlsArray(person.getPictureUrlsPublic());
            photoSwipeFragment.setMyProfile(person.getPersonId() == DBHandler.getInstance().getUserId());
            photoSwipeFragment.setFullScreen(false);
            photoSwipeFragment.setPageNumber(0);
            fragmentTransaction.replace(R.id.personPhotoImageView, photoSwipeFragment);
            fragmentTransaction.commitAllowingStateLoss();
            PersonPage.finishProgressDialog();
        }
        return rootView;

    }


    public void setPerson(PersonObject person) {
        this.person = person;
    }
}
