package com.menemi.pay_classes;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.menemi.R;
import com.menemi.util.IabHelper;
import com.menemi.util.IabResult;
import com.menemi.util.Inventory;
import com.menemi.util.Purchase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;


/**
 * Created by tester03 on 01.10.2016.
 */

public class PayActivity extends AppCompatActivity implements View.OnClickListener
{
    private static String TAG = "Billing";
    private static String ITEM = "400";
    private String base64PublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAx3V/MToBF0HBZLD9n3CTJk3upHv/7qPXTXIDLhR86JPNwsBsfya2Zz71aNN8Hj3OyH9Y1JKH0h2iGpJ/DA4bKIjaoI646IZSm55Ff+XoV3+//JHe7blL/XLMXHquiGAn9U3GZ/eRNU8iCazhxgLyFnaZB4vQVUZ58169Zyc++Ex+hMkJvWE3r7xUSvAWzfUCQ2WPiTifIR8d0Y6S583s+8A6ul/kHM6QmylGCB2kdg9lZ+bz1aajd/vKPCc7qRQCLRB8CNWwgVero0noExIl+spqhRBSldbDx6SBlBC/d4ffMp5O1ILTxt3HAKKlVx2PcIRwgdkCJqihkQPkCf//bQIDAQAB" ;
    IabHelper iabHelper;
    private RelativeLayout payButton;
    private RelativeLayout payButtonInApp;
    IInAppBillingService iInAppBillingService;
    IabHelper.OnIabPurchaseFinishedListener onIabPurchaseFinishedListener;

    ServiceConnection serviceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            iInAppBillingService = IInAppBillingService.Stub.asInterface(service);
            Log.d("iInAppBillingService"," - is connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            Log.d("iInAppBillingService"," - is not connected");
        }
    };

    public static final int PAYPAL_REQUEST_CODE = 123;
    public PayPalConfiguration config;

    //Payment Amount

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_layout);

        //Paypal Configuration Object
        config = new PayPalConfiguration()
                // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
                // or live (ENVIRONMENT_PRODUCTION)
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(PayPalConfig.PAYPAL_CLIENT_ID);
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        payButton = (RelativeLayout)findViewById(R.id.buttonPay);
        payButtonInApp = (RelativeLayout)findViewById(R.id.buttonPayInApp);
        payButton.setOnClickListener(this);
        payButtonInApp.setOnClickListener(this);
        iabHelper= new IabHelper(this,base64PublicKey);
        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener()
        {
            @Override
            public void onIabSetupFinished(IabResult result)
            {
                if (!result.isSuccess()){
                    Log.d(TAG, " In app billing setup failed" + result);
                } else {
                    Log.d(TAG, " In app billing setup is successfully");
                }
            }
        });
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.menemi");

    }

    private void getPayment() {
        String paymentAmount = getIntent().getStringExtra(PayPalConfig.PAYPAL_MONEY_COUNT);
        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "USD", "К оплате",
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);


        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!iabHelper.handleActivityResult(requestCode,resultCode,data)){
            super.onActivityResult(requestCode,resultCode,data);
        }
        IabHelper.OnIabPurchaseFinishedListener onIabPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener()
        {
            @Override
            public void onIabPurchaseFinished(IabResult result, Purchase info)
            {
                if (result.isFailure()){
                    Log.d(TAG," error");
                } else if (info.getSku().equals(ITEM)){
                    consumeItem();                }

            }
        };
        //If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);
                        finish();

                      /*  //Starting a new activity for the payment details and also putting the payment details with intent
                        startActivity(new Intent(this, ConfirmationActivity.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", paymentAmount));
*/
                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }
    public void consumeItem(){
        iabHelper.queryInventoryAsync(receivedInventoryListener);
    }
    IabHelper.QueryInventoryFinishedListener receivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {

            if (result.isFailure()) {
                // Handle failure
            } else {
                iabHelper.consumeAsync(inventory.getPurchase(ITEM),mConsumeFinishedListener);
            }
        }
    };
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {

                    } else {
                        // handle error
                    }
                }
            };
   /* public void consumeItem(){
        iabHelper.queryInventoryAsync(new IabHelper.QueryInventoryFinishedListener()
        {
        *//*
            @Override
            public void onQueryInventoryFinished(IabResult result, Inventory inv)
            {
                if (result.isFailure()){
                    Log.d(TAG,"error");
                } else {
                    iabHelper.consumeAsync(inv.getPurchase(ITEM), new IabHelper.OnConsumeFinishedListener()
                    {
                        @Override
                        public void onConsumeFinished(Purchase purchase, IabResult result)
                        {
                            if (result.isSuccess()){
                                Toast.makeText(getApplicationContext(),"You purchased " + purchase,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }*//*
        });
    }*/

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
        if (iInAppBillingService != null) {
            unbindService(serviceConnection);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.buttonPay : getPayment();
                break;
            case R.id.buttonPayInApp :
                iabHelper.launchPurchaseFlow(this, ITEM, 124, new IabHelper.OnIabPurchaseFinishedListener()
                {
                    @Override
                    public void onIabPurchaseFinished(IabResult result, Purchase info)
                    {
                        Toast.makeText(getApplicationContext(), "" + info,Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }

    }
}
