package com.menemi.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.menemi.PersonPage;
import com.menemi.R;

/**
 * Created by Ui-Developer on 17.08.2016.
 */
public class BuyVipFragment extends Fragment {
    private View rootView = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.buy_vip_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        configureToolbar();

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    for (int i = 0; i < 3; i++) {
                        BuyVipItemFragment buyVipItemFragment = new BuyVipItemFragment();
                       // buyVipItemFragment.setPlan(plans.get(i));
                        fragmentTransaction.add(R.id.fragment_container , buyVipItemFragment);

                    }
                    fragmentTransaction.commitAllowingStateLoss();


        return rootView;
    }



    private void configureToolbar() {
        Toolbar toolbar = PersonPage.getToolbar();

        LinearLayout toolbarContainer = (LinearLayout) toolbar.findViewById(R.id.toolbarContainer);
        toolbarContainer.removeAllViews();
        toolbarContainer.addView(View.inflate(getActivity(), R.layout.ab_buy_vip, null)); // TODO insertt enother

        ImageView menuButton = (ImageView) toolbarContainer.findViewById(R.id.menuButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

    }

}

