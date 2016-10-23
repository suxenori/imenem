package com.menemi.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.menemi.PersonPage;
import com.menemi.R;
import com.menemi.dbfactory.DBHandler;

/**
 * Created by tester03 on 21.06.2016.
 */
public class ShowPeopleCompositeFragment extends Fragment {
    private static View rootView = null;
    private Purpose purpose = Purpose.NEAR;

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.scroll_list_container, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
                parent.removeAllViews();

            }
        }
        DBHandler.getInstance().isRESTAvailable(new DBHandler.ResultListener() {
            @Override
            public void onFinish(Object object) {
                if ((boolean) object == true) {
                    initLayout();
                } else {
                    LostInternetFragment lostInternetFragment = new LostInternetFragment();
                    lostInternetFragment.setOnRetryListener(() -> {
                        getFragmentManager().popBackStack();
                        initLayout();
                    });
                    getFragmentManager().beginTransaction().replace(com.menemi.R.id.content, lostInternetFragment).addToBackStack(null).commitAllowingStateLoss();
                }
            }
        });

        configureToolbar(purpose);
        return rootView;
    }

    private void initLayout() {
        LinearLayout fragment1 = (LinearLayout) rootView.findViewById(R.id.fragment1);
        fragment1.removeAllViews();

        //DBHandler.getInstance().
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        PersonListFragment personListFragment = new PersonListFragment();
        fragmentTransaction.replace(R.id.fragment1, personListFragment);
        personListFragment.setPurpose(purpose);

        if (purpose == Purpose.LIKES || purpose == Purpose.MUTUAL_LIKES) {
            personListFragment.setPurpose(Purpose.LIKES);

            PersonListFragment personListFragment2 = new PersonListFragment();
            personListFragment2.setPurpose(Purpose.MUTUAL_LIKES);
            fragmentTransaction.add(R.id.fragment1, personListFragment2);

        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    public enum Purpose {
        VISITORS,
        LIKES,
        MUTUAL_LIKES,
        FRIENDS,
        FAVORITES,
        NEAR
    }

    private void configureToolbar(Purpose purpose) {
        Toolbar toolbar = PersonPage.getToolbar();

        LinearLayout toolbarContainer = (LinearLayout) toolbar.findViewById(R.id.toolbarContainer);
        toolbarContainer.removeAllViews();

        if (purpose == Purpose.NEAR) {
            toolbarContainer.addView(View.inflate(getActivity(), R.layout.ab_people_near, null));
            ImageView showMap = (ImageView) toolbarContainer.findViewById(R.id.nearButton);
            showMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DBHandler.getInstance().isRESTAvailable(new DBHandler.ResultListener() {
                        @Override
                        public void onFinish(Object object) {

                            if ((boolean) object) {
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                ShowMapFragment showMapFragment = new ShowMapFragment();
                                showMapFragment.setOwnPosition(true);
                                fragmentTransaction.replace(R.id.content, showMapFragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commitAllowingStateLoss();
                            }
                        }});
                        }});
        } else

                {
                    toolbarContainer.addView(View.inflate(getActivity(), R.layout.ab_people_list, null)); // TODO insertt enother
                    TextView title = (TextView) toolbarContainer.findViewById(R.id.screenTitle);
                    if (purpose == Purpose.VISITORS) {
                        title.setText(R.string.visitors);
                    } else if (purpose == Purpose.FAVORITES) {
                        title.setText(R.string.favorites);
                    } else if (purpose == Purpose.LIKES) {
                        title.setText(R.string.liked_you);
                    }

                }


                ImageView menuButton = (ImageView) toolbarContainer.findViewById(R.id.menuButton);
                menuButton.setOnClickListener(PersonPage.getMenuListener());

                ImageView filterButton = (ImageView) toolbarContainer.findViewById(R.id.filterButton);
                filterButton.setOnClickListener(PersonPage.getFilterButtonListener(

                getFragmentManager()

                ));
            }
        }
