package com.menemi.customviews;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.menemi.R;
import com.menemi.personobject.PhotoSetting;
import com.menemi.social_network.social_profile_photo_handler.SquaredImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by tester03 on 28.09.2016.
 */

public class GridAdapter extends BaseAdapter
{
    private final Context context;

    private ArrayList<PhotoSetting> photoSettings = new ArrayList<>() ;
    OnPhotoChooseListener onPhotoChooseListener;
    public GridAdapter(Context context, ArrayList<PhotoSetting> photoSettings, OnPhotoChooseListener onPhotoChooseListener) {
        this.context = context;
        this.photoSettings = photoSettings;
        this.onPhotoChooseListener = onPhotoChooseListener;

    }

public void removePhoto(PhotoSetting photoSetting){
    photoSettings.remove(photoSetting);
    notifyDataSetChanged();
}
    @Override public View getView(int position, final View convertView, ViewGroup parent) {
        SquaredImageView squaredImageView = (SquaredImageView) convertView;
        if (squaredImageView == null) {
            squaredImageView = new SquaredImageView(context);

        }

        // Get the image URL for the current position.
        String url = getItem(position).getPhotoUrl();


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
                onPhotoChooseListener.onChoose(getItem(position));
            }
        });
        squaredImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return squaredImageView;
    }

    @Override
    public int getCount() {
        return photoSettings.size();
    }

    @Override public PhotoSetting getItem(int position) {
        return photoSettings.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }



public interface OnPhotoChooseListener{
    void onChoose(PhotoSetting photoSetting);
}

}
