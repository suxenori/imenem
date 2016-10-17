package com.menemi.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.menemi.PersonPage;
import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.DialogInfo;

import java.util.ArrayList;

/**
 * Created by Ui-Developer on 11.08.2016.
 */
public class DialogsList extends Fragment {
    private View rootView = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.scroll_list_container, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
                parent.removeAllViews();

            }
        }
        configureToolbar();
        LinearLayout fragment1 = (LinearLayout) rootView.findViewById(R.id.fragment1);
        fragment1.removeAllViews();

        Log.d("DialogsList", "onCreate");
      DBHandler.getInstance().getDialogsList(new DBHandler.ResultListener() {
            @Override
            public void onFinish(Object object) {
                ArrayList<DialogInfo> dialogsList = (ArrayList<DialogInfo>) object;
                Log.d("DialogsList", "dialogsList." + dialogsList.size());
                if (dialogsList != null && dialogsList.size() > 0) {

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                    for (int i = 0; i < dialogsList.size(); i++) {

                        DialogListItem dialogListItem = new DialogListItem();
                        dialogListItem.setDialogInfo(dialogsList.get(i));
                        fragmentTransaction.add(R.id.fragment1, dialogListItem);
                    }
                    fragmentTransaction.commitAllowingStateLoss();
                }

            }
        });
        return rootView;
    }

    private void configureToolbar() {
        Toolbar toolbar = PersonPage.getToolbar();

        LinearLayout toolbarContainer = (LinearLayout) toolbar.findViewById(R.id.toolbarContainer);
        toolbarContainer.removeAllViews();


        toolbarContainer.addView(View.inflate(getActivity(), R.layout.ab_chat, null));

        TextView screenTitle = (TextView) toolbarContainer.findViewById(R.id.screenTitle);
        screenTitle.setText(getString(R.string.messages));


        ImageView menuButton = (ImageView) toolbarContainer.findViewById(R.id.menuButton);
        menuButton.setOnClickListener(PersonPage.getMenuListener());


    }
}
