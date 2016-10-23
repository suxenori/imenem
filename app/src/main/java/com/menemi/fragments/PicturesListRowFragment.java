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
 * Created by Ui-Developer on 01.08.2016.
 */
public class PicturesListRowFragment extends Fragment implements View.OnClickListener{
    private View rootView = null;
    private PersonObject personObject = null;
    private ArrayList<PhotoSetting> pictures = new ArrayList<>();
    private int start = 3;

    public void setPictures(ArrayList<PhotoSetting> pictures,int start ) {
        this.start = start;
        this.pictures = pictures;

    }
    public void setStart(int start) {
        this.start = start;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.photos_row_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        Log.d("ShowMorePhotoClick", "fragment");
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

            for (int i = 0; i < lockList.length; i++) {
                lockList[i].setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                lockList[i].setImageResource(R.color.no_color);

            }
        }
        int ownerId = DBHandler.getInstance().getUserId();



                Log.d("ShowMorePhotoClick", "pics" + pictures.size());
                if (pictures.size() == 0 && purpose != PersonDataFragment.Purpose.MY_PROFILE) {
                    RelativeLayout totalContainer = (RelativeLayout) rootView.findViewById(R.id.totalContainer);
                    totalContainer.removeAllViews();
                } else {

                 //   if(pictures.size() == 0)
                    for (int i = 0; i < photosList.length; i++) {
                        if (i < photosList.length - 1 && i+start < pictures.size()) { // грузим до предпоследнего места или последнего фото
                            Log.d("ShowMorePhotoClick", "row" + pictures.size());
                            if (purpose == PersonDataFragment.Purpose.MY_PROFILE) { // My profile
                                int finalI = i;
                                new PictureLoader(pictures.get(i+start).getPhotoUrl(), (Bitmap obj) -> {
                                    pictures.get(finalI+start).setPhoto( obj);
                                    photosList[finalI].setImageBitmap(pictures.get(finalI+start).getPhoto());
                                    photosList[finalI].setOnClickListener(new PrivatePhotoChangeListener(pictures.get(finalI+start)));
                                });
                            } else { //another person profile
                                int finalI = i;
                                new PictureLoader(pictures.get(i+start).getPhotoUrl(), (Bitmap obj) -> {
                                    pictures.get(finalI+start).setPhoto( obj);
                                    if(!pictures.get(finalI + start).isUnlocked()) {
                                        photosList[finalI].setImageBitmap(Utils.blur(getActivity(), pictures.get(finalI + start).getPhoto(),10));
                                        lockList[finalI].setImageResource(R.drawable.lock);
                                    } else {
                                        photosList[finalI].setImageBitmap(pictures.get(finalI + start).getPhoto());
                                    }
                                photosList[finalI].setOnClickListener(new PrivatePhotoOpenListener(pictures.get(finalI+start)));

                                });
                                //TODO: Open dialog to buy photo
                            }

                            photosList[i].setBackgroundResource(R.color.no_color);
                            photosList[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
                            photosList[i].setPadding(0, 0, 0, 0);
                            uploadPhotoText[i].setText("");

                        } else if (purpose == PersonDataFragment.Purpose.MY_PROFILE && (i == photosList.length - 1 && i+start <= pictures.size() || (i < photosList.length && i+start == pictures.size() && pictures.size() > 2) )) {
                            Log.d("ShowMorePhotoClick", "pics" + pictures.size());
                                photosList[i].setOnClickListener(PicturesListRowFragment.this);
                                uploadPhotoText[i].setText(R.string.add_more_photo);
                                photosList[i].setBackgroundResource(R.color.orange);
                                photosList[i].setImageResource(R.drawable.photo_add);



                        } else if (i == photosList.length - 1 && i+start < pictures.size() && purpose != PersonDataFragment.Purpose.MY_PROFILE ) {

                            if(start + i < pictures.size()) {
                                uploadPhotoText[i].setText(R.string.show_more_photo);
                                photosList[i].setBackgroundResource(R.color.orange);
                                photosList[i].setImageResource(R.drawable.photo_add);
                                lockList[i].setImageResource(R.color.no_color);
                                photosList[i].setOnClickListener(new ShowMorePhotoClickListener(personObject, photosList[i], uploadPhotoText[i], lockList[i], pictures.get(i)));
                            } else { // if this is a last photo, no need to show message "load more", just load this freaking photo and that's it
                                int finalI = i;
                                new PictureLoader(pictures.get(i+start).getPhotoUrl(), (Bitmap obj) -> {
                                    pictures.get(finalI + start).setPhoto( obj);
                                    if(!pictures.get(finalI + start).isUnlocked()) {
                                        photosList[finalI].setImageBitmap(Utils.blur(getActivity(), pictures.get(finalI + start).getPhoto(),10));
                                        lockList[finalI].setImageResource(R.drawable.lock);
                                    } else {
                                        photosList[finalI].setImageBitmap(pictures.get(finalI + start).getPhoto());
                                    }
                                    photosList[finalI].setOnClickListener(new PrivatePhotoOpenListener(pictures.get(finalI + start)));
                                });

                                photosList[i].setBackgroundResource(R.color.no_color);
                                photosList[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
                                photosList[i].setPadding(0, 0, 0, 0);
                                uploadPhotoText[i].setText("");
                            }
                        } else {
                            containers[i].removeAllViews();
                            containers[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        }
                    }
                }


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


    class ShowMorePhotoClickListener implements View.OnClickListener{
        PersonObject personObject = null;
        ImageView imageView;
        TextView textView;
        PhotoSetting bitmap;
        ImageView lock;

        public ShowMorePhotoClickListener(PersonObject personObject, ImageView imageView, TextView textView, ImageView lock, PhotoSetting bitmap) {
            this.personObject = personObject;
            this.imageView = imageView;
            this.textView = textView;
            this.bitmap = bitmap;
            this.lock = lock;
        }

        @Override
        public void onClick(View view) {
            new PictureLoader(bitmap.getPhotoUrl(), (Bitmap obj) -> {
                bitmap.setPhoto( obj);
                if(!bitmap.isUnlocked()) {
                    imageView.setImageBitmap(Utils.blur(getActivity(), bitmap.getPhoto(),10));
                    lock.setImageResource(R.drawable.lock);
                } else {
                    imageView.setImageBitmap(bitmap.getPhoto());
            }
                imageView.setBackgroundResource(R.color.no_color);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(0, 0, 0, 0);
                textView.setText("");

                imageView.setOnClickListener(new PrivatePhotoOpenListener(bitmap));
            });
            FragmentTransaction transition = getFragmentManager().beginTransaction();
            PicturesListRowFragment picturesListRowFragment = new PicturesListRowFragment();
            picturesListRowFragment.setPersonObject(personObject);
            picturesListRowFragment.setPurpose(purpose);
            picturesListRowFragment.setPictures(pictures, start+3);

            transition.add(R.id.dataContainerPhoto2, picturesListRowFragment);
            transition.commitAllowingStateLoss();
        }
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
