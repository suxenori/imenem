package com.menemi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.menemi.dbfactory.DBHandler;
import com.menemi.dbfactory.MyFirebaseInstanceIDService;
import com.menemi.dbfactory.MyFirebaseMessagingService;
import com.menemi.dbfactory.rest.PictureLoader;
import com.menemi.filter.FilterObject;
import com.menemi.interests_classes.InterestsGroup;
import com.menemi.personobject.Interests;
import com.menemi.personobject.PersonObject;
import com.menemi.social_network.SocialNetworkHandler;
import com.menemi.social_network.instagram.InstagramApp;
import com.menemi.utils.Utils;
import com.vk.sdk.VKSdk;
import com.vk.sdk.util.VKUtil;

import org.json.JSONObject;

import java.util.ArrayList;

import ru.ok.android.sdk.Odnoklassniki;
import ru.ok.android.sdk.OkListener;

/**
 * Created by tester03 on 01.08.2016.
 */
public class LoadingActivity extends AppCompatActivity {
    private static final String TAG = "LoadingActivity";
    private PersonObject personObject = null;


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


        final Odnoklassniki odnoklassniki = Odnoklassniki.createInstance(this,
                SocialNetworkHandler.getInstance().APPLICATION_OK_ID, SocialNetworkHandler.getInstance().PUBLICK_APPLICATION_OK_KEY);
        InstagramApp instaObj = new InstagramApp(this, SocialNetworkHandler.CLIENT_ID,
                SocialNetworkHandler.CLIENT_SECRET, SocialNetworkHandler.CALLBACK_URL);
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
                personObject = (PersonObject) object;
                DBHandler.getInstance().downloadFilterSettings(personObject.getPersonId(), obj -> personObject.setFilterObject((FilterObject) obj));



                    finish();
                    final Intent personPage = new Intent(LoadingActivity.this, PersonPage.class);
                    if(getIntent() !=null && getIntent().getExtras() != null) {
                        personPage.putExtras(getIntent().getExtras());
                    }
                    startActivity(personPage);
            });

            DBHandler.getInstance().getInterestsGroup(DBHandler.getInstance().getUserId(), object -> {
                InterestsGroup interestGroup;
                ArrayList<InterestsGroup> interestsGroupArray;
                ArrayList groups;
                interestsGroupArray = new ArrayList<>();
                groups = (ArrayList) object;
                for (int i = 0; i < groups.size(); i++) {
                    interestGroup = (InterestsGroup) groups.get(i);
                    interestsGroupArray.add(interestGroup);
                    Log.i("groups", interestGroup.getNameGroup());
                }
                DBHandler.getInstance().setInterestsGroupArray(interestsGroupArray);
            });
            if (SocialNetworkHandler.getInstance().isAuthFb()) {
                SocialNetworkHandler.getInstance().getProfileAlbumId(getApplicationContext(),AccessToken.getCurrentAccessToken());
            }
            if (VKSdk.isLoggedIn()) {
                SocialNetworkHandler.getInstance().getUserPhotoFromVk(String.valueOf(0), String.valueOf(200));
            }
            if (instaObj.isLogged()) {
                SocialNetworkHandler.getInstance().getPhotoUrlInsta().clear();
                instaObj.getPhoto();
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