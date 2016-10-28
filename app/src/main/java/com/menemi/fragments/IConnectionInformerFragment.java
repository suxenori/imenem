package com.menemi.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.menemi.R;
import com.menemi.dbfactory.DBHandler;


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
        new Thread(new ConnectionListener()).start();

        return rootView;
    }
    class ConnectionListener implements Runnable{

        @Override
        public void run() {
            DBHandler.getInstance().isRESTAvailable(new DBHandler.ResultListener() {
                @Override
                public void onFinish(Object object) {
                    if((boolean)object == true){
                        try {
                            if(getFragmentManager()!=null) {
                                getFragmentManager().beginTransaction().remove(IConnectionInformerFragment.this).commitAllowingStateLoss();
                            }
                        } catch (IllegalStateException ilse){

                        }
                    } else {
                        try {
                            Thread.sleep(1000l);
                            new Thread(new ConnectionListener()).start();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}
