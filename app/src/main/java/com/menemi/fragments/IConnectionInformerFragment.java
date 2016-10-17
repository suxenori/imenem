package com.menemi.fragments;

import android.app.Fragment;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.menemi.R;


public class IConnectionInformerFragment extends Fragment
{
    private WindowManager windowManager = null;

    private View rootView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)

    {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.iconnector_checker_layout, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
                parent.removeAllViews();

            }
        }

        return rootView;
    }
}
