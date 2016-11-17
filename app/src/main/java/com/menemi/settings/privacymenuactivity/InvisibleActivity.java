package com.menemi.settings.privacymenuactivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.Configurations;

/**
 * Created by tester03 on 25.06.2016.
 */
public class InvisibleActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invisible_mode_layout);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitle(getString(R.string.invisible_mode));

        Configurations configurations = DBHandler.getInstance().getConfigurations();
        Switch almostSwitch = (Switch) findViewById(R.id.almostSwitch);
        almostSwitch.setChecked(configurations.isAlmostInvisible());
        almostSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    boolean isFailureLunch = false;
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(!isFailureLunch){
                            configurations.setAlmostInvisible(isChecked);
                            DBHandler.getInstance().setConfigurations(configurations, (isSucceed)->{
                                if(!(boolean)isSucceed){
                                    isFailureLunch = true;
                                    buttonView.setChecked(!isChecked);
                                }
                            });
                        } else {
                            configurations.setAlmostInvisible(isChecked);
                            isFailureLunch = false;
                        }

                    }
                }
        );

        Switch cloakedSwitch = (Switch) findViewById(R.id.cloakedSwitch);
        cloakedSwitch.setChecked(configurations.isInvisibleCloacked());
        cloakedSwitch.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            boolean isFailureLunch = false;
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isFailureLunch){
                    configurations.setInvisibleCloacked(isChecked);
                    DBHandler.getInstance().setConfigurations(configurations, (isSucceed)->{
                        if(!(boolean)isSucceed){
                            isFailureLunch = true;
                            buttonView.setChecked(!isChecked);
                        }
                    });
                } else {
                    configurations.setInvisibleCloacked(isChecked);
                    isFailureLunch = false;
                }

            }
        }
     );
        Switch vipStatusSwitch = (Switch) findViewById(R.id.vipStatusSwitch);
        vipStatusSwitch.setChecked(configurations.isHideVipStatus());
        vipStatusSwitch.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            boolean isFailureLunch = false;
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isFailureLunch){
                    configurations.setHideVipStatus(isChecked);
                    DBHandler.getInstance().setConfigurations(configurations, (isSucceed)->{
                        if(!(boolean)isSucceed){
                            isFailureLunch = true;
                            buttonView.setChecked(!isChecked);
                        }
                    });
                } else {
                    configurations.setHideVipStatus(isChecked);
                    isFailureLunch = false;
                }

            }
        });


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


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
