package com.menemi.fragments;

/**
 * Created by Ui-Developer on 17.08.2016.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.menemi.R;
import com.menemi.personobject.VipPlan;

/**
 * Created by Ui-Developer on 10.08.2016.
 */
public class BuyVipItemFragment extends Fragment {
    private View rootView = null;
    private VipPlan plan = null;

    public void setPlan(VipPlan plan) {
        this.plan = plan;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {

            rootView = inflater.inflate(R.layout.buy_vip_item_fragment, container, false);


        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }

        }


        TextView price = (TextView) rootView.findViewById(R.id.price);
       // price.setText("" + plan.getPrice());
        return rootView;

    }

}
