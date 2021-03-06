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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.menemi.customviews.OneButtonDialog;
import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.Interests;
import com.menemi.personobject.PersonObject;
import com.menemi.social_network.SocialNetworkHandler;
import com.menemi.social_network.SocialProfile;
import com.menemi.utils.ViewScaler;
import com.vk.sdk.VKSdk;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;


public class FirstActivity extends Activity
{

    private static final String TAG = "MainActivity";
    private Button signIn;
    private ImageButton fbButton;
    private PersonObject personObject = null;
    private CallbackManager callbackManager;
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
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(final LoginResult loginResult)
            {
                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        (object, response) -> {
                            try
                            {
                                String name = object.getString("name");
                                AccessToken.getCurrentAccessToken().getPermissions();
                                AccessToken.getCurrentAccessToken().getDeclinedPermissions();
                                String gender = object.getString("gender");
                                String id = object.getString("id");
                                DBHandler.getInstance().registerFacebook(new SocialProfile(id,name,gender), object1 -> {
                                    DBHandler.getInstance().getInterestProfile(DBHandler.getInstance().getUserId(),
                                            DBHandler.getInstance().getUserId(), object12 -> {
                                                ArrayList<Interests> profileInterests;
                                                profileInterests = (ArrayList<Interests>) object12;
                                                DBHandler.getInstance().setProfileInterests(profileInterests);
                                            });

                                    DBHandler.getInstance().getInterestsGroup(DBHandler.getInstance().getUserId(), object41 -> {
                                        ArrayList groups = (ArrayList) object41;
                                        DBHandler.getInstance().setInterestsGroupArray(groups);
                                    });
                                       /* DBHandler.getInstance().getInterestProfile(DBHandler.getInstance().getUserId(), DBHandler.getInstance().getUserId(), object -> {
                                            ArrayList<Interests> personInterestsArray;
                                            personInterestsArray = (ArrayList<Interests>) object;
                                            DBHandler.getInstance().setProfileInterests(personInterestsArray);*/
                                    Log.d("","");
                                    SocialNetworkHandler.getInstance().getProfileAlbumId(getApplicationContext(),AccessToken.getCurrentAccessToken());
                                    finish();
                                    Intent personPage = new Intent(FirstActivity.this, PersonPage.class);
                                    startActivity(personPage);
                                    personPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    Log.i("register", "register is successful");

                                });
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,gender");
                request.setParameters(parameters);
                request.executeAsync();
                //fetchCurrentFbUser();

            }

            @Override
            public void onCancel()
            {

            }

            @Override
            public void onError(FacebookException error)
            {

            }
        });
        setContentView(com.menemi.R.layout.activity_first);
        fbButton = (ImageButton) findViewById(com.menemi.R.id.fbButton);

        fbButton.setOnClickListener(view -> {
            DBHandler.getInstance().isRESTAvailable((isOnline)->{
                if((boolean)isOnline){
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_photos", "public_profile", "user_about_me"));

           // SocialNetworkHandler.getInstance().regWithFb(FirstActivity.this,callbackManager);
            Log.d("test_fb","authFb is called");
                } else {
                    OneButtonDialog dialog = new OneButtonDialog(this, getString(R.string.fb_fail), getString(R.string.connection_lost_n_please_check_your_internet_connection_n_and_try_again).replace("\n",""),OneButtonDialog.NO_ICON,()->{});
                }
            });
        });


        TextView register = (TextView)findViewById(com.menemi.R.id.register);
        register.setOnClickListener(v -> {
            Fragment fragment = new SelectSexPage();
            replaceFragment(getFragmentManager().beginTransaction(),fragment);
        });
       // ImageButton fbook_button = (ImageButton)findViewById(com.menemi.R.id.faceBookButton);
        signIn = (Button) findViewById(com.menemi.R.id.signIn);
        signIn.setOnClickListener(v -> {
            finish();
           Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
            startActivity(intent);

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
   public void replaceFragment(android.app.FragmentTransaction transaction,Fragment fragment){
        transaction.replace(com.menemi.R.id.activityFirst,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}




