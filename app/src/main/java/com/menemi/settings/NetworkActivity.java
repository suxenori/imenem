package com.menemi.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.menemi.R;
import com.menemi.customviews.OCDialog;

/**
 * Created by tester03 on 20.06.2016.
 */
public class NetworkActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Network settings");
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Button showDialogButton = (Button) findViewById(R.id.eraseImportedContactButton);
        showDialogButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               new OCDialog(NetworkActivity.this, getString(R.string.erase_imported_contacts),getString(R.string. erase_imported_contacts_message), OCDialog.NO_ICON, ()->{}, ()->{});
            }
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
