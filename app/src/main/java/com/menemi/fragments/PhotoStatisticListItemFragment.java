package com.menemi.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.dbfactory.rest.PictureLoader;
import com.menemi.personobject.PhotoSetting;

/**
 * Created by Ui-Developer on 29.07.2016.
 */
public class PhotoStatisticListItemFragment extends Fragment {
    private View rootView = null;
    private PhotoSetting photoSetting= null;

    public void setPhotoSetting(PhotoSetting photoSetting) {
        this.photoSetting = photoSetting;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.photo_statistic_item_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        ImageView photo = (ImageView) rootView.findViewById(R.id.photo);
        if(photoSetting.getPhoto() == null){
            new PictureLoader(photoSetting.getPhotoUrl(), (Object object)->{
                photoSetting.setPhoto((Bitmap)object);
                photo.setImageBitmap(photoSetting.getPhoto());
            });

        } else{
            photo.setImageBitmap(photoSetting.getPhoto());
        }

        TextView priceData = (TextView)rootView.findViewById(R.id.priceData);
        priceData.setText("" + photoSetting.getPrice());
        TextView viewsData = (TextView) rootView.findViewById(R.id.viewsData);
        viewsData.setText("" + photoSetting.getViews());
        TextView coinsData = (TextView) rootView.findViewById(R.id.coinsData);
        coinsData.setText("" + (int)photoSetting.getProfit());
        LinearLayout templatesHolder = (LinearLayout) rootView.findViewById(R.id.templatesHolder);

        for (int i = 0; i < photoSetting.getTemplateIds().length; i++) {
            View layout = inflater.inflate(R.layout.small_template, null);
            ImageView template = (ImageView) layout.findViewById(R.id.template);
            template.setImageBitmap(DBHandler.getInstance().getPhotoTemplatePictureById(photoSetting.getTemplateIds()[i]));
            templatesHolder.addView(layout);
        }
        Log.d("PrivatTagFinder","1" +  photoSetting.isPrivate());


        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoSettingsFragment photoSettingsFragment = new PhotoSettingsFragment();
                photoSettingsFragment.setPhotoSetting(photoSetting);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.content, photoSettingsFragment);
                transaction.commitAllowingStateLoss();
                photoSettingsFragment.setOnChangeListener(new PhotoSettingsFragment.OnChangeListener() {
                    @Override
                    public void onChangesMade(PhotoSetting photoSetting) {
                        if(photoSetting == null){
                            if(getActivity() != null && getFragmentManager() != null) {
                                ViewGroup parent = (ViewGroup) rootView.getParent();
                                if (parent != null) {
                                    parent.removeView(rootView);
                                }
                                Log.d("PhotoSettingsFragment", "removed");
                            }
                        }
                    }
                });
            }
        });
        return rootView;
    }

}
