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
        almostSwitch.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
                configurations.setAlmostInvisible(isChecked);
                DBHandler.getInstance().setConfigurations(configurations, (Object object) ->{});
        });

        Switch cloakedSwitch = (Switch) findViewById(R.id.cloakedSwitch);
        cloakedSwitch.setChecked(configurations.isInvisibleCloacked());
        cloakedSwitch.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            configurations.setInvisibleCloacked(isChecked);
            DBHandler.getInstance().setConfigurations(configurations, (Object object) ->{});
        });
        Switch vipStatusSwitch = (Switch) findViewById(R.id.vipStatusSwitch);
        vipStatusSwitch.setChecked(configurations.isHideVipStatus());
        vipStatusSwitch.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            configurations.setHideVipStatus(isChecked);
            DBHandler.getInstance().setConfigurations(configurations, (Object object) ->{});
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
