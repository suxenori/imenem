package com.menemi.customviews;

import android.content.Context;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Ui-Developer on 18.08.2016.
 */
public class HackyViewPager extends ViewPager {
    FragmentStatePagerAdapter pagerAdapter;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (pagerAdapter != null && super.getAdapter() == null) {
            super.setAdapter(pagerAdapter);
           // mPageIndicator.setViewPager(this);
        }
    }
    public void storeAdapter(FragmentStatePagerAdapter pagerAdapter) {
        pagerAdapter = pagerAdapter;
    }
    public HackyViewPager(Context context) {
        super(context);
    }

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
}
