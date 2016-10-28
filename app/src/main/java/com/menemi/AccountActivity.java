package com.menemi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.Configurations;


/**
 * Created by tester03 on 25.06.2016.
 */
public class AccountActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(com.menemi.R.layout.account_layout);
        Toolbar toolbar = (Toolbar)findViewById(com.menemi.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.account));
        Configurations configurations = DBHandler.getInstance().getConfigurations();

        CheckBox hideProfileAsDeleted = (CheckBox) findViewById(R.id.hideAsDeleted);

            hideProfileAsDeleted.setChecked(configurations.isHideProfileAsDeleted());
        hideProfileAsDeleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                configurations.setHideProfileAsDeleted(isChecked);
                DBHandler.getInstance().setConfigurations(configurations, new DBHandler.ResultListener() {
                    @Override
                    public void onFinish(Object object) {

                    }
                });
            }
        });
        EditText textEmailAddress = (EditText) findViewById(R.id.editEmailInAccountView);
        textEmailAddress.setText("" + DBHandler.getInstance().getMyProfile().getEmail());
        textEmailAddress.setKeyListener(null);
        Button logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHandler.getInstance().logout(AccountActivity.this.getApplicationContext());

                Intent firstActivity = new Intent(AccountActivity.this, FirstActivity.class);
                startActivity(firstActivity);
                firstActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
            }
        });
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
