package com.menemi.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.menemi.R;
import com.menemi.dbfactory.rest.PictureLoader;
import com.menemi.interests_classes.InterestContainer;
import com.menemi.interests_classes.PersonInterestsFragment;
import com.menemi.personobject.Interests;
import com.menemi.personobject.PersonObject;

import java.util.ArrayList;

/**
 * Created by Ui-Developer on 06.07.2016.
 */
public class InterestsFragment extends Fragment {
    private View rootView = null;

    ArrayList<Interests> interests;
    View[] items = null;
    boolean isLayoutSet = false;
    PersonObject ownerProfile = null;

    public void setOwnerProfile(PersonObject ownerProfile) {
        this.ownerProfile = ownerProfile;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        if(purpose != PersonDataFragment.Purpose.MY_PROFILE && interests.size() == 0){
            return null;
        }
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.interests_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        final LinearLayout interestsLayout = (LinearLayout) rootView.findViewById(R.id.interestsLayout);
        TextView changeButton = (TextView) rootView.findViewById(R.id.changeButton);
        if (purpose != PersonDataFragment.Purpose.MY_PROFILE) {

            changeButton.setText("");
        } else {
            changeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PersonInterestsFragment.setChoiceListener(new PersonInterestsFragment.ChangeInterestListener()
                    {
                        @Override
                        public void changeInterests(ArrayList<Interests> arrayList)
                        {
                            ownerProfile.setInterests(arrayList);
                            setInterests(arrayList);
                            ownerFragment.refreshInterests();
                        }


                    });
                    Intent getInterestEdit = new Intent(getActivity(), InterestContainer.class);
                    startActivity(getInterestEdit);
                }
            });
        }
        final Context ctx = getActivity();


        refreshFragment(ctx,inflater,interestsLayout);


        return rootView;
    }

    private void prepareOwnInterests(LayoutInflater inflater, LinearLayout interestsLayout) {
        items = new View[interests.size()];

        for (int i = 0; i < interests.size(); i++) {


                View item = inflater.inflate(R.layout.interest_item, interestsLayout, false);
                TextView interest = (TextView) item.findViewById(R.id.interestText);
                interest.setText(interests.get(i).getInterest());
                if (interests.get(i).isMutual()) {
                    interest.setTextColor(getResources().getColor(R.color.orange_text));
                }
            new PictureLoader(interests.get(i).getGroupIconUrl(), (Bitmap icon)->{
                ImageView groupIcon = (ImageView) item.findViewById(R.id.groupIcon);
                groupIcon.setImageBitmap(icon);
            });
                interestsLayout.addView(item);
                items[i] = item;
                   }

    }

    ArrayList<ArrayList<Integer>> preparePositions(int widthMeasureSpec, int[] sizes) {

        ArrayList<ArrayList<Integer>> elementsInRows = new ArrayList<>();
        elementsInRows.add(new ArrayList<Integer>());
        int spaceForView = widthMeasureSpec;
        int spaceAvailable = spaceForView;

        for (int i = 0; i < sizes.length; i++) {
            if (findMax(sizes) != -1) {

                int nextElementIndex = -1;
                if (elementsInRows.get(elementsInRows.size() - 1).size() == 0) {
                    nextElementIndex = findMax(sizes);

                } else if (findBestFit(sizes, spaceAvailable) != -1) {
                    nextElementIndex = findBestFit(sizes, spaceAvailable);

                }

                if (findBestFit(sizes, spaceAvailable) == -1) {
                    spaceAvailable = spaceForView;
                    elementsInRows.add(new ArrayList<Integer>());
                    i--;
                    continue;
                }

                elementsInRows.get(elementsInRows.size() - 1).add(nextElementIndex);
                spaceAvailable -= sizes[nextElementIndex];
                sizes[nextElementIndex] = 0;

            }
        }

        return elementsInRows;
    }

    int findBestFit(int[] array, int space) {
        int max = 0;
        int index = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max && array[i] <= space) {
                max = array[i];
                index = i;
            }
        }

        return index;

    }

    void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    int findMax(int[] array) {
        int max = 0;
        int index = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
                index = i;
            }
        }

        return index;

    }

    private PersonDataFragment.Purpose purpose = PersonDataFragment.Purpose.LIKE;

    public void setPurpose(PersonDataFragment.Purpose purpose) {
        this.purpose = purpose;
    }

    public void setInterests(ArrayList<Interests> interests) {
        this.interests = interests;
    }
    private ViewTreeObserver vto;
    public void refreshFragment(final Context ctx, LayoutInflater inflater, final LinearLayout interestsLayout){
        interestsLayout.removeAllViews();
        prepareOwnInterests(inflater, interestsLayout);

        if(vto == null){
            vto = interestsLayout.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    auxilary(ctx, interestsLayout);
                }
            });
        } else{

            auxilary(ctx,interestsLayout);
        }


    }
    private void auxilary(Context ctx, ViewGroup interestsLayout){
        if (!isLayoutSet) {
            isLayoutSet = true;

            int[] sizes = new int[interests.size()];

            for (int i = 0; i < items.length; i++) {
                sizes[i] = items[i].getWidth();
            }
            interestsLayout.removeAllViews();


            ArrayList<ArrayList<Integer>> preparedPositions = preparePositions(interestsLayout.getWidth(), sizes);

            LinearLayout[] layoutInUse = new LinearLayout[preparedPositions.size()];
            LinearLayout.LayoutParams layoutInUseParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            for (int i = 0; i < preparedPositions.size(); i++) {
                Log.d("InterestsFragment", "add row");

                layoutInUse[i] = new LinearLayout(ctx);
                layoutInUse[i].setLayoutParams(layoutInUseParams);

                for (int j = 0; j < preparedPositions.get(i).size(); j++) {
                    LinearLayout.LayoutParams lp = null;
                    if (preparedPositions.get(i).size() == 1) {
                        lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    } else if (i < preparedPositions.get(i).size() - 1) {
                        lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        lp.weight = 1f;
                    } else {
                        lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
                    items[preparedPositions.get(i).get(j)].setLayoutParams(lp);


                    layoutInUse[i].addView(items[preparedPositions.get(i).get(j)], lp);
                }

            }
            for (int i = 0; i < layoutInUse.length; i++) {
                interestsLayout.addView(layoutInUse[i], layoutInUseParams);
            }

        }
    }

    FragmentWithInfoAboutPerson ownerFragment;
    public void setOwnerFragment(FragmentWithInfoAboutPerson ownerFragment) {
        this.ownerFragment = ownerFragment;
    }
}
