package com.menemi.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.menemi.PersonPage;
import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.Gift;
import com.menemi.personobject.PersonObject;

import java.util.LinkedList;

/**
 * Created by tester03 on 11.07.2016.
 */
public class GiftShureDialogFragment extends android.app.DialogFragment {
    private View dialogView;
    private Gift giftToPresent;
    private PersonObject personObject;

    Activity ctx;
    private static boolean isOpened = false;
    private static LinkedList<android.app.DialogFragment> dialogs = new LinkedList<>();

    public GiftShureDialogFragment(){
        super();
        for (android.app.DialogFragment dialogFragment: dialogs) {
            dialogFragment.dismiss();
        }
        dialogs.add(this);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ctx=getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.gift_shure, null);


        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.full_screen_dialog);
        builder.setView(dialogView);
        Button closeButton = (Button) dialogView.findViewById(R.id.closeButton);

        closeButton.setOnClickListener((View view) ->{getDialog().dismiss();});

        Button sendGift =  (Button) dialogView.findViewById(R.id.sendGift);
        sendGift.setOnClickListener(new SendGiftButtonListener(personObject,giftToPresent));

        return builder.create();
    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawableResource(R.color.no_color);

        ImageView gift = (ImageView)dialogView.findViewById(R.id.gift);
        gift.setImageBitmap(giftToPresent.getImage());

        TextView nameAgeText = (TextView)dialogView.findViewById(R.id.nameAgeText);
        nameAgeText.setText(personObject.getPersonName());

        TextView price = (TextView)dialogView.findViewById(R.id.price);
        price.setText("" + giftToPresent.getPrice());

        return view;
    }

    public void setGiftToPresent(Gift giftToPresent) {
        this.giftToPresent = giftToPresent;
    }

    public void setPersonObject(PersonObject personObject) {
        this.personObject = personObject;
    }

    class SendGiftButtonListener implements View.OnClickListener{
        PersonObject personObject;
        Gift giftToPresent;

        public SendGiftButtonListener(PersonObject personObject, Gift giftToPresent) {
            this.personObject = personObject;
            this.giftToPresent = giftToPresent;
        }

        @Override
    public void onClick(View v) {
            Log.d("GiftDialogTest", "my credits " + DBHandler.getInstance().getMyProfile().getPersonCredits() + " giftPrice " + giftToPresent.getPrice());
            if(DBHandler.getInstance().getMyProfile().getPersonCredits() >= giftToPresent.getPrice()){
                DBHandler.getInstance().buyGift(giftToPresent.getGiftId(), personObject.getPersonId(), (Object object) ->{
                    //this line to keep cashed profile up to date
                    DBHandler.getInstance().getMyProfile().setPersonCredits(DBHandler.getInstance().getMyProfile().getPersonCredits()- giftToPresent.getPrice());

                    DBHandler.getInstance().getOtherProfile(personObject.getPersonId(), new DBHandler.ResultListener() {
                        @Override
                        public void onFinish(Object object) {

                            FragmentTransaction fragmentTransaction = ctx.getFragmentManager().beginTransaction();
                            PersonDataFragment personDataFragment = new PersonDataFragment();
                            personDataFragment.setPurpose(PersonDataFragment.Purpose.PROFILE);
                            personDataFragment.setPersonObject((PersonObject)object);
                            //Log.d("LOG SYKA", "blya " + ((PersonObject) object).getPhotoCount());
                            fragmentTransaction.replace(R.id.content, personDataFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commitAllowingStateLoss();

                            dismiss();


                        }});
                });
            } else {

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                BuyCoinsFragment buyCoinsFragment = new BuyCoinsFragment();
                fragmentTransaction.replace(R.id.content, buyCoinsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();
                dismiss();
            }
            PersonPage.prepareNavigationalHeader();
    }
}
   /* class OpenProfileListener implements View.OnClickListener {
        int profileId = 0;

        public OpenProfileListener(int profileId) {
            this.profileId = profileId;
        }

        @Override
        public void onClick(View view) {
            DBHandler.getInstance().getOtherProfile(profileId, new DBHandler.ResultListener() {
                @Override
                public void onFinish(Object object) {

                    FragmentTransaction fragmentTransaction = ctx.getFragmentManager().beginTransaction();
                    PersonDataFragment personDataFragment = new PersonDataFragment();
                    personDataFragment.setPurpose(PersonDataFragment.Purpose.PROFILE);
                    personDataFragment.setPersonObject((PersonObject)object);
                    //Log.d("LOG SYKA", "blya " + ((PersonObject) object).getPhotoCount());
                    fragmentTransaction.replace(R.id.content, personDataFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commitAllowingStateLoss();

                        dismiss();


                }});
            }
        }
*/
    @Override
    public void show(FragmentManager manager, String tag) {
        if(isOpened){
            isOpened = false;
            return;
        }
        isOpened = true;
        super.show(manager, tag);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        isOpened = false;
        super.onDismiss(dialog);
    }
}