package com.menemi.social_network.social_profile_photo_handler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.menemi.R;
import com.menemi.social_network.PhotoSettingsContainer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by tester03 on 28.09.2016.
 */

public class GridViewAdapter extends BaseAdapter
{
    private final Context context;

    private ArrayList<String> urls = new ArrayList<>() ;

    public GridViewAdapter(Context context, ArrayList<String> urls) {
        this.context = context;
        this.urls = urls;

    }


    @Override public View getView(int position, final View convertView, ViewGroup parent) {
        SquaredImageView squaredImageView = (SquaredImageView) convertView;
        if (squaredImageView == null) {
            squaredImageView = new SquaredImageView(context);

        }

        // Get the image URL for the current position.
        String url = getItem(position);


        // Trigger the download of the URL asynchronously into the image view.
        /*final RequestCreator requestCreator =  */
        Picasso.with(context) //
                .load(url) //
                .placeholder(R.drawable.empty_photo) //
                .error(R.drawable.empty_photo) //
                .tag(context) //
        /*requestCreator*/.into(squaredImageView);

        final SquaredImageView finalSquaredImageView = squaredImageView;
        squaredImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bitmap image = ((BitmapDrawable)finalSquaredImageView.getDrawable()).getBitmap();
                Intent i = new Intent(context, PhotoSettingsContainer.class);
                PhotoSettingsContainer.image = image;
                context.startActivity(i);
            }
        });
        squaredImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return squaredImageView;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override public String getItem(int position) {
        return urls.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }





}
