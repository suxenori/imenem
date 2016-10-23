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
import android.widget.TextView;

import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.PersonObject;
import com.menemi.utils.Utils;

import java.util.ArrayList;


/**
 * Created by tester03 on 17.06.2016.
 */

public class PeopleRowFragment extends Fragment {

    private View rootView = null;
    private LayoutInflater inflater;

    private int rowsShown = 0;
    ArrayList<PersonObject> personObjects;
    private boolean isLastRow;

    public void setPersonObjects(ArrayList<PersonObject> personObjects) {
        this.personObjects = personObjects;
    }

    public void setLastRow(boolean lastRow) {
        isLastRow = lastRow;
    }

    public void setRowsShown(int rowsShown) {
        this.rowsShown = rowsShown;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.person_set_fragment, null);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
                parent.removeAllViews();
            }
        }

        int start = rowsShown * 3;

        for (int i = start; i < start + 3; i++) {
            if (i < personObjects.size()) {
                final int finalI = i;
                if (personObjects.get(i) != null) {
                    DBHandler.getInstance().getAvatar(personObjects.get(i).getPersonId(), (Object object) -> {
                        Bitmap avatar = ((Bitmap) object);
                        personObjects.get(finalI).setPersonAvatar(avatar);
                        if (finalI == start + 2 || finalI == personObjects.size() - 1) {

                            prepareViews(isLastRow, start);
                        }
                    });
                } else {
                    prepareViews(isLastRow, start);
               break;
                }
            }
        }
        return rootView;


    }

    private void prepareViews(boolean isLastRow, int start) {
        if (personObjects.size() > start && personObjects.get(start) != null) {
            LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.personItem0);
            layout.addView(createPicture(inflater, personObjects.get(start), false));
        }
        if (personObjects.size() > start + 1 && personObjects.get(start + 1) != null) {
            LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.personItem1);
            layout.addView(createPicture(inflater, personObjects.get(start + 1), false));
        }


        if (personObjects.size() > start + 2 && personObjects.get(start + 2) != null) {
            LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.personItem2);
            if (start + 2 == personObjects.size() - 1) {
                layout.addView(createPicture(inflater, personObjects.get(start + 2), false));
            } else {
                layout.addView(createPicture(inflater, personObjects.get(start + 2), isLastRow));
            }
        }
    }


    @Nullable
    public View createPicture(LayoutInflater inflater, PersonObject personObject, boolean isForShowMore) {
        View photoView = null;
        try {
            photoView = inflater.inflate(R.layout.person_list_item, null);
        } catch (OutOfMemoryError error) {
            System.gc();
            return createPicture(inflater, personObject, isForShowMore);

        }

        if (personObject != null) {
            if (isForShowMore) {
                ImageView photo = (ImageView) photoView.findViewById(R.id.photo);
                photo.setImageBitmap(personObject.getPersonAvatar());
                LinearLayout linearLayout = (LinearLayout) photoView.findViewById(R.id.linearLayout);
                linearLayout.setVisibility(View.GONE);
                ImageView forShowMore = (ImageView) photoView.findViewById(R.id.forShowMore);
                forShowMore.setOnClickListener(new ShowMoreListener(personObject, photoView));

            } else {

                String nameAge = "" + personObject.getPersonName() + ", " + personObject.getPersonAge();
                TextView nameAgeText = (TextView) photoView.findViewById(R.id.nameAgeText);
                nameAgeText.setText(nameAge);

                if (!personObject.isOnline()) {
                    ImageView online = (ImageView) photoView.findViewById(R.id.online);
                    online.setImageResource(R.color.no_color);
                }

                TextView locationText = (TextView) photoView.findViewById(R.id.locationText);
                locationText.setText(personObject.getPersonCurrLocation());
                ImageView photo = (ImageView) photoView.findViewById(R.id.photo);
                if (personObject.getPersonAvatar() != null) {
                    photo.setImageBitmap(personObject.getPersonAvatar());
                } else {
                    photo.setImageResource(R.drawable.empty_photo);
                }


                photo.setOnClickListener(new OpenPersonDetailsListener(personObject));
                ImageView forShowMore = (ImageView) photoView.findViewById(R.id.forShowMore);
                forShowMore.setVisibility(View.GONE);
                TextView moreText = (TextView) photoView.findViewById(R.id.moreText);
                moreText.setVisibility(View.GONE);

                Log.d("PersonListItemFragment", "p != n, ifsm = f " + personObject.getPersonAvatar());
            }
        }
        Log.d("PersonListItemFragment", "created");
        return photoView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    class ShowMoreListener implements View.OnClickListener {
        PersonObject personObject = null;
        View photoView;

        public ShowMoreListener(PersonObject object, View photoView) {
            this.personObject = object;
            this.photoView = photoView;
        }

        @Override
        public void onClick(View view) {
            rowsShown++;
            //addRow(rowsShown, true);
            String nameAge = "" + personObject.getPersonName() + ", " + personObject.getPersonAge();
            TextView nameAgeText = (TextView) photoView.findViewById(R.id.nameAgeText);
            nameAgeText.setText(nameAge);

            TextView locationText = (TextView) photoView.findViewById(R.id.locationText);
            locationText.setText(personObject.getPersonCurrLocation());
            if (personObject.getPersonAvatar() != null) {
                ImageView photo = (ImageView) photoView.findViewById(R.id.photo);
                photo.setImageBitmap(personObject.getPersonAvatar());
            }
            LinearLayout linearLayout = (LinearLayout) photoView.findViewById(R.id.linearLayout);
            linearLayout.setVisibility(View.VISIBLE);

            ImageView forShowMore = (ImageView) photoView.findViewById(R.id.forShowMore);
            forShowMore.setVisibility(View.GONE);
            TextView moreText = (TextView) photoView.findViewById(R.id.moreText);
            moreText.setVisibility(View.GONE);
        }
    }

    class OpenPersonDetailsListener implements View.OnClickListener {
        PersonObject personObject = null;

        public OpenPersonDetailsListener(PersonObject personObject) {
            this.personObject = personObject;
        }

        @Override
        public void onClick(View view) {
            DBHandler.getInstance().getOtherProfile(personObject.getPersonId(), new DBHandler.ResultListener() {
                @Override
                public void onFinish(Object object) {
                    if (getActivity() == null || getFragmentManager() == null) {
                        return;
                    }
                    FragmentTransaction fragmentTransaction2 = getFragmentManager().beginTransaction();
                    PersonDataFragment personDataFragment = new PersonDataFragment();
                    personDataFragment.setPurpose(PersonDataFragment.Purpose.PROFILE);
                    personDataFragment.setPersonObject((PersonObject) object);
                    fragmentTransaction2.addToBackStack(null);
                    fragmentTransaction2.replace(R.id.content, personDataFragment);
                    fragmentTransaction2.commitAllowingStateLoss();
                }
            });

        }
    }
}