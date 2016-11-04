package com.menemi.social_network.social_profile_photo_handler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.GridView;

import com.menemi.R;
import com.menemi.social_network.SocialNetworkHandler;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by tester03 on 28.09.2016.
 */

public class SocialGridView extends AppCompatActivity
{
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (prepareUrlArray(getIntent().getStringExtra(SocialNetworkHandler.getInstance().TARGET_SOCIAL_CONST)).length == 0){
            setContentView(R.layout.no_photos_in_images);
            Button backButton = (Button)findViewById(R.id.back_button);
            backButton.setOnClickListener(v -> {
                finish();
            });
        } else {
            setContentView(R.layout.gridview_activity);
            ArrayList<String> urls = new ArrayList<>();
            Collections.addAll(urls, prepareUrlArray(getIntent().getStringExtra(SocialNetworkHandler.getInstance().TARGET_SOCIAL_CONST)));
            Collections.shuffle(urls);

            GridView gv = (GridView) findViewById(R.id.grid_view);
            gv.setAdapter(new GridViewAdapter(this,urls));
            gv.setOnScrollListener(new ScrollViewListener(this));
            gv.invalidateViews();
        }



    }
    private String[] prepareUrlArray(String social){
        ArrayList<String> urlArray = new ArrayList<>();
        if (social.equals(SocialNetworkHandler.getInstance().OK_SOCIAL)){
            urlArray = SocialNetworkHandler.getInstance().getPhotoUrlOk();
        } else if (social.equals(SocialNetworkHandler.getInstance().VK_SOCIAL)){
            urlArray = SocialNetworkHandler.getInstance().getPhotoUrlVk();
        } else if (social.equals(SocialNetworkHandler.getInstance().INSTA_SOCIAL)){
            urlArray = SocialNetworkHandler.getInstance().getPhotoUrlInsta();
        } else if (social.equals(SocialNetworkHandler.getInstance().FB_SOCIAL)){
            urlArray = SocialNetworkHandler.getInstance().getPhotoUrlFb();
        } else if (social.equals(SocialNetworkHandler.getInstance().G_SOCIAL)){
            urlArray = SocialNetworkHandler.getInstance().getPhotoUrlG_plus();
        }
        String[] data = urlArray.toArray(new String[urlArray.size()]);
        return data;
    }
}
