package com.menemi.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.menemi.PersonPage;
import com.menemi.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v13.FragmentPagerItems;
import com.ogaclejapan.smarttablayout.utils.v13.FragmentStatePagerItemAdapter;

/**
 * Created by Ui-Developer on 28.07.2016.
 */
public class PictureDetatilsListFragment extends Fragment {
    private View rootView = null;
    FragmentStatePagerItemAdapter adapter;

    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.pictures_datails_list, container, false);

        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        Log.d("PictureDetatilsList", "create");
if(adapter == null) {
    adapter = new FragmentStatePagerItemAdapter(
            getFragmentManager(), FragmentPagerItems.with(getActivity())
            .add(R.string.privatePhoto, PhotoStatisticListFragment.class)
            .add(R.string.publicPhoto, PublicPhotoListFragment.class)
            .create());

        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);


}
        SmartTabLayout viewPagerTab = (SmartTabLayout) rootView.findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

        configureToolbar();

        return rootView;
    }

    private void configureToolbar() {
        Toolbar toolbar = PersonPage.getToolbar();
        LinearLayout toolbarContainer = (LinearLayout) toolbar.findViewById(R.id.toolbarContainer);
        toolbarContainer.removeAllViews();
        toolbarContainer.addView(View.inflate(getActivity(),R.layout.ab_buy_coins,null));

        ImageView menuButton = (ImageView) toolbarContainer.findViewById(R.id.menuButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        TextView title = (TextView) toolbarContainer.findViewById(R.id.screenTitle);
        title.setText(getString(R.string.photos));

        /*TextView nameAgeText = (TextView) toolbarContainer.findViewById(R.id.nameAgeText);
        nameAgeText.setText(personObject.getPersonName() +", " + personObject.getPersonAge());*/
    }


}