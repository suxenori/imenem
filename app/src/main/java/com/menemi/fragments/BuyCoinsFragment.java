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
import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.PayPlan;
import com.menemi.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Ui-Developer on 29.07.2016.
 */
public class BuyCoinsFragment extends Fragment {
    private View rootView = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.buy_coins_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }

        }
        configureToolbar();
        DBHandler.getInstance().getPayPlans(new DBHandler.ResultListener() {
            @Override
            public void onFinish(Object object) {
                ArrayList<PayPlan> plans = ( ArrayList<PayPlan>) object;

                if(plans != null && plans.size() > 0 && getFragmentManager() != null && rootView != null && rootView.findViewById(R.id.planContainer)!=null){
                    Utils.sortPlans(plans);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                for (int i = 0; i < plans.size(); i++) {
                    BuyCoinsItemFragment buyCoinsItemFragment = new BuyCoinsItemFragment();
                    buyCoinsItemFragment.setPlan(plans.get(i));
                    fragmentTransaction.add(R.id.planContainer ,buyCoinsItemFragment);

                }
              fragmentTransaction.commitAllowingStateLoss();
            }}
        });


        return rootView;
    }



    private void configureToolbar() {
        Toolbar toolbar = PersonPage.getToolbar();

        LinearLayout toolbarContainer = (LinearLayout) toolbar.findViewById(R.id.toolbarContainer);
        toolbarContainer.removeAllViews();
        toolbarContainer.addView(View.inflate(getActivity(), R.layout.ab_buy_coins, null)); // TODO insertt enother

        ImageView menuButton = (ImageView) toolbarContainer.findViewById(R.id.menuButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }}
        });

    }

}

