package com.menemi.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.dbfactory.rest.PictureLoader;
import com.menemi.personobject.NewsInfo;
import com.menemi.utils.Utils;

/**
 * Created by Ui-Developer on 16.11.2016.
 */

public class NewsInfoFragment extends Fragment{
    private View rootView;
    private NewsInfo newsInfo = null;
    private ImageView avatar;
    private ImageView photoNews;
    private LinearLayout newsImage;
    private TextView newsOwnerName;
    private TextView newsNameType;
    private TextView date;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.news_row, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
                parent.removeAllViews();

            }
        }

        avatar = (ImageView)rootView.findViewById(R.id.circleAvatar);
        photoNews = (ImageView)rootView.findViewById(R.id.image);
        newsImage = (LinearLayout)rootView.findViewById(R.id.newsImageType);
        newsOwnerName = (TextView)rootView.findViewById(R.id.newsOwnerName);
        newsNameType = (TextView)rootView.findViewById(R.id.newsTypeName);
        date = (TextView)rootView.findViewById(R.id.date);
        handleNews(newsInfo);

        return rootView;
    }

    public void handleNews(NewsInfo newsInfo){

         DBHandler.getInstance().getAvatar(newsInfo.getId(), object -> avatar.setImageBitmap(Utils.getCroppedBitmap((Bitmap) object)));

        if (newsInfo.getType() == NewsInfo.NEWS_TYPE.PHOTO){
            new PictureLoader(newsInfo.getImageNewsUrl(), picture -> photoNews.setImageBitmap(picture));
            newsImage.setBackgroundResource(R.drawable.photo_add_1);

        } else if (newsInfo.getType() ==  NewsInfo.NEWS_TYPE.LIKE){
            newsImage.setBackgroundResource(R.drawable.liked_you);

        } else if (newsInfo.getType() == NewsInfo.NEWS_TYPE.FAVORITE){
            newsImage.setBackgroundResource(R.drawable.fav_icon);
        }
        newsOwnerName.setText(newsInfo.getName());
        newsNameType.setText(newsInfo.getAction());
        date.setText(newsInfo.getDate().toString());

    }



    public void setNewsInfo(NewsInfo newsInfo) {
        this.newsInfo = newsInfo;
    }
}
