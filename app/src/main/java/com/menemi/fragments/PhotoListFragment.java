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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.dbfactory.rest.PictureLoader;
import com.menemi.personobject.PersonObject;
import com.menemi.personobject.PhotoSetting;
import com.menemi.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Ui-Developer on 06.07.2016.
 */
public class PhotoListFragment extends Fragment implements View.OnClickListener {
    private View rootView = null;
    private PersonObject personObject = null;
    private int ownerId = -1;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.photos_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        ownerId = DBHandler.getInstance().getUserId();
        ImageView icon = (ImageView) rootView.findViewById(R.id.icon);
        TextView photoCount = (TextView) rootView.findViewById(R.id.photoCount);


        final TextView[] uploadPhotoText = new TextView[3];
        uploadPhotoText[0] = (TextView) rootView.findViewById(R.id.uploadPhotoText0);
        uploadPhotoText[1] = (TextView) rootView.findViewById(R.id.uploadPhotoText1);
        uploadPhotoText[2] = (TextView) rootView.findViewById(R.id.uploadPhotoText2);


        final RelativeLayout[] containers = new RelativeLayout[3];
        containers[0] = (RelativeLayout) rootView.findViewById(R.id.container0);
        containers[1] = (RelativeLayout) rootView.findViewById(R.id.container1);
        containers[2] = (RelativeLayout) rootView.findViewById(R.id.container2);


        final ImageView[] lockList = new ImageView[3];
        lockList[0] = (ImageView) rootView.findViewById(R.id.lock0);
        lockList[1] = (ImageView) rootView.findViewById(R.id.lock1);
        lockList[2] = (ImageView) rootView.findViewById(R.id.lock2);


        final ImageView[] photosList = new ImageView[3];
        photosList[0] = (ImageView) rootView.findViewById(R.id.photo0);
        photosList[1] = (ImageView) rootView.findViewById(R.id.photo1);
        photosList[2] = (ImageView) rootView.findViewById(R.id.photo2);


        if (purpose == PersonDataFragment.Purpose.MY_PROFILE) {
            LinearLayout iconContainer = (LinearLayout) rootView.findViewById(R.id.iconContainer);
            iconContainer.removeView(icon);
            photoCount.setText(getString(R.string.all_photos));
            photoCount.setOnClickListener(new AllMyPhotoClickListener(personObject));
            for (int i = 0; i < lockList.length; i++) {
                lockList[i].setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                lockList[i].setImageResource(R.color.no_color);

            }
        } else {
            photoCount.setText("" + personObject.getPhotoCountPrivate());
        }
        Log.d("TESTBLEAT", "Adding fragment 0 ");
        //(int personId, int requestedId, int photoNumber, int count, String quality,
DBHandler.getInstance().getPhotoUrls(personObject.getPersonId(), Utils.PICTURE_QUALITY_THUMBNAIL, true,(Object object) ->{

    ArrayList<PhotoSetting> pictures = (ArrayList<PhotoSetting>) object;
    Log.d("TESTBLEAT", "Adding fragment 1 "+pictures.size());
    if (pictures.size() == 0 && purpose != PersonDataFragment.Purpose.MY_PROFILE) {
        RelativeLayout totalContainer = (RelativeLayout) rootView.findViewById(R.id.totalContainer);
        totalContainer.removeAllViews();
    } else {

        for (int i = 0; i < photosList.length; i++) {
            if (i < photosList.length && i < pictures.size()) { // грузим до предпоследнего места или последнего фото

                if (purpose == PersonDataFragment.Purpose.MY_PROFILE) { // My profile
                    int finalI = i;
                    new PictureLoader(pictures.get(i).getPhotoUrl(), (Bitmap obj) ->{
                        pictures.get(finalI).setPhoto( obj);
                        photosList[finalI].setImageBitmap(pictures.get(finalI).getPhoto());
                        photosList[finalI].setOnClickListener(new PrivatePhotoChangeListener( pictures.get(finalI) ));
                    });


                } else { //another person profile
                    int finalI1 = i;
                    new PictureLoader(pictures.get(i).getPhotoUrl(), (Bitmap obj) ->{
                        pictures.get(finalI1).setPhoto(obj);
                        if(!pictures.get(finalI1).isUnlocked()) {
                            photosList[finalI1].setImageBitmap(Utils.blur(getActivity(), pictures.get(finalI1).getPhoto(),10));
                            lockList[finalI1].setImageResource(R.drawable.lock);
                        } else {
                            photosList[finalI1].setImageBitmap(pictures.get(finalI1).getPhoto());
                        }

                    photosList[finalI1].setOnClickListener(new PrivatePhotoOpenListener(pictures.get(finalI1)));

                    });
                }

                photosList[i].setBackgroundResource(R.color.no_color);
                photosList[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
                photosList[i].setPadding(0, 0, 0, 0);
                uploadPhotoText[i].setText("");

            } else if (purpose == PersonDataFragment.Purpose.MY_PROFILE && (i == photosList.length - 1 && i <= pictures.size() || i < photosList.length && i == pictures.size())) {
                if (purpose == PersonDataFragment.Purpose.MY_PROFILE) {
                    photosList[i].setOnClickListener(PhotoListFragment.this);
                    uploadPhotoText[i].setText(R.string.add_more_photo);
                    photosList[i].setBackgroundResource(R.color.orange);
                    photosList[i].setImageResource(R.drawable.photo_add);


                }
            }  else {
                containers[i].removeAllViews();
                containers[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }
    }
    Log.d("ShowMorePhoto", "Adding fragment" + pictures.size());
    if((purpose == PersonDataFragment.Purpose.MY_PROFILE && pictures.size() >= 2) || (purpose != PersonDataFragment.Purpose.MY_PROFILE && pictures.size() > 2)){
        Log.d("ShowMorePhoto", "dataContainerPhoto");
            PicturesListRowFragment picturesListRowFragment = new PicturesListRowFragment();
            picturesListRowFragment.setPersonObject(personObject);
            picturesListRowFragment.setPurpose(purpose);
            picturesListRowFragment.setPictures(pictures, 3);
            if(getActivity() == null || getFragmentManager() == null || !PhotoListFragment.this.isVisible()){
                return;
            }
        Log.d("ShowMorePhoto", "dataContainerPhoto 2");
            getFragmentManager().beginTransaction().replace(R.id.dataContainerPhoto, picturesListRowFragment).commitAllowingStateLoss();

    }
});



        return rootView;

    }

    public void setPersonObject(PersonObject personObject) {
        this.personObject = personObject;
    }


    private PersonDataFragment.Purpose purpose = PersonDataFragment.Purpose.LIKE;

    public void setPurpose(PersonDataFragment.Purpose purpose) {
        this.purpose = purpose;
    }

    @Override
    public void onClick(View view) {

        UploadPhotoDialogFragments dialogFragment = new UploadPhotoDialogFragments();
        dialogFragment.show(getFragmentManager(), "Dialog Fragment");
    }

    class PrivatePhotoChangeListener implements View.OnClickListener {
        PhotoSetting photo = null;

        PrivatePhotoChangeListener(PhotoSetting photo) {
            this.photo = photo;
        }

        @Override
        public void onClick(View view) {

            PhotoSettingsFragment photoSettingsFragment = new PhotoSettingsFragment();
            photoSettingsFragment.setPhotoSetting(photo);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.content, photoSettingsFragment);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
            photoSettingsFragment.setOnChangeListener(new PhotoSettingsFragment.OnChangeListener() {
                @Override
                public void onChangesMade(PhotoSetting photoSetting) {
                 Log.d("PhotoSettingsFragment", "PhotoListFragment");
                }
            });

        }
    }

    class AllMyPhotoClickListener implements View.OnClickListener {
        PersonObject personObject = null;

        public AllMyPhotoClickListener(PersonObject personObject) {
            this.personObject = personObject;
        }

        @Override
        public void onClick(View view) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            PictureDetatilsListFragment pictureDetatilsListFragment = new PictureDetatilsListFragment();

            fragmentTransaction.replace(R.id.content, pictureDetatilsListFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();

        }
    }

    class PrivatePhotoOpenListener implements View.OnClickListener {
        PhotoSetting photo = null;

        PrivatePhotoOpenListener(PhotoSetting photo) {
            this.photo = photo;
        }

        @Override
        public void onClick(View view) {
            if(!photo.isUnlocked()) {

                PayForPhotoDialogFragment dialogFragment = new PayForPhotoDialogFragment();
                dialogFragment.setPhotoSetting(photo);
                dialogFragment.show(getFragmentManager(), "Pay Fragment");
            } else{
                PhotoFragment photoFragment = new PhotoFragment();
                ArrayList<PhotoSetting> photoSetting = new ArrayList<>();
                photoSetting.add(photo);
                photoFragment.setUrlsArray(photoSetting);
                photoFragment.setFullScreen(true);
                photoFragment.setPageNumber(0);
                getFragmentManager().beginTransaction().replace(R.id.content, photoFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        }
    }

}
