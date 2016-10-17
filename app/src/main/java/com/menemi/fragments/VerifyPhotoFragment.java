package com.menemi.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.menemi.R;

/**
 * Created by tester03 on 20.07.2016.
 */
public class VerifyPhotoFragment extends Fragment
{
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.verify_photo_fragment,container,false);
        return view;
    }
}
