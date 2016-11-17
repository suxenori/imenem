package com.menemi.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.menemi.PersonPage;
import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.NewsInfo;

import java.util.ArrayList;

/**
 * Created by Ui-Developer on 16.11.2016.
 */

public class NewsListFragment extends Fragment {
    private View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.scroll_list_container, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
                parent.removeAllViews();

            }
        }

        DBHandler.getInstance().getNews(10, 0,(object)->{

            ArrayList<NewsInfo> news = (ArrayList<NewsInfo>)object;
            for (int i = 0; i < news.size(); i++) {
                NewsInfoFragment newsInfoFragment = new NewsInfoFragment();
                newsInfoFragment.setNewsInfo(news.get(i));
                getFragmentManager().beginTransaction().add(R.id.fragment1, newsInfoFragment).commitAllowingStateLoss();
            }
            PersonPage.finishProgressDialog();
        });


        return rootView;
    }
}
