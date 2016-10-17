package com.menemi.fragments;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.menemi.R;
import com.menemi.personobject.Reward;
import com.menemi.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ui-Developer on 01.07.2016.
 */
public class AwardsFragment extends Fragment {
    private View rootView = null;
    ArrayList<Reward> awardsList = null;
    private HashMap<String, Drawable> awardsPictures = new HashMap<>();
    public void setAwards(ArrayList<Reward> awardsList) {
        this.awardsList = awardsList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.awards_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }

        }

        awardsPictures.put( getString(R.string.Flowers), Utils.getDrawableRes(getActivity(), R.drawable.prise_like));
        awardsPictures.put( getString(R.string.Martini),Utils.getDrawableRes(getActivity(), R.drawable.prise_active));
        awardsPictures.put( getString(R.string.Kiss),Utils.getDrawableRes(getActivity(), R.drawable.prise_dialog));
        awardsPictures.put( getString(R.string.Candies),Utils.getDrawableRes(getActivity(), R.drawable.prise_magnet));
        awardsPictures.put( getString(R.string.Car),Utils.getDrawableRes(getActivity(), R.drawable.prise_poll));

        Log.d("AwardsFragment", "called");
        LinearLayout awardsScroll = (LinearLayout) rootView.findViewById(R.id.awardsScroll);
        Log.d("AwardsFragment", "list " + awardsList.size());

       /* if(awardsList != null) {
            Log.d("AwardsFragment", "called");
            for (int i = 0; i < awardsList.size(); i++) {
                Log.d("AwardsFragment", "giftsList.get(i).getGiftName() = " + awardsList.get(i).getGiftName());
                Log.d("AwardsFragment", "giftsPictures.get(giftsList.get(i).getGiftName()) = " + awardsPictures.get(((Reward)awardsList.get(i)).getGiftName()));

                if(awardsPictures.get(awardsList.get(i).getGiftName()) != null){

                    Drawable awardPicture = awardsPictures.get(awardsList.get(i).getGiftName());
                    View item = inflater.inflate(R.layout.gift_image, awardsScroll, false);
                    ImageView image = (ImageView) item.findViewById(R.id.giftPhoto);
                    image.setImageDrawable(awardPicture);
                    awardsScroll.addView(item);
                }

            }
        }*/
        return rootView;
    }

}
