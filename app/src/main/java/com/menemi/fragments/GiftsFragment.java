package com.menemi.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.PersonObject;
import com.menemi.personobject.PersonalGift;

import java.util.ArrayList;

/**
 * Created by Ui-Developer on 01.07.2016.
 */
public class GiftsFragment extends Fragment {
    private View rootView = null;
    ArrayList<PersonalGift> giftsList = null;
    PersonObject personObject;


    public void setGifts(ArrayList<PersonalGift> giftsList) {
        this.giftsList = giftsList;
    }

    public void setPersonObject(PersonObject personObject) {
        this.personObject = personObject;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.gifts_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }

        LinearLayout viewList = (LinearLayout) rootView.findViewById(R.id.friendsScroll);


        if(personObject.getPersonId() != DBHandler.getInstance().getUserId()) {
            int randomGiftId = (int) (Math.random() * DBHandler.getInstance().getGiftsCount());
            Bitmap giftPicture = DBHandler.getInstance().getGiftById(randomGiftId);
            View item0 = inflater.inflate(R.layout.gift_image, viewList, false);
            ImageView image0 = (ImageView) item0.findViewById(R.id.giftPhoto);
            image0.setImageBitmap(giftPicture);
            image0.setBackgroundResource(R.drawable.add_gift_bg);
            image0.setOnClickListener(new MakeGiftListener(personObject));
            ImageView addGiftButton = (ImageView) item0.findViewById(R.id.addGiftButton);
            addGiftButton.setImageResource(R.drawable.add_gift);
            viewList.addView(item0);
        }

        if (giftsList != null) {

            for (int i = 0; i < giftsList.size(); i++) {
                View item = inflater.inflate(R.layout.gift_image, viewList, false);
                ImageView image = (ImageView) item.findViewById(R.id.giftPhoto);
                Bitmap picture = DBHandler.getInstance().getGiftById(giftsList.get(i).getGiftId());
                image.setImageBitmap(picture);
                viewList.addView(item);
                item.setOnClickListener(new ItemClickListener(giftsList.get(i)));
            }
        }


        return rootView;
    }

    class ItemClickListener implements View.OnClickListener {


        private PersonalGift gift = null;

        public ItemClickListener(PersonalGift gift) {
            this.gift = gift;
        }

        @Override
        public void onClick(View view) {
            GiftInfoDialogFragment giftInfoDialogFragment = new GiftInfoDialogFragment();
            giftInfoDialogFragment.setGift(gift);

            giftInfoDialogFragment.show(getFragmentManager(), "Dialog Fragment");
        }
    }
    class MakeGiftListener implements View.OnClickListener {


        PersonObject personObject;

        public MakeGiftListener(PersonObject personObject) {
            this.personObject = personObject;
        }

        @Override
        public void onClick(View view) {

            GiftsListFragment giftsListFragment = new GiftsListFragment();
            giftsListFragment.setPersonObject(personObject);
            getFragmentManager().beginTransaction().replace(R.id.content, giftsListFragment).addToBackStack(null).commitAllowingStateLoss();
        }
    }
}
