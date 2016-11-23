package com.menemi;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.menemi.customviews.HackyViewPager;
import com.menemi.fragments.PhotoFragment;
import com.menemi.personobject.PhotoSetting;

import java.util.ArrayList;


/**
 * Created by irondev on 22.06.16.
 */
public class PhotoSwipeFragment extends Fragment {

    public static final String PAGE_COUNT = "totalPages";


    private int pageNumber = 0;
    private View rootView = null;
    private boolean isFullScreen = false;
    private boolean isMyProfile = false;

    public void setMyProfile(boolean myProfile) {
        isMyProfile = myProfile;
    }

    private ArrayList<PhotoSetting> urlsArray = new ArrayList<>();
    HackyViewPager pager;
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
    }

    public void setUrlsArray(ArrayList<PhotoSetting> urlsArray) {
        this.urlsArray = urlsArray;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.setRetainInstance(false);
    }



    public PhotoSetting getCurrentItem(){
        return urlsArray.get(pager.getCurrentItem());
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(com.menemi.R.layout.activity_detailed, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
                parent.removeAllViews();
            }
        }
        Log.d("urlsCount", "" + urlsArray.size());
        //preparing Swiping sreens
        if(pager == null) {
            pager = (HackyViewPager) rootView.findViewById(com.menemi.R.id.pager);

            //adapter wwhich will manage our swaping
            pager.setAdapter(new MyFragmentPagerAdapter(getActivity().getFragmentManager()));
            pager.setCurrentItem(pageNumber);
        }
        return rootView;

    }


    private class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
        @Override
        public Fragment getItem(int position) {
            Log.d("PhotoFragment", "getItem " + position);
            PhotoFragment photoFragment = new PhotoFragment();
            photoFragment.setUrlsArray(urlsArray);
            photoFragment.setPageNumber(position);
            photoFragment.setMyProfile(isMyProfile);
            photoFragment.setFullScreen(isFullScreen);
            return photoFragment;
        }

        @Override
        public int getCount() {if(urlsArray.size() ==0){return 1;}else{
            return urlsArray.size();}
        }

    }

}
