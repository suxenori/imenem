package com.menemi.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.PhotoSetting;
import com.menemi.social_network.social_profile_photo_handler.GridViewAdapter;
import com.menemi.social_network.social_profile_photo_handler.ScrollViewListener;
import com.menemi.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Ui-Developer on 29.09.2016.
 */

public class PublicPhotoListFragment extends Fragment{

    /* GridView gv = (GridView) findViewById(R.id.grid_view);
        gv.setAdapter(new GridViewAdapter(this,urls));
        gv.setOnScrollListener(new ScrollViewListener(this));
        gv.invalidateViews();*/
    private View rootView = null;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.gridview_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        int id = DBHandler.getInstance().getMyProfile().getPersonId();
        DBHandler.getInstance().getPhotoUrls(id, Utils.PICTURE_QUALITY_THUMBNAIL, false, (Object object) -> {
            if(getActivity() == null){
                return;
            }
            ArrayList<PhotoSetting> pictures = (ArrayList<PhotoSetting>)object;
            pictures.size();
            ArrayList<String> urls = new ArrayList<>();
            for (int i = 0; i < pictures.size(); i++) {
                urls.add( pictures.get(i).getPhotoUrl());
            }

            GridView gv = (GridView) rootView.findViewById(R.id.grid_view);
            gv.setAdapter(new GridViewAdapter(getActivity(), urls));
            gv.setOnScrollListener(new ScrollViewListener(getActivity()));
            gv.invalidateViews();
        });




        return rootView;
    }
}
