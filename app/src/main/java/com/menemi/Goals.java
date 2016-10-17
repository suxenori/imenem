package com.menemi;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.menemi.personobject.PersonObject;

public class Goals extends Fragment
{
    private Fragment fragment;
    private FirstActivity firstActivity = new FirstActivity();
    private Button findFriendButton = null;
    private Button communicateButton = null;
    private Button meetingButton = null;
    private ImageButton arrowButton = null;
    private static PersonObject.IamHereTo iamHereTo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {


        View rootView = inflater.inflate(com.menemi.R.layout.goals_page,container,false);
        findFriendButton = (Button)rootView.findViewById(com.menemi.R.id.findNewFriendButton);
        findFriendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setIamHereTo(PersonObject.IamHereTo.MAKE_NEW_FRIEND);
                firstActivity.replaceFragment(getFragmentManager().beginTransaction(),fragment = new Register());
            }
        });
        communicateButton = (Button)rootView.findViewById(com.menemi.R.id.communicateButton);
        communicateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setIamHereTo(PersonObject.IamHereTo.CHAT);
                firstActivity.replaceFragment(getFragmentManager().beginTransaction(),fragment = new Register());
            }
        });
        meetingButton = (Button)rootView.findViewById(com.menemi.R.id.goToDateButton);
        meetingButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setIamHereTo(PersonObject.IamHereTo.DATE);
                firstActivity.replaceFragment(getFragmentManager().beginTransaction(),fragment = new Register());
            }
        });
        arrowButton = (ImageButton)rootView.findViewById(com.menemi.R.id.arrowBackButtonGoals);
        arrowButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {   getFragmentManager().popBackStack();
            }
        });

        return rootView;
    }

    public static PersonObject.IamHereTo getiamHereTo()
    {
        return iamHereTo;
    }
    public void setIamHereTo(PersonObject.IamHereTo iamHereTo)
    {
        this.iamHereTo = iamHereTo;
    }
}
