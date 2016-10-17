package com.menemi.social_network;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.menemi.R;
import com.menemi.fragments.PhotoSettingsFragment;
import com.menemi.personobject.PhotoSetting;

/**
 * Created by tester03 on 26.09.2016.
 */
public class PhotoSettingsContainer extends AppCompatActivity
{

    private static Toolbar toolbar;
    public static Bitmap image;
    public static Toolbar getToolbar()
    {
        return toolbar;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interests_container);
        toolbar = (Toolbar) findViewById(R.id.toolbar_with_checkBox);
        setSupportActionBar(toolbar);
        PhotoSettingsFragment fragment = new PhotoSettingsFragment();
        Log.d("bitmap_image",image + "");
        fragment.setSocial(true);
        fragment.setPhotoSetting(new PhotoSetting(image));
        openFragment(fragment, getFragmentManager());
    }

    public void openFragment(Fragment fragment, FragmentManager fm ){
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.interest_empty, fragment);
        transaction.commitAllowingStateLoss();
    }

   /* @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();

        } else {
            getFragmentManager().popBackStack();
            finish();
        }

    }*/


}
