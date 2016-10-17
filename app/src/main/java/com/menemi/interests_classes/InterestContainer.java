package com.menemi.interests_classes;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.menemi.R;

/**
 * Created by tester03 on 30.07.2016.
 */
public class InterestContainer extends AppCompatActivity
{
    private static Toolbar toolbar;

    public static Toolbar getToolbar()
    {
        return toolbar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interests_container);

        toolbar = (Toolbar) findViewById(R.id.toolbar_with_checkBox);
        setSupportActionBar(toolbar);
        PersonInterestsFragment personInterestsFragment = new PersonInterestsFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.interest_empty, personInterestsFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

}



