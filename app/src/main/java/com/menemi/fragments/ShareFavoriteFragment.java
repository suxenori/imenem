package com.menemi.fragments;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.PersonObject;

/**
 * Created by Ui-Developer on 30.07.2016.
 */
public class ShareFavoriteFragment extends Fragment{
    private View rootView = null;

    private PersonObject personObject = null;
    private boolean isFavorite = false;
    private ShareDialog shareDialog;


    public void setTargetObject(PersonObject personObject) {
        this.personObject = personObject;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.share_favorite_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        FacebookSdk.sdkInitialize(getActivity());
        ImageView shareButton = (ImageView)rootView.findViewById(R.id.share);
        CallbackManager shareCallback = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(getActivity());
        shareDialog.registerCallback(shareCallback, new FacebookCallback<Sharer.Result>()
        {
            @Override
            public void onSuccess(Sharer.Result result)
            {

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

        shareButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (ShareDialog.canShow(ShareLinkContent.class))
                {
                    DBHandler.getInstance().getAvatarURL(personObject.getPersonId(), new DBHandler.ResultListener()
                    {
                        @Override
                        public void onFinish(Object object)
                        {
                            String imageUrl = (String) object;
                            if (imageUrl.equals("")){
                                imageUrl = "http://minemi.ironexus.com/no-profile-picture.jpg";
                            }
                            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                    .setImageUrl(Uri.parse(imageUrl))
                                    .setContentTitle(personObject.getPersonName())
                                    .setContentDescription("Как вам?")
                                    .setContentUrl(Uri.parse("https://fb.me/1237630929626385"))
                                    .build();

                            shareDialog.show(linkContent);
                        }
                    });
                }
            }
        });


        DBHandler.getInstance().getMyProfile(new DBHandler.ResultListener() {
            @Override
            public void onFinish(Object object) {
                final PersonObject owner = (PersonObject)object;
                isFavorite = owner.isAddedAsFavorite(personObject);
                toogleFavorite(isFavorite);
                ImageView favorite = (ImageView) rootView.findViewById(R.id.favorite);
                favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isFavorite = !isFavorite;
                        toogleFavorite(isFavorite);
                    DBHandler.getInstance().addFavorites(DBHandler.getInstance().getUserId(), personObject.getPersonId(), isFavorite, new DBHandler.ResultListener()
                    {
                        @Override
                        public void onFinish(Object object)
                        {
                            owner.setAddedAsFavorite(personObject,isFavorite);
                        }
                    });
                    }
                });
            }
        });




        return rootView;
    }
    private void toogleFavorite(boolean isFavorite){
        ImageView favorite = (ImageView) rootView.findViewById(R.id.favorite);
        if(isFavorite){
            favorite.setImageResource(R.drawable.star_full);
        } else {
            favorite.setImageResource(R.drawable.star_empty);
        }
    }

}
