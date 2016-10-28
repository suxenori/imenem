package com.menemi.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.menemi.PersonPage;
import com.menemi.R;
import com.menemi.customviews.GridAdapter;
import com.menemi.dbfactory.DBHandler;
import com.menemi.dbfactory.rest.PictureLoader;
import com.menemi.personobject.PhotoSetting;
import com.menemi.social_network.social_profile_photo_handler.ScrollViewListener;
import com.menemi.utils.Utils;

import java.util.ArrayList;

import static com.menemi.fragments.PublicPhotoListFragment.Purpose.CHOOSE_AVATAR;
import static com.menemi.fragments.PublicPhotoListFragment.Purpose.NORMAL;

/**
 * Created by Ui-Developer on 29.09.2016.
 */

public class PublicPhotoListFragment extends Fragment{


    private View rootView = null;
    private Purpose purpose = NORMAL;
    GridAdapter gridAdapter =null;
    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    @Override
    public void onResume() {
        super.onResume();
        configure();
    }

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


configure();



        return rootView;
    }
    public void configure(){
        if(rootView == null){
            return;
        }
        if(purpose == CHOOSE_AVATAR){
            configureToolbar();
        }
        int id = DBHandler.getInstance().getMyProfile().getPersonId();
        DBHandler.getInstance().getPhotoUrls(id, Utils.PICTURE_QUALITY_THUMBNAIL, false, (Object object) -> {
            if(getActivity() == null){
                return;
            }
            ArrayList<PhotoSetting> pictures = (ArrayList<PhotoSetting>)object;



            GridView gv = (GridView) rootView.findViewById(R.id.grid_view);
            if(purpose != CHOOSE_AVATAR) {

                gridAdapter = new GridAdapter(getActivity(), pictures, (PhotoSetting photoSetting) ->{
                    PhotoSettingsFragment photoSettingsFragment = new PhotoSettingsFragment();
                    photoSettingsFragment.setPhotoSetting(photoSetting);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.content, photoSettingsFragment);
                    transaction.addToBackStack(null);
                    transaction.commitAllowingStateLoss();
                    photoSettingsFragment.setOnChangeListener(new PhotoSettingsFragment.OnChangeListener() {
                        @Override
                        public void onChangesMade(PhotoSetting photoSetting) {
                            configure();
                        }
                    });

                }) ;
                gv.setAdapter(gridAdapter);
            } else{
                gridAdapter = new GridAdapter(getActivity(), pictures, (PhotoSetting photoSetting) -> {
                    DBHandler.getInstance().setAvatar(photoSetting, (o) -> {
                        new PictureLoader(photoSetting.getPhotoUrl(), (bitmap) -> {
                            PersonPage.prepareNavigationalHeader(getActivity().getApplicationContext(), bitmap);
                            getFragmentManager().popBackStack();
                        });

                    });
                });
                gv.setAdapter(gridAdapter);
            }
            gv.setOnScrollListener(new ScrollViewListener(getActivity()));
            gv.invalidateViews();
        });
    }
    public enum Purpose {
        NORMAL,
        CHOOSE_AVATAR
    }
    private void configureToolbar() {
        Toolbar toolbar = PersonPage.getToolbar();
        LinearLayout toolbarContainer = (LinearLayout) toolbar.findViewById(R.id.toolbarContainer);
        toolbarContainer.removeAllViews();
        toolbarContainer.addView(View.inflate(getActivity(),R.layout.ab_buy_coins,null));

        ImageView menuButton = (ImageView) toolbarContainer.findViewById(R.id.menuButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        TextView title = (TextView) toolbarContainer.findViewById(R.id.screenTitle);
        title.setText(getString(R.string.choose_avatar));

        /*TextView nameAgeText = (TextView) toolbarContainer.findViewById(R.id.nameAgeText);
        nameAgeText.setText(personObject.getPersonName() +", " + personObject.getPersonAge());*/
    }
}
