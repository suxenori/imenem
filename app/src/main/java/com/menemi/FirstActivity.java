package com.menemi;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.menemi.dbfactory.AndroidDatabaseManager;
import com.menemi.personobject.PersonObject;
import com.menemi.social_network.SocialNetworkHandler;
import com.menemi.utils.ViewScaler;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.vk.sdk.VKSdk;


public class FirstActivity extends Activity
{

    private static final String TAG = "MainActivity";
    private Button signIn;
    private ImageButton button;
    PersonObject personObject = null;
    private void formatViews()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        ViewScaler.setScreen(metrics.heightPixels, metrics.widthPixels);
        View signInButton = findViewById(com.menemi.R.id.signIn);

        ViewScaler.setSize(signInButton, 10, 50);
        ViewScaler.setMargins(signInButton, 0, 0, 0, 10);
        TextView register = (TextView) findViewById(com.menemi.R.id.register);
        register.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewScaler.getTextSize(2.123f));
        ViewScaler.setSize(register,3,100);
        ViewScaler.setMargins(register, 0, 0, 0, 1);

        View socials = findViewById(com.menemi.R.id.socialNetworks);
        ViewScaler.setSize(socials, 5.03778f,100);
        ViewScaler.setMargins(socials, 0, 0, 0, 1);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        VKSdk.initialize(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(com.menemi.R.layout.activity_first);
        button = (ImageButton) findViewById(com.menemi.R.id.fbButton);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SocialNetworkHandler.getInstance().authFb(FirstActivity.this);
                Log.d("test_fb","authFb is called");
            }
        });
        ImageButton gplusButton = (ImageButton)findViewById(com.menemi.R.id.gpButton);
        gplusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FirstActivity.this, AndroidDatabaseManager.class);
                startActivity(i);
            }
        });

        ImageButton vkButton = (ImageButton)findViewById(com.menemi.R.id.vkButton);
        vkButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });

        TextView register = (TextView)findViewById(com.menemi.R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SelectSexPage();
                replaceFragment(getFragmentManager().beginTransaction(),fragment);
            }
        });
        ImageButton fbook_button = (ImageButton)findViewById(com.menemi.R.id.faceBookButton);
        signIn = (Button) findViewById(com.menemi.R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CallbackManager.Factory.create().onActivityResult(requestCode, resultCode, data);
    }
   public void replaceFragment(android.app.FragmentTransaction transaction,Fragment fragment){
        transaction.replace(com.menemi.R.id.activityFirst,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}




