package com.menemi.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.menemi.R;
import com.menemi.personobject.Gift;
import com.menemi.personobject.PersonObject;

import java.util.ArrayList;

/**
 * Created by Ui-Developer on 09.09.2016.
 */
public class GiftsSetFragment extends Fragment {
    private View rootView = null;
    private ArrayList<Gift> gifts;
    private ImageView[] images = new ImageView[4];
    private int start;
   private PersonObject personObject;

    public void setGifts(ArrayList<Gift> gifts, int start) {
        this.gifts = gifts;
        this.start = start;
    }

    public void setPersonObject(PersonObject personObject) {
        this.personObject = personObject;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.gifts_set, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }

        images[0] = (ImageView) rootView.findViewById(R.id.gift0);
        images[1] = (ImageView) rootView.findViewById(R.id.gift1);
        images[2] = (ImageView) rootView.findViewById(R.id.gift2);
        images[3] = (ImageView) rootView.findViewById(R.id.gift3);




        for (int i = start, j = 0; i < gifts.size() && j < images.length; i++, j++) {
            images[j].setImageBitmap(gifts.get(i).getImage());
            images[j].setOnClickListener(new GiftChooseListener(gifts.get(i), personObject));
        }
        return rootView;
    }

    class GiftChooseListener implements View.OnClickListener{
        private Gift gift;
        private PersonObject personObject;

        public GiftChooseListener(Gift gift, PersonObject personObject) {
            this.gift = gift;
            this.personObject = personObject;
        }

        @Override
        public void onClick(View v) {
            GiftShureDialogFragment giftShureDialogFragment = new GiftShureDialogFragment();
            giftShureDialogFragment.setGiftToPresent(gift);
            giftShureDialogFragment.setPersonObject(personObject);
            giftShureDialogFragment.show(getFragmentManager(), "Dialog Fragment");
        }
    }

}
