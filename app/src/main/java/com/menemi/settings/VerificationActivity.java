package com.menemi.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.Configurations;
import com.menemi.settings.privacymenuactivity.SwitcherActivity;

/**
 * Created by tester03 on 21.06.2016.
 */
public class VerificationActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_layout);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.verifications));
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        LinearLayout getVerifiedButton = (LinearLayout)findViewById(R.id.getVerifiedButton);
        getVerifiedButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent getVerifiedActivity = new Intent(VerificationActivity.this, VerificationContainerActivity.class);
                startActivity(getVerifiedActivity);
            }
        });
        Configurations configurations = DBHandler.getInstance().getConfigurations();
        TextView limitMessagesOnOff = (TextView)findViewById(R.id.limitMessagesOnOff);
        if(configurations.isLimitMessages()){
            limitMessagesOnOff.setText(getString(R.string.on));
        } else{
            limitMessagesOnOff.setText(getString(R.string.off));
        }

        LinearLayout limitMessagesButton = (LinearLayout)findViewById(R.id.limitMessagesButton);
        limitMessagesButton.setOnClickListener((v) -> {
            Intent switcherActivity = new Intent(VerificationActivity.this, SwitcherActivity.class);
            switcherActivity.putExtra(SwitcherActivity.VALUE, configurations.isLimitMessages());
            switcherActivity.putExtra(SwitcherActivity.TITLE, getString(R.string.limit_messages_title));
            switcherActivity.putExtra(SwitcherActivity.TEXT, getString(R.string.limit_messages_text));

            SwitcherActivity.setStateChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                        boolean isFailureLunch = false;
                                                        @Override
                                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                            if(!isFailureLunch){
                                                                configurations.setLimitMessages(isChecked);
                                                                DBHandler.getInstance().setConfigurations(configurations, (isSucceed)->{
                                                                    if(!(boolean)isSucceed){
                                                                        isFailureLunch = true;
                                                                        buttonView.setChecked(!isChecked);
                                                                    }
                                                                });
                                                            } else {
                                                                configurations.setLimitMessages(isChecked);
                                                                isFailureLunch = false;
                                                            }
                                                            if(isChecked){
                                                                limitMessagesOnOff.setText(getString(R.string.on));
                                                            } else{
                                                                limitMessagesOnOff.setText(getString(R.string.off));
                                                            }

                                                        }
                                                    }
            );

            startActivity(switcherActivity);
        });

        TextView hideVerificationOnOff = (TextView)findViewById(R.id.hideVerificationOnOff);
        if(configurations.isHideMyVerifications()){
            hideVerificationOnOff.setText(getString(R.string.on));
        } else{
            hideVerificationOnOff.setText(getString(R.string.off));
        }

        LinearLayout hideVerificationButton = (LinearLayout)findViewById(R.id.hideVerificationButton);
        hideVerificationButton.setOnClickListener((v) -> {
            Intent switcherActivity = new Intent(VerificationActivity.this, SwitcherActivity.class);
            switcherActivity.putExtra(SwitcherActivity.VALUE, configurations.isHideMyVerifications());
            switcherActivity.putExtra(SwitcherActivity.TITLE, getString(R.string.hide_verification_details_title));
            switcherActivity.putExtra(SwitcherActivity.TEXT, getString(R.string.hide_verification_details_text));

            SwitcherActivity.setStateChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                        boolean isFailureLunch = false;
                                                        @Override
                                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                            if(!isFailureLunch){
                                                                configurations.setHideMyVerifications(isChecked);
                                                                DBHandler.getInstance().setConfigurations(configurations, (isSucceed)->{
                                                                    if(!(boolean)isSucceed){
                                                                        isFailureLunch = true;
                                                                        buttonView.setChecked(!isChecked);
                                                                    }
                                                                });
                                                            } else {
                                                                configurations.setHideMyVerifications(isChecked);
                                                                isFailureLunch = false;
                                                            }
                                                            if(isChecked){
                                                                hideVerificationOnOff.setText(getString(R.string.on));
                                                            } else{
                                                                hideVerificationOnOff.setText(getString(R.string.off));
                                                            }

                                                        }
                                                    }
            );

            startActivity(switcherActivity);
        });
    }
    /*class Solution{}
    public static Solution parceString(String data) throws JSONException {

        JSONObject mainObjectJSON = new JSONObject(data);
        JSONObject pageInfoObjectJSON = mainObjectJSON.getJSONObject("pageInfo");
        String pageName = p.getString("pageName");//было "pageName": "abc",
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
