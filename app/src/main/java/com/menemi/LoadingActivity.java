package com.menemi;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.menemi.dbfactory.DBHandler;
import com.menemi.dbfactory.MyFirebaseInstanceIDService;
import com.menemi.dbfactory.MyFirebaseMessagingService;
import com.menemi.dbfactory.rest.PictureLoader;
import com.menemi.personobject.Interests;
import com.menemi.social_network.SocialNetworkHandler;
import com.menemi.social_network.instagram.InstagramApp;
import com.menemi.utils.Utils;
import com.vk.sdk.util.VKUtil;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import ru.ok.android.sdk.Odnoklassniki;
import ru.ok.android.sdk.OkListener;

/**
 * Created by tester03 on 01.08.2016.
 */
public class LoadingActivity extends AppCompatActivity {
    private static final String TAG = "LoadingActivity";

    @Override
    protected void onStart() {
        super.onStart();
        if( getIntent() != null && getIntent().getExtras() != null && PersonPage.isActivityOn) {
            sendBroadcast(getIntent());
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(com.menemi.R.layout.loading_layout);
        Log.d("onCreate","onCreate called");
        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        Log.d("fingerprints",fingerprints[0] + "");
        Log.d(TAG, "FaceBookInit");
        DBHandler.setUP(this.getApplicationContext());
        PictureLoader.setDefaultPicture(Utils.getBitmapFromResource(this, R.drawable.empty_photo));
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.menemi",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        final Odnoklassniki odnoklassniki = Odnoklassniki.createInstance(this,
                SocialNetworkHandler.getInstance().APPLICATION_OK_ID, SocialNetworkHandler.getInstance().PUBLICK_APPLICATION_OK_KEY);
        InstagramApp instaObj = new InstagramApp(this, SocialNetworkHandler.getInstance().CLIENT_ID,
                SocialNetworkHandler.getInstance().CLIENT_SECRET, SocialNetworkHandler.getInstance().CALLBACK_URL);
        Intent firebaseMessagingService = new Intent(LoadingActivity.this, MyFirebaseMessagingService.class);
        startService(firebaseMessagingService);

        Intent firebaseIDService = new Intent(LoadingActivity.this, MyFirebaseInstanceIDService.class);
        startService(firebaseIDService);

        if (Profile.getCurrentProfile() != null) {
            SocialNetworkHandler.getInstance().setFbProfileId(Profile.getCurrentProfile().getId());
        }


        final int userId = DBHandler.getInstance().loadLastId();
        Log.v(TAG, "userId = " + userId);

        if (userId != -1) {
            DBHandler.getInstance().authorise(userId, object -> {


                    finish();
                    final Intent personPage = new Intent(LoadingActivity.this, PersonPage.class);
                    if(getIntent() !=null && getIntent().getExtras() != null) {
                        personPage.putExtras(getIntent().getExtras());
                       // Log.d(TAG, getIntent().getExtras().getString("action"));
                    }
                    startActivity(personPage);
            });

            DBHandler.getInstance().getInterestsGroup(DBHandler.getInstance().getUserId(), object -> {
                ArrayList groups = (ArrayList) object;
                DBHandler.getInstance().setInterestsGroupArray(groups);
            });
            if (SocialNetworkHandler.getInstance().isAuthFb()) {
            }
            odnoklassniki.checkValidTokens(new OkListener() {
                @Override
                public void onSuccess(JSONObject json) {
                    SocialNetworkHandler.getInstance().getCurrentOkUserPhoto(null);
                }

                @Override
                public void onError(String error) {

                }
            });

            DBHandler.getInstance().getInterestProfile(DBHandler.getInstance().getUserId(), DBHandler.getInstance().getUserId(), object -> {
                ArrayList<Interests> personInterestsArray;
                personInterestsArray = (ArrayList<Interests>) object;
                DBHandler.getInstance().setProfileInterests(personInterestsArray);

            });


        } else {
            Log.d(TAG, "NO ID");
            finish();
            Intent intent = new Intent(LoadingActivity.this, FirstActivity.class);
            startActivity(intent);
        }

    }
}