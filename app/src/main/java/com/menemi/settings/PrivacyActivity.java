package com.menemi.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.Configurations;
import com.menemi.settings.privacymenuactivity.SwitcherActivity;

/**
 * Created by tester03 on 17.06.2016.
 */
public class PrivacyActivity extends AppCompatActivity
{
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final Configurations configurations = DBHandler.getInstance().getConfigurations();
        toolbar.setTitle("Privacy");
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView distanceOnOff = (TextView)findViewById(R.id.distanceOnOff);
        if(configurations.isShowDistance()){
            distanceOnOff.setText(getString(R.string.on));
        } else{
            distanceOnOff.setText(getString(R.string.off));
        }

        LinearLayout showDistanceButton = (LinearLayout)findViewById(R.id.showDistanceButton);
        showDistanceButton.setOnClickListener((v) -> {
                Intent switherActivity = new Intent(PrivacyActivity.this, SwitcherActivity.class);
                switherActivity.putExtra(SwitcherActivity.VALUE, configurations.isShowDistance());
                switherActivity.putExtra(SwitcherActivity.TITLE, getString(R.string.distance_switcher_text_title));
                switherActivity.putExtra(SwitcherActivity.TEXT, getString(R.string.distance_switcher_text));

                SwitcherActivity.setStateChangeListener( new CompoundButton.OnCheckedChangeListener() {
                    boolean isFailureLunch = false;
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(!isFailureLunch){
                            configurations.setShowDistance(isChecked);
                            DBHandler.getInstance().setConfigurations(configurations, (isSucceed)->{
                                if(!(boolean)isSucceed){
                                    isFailureLunch = true;
                                    buttonView.setChecked(!isChecked);
                                }
                            });
                        } else {
                            configurations.setShowDistance(isChecked);
                            isFailureLunch = false;
                        }
                        if(isChecked){
                            distanceOnOff.setText(getString(R.string.on));
                        } else{
                            distanceOnOff.setText(getString(R.string.off));
                        }

                    }
                });
            startActivity(switherActivity);
        });

        TextView statusOnOff = (TextView)findViewById(R.id.statusOnOff);
        if(configurations.isHideOnlineStatus()){
            statusOnOff.setText(getString(R.string.on));
        } else{
            statusOnOff.setText(getString(R.string.off));
        }

        LinearLayout onlineStatusButton  = (LinearLayout)findViewById(R.id.onlineStatusButton);
        onlineStatusButton.setOnClickListener((v)-> {
                Intent switherActivity = new Intent(PrivacyActivity.this, SwitcherActivity.class);
                switherActivity.putExtra(SwitcherActivity.VALUE, configurations.isHideOnlineStatus());
                switherActivity.putExtra(SwitcherActivity.TITLE, getString(R.string.show_online_status_title));
                switherActivity.putExtra(SwitcherActivity.TEXT, getString(R.string.show_online_status));

                SwitcherActivity.setStateChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    boolean isFailureLunch = false;
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(!isFailureLunch){
                            configurations.setHideOnlineStatus(isChecked);
                            DBHandler.getInstance().setConfigurations(configurations, (isSucceed)->{
                                if(!(boolean)isSucceed){
                                    isFailureLunch = true;
                                    buttonView.setChecked(!isChecked);
                                }
                            });
                        } else {
                            configurations.setHideOnlineStatus(isChecked);
                            isFailureLunch = false;
                        }
                        if(isChecked){
                            statusOnOff.setText(getString(R.string.on));
                        } else{
                            statusOnOff.setText(getString(R.string.off));
                        }

                    }
                }
            );
            startActivity(switherActivity);
        });

        TextView showSearchOnOff = (TextView)findViewById(R.id.showSearchOnOff);
        if(configurations.isPublicSearch()){
            showSearchOnOff.setText(getString(R.string.on));
        } else{
            showSearchOnOff.setText(getString(R.string.off));
        }

        LinearLayout showSearchButton = (LinearLayout)findViewById(R.id.showSearchButton);
        showSearchButton.setOnClickListener((v)-> {
                Intent switherActivity = new Intent(PrivacyActivity.this, SwitcherActivity.class);
                switherActivity.putExtra(SwitcherActivity.VALUE, configurations.isPublicSearch());
                switherActivity.putExtra(SwitcherActivity.TITLE, getString(R.string.enable_public_search_title));
                switherActivity.putExtra(SwitcherActivity.TEXT, getString(R.string.enable_public_search));

                SwitcherActivity.setStateChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                            boolean isFailureLunch = false;
                                                            @Override
                                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                                if(!isFailureLunch){
                                                                    configurations.setPublicSearch(isChecked);
                                                                    DBHandler.getInstance().setConfigurations(configurations, (isSucceed)->{
                                                                        if(!(boolean)isSucceed){
                                                                            isFailureLunch = true;
                                                                            buttonView.setChecked(!isChecked);
                                                                        }
                                                                    });
                                                                } else {
                                                                    configurations.setPublicSearch(isChecked);
                                                                    isFailureLunch = false;
                                                                }
                                                                if(isChecked){
                                                                    showSearchOnOff.setText(getString(R.string.on));
                                                                } else{
                                                                    showSearchOnOff.setText(getString(R.string.off));
                                                                }

                                                            }
                                                        }
                );

                startActivity(switherActivity);
                /*Intent enablePublicSearchSwitcher = new Intent(PrivacyActivity.this, EnablePublicSearchSwitherActivity.class);
                enablePublicSearchSwitcher.putExtra("value", configurations.isPublicSearch());
                startActivity(enablePublicSearchSwitcher);*/

        });

        TextView bumpedIntoOnOff = (TextView)findViewById(R.id.bumpedIntoOnOff);
        if(configurations.isShowNearby()){
            bumpedIntoOnOff.setText(getString(R.string.on));
        } else{
            bumpedIntoOnOff.setText(getString(R.string.off));
        }

        LinearLayout bumpedInfoButton  = (LinearLayout)findViewById(R.id.bumpedInfoButton);
        bumpedInfoButton.setOnClickListener((v)-> {
            Intent switherActivity = new Intent(PrivacyActivity.this, SwitcherActivity.class);
            switherActivity.putExtra(SwitcherActivity.VALUE, configurations.isShowNearby());
            switherActivity.putExtra(SwitcherActivity.TITLE, getString(R.string.enable_bumped_into_title));
            switherActivity.putExtra(SwitcherActivity.TEXT, getString(R.string.enable_bumped_into));

            SwitcherActivity.setStateChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                        boolean isFailureLunch = false;
                                                        @Override
                                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                            if(!isFailureLunch){
                                                                configurations.setShowNearby(isChecked);
                                                                DBHandler.getInstance().setConfigurations(configurations, (isSucceed)->{
                                                                    if(!(boolean)isSucceed){
                                                                        isFailureLunch = true;
                                                                        buttonView.setChecked(!isChecked);
                                                                    }
                                                                });
                                                            } else {
                                                                configurations.setShowNearby(isChecked);
                                                                isFailureLunch = false;
                                                            }
                                                            if(isChecked){
                                                                bumpedIntoOnOff.setText(getString(R.string.on));
                                                            } else{
                                                                bumpedIntoOnOff.setText(getString(R.string.off));
                                                            }

                                                        }
                                                    }
            );


            startActivity(switherActivity);
                /*Intent bumpedInfo = new Intent(PrivacyActivity.this, EnableBumpedIntoActivity.class);
                bumpedInfo.putExtra("value", configurations.isShowNearby());
                startActivity(bumpedInfo);*/
            });


        TextView profileSharingOnOff = (TextView)findViewById(R.id.profileSharingOnOff);
        if(configurations.isShareProfile()){
            profileSharingOnOff.setText(getString(R.string.on));
        } else{
            profileSharingOnOff.setText(getString(R.string.off));
        }

        LinearLayout profileSharingButton = (LinearLayout)findViewById(R.id.profileSharingButton);
        profileSharingButton.setOnClickListener((v)-> {
                Intent switherActivity = new Intent(PrivacyActivity.this, SwitcherActivity.class);
                switherActivity.putExtra(SwitcherActivity.VALUE, configurations.isShareProfile());
                switherActivity.putExtra(SwitcherActivity.TITLE, getString(R.string.allow_profile_sharing_title));
                switherActivity.putExtra(SwitcherActivity.TEXT, getString(R.string.allow_profile_sharing));

                SwitcherActivity.setStateChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                            boolean isFailureLunch = false;
                                                            @Override
                                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                                if(!isFailureLunch){
                                                                    configurations.setShareProfile(isChecked);
                                                                    DBHandler.getInstance().setConfigurations(configurations, (isSucceed)->{
                                                                        if(!(boolean)isSucceed){
                                                                            isFailureLunch = true;
                                                                            buttonView.setChecked(!isChecked);
                                                                        }
                                                                    });
                                                                } else {
                                                                    configurations.setShareProfile(isChecked);
                                                                    isFailureLunch = false;
                                                                }
                                                                if(isChecked){
                                                                    profileSharingOnOff.setText(getString(R.string.on));
                                                                } else{
                                                                    profileSharingOnOff.setText(getString(R.string.off));
                                                                }

                                                            }
                                                        }
                );

                startActivity(switherActivity);
               /* Intent allowSharing = new Intent(PrivacyActivity.this, AllowProfileSharing.class);
                allowSharing.putExtra("value", configurations.isShareProfile());
                startActivity(allowSharing);*/
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
