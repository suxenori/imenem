package com.menemi.settings.privacymenuactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.menemi.R;

/**
 * Created by tester03 on 17.06.2016.
 */
public class SwitcherActivity extends AppCompatActivity {
    private Toolbar toolbar;
    public static final String VALUE = "value";
    public static final String TITLE = "title";
    public static final String TEXT = "text";
    private static CompoundButton.OnCheckedChangeListener stateChangeListener= null;

    public static void setStateChangeListener(CompoundButton.OnCheckedChangeListener stateChangeListener) {
        SwitcherActivity.stateChangeListener = stateChangeListener;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distance_switcher_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Switch onOffSwitch = (Switch) findViewById(R.id.onOffSwitch);

        Intent intent = getIntent();
        boolean initialValue = intent.getBooleanExtra(VALUE, true);
        String title = intent.getStringExtra(TITLE);
        String text = intent.getStringExtra(TEXT);

        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText(text);

        onOffSwitch.setChecked(initialValue);
        onOffSwitch.setOnCheckedChangeListener(stateChangeListener);
               /* Configurations configurations = DBHandler.getInstance().getConfigurations();
                configurations.setShowDistance(b);
                DBHandler.getInstance().setConfigurations(configurations, new DBHandler.ResultListener() {
                    @Override
                    public void onFinish(Object object) {
                    }
                });*/

        toolbar.setTitle(title);
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
