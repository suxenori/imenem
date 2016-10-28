package com.menemi.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.dbfactory.SQLiteEngine;
import com.menemi.fragments.PersonDataFragment;
import com.menemi.fragments.VerificationFragment;
import com.menemi.social_network.SocialNetworkHandler;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import org.json.JSONObject;

import ru.ok.android.sdk.Odnoklassniki;
import ru.ok.android.sdk.OkListener;

/**
 * Created by tester03 on 21.06.2016.
 */
public class VerificationContainerActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_container_layout);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.get_verified));
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
VerificationFragment verificationFragment = new VerificationFragment();
        verificationFragment.setPersonObject(DBHandler.getInstance().getMyProfile());
        verificationFragment.setPurpose(PersonDataFragment.Purpose.MY_PROFILE);
        getFragmentManager().beginTransaction().replace(R.id.fragment1, verificationFragment).commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 22222) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
           /* if (result != null){
                //SocialNetworkHandler.getInstance().getImageG_plus();
            }
*/
        }

        CallbackManager.Factory.create().onActivityResult(requestCode, resultCode, data);

        if (SocialNetworkHandler.getInstance().isAuthFb()){
            ImageView imageView = (ImageView)findViewById(R.id.fbSrc);
            imageView.setImageResource(R.drawable.fb_icon);
            TextView textView = (TextView)findViewById(R.id.fbState);
            textView.setText("(подтвержден)");

            SocialNetworkHandler.getInstance().getProfileAlbumId(getApplicationContext(), AccessToken.getCurrentAccessToken());
        }
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                ImageView imageView = (ImageView)findViewById(R.id.vkSrc);
                imageView.setImageResource(R.drawable.vk_icon);
                SocialNetworkHandler.getInstance().getUserPhotoFromVk(String.valueOf(0), String.valueOf(200));
                if (DBHandler.getInstance().isEmtyTable(SQLiteEngine.TABLE_SOCIAL_VK)){
                    Log.d("check_table", String.valueOf(DBHandler.getInstance().isEmtyTable(SQLiteEngine.TABLE_SOCIAL_VK )));
                    SocialNetworkHandler.getInstance().getCurrentVkUser();
                }



            }
            @Override
            public void onError(VKError error) {
                // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
            }
        }));
        if (Odnoklassniki.getInstance().isActivityRequestOAuth(requestCode)) {
            Odnoklassniki.getInstance().onAuthActivityResult(requestCode, resultCode, data, new OkListener()
            {
                @Override
                public void onSuccess(JSONObject json)
                {
                    SocialNetworkHandler.getInstance().getCurrentOkUser();
                    SocialNetworkHandler.getInstance().getCurrentOkUserPhoto(null);
                    ImageView imageView = (ImageView)findViewById(R.id.okImage);
                    imageView.setImageResource(R.drawable.ok_logo);
                    TextView textView = (TextView)findViewById(R.id.okState);
                    textView.setText("(подтвержден)");
                }

                @Override
                public void onError(String error)
                {

                }
            });
        }



    }
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String s = acct.getId();
            String s1 = acct.getDisplayName();
            /*Toast.makeText(this,acct.getFamilyName() + "",Toast.LENGTH_SHORT).show();*/

        }
    }
}
