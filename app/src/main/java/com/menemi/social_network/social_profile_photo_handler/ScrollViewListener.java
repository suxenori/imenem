package com.menemi.social_network.social_profile_photo_handler;

import android.content.Context;
import android.widget.AbsListView;

import com.squareup.picasso.Picasso;

/**
 * Created by tester03 on 28.09.2016.
 */

public class ScrollViewListener implements AbsListView.OnScrollListener {
    private final Context context;

    public ScrollViewListener(Context context) {
        this.context = context;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        final Picasso picasso = Picasso.with(context);
        if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_TOUCH_SCROLL) {
            picasso.resumeTag(context);
        } else {
            picasso.pauseTag(context);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        // Do nothing.
    }
}
