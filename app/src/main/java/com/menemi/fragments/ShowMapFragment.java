package com.menemi.fragments;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.menemi.PersonPage;
import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.dbfactory.rest.PictureLoader;
import com.menemi.personobject.PersonObject;
import com.menemi.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Note to use, in full screen, call setOwnPostiton(true) first
 */
public class ShowMapFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private static View rootView = null;
    LocationManager locationManager = null;
    GoogleMap googleMap = null;
    ArrayList<PersonMarker> myPositionMarker = new ArrayList<>();
    ArrayList<PersonMarker> markers = new ArrayList<>();
    LatLng myLocation = null;
    private int rangeMiles = 5;
    private boolean isOwnPosition = false;
    PersonObject personToShow;


    private void getPersonMarker(final Runnable onFinishListener) {
        myLocation = new LatLng(personToShow.getPositionLatitude(), personToShow.getPositionLongitude());
        DBHandler.getInstance().getAvatar(personToShow.getPersonId(), new DBHandler.ResultListener() {
            @Override
            public void onFinish(Object object) {
                Bitmap avatar = (Bitmap) object;
                markers.add(new PersonMarker(avatar, personToShow));
                onFinishListener.run();
            }
        });

    }



    int photosLoaded = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.base_map_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }

        MapsInitializer.initialize(getActivity()); /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if (isOwnPosition) {
            configureToolbar();

            Utils.buildLocationSettingsRequest(getActivity());

            Utils.getPosition(getActivity(), new Utils.OnFinishListener() {
                @Override
                public void onFinish(Object object) {
                    myLocation = (LatLng) object;
                    DBHandler.getInstance().getUsersAround(rangeMiles, new DBHandler.ResultListener() {
                        @Override
                        public void onFinish(final Object ALPO) {
                            final ArrayList<PersonObject> people = (ArrayList<PersonObject>) ALPO;
                            DBHandler.getInstance().getMultiplePeoplePictures(Utils.PICTURE_QUALITY_THUMBNAIL, Utils.extractIds((ArrayList<PersonObject>) ALPO),(Object HMIB) ->{
                                    HashMap<Integer, String> bitmaps = (HashMap<Integer, String>) HMIB;


                                    for (int i = 0; i < people.size(); i++) {
                                        int finalI = i;
                                        new PictureLoader(bitmaps.get(people.get(i).getPersonId()), (Bitmap icon) ->{
                                           if (icon == null) {
                                               icon = Utils.getBitmapFromResource(getActivity(), R.drawable.empty_photo);
                                           }
                                           markers.add(new PersonMarker(icon, people.get(finalI)));
                                            photosLoaded++;
                                           if(photosLoaded == people.size()-1){
                                               createMapView();
                                           }
                                       });
                                        //Bitmap icon = bitmaps.get(people.get(i).getPersonId());

                                    }


                            });
                        }
                    });

                }
            }, this);
        } else {
            getPersonMarker(new Runnable() {
                @Override
                public void run() {
                    createMapView();
                }
            });

        }
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        if(isOwnPosition) {
            Utils.buildLocationSettingsRequest(getActivity());
        }
        }

    /**
     * Initialises the mapview
     */
    private void createMapView() {
        /**
         * Catch the null pointer exception that
         * may be thrown when initialising the map
         */
        try {
            if (googleMap == null) {
                MapFragment mapFragment = (MapFragment) getMapFragment();
                mapFragment.getMapAsync(this);


                /**
                 * If the map is still null after attempted initialisation,
                 * show an error to the user
                 */
                if (googleMap == null) {
                    Log.e("ShowMapFragment", "Error creating map");
                }
            }
        } catch (NullPointerException exception) {
            Log.e("mapApp", exception.toString());
        }
    }

    private MapFragment getMapFragment() {
        FragmentManager fm = null;



        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            fm = getFragmentManager();
        } else {

            fm = getChildFragmentManager();
        }

        return (MapFragment) fm.findFragmentById(R.id.mapView);
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.v("ShowMapFragment", "onLocationChanged " + location.toString());
        if(getActivity() != null) {
            LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
            for (int i = 0; i < myPositionMarker.size(); i++) {
                myPositionMarker.get(i).getMarkerOptions().position(position);
            }

            DBHandler.getInstance().setPersonPosition(position, (Object object) -> {});
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        try {

            Location location1 = locationManager.getLastKnownLocation(provider);
            if (location1 != null) {
                Log.v("ShowMapFragment", "onStatusChanged " + location1.toString());
            }
        } catch (SecurityException se) {
            se.printStackTrace();
        }

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.v("ShowMapFragment", "onProviderEnabled ");
        try {
            if(locationManager !=null) {
                Location location1 = locationManager.getLastKnownLocation(provider);
                if (location1 != null) {
                    Log.v("ShowMapFragment", "onProviderEnabled " + location1.toString());
                }
            }
        } catch (SecurityException se) {
            se.printStackTrace();
        }


    }

    @Override
    public void onProviderDisabled(String provider) {
    }


    private void openPersonDetailsFragment(PersonObject personObject) {



        PersonDataFragment personDataFragment = new PersonDataFragment();
        personDataFragment.setPurpose(PersonDataFragment.Purpose.PROFILE);
        personDataFragment.setPersonObject(personObject);
        getFragmentManager().beginTransaction().replace(R.id.content, personDataFragment).addToBackStack(null).commitAllowingStateLoss();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        Log.v("ShowMapFragment", "markers size = " + markers.size());

        for (int i = 0; i < markers.size(); i++) {
            Log.d("ShowMapFragment", "i = " + i + " marker = " + markers.get(i).getMarkerOptions());
            if (markers.get(i).getMarkerOptions() != null) {
                markers.get(i).setPreparedMarker(googleMap.addMarker(markers.get(i).getMarkerOptions()));
                if (i == 0) {
                    // markers.get(i).animateMarker();
                }
            }

        }
        if (isOwnPosition) {
            setMyMarker(myLocation);
            for (int i = 0; i < myPositionMarker.size(); i++) {
                //Log.d("ShowMapFragment", "i = " + i + " marker = " + markers.get(i).getMarkerOptions());
                if (myPositionMarker.get(i).getMarkerOptions() != null) {
                    myPositionMarker.get(i).setPreparedMarker(googleMap.addMarker(myPositionMarker.get(i).getMarkerOptions()));
                    myPositionMarker.get(i).animate();
                }
            }
        }
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (int i = 0; i < markers.size(); i++) {
                    if (markers.get(i).getMarkerOptions() != null && markers.get(i).getMarkerOptions().getTitle().equals(marker.getTitle())) {
                        openPersonDetailsFragment(markers.get(i).getPersonObject());

                        return true;
                    }
                }
                return false;
            }
        });
        if (isOwnPosition) {

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, calculateZoomLevel(rangeMiles)));//5 = 15 // 4000 = 2

        } else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 18));//2 - 22

        }
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("хуй")
                        .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.test_circle_shadow)))
                        .draggable(false)
                        .zIndex(0.5f));
            }
        });
    }

    public byte calculateZoomLevel(int rangeMiles) {
        byte zoom = 1;

        double E = 21638;
        zoom = (byte) Math.round(Math.log(E / rangeMiles) / Math.log(2) + 1);
        zoom++;
        // to avoid exeptions
        if (zoom > 21) {
            zoom = 21;
        }
        if (zoom < 1) {
            zoom = 1;
        }

        return zoom;
    }

    public void setOwnPosition(boolean isOwnPosition) {
        this.isOwnPosition = isOwnPosition;
    }

    public void setPersonToShow(PersonObject personToShow) {
        this.personToShow = personToShow;
    }

    private void setMyMarker(LatLng position) {
        Context ctx = getActivity();

        Bitmap start = Utils.getBitmapFromResource(ctx, R.drawable.geo_start);
        Bitmap middle = Utils.getBitmapFromResource(ctx, R.drawable.geo_midle);
        Bitmap end = Utils.getBitmapFromResource(ctx, R.drawable.geo_end);

        start = Bitmap.createScaledBitmap(start, start.getWidth() / 2, start.getHeight() / 2, false);
        middle = Bitmap.createScaledBitmap(middle, middle.getWidth() / 2, middle.getHeight() / 2, false);
        end = Bitmap.createScaledBitmap(end, end.getWidth() / 2, end.getHeight() / 2, false);

        myPositionMarker.add(new PersonMarker(start, position, POSITION.START));
        myPositionMarker.add(new PersonMarker(middle, position, POSITION.MIDDLE));
        myPositionMarker.add(new PersonMarker(end, position, POSITION.END));
    }

    class PersonMarker {
        Marker preparedMarker = null;
        PersonObject personObject = null;
        MarkerOptions markerOptions = null;
        POSITION animationPosition;
Bitmap icon;
        public void animate() {
            if (animationPosition == POSITION.END) {
                endAnimation();
            } else if (animationPosition == POSITION.MIDDLE) {
                middleAnimation();
            }

        }

        private void middleAnimation() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        hide();
                        Thread.sleep(300l);
                        show();
                        Thread.sleep(600l);

                        run();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        private void endAnimation() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        hide();
                        Thread.sleep(600l);
                        show();
                        Thread.sleep(300l);

                        run();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        @UiThread
        private void hide() {
            if(getActivity()!=null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (preparedMarker != null) {
                            preparedMarker.setVisible(false);
                        }
                    }
                });
            }
        }

        private void show() {
            if(getActivity()!=null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (preparedMarker != null) {
                        preparedMarker.setVisible(true);
                    }
                }
            });
            }
        }

        public void setPreparedMarker(Marker preparedMarker) {
            this.preparedMarker = preparedMarker;
        }

        public PersonMarker(Bitmap icon, LatLng position, POSITION animationPosition) {
            if(icon == null){
                icon = Utils.getBitmapFromResource(getActivity(), R.drawable.empty_photo);
            }
            this.animationPosition = animationPosition;
            this.markerOptions = new MarkerOptions()
                    .position(position)
                    .icon(BitmapDescriptorFactory.fromBitmap(icon))
                    .draggable(false);


        }

        public PersonMarker(Bitmap icon, PersonObject personObject) {
        if(icon == null){
            icon = Utils.getBitmapFromResource(getActivity(), R.drawable.empty_photo);
        }
            this.icon = icon;
            this.personObject = personObject;



        }

        public PersonObject getPersonObject() {
            return personObject;
        }

        public MarkerOptions getMarkerOptions() {

            this.markerOptions = new MarkerOptions()
                    .position(new LatLng(personObject.getPositionLatitude(), personObject.getPositionLongitude()))
                    .title(personObject.getPersonName())
                    .icon(BitmapDescriptorFactory.fromBitmap(Utils.overlay(Utils.getCroppedBitmap(icon), BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.test_circle_shadow))))
                    .draggable(false)
                    .zIndex(0.5f);
            return markerOptions;
        }
    }

    private void configureToolbar() {
        Toolbar toolbar = PersonPage.getToolbar();

        LinearLayout toolbarContainer = (LinearLayout) toolbar.findViewById(R.id.toolbarContainer);
        toolbarContainer.removeAllViews();
        TextView title = (TextView) toolbarContainer.findViewById(R.id.screenTitle);

        toolbarContainer.addView(View.inflate(getActivity(), R.layout.ab_people_near, null)); // TODO insertt enother

        ImageView showMap = (ImageView) toolbarContainer.findViewById(R.id.nearButton);
        showMap.setImageResource(R.drawable.show_grid);
        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                ShowPeopleCompositeFragment showPeopleCompositeFragment = new ShowPeopleCompositeFragment();
                showPeopleCompositeFragment.setPurpose(ShowPeopleCompositeFragment.Purpose.NEAR);
                fragmentTransaction.replace(R.id.content, showPeopleCompositeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        ImageView menuButton = (ImageView) toolbarContainer.findViewById(R.id.menuButton);
        menuButton.setOnClickListener(PersonPage.getMenuListener());

        ImageView filterButton = (ImageView) toolbarContainer.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(PersonPage.getFilterButtonListener(getFragmentManager()));
    }

    public enum POSITION {
        START,
        MIDDLE,
        END
    }
}
