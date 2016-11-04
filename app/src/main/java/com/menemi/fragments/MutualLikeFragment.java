package com.menemi.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.menemi.R;

/**
 * Created by Ui-Developer on 04.11.2016.
 */

public class MutualLikeFragment extends Fragment {

    private View rootView = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.mutual_like_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        Button startChat = (Button) rootView.findViewById(R.id.startChat);
        Button cancel = (Button) rootView.findViewById(R.id.cancel);
        ImageView myAvatar = (ImageView) rootView.findViewById(R.id.myAvatar);
        ImageView likedAvatar = (ImageView) rootView.findViewById(R.id.likedAvatar);



        return rootView;
    }
}
