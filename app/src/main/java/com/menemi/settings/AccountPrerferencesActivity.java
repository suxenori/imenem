package com.menemi.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.menemi.R;
import com.menemi.settings.privacymenuactivity.InvisibleActivity;

/**
 * Created by irondev on 06.06.16.
 */
public class AccountPrerferencesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_preferences_activity);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_top);
        TextView title = (TextView)toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        title.setText("Account preference");
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        LinearLayout privacyButton = (LinearLayout) findViewById(R.id.privacyButton);
        privacyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent privacyPage = new Intent(AccountPrerferencesActivity.this,PrivacyActivity.class);
                startActivity(privacyPage);
            }
        });

        LinearLayout notificationsButton = (LinearLayout)findViewById(R.id.notificationsButton);
        notificationsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent notificationsActivity = new Intent(AccountPrerferencesActivity.this,NotificationActivity.class);
                startActivity(notificationsActivity);
            }
        });

        /*LinearLayout networkButton = (LinearLayout)findViewById(R.id.networkButton);
        networkButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent networkActivity = new Intent(AccountPrerferencesActivity.this, NetworkActivity.class);
                startActivity(networkActivity);
            }
        });
*/
        LinearLayout verificationsButton = (LinearLayout)findViewById(R.id.verificationsButton);
        verificationsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent verificationsActivity = new Intent(AccountPrerferencesActivity.this, VerificationActivity.class);
                startActivity(verificationsActivity);
            }
        });

        LinearLayout invisibleButton = (LinearLayout)findViewById(R.id.invisibleModeButton);
        invisibleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent invisibleActivity = new Intent(AccountPrerferencesActivity.this, InvisibleActivity.class);
                startActivity(invisibleActivity);
            }
        });
/*
        LinearLayout paymentSettingsButton = (LinearLayout)findViewById(R.id.paymentSettingsButton);
       paymentSettingsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent invisibleActivity = new Intent(AccountPrerferencesActivity.this, PaymentSettingsActivity.class);
                startActivity(invisibleActivity);
            }
        });*/
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
