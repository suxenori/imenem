package com.menemi.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.menemi.R;

public class LostInternetFragment extends Fragment {
    private View rootView = null;
    private OnRetryListener onRetryListener = ()->{Log.d("OnRetryListener", "Listener is called but was not set");};

    public void setOnRetryListener(OnRetryListener onRetryListener) {
        this.onRetryListener = onRetryListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.lost_internet_fragment, container, false);

        Button retryButton= (Button) rootView.findViewById(R.id.retryButton);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRetryListener.onRetry();
            }
        });
        return rootView;
    }
interface OnRetryListener{
        void onRetry();
    }

}
