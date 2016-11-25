package com.menemi.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.menemi.PersonPage;
import com.menemi.R;
import com.menemi.customviews.ObservableScrollView;
import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.NewsInfo;

import java.util.ArrayList;


public class NewsListFragment extends Fragment {
    private View rootView = null;
    private  ArrayList<NewsInfo> news = new  ArrayList<>();
    private int newsToShow = 7;
    private int newsOffset = 0;
    private LayoutInflater inflater;
    boolean isLoading = false;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.picture_scroll_list_container, container, false);
            this.inflater = inflater;
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        PersonPage.finishProgressDialog();
            insertNews(newsOffset,newsToShow);
        configureToolbar();
        ObservableScrollView list = (ObservableScrollView) rootView.findViewById(R.id.content);
        list.setScrollViewListener((scrollView, x, y, oldx, oldy) -> {
            View view =  scrollView.getChildAt(scrollView.getChildCount() - 1);
            int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

            // if diff is zero, then the bottom has been reached
            if (diff== 0) {
                if(isLoading == true){
                    return;
                }
                isLoading = true;

                if(areNewsAvailable){
                    insertNews(newsOffset,newsToShow);

                }
            }
        });


        return rootView;
    }
    boolean areNewsAvailable = true;
    private void insertNews(int start, int count){
        final LinearLayout fragment1 = (LinearLayout) rootView.findViewById(R.id.fragment1);
        final View progressBar = inflater.inflate(R.layout.progress_bar, null);
        fragment1.addView(progressBar);

        //(int personId, int requestedId, int photoNumber, int count, String quality,

        DBHandler.getInstance().getNews(count, start,(object)->{
            ArrayList<NewsInfo> newsFromServer = (ArrayList<NewsInfo>) object;
            if(start == 0 && (newsFromServer == null || newsFromServer.size() == 0))
            {
                getFragmentManager().beginTransaction().replace(R.id.fullScreenContent, new NoDataFragment().setPurpose(NoDataFragment.PURPOSE.NEWS)).commitAllowingStateLoss();
                PersonPage.finishProgressDialog();
                fragment1.removeView(progressBar);
            } else if( object != null) {
                news.addAll(newsFromServer);
                newsOffset = news.size();
                fragment1.removeView(progressBar);
                isLoading = false;
                if (getActivity() == null || getFragmentManager() == null) {
                    return;
                }
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                for (int i = start; i < count; i++) {
                    if (i <= news.size() - 1 ){
                        NewsInfoFragment newsInfoFragment = new NewsInfoFragment();
                        newsInfoFragment.setNewsInfo(news.get(i));

                        fragmentTransaction.add(R.id.fragment1, newsInfoFragment);
                    }
                }
                fragmentTransaction.commitAllowingStateLoss();
            } else {
                areNewsAvailable = false;
            }
        });

    }
    private void configureToolbar() {
        Toolbar toolbar = PersonPage.getToolbar();

        LinearLayout toolbarContainer = (LinearLayout) toolbar.findViewById(R.id.toolbarContainer);
        toolbarContainer.removeAllViews();

        toolbarContainer.addView(View.inflate(getActivity(), R.layout.ab_news, null)); // TODO insertt enother


        ImageView menuButton = (ImageView) toolbarContainer.findViewById(R.id.menuButton);
        menuButton.setOnClickListener(PersonPage.getMenuListener());

    }
}