package com.menemi.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.menemi.R;
import com.menemi.customviews.ObservableScrollView;
import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.PersonObject;
import com.menemi.personobject.PhotoSetting;

import java.util.ArrayList;

/**
 * Created by Ui-Developer on 28.07.2016.
 */
public class PhotoStatisticListFragment extends Fragment {
    private View rootView = null;
    private ArrayList<PhotoSetting> pictures = null;
    PersonObject personObject = null;
    private int photosToShow = 7;
    private LayoutInflater inflater;
    boolean isLoading = false;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.picture_scroll_list_container, container, false);
            this.inflater = inflater;
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        Log.d("PhotoStatisticList", "create2");
        this.personObject = DBHandler.getInstance().getMyProfile();



        if(pictures == null) {


            if(personObject.getPhotoCountPrivate() < photosToShow){
                photosToShow = personObject.getPhotoCountPrivate();
            }
          insertPhotos(0,photosToShow);

        }
        ObservableScrollView list = (ObservableScrollView) rootView.findViewById(R.id.content);
        list.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                View view =  scrollView.getChildAt(scrollView.getChildCount() - 1);
                int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

                // if diff is zero, then the bottom has been reached
                if (diff== 0) {
                    if(isLoading == true){
                        return;
                    }
                    isLoading = true;

                    if(personObject.getPhotoCountPrivate() - photosToShow > 0){
                        insertPhotos(photosToShow, personObject.getPhotoCountPrivate() - photosToShow);
                        photosToShow +=  personObject.getPhotoCountPrivate() - photosToShow;

                    }

                }
            }


        });


        return rootView;
    }
    private void insertPhotos( int start, int count){
        final LinearLayout fragment1 = (LinearLayout) rootView.findViewById(R.id.fragment1);
        final View progressBar = inflater.inflate(R.layout.progress_bar, null);
        fragment1.addView(progressBar);
        pictures = personObject.getPictureUrlsPrivate();
        //(int personId, int requestedId, int photoNumber, int count, String quality,



                fragment1.removeView(progressBar);
                isLoading = false;
                if(getActivity() == null || getFragmentManager() == null) {
                    return;
                }
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                for ( int i = start; i < count; i++) {
                    PhotoStatisticListItemFragment photoStatisticListItemFragment = new PhotoStatisticListItemFragment();
                        photoStatisticListItemFragment.setPhotoSetting(pictures.get(i));

                        fragmentTransaction.add(R.id.fragment1, photoStatisticListItemFragment);

                }
        fragmentTransaction.commitAllowingStateLoss();
    }

}