package com.menemi.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.dbfactory.rest.PictureLoader;
import com.menemi.personobject.PersonObject;
import com.menemi.personobject.PersonalGift;
import com.menemi.utils.Utils;

import java.util.LinkedList;

/**
 * Created by tester03 on 11.07.2016.
 */
public class GiftInfoDialogFragment extends android.app.DialogFragment {
    private View dialogView;
    private PersonalGift personalGift;
    Activity ctx;
    private static boolean isOpened = false;
    private static LinkedList<android.app.DialogFragment> dialogs = new LinkedList<>();
    public GiftInfoDialogFragment(){
        super();
        for (android.app.DialogFragment dialogFragment: dialogs) {
            if(dialogFragment != null) {
                dialogFragment.dismiss();
            }
        }
        dialogs.add(this);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ctx=getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.gift_info, null);


        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.full_screen_dialog);
        builder.setView(dialogView);
        Button closeButton = (Button) dialogView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return builder.create();
    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawableResource(R.color.no_color);

        ImageView gift = (ImageView)dialogView.findViewById(R.id.gift);
        gift.setImageBitmap(DBHandler.getInstance().getGiftById(personalGift.getGiftId()));

        TextView nameAgeText = (TextView)dialogView.findViewById(R.id.nameAgeText);
        nameAgeText.setText(getString(R.string.who_sended, personalGift.getPersonName()));

        TextView sendDate = (TextView)dialogView.findViewById(R.id.sendDate);
        sendDate.setText("" + personalGift.getSendDate());


        final ImageView sender = (ImageView)dialogView.findViewById(R.id.photo);
        new PictureLoader(personalGift.getAvatarUrl(), (Bitmap photo) ->{

                if(photo != null){
                    sender.setImageBitmap(Utils.getCroppedBitmap(photo));
                } else {
                    Bitmap emptyPhoto = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.empty_photo);
                    sender.setImageBitmap(Utils.getCroppedBitmap(emptyPhoto));

                }
                sender.setOnClickListener(new OpenProfileListener(personalGift.getPersonId()));
            });

        return view;
    }

    public void setGift(PersonalGift gift) {
        this.personalGift = gift;
    }


    class OpenProfileListener implements View.OnClickListener {
        int profileId = 0;

        public OpenProfileListener(int profileId) {
            this.profileId = profileId;
        }

        @Override
        public void onClick(View view) {
            DBHandler.getInstance().getOtherProfile(profileId, new DBHandler.ResultListener() {
                @Override
                public void onFinish(Object object) {

                    PersonObject profile = (PersonObject)object;
                    FragmentTransaction fragmentTransaction = ctx.getFragmentManager().beginTransaction();
                    PersonDataFragment personDataFragment = new PersonDataFragment();
                    personDataFragment.setPurpose(PersonDataFragment.Purpose.PROFILE);
                    personDataFragment.setPersonObject(profile);
                    fragmentTransaction.replace(R.id.content, personDataFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commitAllowingStateLoss();

                        dismiss();


                }});
            }
        }

  /*  @Override
    public void show(FragmentManager manager, String tag) {
        if(isOpened){
            isOpened = false;
            return;
        }
        isOpened = true;
        super.show(manager, tag);
    }*/

    @Override
    public void onDismiss(DialogInterface dialog) {
        isOpened = false;
        super.onDismiss(dialog);
    }
}