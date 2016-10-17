package com.menemi.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.menemi.R;

/**
 * Created by Ui-Developer on 01.07.2016.
 */
public class CryFragment extends Fragment {
    private View rootView = null;
    private int id = -1;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.cry_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }

        }

        TextView cryButton = (TextView)rootView.findViewById(R.id.cryButton);
        cryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("CryFragment", "" + id);
            }
        });

        return rootView;
    }

    public void setId(int id) {
        this.id = id;
    }
}
