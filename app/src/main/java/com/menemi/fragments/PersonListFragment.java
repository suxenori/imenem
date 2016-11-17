package com.menemi.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.menemi.PersonPage;
import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.dbfactory.rest.PictureLoader;
import com.menemi.personobject.PersonObject;
import com.menemi.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by tester03 on 17.06.2016.
 */

public class PersonListFragment extends Fragment {

    private View rootView = null;
    private int rowsShown = 1;
    private ShowPeopleCompositeFragment.Purpose purpose = null;
    private LayoutInflater inflater;
    ArrayList<PersonObject> personObjects;
    private String title = "";

    public void setPurpose(ShowPeopleCompositeFragment.Purpose purpose) {
        this.purpose = purpose;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.people_fragment, null);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
                parent.removeAllViews();
            }
        }

        lunchFragment();
        return rootView;
    }

    private void lunchFragment() {
        LinearLayout rows = (LinearLayout) rootView.findViewById(R.id.rows);
        rows.removeAllViews();
        if (purpose == ShowPeopleCompositeFragment.Purpose.VISITORS) {
            title = getString(R.string.visitors);
            showVisitors();
        }
        if (purpose == ShowPeopleCompositeFragment.Purpose.LIKES) {
            title = getString(R.string.liked_you);
            showLikes();
        }
        if (purpose == ShowPeopleCompositeFragment.Purpose.MUTUAL_LIKES) {
            title = getString(R.string.mutual_likes);
            showMutualLikes();
        }
        if (purpose == ShowPeopleCompositeFragment.Purpose.NEAR) {
            title = getString(R.string.people_nearby);

            showPeopleNear();
        }
        if (purpose == ShowPeopleCompositeFragment.Purpose.FAVORITES) {
            title = getString(R.string.favorites);
            showFavorites();
        }

        TextView blockTitle = (TextView) rootView.findViewById(R.id.blockTitle);
        blockTitle.setText(title);
    }

    private void showFavorites() {
        DBHandler.getInstance().getMyFavorites(new DBHandler.ResultListener() {
            @Override
            public void onFinish(Object object) {
                personObjects = (ArrayList<PersonObject>) object;
                Log.d("PersonListFragment", "favorites " + personObjects);
                Log.i("PersonListFragment", personObjects.size() + " ");
                prepare();
            }
        });
    }

    private void showMutualLikes() {
        DBHandler.getInstance().getMutualLikes(new DBHandler.ResultListener() {
            @Override
            public void onFinish(Object object) {
                personObjects = (ArrayList<PersonObject>) object;
                Log.d("PersonListFragment", "mutual " + personObjects);
                prepare();
            }
        });
    }

    private void showLikes() {
        DBHandler.getInstance().getLikes(new DBHandler.ResultListener() {
            @Override
            public void onFinish(Object object) {
                personObjects = (ArrayList<PersonObject>) object;
                Log.d("PersonListFragment", "likes " + personObjects);
                prepare();
            }
        });
    }

    private void showPeopleNear() {
        Log.d("NEAR", "CALLED");
        Utils.buildLocationSettingsRequest(getActivity());

        Utils.getPosition(getActivity(), new Utils.OnFinishListener() {
            @Override
            public void onFinish(Object object) {
                LatLng position = (LatLng) object;
                DBHandler.getInstance().getVisitors(new DBHandler.ResultListener() {
                    @Override
                    public void onFinish(Object object) {
                        personObjects = (ArrayList<PersonObject>) object;
                        prepare();

                    }
                });
            }
        }, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
                DBHandler.getInstance().setPersonPosition(position, new DBHandler.ResultListener() {
                    @Override
                    public void onFinish(Object object) {
                        Log.v("location", "Location Changed");

                        DBHandler.getInstance().getVisitors(new DBHandler.ResultListener() {
                            @Override
                            public void onFinish(Object object) {
                                LinearLayout rows = (LinearLayout) rootView.findViewById(R.id.rows);
                                rows.removeAllViews();
                                personObjects = (ArrayList<PersonObject>) object;
                                prepare();

                            }
                        });
                    }
                });
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });


    }

   /* private void prepare() {
        PeopleRowFragment row = new PeopleRowFragment();
        row.setLastRow(true);
        row.setPersonObjects(personObjects);
        row.setRowsShown(0);
        getFragmentManager().beginTransaction().add(R.id.rows, row).commitAllowingStateLoss();
    }*/
    private void prepare() {


        alignArray(personObjects);
        for (int j = 0; j <= rowsShown; j++) {
            final boolean isLastRow = rowsShown == j;
            addRow2(j, isLastRow);
        }
    }

    private void addRow2(int j, final boolean isLastRow) {
        final int start = j * 3;

        final View v = inflater.inflate(R.layout.person_set_fragment, null);
        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LinearLayout rows = (LinearLayout) rootView.findViewById(R.id.rows);
        rows.addView(v);
        Log.d("PersonListFragment", "" + personObjects);

        for (int i = start; i < start + 3; i++) {
            if (i < personObjects.size()) {
                final int finalI = i;
                if (personObjects.get(i) != null) {
                    DBHandler.getInstance().getAvatar(personObjects.get(i).getPersonId(), (Object object) -> {
                        Bitmap avatar = ((Bitmap) object);
                        personObjects.get(finalI).setPersonAvatar(avatar);
                        if (finalI == start + 2 || finalI == personObjects.size() - 1) {

                            prepareViews(isLastRow, start, v);
                        }
                    });
                } else {
                    prepareViews(isLastRow, start, v);
                    return;
                }
            }
        }


    }

    private void prepareViews(boolean isLastRow, int start, View v) {
        if (personObjects.size() > start && personObjects.get(start) != null) {
            LinearLayout layout = (LinearLayout) v.findViewById(R.id.personItem0);
            layout.addView(createPicture(inflater, personObjects.get(start), false));
        }
        if (personObjects.size() > start + 1 && personObjects.get(start + 1) != null) {
            LinearLayout layout = (LinearLayout) v.findViewById(R.id.personItem1);
            layout.addView(createPicture(inflater, personObjects.get(start + 1), false));
        }


        if (personObjects.size() > start + 2 && personObjects.get(start + 2) != null) {
            LinearLayout layout = (LinearLayout) v.findViewById(R.id.personItem2);
            if (start + 2 == personObjects.size() - 1) {
                layout.addView(createPicture(inflater, personObjects.get(start + 2), false));
            } else {
                layout.addView(createPicture(inflater, personObjects.get(start + 2), isLastRow));
            }
        }
    }


    private void addRow(int j, final boolean isLastRow) {
        final int start = j * 3;
        ArrayList<Integer> ids = new ArrayList<>();
        final View v = inflater.inflate(R.layout.person_set_fragment, null);
        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LinearLayout rows = (LinearLayout) rootView.findViewById(R.id.rows);
        rows.addView(v);
        Log.d("PersonListFragment", "" + personObjects);

        for (int i = start; i < start + 3; i++) {
            Log.i("Fragm", personObjects.size() + " ");
            if (personObjects.size() > i && personObjects.get(i) != null) {
                ids.add(personObjects.get(i).getPersonId());
                Log.i("Fragm", personObjects.size() + " ");
            }

        }

        Log.i("Fragm", ids.size() + " ids");
        DBHandler.getInstance().getMultiplePeoplePictures(Utils.PICTURE_QUALITY_THUMBNAIL, ids, new DBHandler.ResultListener() {
            @Override
            public void onFinish(Object object) {
                HashMap<Integer, String> pictures = (HashMap<Integer, String>) object;

                for (Map.Entry<Integer, String> entry : pictures.entrySet()) {
                    new PictureLoader(entry.getValue(), (Bitmap bitmap) -> {
                        setPictureToPerson(personObjects, entry.getKey(), bitmap);
                    });

                }

                prepareViews(isLastRow, start, v);


            }
        });
    }


    private void setPictureToPerson(ArrayList<?> personObjects, int id, Bitmap picture) {

        for (int i = 0; i < personObjects.size(); i++) {
            if (((PersonObject) personObjects.get(i)).getPersonId() == id) {
                ((PersonObject) personObjects.get(i)).setPersonAvatar(picture);
                return;
            }
        }

    }

    private void showVisitors() {

        DBHandler.getInstance().getVisitors(new DBHandler.ResultListener() {
            @Override
            public void onFinish(Object object) {
                personObjects = (ArrayList<PersonObject>) object;
                prepare();

            }
        });


    }

    private void alignArray(ArrayList<?> personObjects) {
        if (personObjects.size() % 3 == 1) {
            personObjects.add(null);
            personObjects.add(null);
        }
        if (personObjects.size() % 3 == 2) {
            personObjects.add(null);

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


                Log.d("PersonListItemFragment", "p != n, ifsm = t");
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
        PersonPage.finishProgressDialog();
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
            addRow(rowsShown, true);
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