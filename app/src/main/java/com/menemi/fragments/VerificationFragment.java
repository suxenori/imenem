package com.menemi.fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.menemi.R;
import com.menemi.personobject.PersonObject;
import com.menemi.social_network.LogOutSocialDialog;
import com.menemi.social_network.SocialNetworkHandler;
import com.menemi.social_network.instagram.InstagramApp;
import com.vk.sdk.VKSdk;

import org.json.JSONObject;

import ru.ok.android.sdk.Odnoklassniki;
import ru.ok.android.sdk.OkListener;

/**
 * Created by Ui-Developer on 07.07.2016.
 */
public class VerificationFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener
{
    private View rootView = null;
    private PersonObject personObject = null;
    private ImageView fbImage;
    private ImageView vkImage;
    private TextView fbState;
    private TextView vkState;
    private TextView okState;
    private ImageView okImage;
    private ImageView instaImage;
    private TextView instaState;
    private ImageView gImage;
    private TextView gState;
    private GoogleApiClient mGoogleApiClient;
    private OptionalPendingResult<GoogleSignInResult> opr;
    private InstagramApp instaObj;



    public void setPersonObject(PersonObject personObject) {
        this.personObject = personObject;
    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity());
        AppEventsLogger.activateApp(getActivity());
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).addOnConnectionFailedListener(this).build();
        instaObj = new InstagramApp(getActivity(), SocialNetworkHandler.CLIENT_ID,
                SocialNetworkHandler.CLIENT_SECRET, SocialNetworkHandler.CALLBACK_URL);

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.verification_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        if(purpose != PersonDataFragment.Purpose.MY_PROFILE){
            LinearLayout verifyPhotoContainer = (LinearLayout) rootView.findViewById(R.id.verifyPhotoContainer);
            verifyPhotoContainer.removeAllViews();
            TextView fbState = (TextView) rootView.findViewById(R.id.fbState);
            fbState.setText("127 друзей");
            TextView vkState = (TextView) rootView.findViewById(R.id.vkState);
            vkState.setText("54 друга");
            TextView instaState = (TextView) rootView.findViewById(R.id.instaState);
            instaState.setText("250 подписчиков");
        } else {
            LinearLayout fbButton = (LinearLayout) rootView.findViewById(R.id.verifyFBContainer);
            LinearLayout vkButton = (LinearLayout) rootView.findViewById(R.id.verifyVKContainer);
            LinearLayout okButton = (LinearLayout) rootView.findViewById(R.id.verifyOkContainer);
            LinearLayout instaButton = (LinearLayout) rootView.findViewById(R.id.verifyInstaContainer);
            LinearLayout gButton = (LinearLayout)rootView.findViewById(R.id.verifyGContainer);
            fbState = (TextView) rootView.findViewById(R.id.fbState);
            gState = (TextView)rootView.findViewById(R.id.gState);
            gImage = (ImageView)rootView.findViewById(R.id.g_Src);
            fbImage = (ImageView) rootView.findViewById(R.id.fbSrc);
            vkState = (TextView) rootView.findViewById(R.id.vkState);
            vkImage = (ImageView) rootView.findViewById(R.id.vkSrc);
            okState = (TextView) rootView.findViewById(R.id.okState);
            okImage = (ImageView) rootView.findViewById(R.id.okImage);
            instaImage = (ImageView) rootView.findViewById(R.id.instaImage);
            instaState = (TextView) rootView.findViewById(R.id.instaState);
            final Odnoklassniki odnoklassniki = Odnoklassniki.createInstance(getActivity(),
                    SocialNetworkHandler.getInstance().APPLICATION_OK_ID, SocialNetworkHandler.getInstance().PUBLICK_APPLICATION_OK_KEY);
            odnoklassniki.checkValidTokens(new OkListener()
            {
                @Override
                public void onSuccess(JSONObject json)
                {
                    okImage.setImageResource(R.drawable.ok_logo);
                }

                @Override
                public void onError(String error)
                {
                    okImage.setImageResource(R.drawable.ok_off);
                    okState.setText("(привязать)");
                }
            });
            final LogOutSocialDialog dialog = new LogOutSocialDialog();
            if (true){                                                                   //<--- change this loop, when generate correct SH1
                gImage.setImageDrawable(getResources().getDrawable(R.drawable.gplus_ic));
                gState.setText("(подтвержден)");
            } else {
                gImage.setImageDrawable(getResources().getDrawable(R.drawable.gplus_ic_off));
                gState.setText("(привязать)");
            }
            if (AccessToken.getCurrentAccessToken() != null){
                fbImage.setImageDrawable(getResources().getDrawable(R.drawable.fb_icon));
                fbState.setText("(подтвержден)");
            } else {
                fbImage.setImageDrawable(getResources().getDrawable(R.drawable.fb_off));
                fbState.setText("(привязать)");
            }
            if (VKSdk.isLoggedIn()){
                vkImage.setImageDrawable(getResources().getDrawable(R.drawable.vk_icon));
                vkState.setText("(подтвержден)");
            } else {
                vkImage.setImageDrawable(getResources().getDrawable(R.drawable.vk_off));
                vkState.setText("(привязать)");
            }
            if (instaObj.isLogged()){
                instaImage.setImageDrawable(getResources().getDrawable(R.drawable.add_insta));
                instaState.setText("(подтвержден)");
            } else {
                instaImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_verification_instagram_disabled));
                instaState.setText("(привязать)");
            }
            gButton.setOnClickListener(view -> {

                signIn();
                Log.d("g_button", " button is pressed");
            });

            fbButton.setOnClickListener(view -> {
                if (SocialNetworkHandler.getInstance().isAuthFb()){
                    dialog.setSocial(SocialNetworkHandler.getInstance().FB_SOCIAL);
                    SocialNetworkHandler.getInstance().getUserAvatar((Profile.getCurrentProfile().getProfilePictureUri(100,100)).toString());
                    dialog.setStyle(DialogFragment.STYLE_NORMAL,R.style.CustomDialog);
                    dialog.show(getFragmentManager(),"fb");


                } else {
                    SocialNetworkHandler.getInstance().authFb(getActivity());
                }
            });

            vkButton.setOnClickListener(view -> {
                if (VKSdk.isLoggedIn()){
                    dialog.setSocial(SocialNetworkHandler.getInstance().VK_SOCIAL);
                    dialog.setStyle(DialogFragment.STYLE_NORMAL,R.style.CustomDialog);
                    dialog.show(getFragmentManager(),"vk");
                } else {
                    SocialNetworkHandler.getInstance().authVk(getActivity());
                }
            });

            instaButton.setOnClickListener(view -> {
                    if (instaObj.isLogged()){
                        dialog.setSocial(SocialNetworkHandler.getInstance().INSTA_SOCIAL);
                        dialog.setStyle(DialogFragment.STYLE_NORMAL,R.style.CustomDialog);
                        dialog.show(getFragmentManager(),"Insta");
                        dialog.setInstagramApp(instaObj);
                    } else {
                        instaObj.authorize();
                        instaObj.setListener(new InstagramApp.OAuthAuthenticationListener()
                        {
                            @Override
                            public void onSuccess()
                            {
                                Log.e("Userid", instaObj.getId());
                                Log.e("Name", instaObj.getName());
                                Log.e("UserName", instaObj.getUserName());
                                dialog.setInstagramApp(instaObj);
                                instaImage.setImageResource(R.drawable.add_insta);
                                instaState.setText("(подтвержден)");
                            }

                            @Override
                            public void onFail(String error)
                            {
                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            okButton.setOnClickListener(view -> odnoklassniki.checkValidTokens(new OkListener()
            {
                @Override
                public void onSuccess(JSONObject json)
                {
                    dialog.setSocial(SocialNetworkHandler.getInstance().OK_SOCIAL);
                    dialog.setStyle(DialogFragment.STYLE_NORMAL,R.style.CustomDialog);
                    dialog.show(getFragmentManager(),"ok");
                }

                @Override
                public void onError(String error)
                {
                    SocialNetworkHandler.getInstance().okAuth(getActivity());
                }
            }));
        }
        return rootView;

    }
    private PersonDataFragment.Purpose purpose = PersonDataFragment.Purpose.LIKE;
    public void setPurpose(PersonDataFragment.Purpose purpose) {
        this.purpose = purpose;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.

            opr.setResultCallback(googleSignInResult -> handleSignInResult(googleSignInResult));
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null){
            mGoogleApiClient.disconnect();
        }
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        getActivity().startActivityForResult(signInIntent, 22222);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }
}
