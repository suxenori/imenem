package com.menemi.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.menemi.PersonPage;
import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.PersonObject;
import com.menemi.personobject.PhotoSetting;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by tester03 on 15.07.2016.
 */
public class PayForPhotoDialogFragment extends DialogFragment
{
    private static LinkedList<DialogFragment> dialogs = new LinkedList<>();
    PhotoSetting photoSetting = null;
    public PayForPhotoDialogFragment(){
        super();
        for (DialogFragment dialogFragment: dialogs) {
            dialogFragment.dismiss();
        }
        dialogs.add(this);
}

    public void setPhotoSetting(PhotoSetting photoSetting) {
        this.photoSetting = photoSetting;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {


        PersonObject ownerProfile = DBHandler.getInstance().getMyProfile();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = View.inflate(getActivity(),R.layout.pay_for_photo_dialog_fragment,null);
        builder.setView(dialogView);
        //setStyle(android.app.DialogFragment.STYLE_NO_TITLE, R.style.full_screen_dialog);
        TextView balance = (TextView) dialogView.findViewById(R.id.balance);
        TextView  price = (TextView) dialogView.findViewById(R.id.price);
        balance.setText("" +ownerProfile.getPersonCredits());
        price.setText("" +photoSetting.getPrice());


        Button cancelButton = (Button)dialogView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Button openButton = (Button) dialogView.findViewById(R.id.openButton);
        openButton.setText(getString(R.string.buy_money));
        openButton.setOnClickListener(new PrivatePhotoBuyListener(ownerProfile, photoSetting));
        return  builder.create();
    }
    class PrivatePhotoBuyListener implements View.OnClickListener {
        PersonObject ownerProfile;
        PhotoSetting pictureToOpen;

        public PrivatePhotoBuyListener(PersonObject ownerProfile, PhotoSetting pictureToOpen) {
            this.ownerProfile = ownerProfile;
            this.pictureToOpen = pictureToOpen;
        }

        @Override
    public void onClick(View view) {
        if(ownerProfile.getPersonCredits() >= pictureToOpen.getPrice()){
            DBHandler.getInstance().unlockPhoto(pictureToOpen, (Object object) ->{
                DBHandler.getInstance().getMyProfile().setPersonCredits(ownerProfile.getPersonCredits() - pictureToOpen.getPrice());
                PersonPage.prepareNavigationalHeader();
                Log.v("PayForPhotoDialog", "photo is unlocked id = " + pictureToOpen.getPhotoId());
                dismiss();
                if(getActivity() == null || getFragmentManager() == null){
                    return;
                }
                PhotoFragment photoFragment = new PhotoFragment();
                ArrayList<PhotoSetting> photoSetting = new ArrayList<>();
                photoSetting.add((PhotoSetting) object);
                photoFragment.setUrlsArray(photoSetting);
                photoFragment.setFullScreen(true);
                photoFragment.setPageNumber(0);
                getFragmentManager().beginTransaction().replace(R.id.content, photoFragment).addToBackStack(null).commitAllowingStateLoss();
            });
        } else {
            dismiss();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            BuyCoinsFragment buyCoinsFragment = new BuyCoinsFragment();
            fragmentTransaction.replace(R.id.content, buyCoinsFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
        }

    }
}
}
