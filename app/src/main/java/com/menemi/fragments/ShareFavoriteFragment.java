package com.menemi.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.PersonObject;

/**
 * Created by Ui-Developer on 30.07.2016.
 */
public class ShareFavoriteFragment extends Fragment{
    private View rootView = null;

    PersonObject personObject = null;
    boolean isFavorite = false;


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
        DBHandler.getInstance().getMyProfile(new DBHandler.ResultListener() {
            @Override
            public void onFinish(Object object) {
                final PersonObject owner = (PersonObject)object;
                ImageView share = (ImageView) rootView.findViewById(R.id.share);
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
