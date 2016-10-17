package com.menemi;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.menemi.utils.Utils;

public class SelectSexPage extends Fragment
{
    private Fragment fragment = new Goals();
    private FirstActivity firstActivity = new FirstActivity();
    private static boolean isMale;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(com.menemi.R.layout.select_sex_page,container,false);
        final Button maleButton = (Button)rootView.findViewById(com.menemi.R.id.maleButton);
        maleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                isMale(maleButton.getId());
                firstActivity.replaceFragment(getFragmentManager().beginTransaction(),fragment);

            }
        });
        final Button femaleButton = (Button)rootView.findViewById(com.menemi.R.id.femaleButton);
        femaleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                isMale(femaleButton.getId());
                firstActivity.replaceFragment(getFragmentManager().beginTransaction(),fragment);
            }
        });

        final ImageButton arrowButton = (ImageButton)rootView.findViewById(com.menemi.R.id.arrowBackButton);
        arrowButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getFragmentManager().popBackStack();
                //firstActivity.removeFragment(getFragmentManager().beginTransaction(),SelectSexPage.this);

            }
        });
        return rootView;
    }



    public  boolean isMale(int id){
        if (id == com.menemi.R.id.maleButton){
            Log.d("male", Utils.boolToInt(true) +"");
            return isMale = true;
        } else {
            Log.d("male",Utils.boolToInt(false) +"");
            return isMale = false;
        }
    }
    public static boolean isMale()
    {
        return isMale;
    }
    public void setMale(boolean male)
    {
        isMale = male;
    }
}
