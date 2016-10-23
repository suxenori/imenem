package com.menemi.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.menemi.PersonPage;
import com.menemi.R;
import com.menemi.customviews.OCDialog;
import com.menemi.dbfactory.DBHandler;
import com.menemi.dbfactory.rest.PictureLoader;
import com.menemi.personobject.PhotoSetting;
import com.menemi.personobject.PhotoTemplate;
import com.menemi.social_network.PhotoSettingsContainer;
import com.menemi.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Ui-Developer on 20.07.2016.
 */
public class PhotoSettingsFragment extends Fragment {
    private View rootView = null;
    PhotoSetting oldPhotoSetting = null;
    boolean isSocial = false;
    PhotoSetting photoSetting = null;
    LinearLayout toolbarContainer = null;
    ArrayList<PhotoTemplate> photoTemplates;
    OnChangeListener onChangeListener = new OnChangeListener() {
        @Override
        public void onChangesMade(PhotoSetting photoSetting) {
                Log.d("PhotoSettingsFragment", "listener is called, but not set");
        }
    };

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    public void setSocial(boolean social)
    {
        isSocial = social;
    }

    public void setPhotoSetting(PhotoSetting photoSetting) {
        this.oldPhotoSetting = photoSetting;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.photo_settings_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        photoTemplates = DBHandler.getInstance().getPhotoTemplates();
        configureToolbar();

        photoSetting = new PhotoSetting(oldPhotoSetting);
        Log.d("PrivatTagFinder","5" +  oldPhotoSetting.isPrivate());
        Log.d("PrivatTagFinder","3" +  photoSetting.isPrivate());
        final ImageView photo = (ImageView) rootView.findViewById(R.id.photo);
        if(photoSetting.getPhotoId() != -1){
            DBHandler.getInstance().getPhotoByID(photoSetting.getPhotoId(), Utils.PICTURE_QUALITY_LARGE, (Object object) ->{
                Log.d("PrivatTagFinder","4" +  photoSetting.isPrivate());
                    photoSetting = (PhotoSetting) object;
                    new PictureLoader(photoSetting.getPhotoUrl(),(Object bitmap) ->{
                        photoSetting.setPhoto((Bitmap) bitmap);
                        photo.setImageBitmap(photoSetting.getPhoto());

                    });
                    final ImageView rotateButton = (ImageView) rootView.findViewById(R.id.rotateButton);
                    rotateButton.setOnClickListener(new OnRotateListener(photoSetting));
                Log.d("PrivatTagFinder","5" +  photoSetting.isPrivate());
                    showPrivate(photoSetting.isPrivate());

            });
        }



        Log.d("PhotoSettingsFragment" , " photo ID = " + photoSetting.getPhotoId());
        photo.setImageBitmap(photoSetting.getPhoto());
        if(photoSetting.isPrivate()) {
            PhotoPrivacySettings photoPrivacySettings = new PhotoPrivacySettings();
            photoPrivacySettings.setPhotoSetting(photoSetting);
            photoPrivacySettings.setPhotoTemplates(photoTemplates);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.privacyContainer, photoPrivacySettings);
            transaction.commitAllowingStateLoss();
        }
        final ImageView rotateButton = (ImageView) rootView.findViewById(R.id.rotateButton);
        rotateButton.setOnClickListener(new OnRotateListener(photoSetting));

        final Switch privateSwitch = (Switch) rootView.findViewById(R.id.privateSwitch);
        privateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean showPrivate) {

                showPrivate(showPrivate);
            }
        });


        Button submitButton = (Button) rootView.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progress = new ProgressDialog(getActivity());
                progress.setMessage(getString(R.string.photo_upload));
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setIndeterminate(true);
                progress.show();
                progress.setCancelable(false);
                photoSetting.setProfileId(DBHandler.getInstance().getUserId());
                photoSetting.setTemplateIds(photoTemplates);

                DBHandler.getInstance().addPhoto(photoSetting, new DBHandler.ResultListener() {
                    @Override
                    public void onFinish(Object object) {
                        progress.dismiss();
                        final FragmentManager fm = getFragmentManager();
                        onChangeListener.onChangesMade(photoSetting);
                        if( fm != null && !fieldActivityIsDestroying) {

                                    fm.popBackStack();
                        } else {
                            popedFromStack = true;
                        }
                    }
                });
            }
        });
        Button cancelButton = (Button) rootView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSocial){
                    getActivity().finish();
                } else {
                    FragmentManager fm = getFragmentManager();

                    if( fm != null) {
                        fm.popBackStack();
                    }
                }

            }
        });


        return rootView;
    }

    private void showPrivate(boolean showPrivate) {
        Switch privateSwitch = (Switch) rootView.findViewById(R.id.privateSwitch);
        if (showPrivate) {
            photoSetting.setPrivate(showPrivate);
            PhotoPrivacySettings photoPrivacySettings = new PhotoPrivacySettings();
            photoPrivacySettings.setPhotoSetting(photoSetting);
            photoPrivacySettings.setPhotoTemplates(photoTemplates);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.privacyContainer, photoPrivacySettings);
            transaction.commitAllowingStateLoss();
            privateSwitch.setChecked(true);

        } else {
            photoSetting.setPrivate(showPrivate);
            privateSwitch.setChecked(false);
            LinearLayout privacyContainer = (LinearLayout) rootView.findViewById(R.id.privacyContainer);
            privacyContainer.removeAllViews();
        }
    }

    private void configureToolbar() {
        Toolbar toolbar;
        if(isSocial){
            toolbar = PhotoSettingsContainer.getToolbar();
        } else {

            toolbar = PersonPage.getToolbar();
        }

        toolbarContainer = (LinearLayout) toolbar.findViewById(R.id.toolbarContainer);
        toolbarContainer.removeAllViews();
        toolbarContainer.addView(View.inflate(getActivity(), R.layout.ab_photo_settings, null));
        if(isSocial){
            ImageView menuButton = (ImageView) toolbarContainer.findViewById(R.id.menuButton);
            menuButton.setVisibility(View.INVISIBLE);
        } else {

            ImageView menuButton = (ImageView) toolbarContainer.findViewById(R.id.menuButton);
            menuButton.setOnClickListener(PersonPage.getMenuListener());
        }


        ImageView delete = (ImageView) toolbarContainer.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new OCDialog(getActivity(), getString(R.string.delete_photo_title), getString(R.string.delete_photo_message), R.drawable.delete_icon, new OCDialog.OKListener() {
                    @Override
                    public void onOKpressed() {
                        if (oldPhotoSetting.getPhotoId() != -1) {
                            DBHandler.getInstance().deletePhoto(oldPhotoSetting, DBHandler.getInstance().getUserId(), new DBHandler.ResultListener() {
                                @Override
                                public void onFinish(Object object) {
                                    if(getActivity()!= null && getFragmentManager() != null) {
                                        Log.d("PhotoSettingsFragment", "OkPressed");
                                        onChangeListener.onChangesMade(null);
                                        if (isSocial){
                                            getActivity().finish();
                                        }
                                        getFragmentManager().popBackStack();
                                    }
                                }
                            });
                        } else {
                            if (isSocial){
                                getActivity().finish();
                            }
                            getFragmentManager().popBackStack();
                        }
                    }
                },
                        new OCDialog.CANCELListener() {
                            @Override
                            public void onCANCELpressed() {

                            }
                        });

                Log.d("Photo", "" + oldPhotoSetting.getPhotoId());
            }
        });
    }
    class OnRotateListener implements View.OnClickListener{
        private PhotoSetting photoSetting;

        public OnRotateListener(PhotoSetting photoSetting) {
            this.photoSetting = photoSetting;
        }

        @Override
        public void onClick(View view) {
           ImageView photo = (ImageView) rootView.findViewById(R.id.photo);
            photoSetting.setPhoto(Utils.rotateImage(photoSetting.getPhoto(), 90));
            photo.setImageBitmap(photoSetting.getPhoto());
        }
    }

    interface OnChangeListener{
        void onChangesMade(PhotoSetting photoSetting);
    }
boolean popedFromStack = false;
    @Override
    public void onResume() {
        super.onResume();
        if(popedFromStack){
            getFragmentManager().popBackStack();
        }
    }

    boolean fieldActivityIsDestroying = false;
    public void onSaveInstanceState(Bundle out){
        super.onSaveInstanceState(out);
        fieldActivityIsDestroying = true;
    }






}
