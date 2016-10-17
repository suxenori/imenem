package com.menemi;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.menemi.personobject.PhotoSetting;

import java.util.ArrayList;

/**
 * Created by Ui-Developer on 08.07.2016.
 */
public class PictureSlideActivity  extends Activity
{
    static ArrayList<PhotoSetting> photosUrls = new ArrayList<>();

    public static void setPhotosUrls(ArrayList<PhotoSetting> photosUrls) {
        PictureSlideActivity.photosUrls = photosUrls;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(com.menemi.R.layout.activity_picture_slide);

        int pageNumber = 0;
        int totalPages = 0;
        if (savedInstanceState == null) {
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            pageNumber = extras.getInt("page");
            totalPages = extras.getInt("totalPages");
        }
    }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PhotoSwipeFragment photoSwipeFragment = new PhotoSwipeFragment();
        photoSwipeFragment.setUrlsArray(photosUrls);
        photoSwipeFragment.setFullScreen(true);
        photoSwipeFragment.setPageNumber(pageNumber);


        fragmentTransaction.replace(com.menemi.R.id.fragmentContainer, photoSwipeFragment);
        fragmentTransaction.commitAllowingStateLoss();

    }




}
