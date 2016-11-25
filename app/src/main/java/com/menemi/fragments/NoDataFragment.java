package com.menemi.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.menemi.R;

/**
 * Created by Ui-Developer on 24.11.2016.
 */

public class NoDataFragment extends Fragment {
    private View rootView;
    private PURPOSE purpose;
    private static final int[] drawableResources = {R.drawable.nofav_ic, R.drawable.noguests_ic, R.drawable.nointeres_ic, R.drawable.nomessage_ic, R.drawable.nolike_ic, R.drawable.nonerby_ic, R.drawable.nonres_ic};

    public NoDataFragment setPurpose(PURPOSE purpose) {
        this.purpose = purpose;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.no_data_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
                parent.removeAllViews();
            }
        }

        TextView message = (TextView) rootView.findViewById(R.id.message);
        message.setText(getResources().getStringArray(R.array.error_messages)[purpose.ordinal()]);
        ImageView icon = (ImageView) rootView.findViewById(R.id.icon);
        icon.setImageResource(drawableResources[purpose.ordinal()]);
        Button backButton = (Button) rootView.findViewById(R.id.backButton);
        backButton.setOnClickListener((v) -> {
            getFragmentManager().popBackStack();
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
                parent.removeAllViews();
            }
        });

        return rootView;


    }

    public enum PURPOSE {
        FAVORITE,
        VISITORS,
        INTERESTS,
        MESSAGES,
        LIKES,
        NEARBY,
        NEWS
    }
}
