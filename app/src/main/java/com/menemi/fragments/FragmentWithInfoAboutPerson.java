package com.menemi.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.menemi.PersonPage;
import com.menemi.R;
import com.menemi.personobject.PersonObject;

/**
 * Created by tester03 on 17.06.2016.
 */
public class FragmentWithInfoAboutPerson extends Fragment {
    private PersonObject personObject = null;
    private View rootView = null;
    private PersonDataFragment.Purpose puropose = PersonDataFragment.Purpose.LIKE;

    public void setPersonObject(PersonObject personObject) {
        this.personObject = personObject;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_with_info_about_person, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
                parent.removeAllViews();
            }
        }

        if (personObject != null) {


            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


            FriendsFragment friendsFragment = new FriendsFragment();
            friendsFragment.setFriends(personObject.getPersonFriends());

            ShareFavoriteFragment shareFavoriteFragment = new ShareFavoriteFragment();
            shareFavoriteFragment.setTargetObject(personObject);

            InterestsFragment interestsFragment = new InterestsFragment();
            interestsFragment.setInterests(personObject.getInterests());
            interestsFragment.setPurpose(puropose);

            interestsFragment.setOwnerFragment(this);

            PhotoListFragment photoListFragment = new PhotoListFragment();
            photoListFragment.setPersonObject(personObject);
            photoListFragment.setPurpose(puropose);


            AboutFragment aboutFragment = new AboutFragment();
            aboutFragment.setPersonObject(personObject);
            aboutFragment.setPurpose(puropose);

            LanguagesFragment languagesFragment = new LanguagesFragment();
            languagesFragment.setPersonObject(personObject);
            languagesFragment.setPurpose(puropose);

            GiftsFragment giftsFragment = new GiftsFragment();
            giftsFragment.setPersonObject(personObject);
            giftsFragment.setGifts(personObject.getGifts());

            AwardsFragment awardsFragment = new AwardsFragment();
            awardsFragment.setAwards(personObject.getRewards());

            MapProfileFragment mapProfileFragment = new MapProfileFragment();
            mapProfileFragment.setPersonToShow(personObject);

            VerificationFragment verificationFragment = new VerificationFragment();
            verificationFragment.setPurpose(puropose);
            verificationFragment.setPersonObject(personObject);

            CryFragment cryFragment = new CryFragment();
            cryFragment.setId(personObject.getPersonId());


            if (personObject.getPersonFriends() != null && personObject.getPersonFriends().size() > 0) {
                Log.d("LOG", "replaced");
                fragmentTransaction.replace(R.id.friendsPlaceHolder, friendsFragment);
            }


            fragmentTransaction.replace(R.id.interestsPlaceHolder, interestsFragment);


            fragmentTransaction.replace(R.id.photoPlaceHolder, photoListFragment);

            if (puropose != PersonDataFragment.Purpose.MY_PROFILE) {
                fragmentTransaction.replace(R.id.shareFavoritePlaceHolder, shareFavoriteFragment);
                fragmentTransaction.replace(R.id.mapPlaceHolder, mapProfileFragment);
                //  fragmentTransaction.replace(R.id.cryPlaceHolder, cryFragment);
            }

            fragmentTransaction.replace(R.id.aboutInfoPlaceHolder, aboutFragment);
            fragmentTransaction.replace(R.id.languagesPlaceHolder, languagesFragment);
            if (personObject.getRewards().size() != 0) {
                fragmentTransaction.replace(R.id.awardsPlaceHolder, awardsFragment);
            }
            if(puropose != PersonDataFragment.Purpose.MY_PROFILE){
                fragmentTransaction.replace(R.id.giftsPlaceHolder, giftsFragment);
            } else{
                if (personObject.getGifts() != null && personObject.getGifts().size() > 0){
                    fragmentTransaction.replace(R.id.giftsPlaceHolder, giftsFragment);
                }
            }

            fragmentTransaction.replace(R.id.verificationPlaceHolder, verificationFragment);


            fragmentTransaction.commitAllowingStateLoss();

            PersonPage.finishProgressDialog();
        }

        return rootView;
    }

    public void refreshInterests() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final InterestsFragment interestsFragment = new InterestsFragment();
        interestsFragment.setInterests(personObject.getInterests());
        interestsFragment.setPurpose(puropose);
        interestsFragment.setOwnerFragment(this);

        fragmentTransaction.replace(R.id.interestsPlaceHolder, interestsFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void setPuropose(PersonDataFragment.Purpose puropose) {
        this.puropose = puropose;
    }
}
