package com.menemi.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.menemi.AccountActivity;
import com.menemi.R;
import com.menemi.dbfactory.DBHandler;


/**
 * Created by irondev on 06.06.16.
 */
public class SettingsActivity extends AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_top);
        TextView title = (TextView)toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        title.setText(getString(R.string.settings));
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
if(DBHandler.getInstance().getConfigurations() == null){
    DBHandler.getInstance().prepareConfigurations((b)->{
        prepareScreen();
    });
} else{
    prepareScreen();
}



    }

    private void prepareScreen() {
        LinearLayout basicInfo = (LinearLayout)findViewById(R.id.basicInfoButton);
        basicInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, BasicInfoActivity.class);
                startActivity(i);
            }
        });
        LinearLayout accountPreferencesButton = (LinearLayout)findViewById(R.id.acountPreferencesButton);
        accountPreferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, AccountPrerferencesActivity.class);
                startActivity(i);
            }
        });

        LinearLayout accountButton = (LinearLayout)findViewById(R.id.accountButton);
        accountButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent accountActivity = new Intent(SettingsActivity.this, AccountActivity.class);
                startActivity(accountActivity);
            }
        });
        TextView email = (TextView) findViewById(R.id.email);
        email.setText("" + DBHandler.getInstance().getMyProfile().getEmail());
       /* LinearLayout aboutButton = (LinearLayout)findViewById(R.id.aboutButton);
        aboutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent aboutActivity = new Intent(SettingsActivity.this, AboutActivity.class);
                startActivity(aboutActivity);
            }
        });
*/

      /*  LinearLayout helpCenterButton = (LinearLayout)findViewById(R.id.helpCenterButton);
        helpCenterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent helpCenterActivity = new Intent(SettingsActivity.this, HelpCenter.class);
                startActivity(helpCenterActivity);
            }
        });

        Button sqButton = (Button)findViewById(R.id.sqlite);
        sqButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(SettingsActivity.this, AndroidDatabaseManager.class);
                startActivity(i);
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
