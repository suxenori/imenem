package com.menemi.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.menemi.R;
import com.menemi.pay_classes.PayActivity;
import com.menemi.pay_classes.PayPalConfig;
import com.menemi.personobject.PayPlan;

/**
 * Created by Ui-Developer on 10.08.2016.
 */
public class BuyCoinsItemFragment extends Fragment{
    private View rootView = null;
    private PayPlan plan = null;

    public void setPlan(PayPlan plan) {
        this.plan = plan;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            if(plan.isPopular()){
                rootView = inflater.inflate(R.layout.buy_coins_item_popular, container, false);
            } else{
                rootView = inflater.inflate(R.layout.buy_coins_item, container, false);
            }

        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }

        }

        TextView coinsNumber = (TextView) rootView.findViewById(R.id.coinsNumber);
        coinsNumber.setText("" + plan.getCoins());
        TextView price = (TextView)rootView.findViewById(R.id.price);
        price.setText("" + plan.getPrice());
        LinearLayout buyButton = (LinearLayout)rootView.findViewById(R.id.buyButton);
        buyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(),PayActivity.class);
                intent.putExtra(PayPalConfig.PAYPAL_MONEY_COUNT, plan.getPrice() + "");
                Log.d("plan.getPrice",plan.getPrice() + "");
                startActivity(intent);
            }
        });
        return rootView;

    }

}
