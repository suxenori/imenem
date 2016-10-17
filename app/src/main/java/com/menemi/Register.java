package com.menemi;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.PersonObject;
import com.menemi.utils.Utils;

public class Register extends Fragment
{

    private EditText editName;
    private EditText editBDay;
    private EditText editEmail;
    private Button registerButton;
    private ImageButton arrowBack;
    private EditText editPass;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(com.menemi.R.layout.register_page,container,false);
        registerButton = (Button)rootView.findViewById(com.menemi.R.id.button_register);
        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                        PersonObject personObject = new PersonObject(SelectSexPage.isMale(),Goals.getiamHereTo()
                        , PersonObject.InterestGender.ANY_GENDER,
                        editEmail.getText().toString(),editName.getText().toString(), Utils.getDateFromString1(editBDay.getText().toString()), editPass.getText().toString());
                        DBHandler.getInstance().register(personObject, new DBHandler.ResultListener()
                {
                    @Override
                    public void onFinish(Object object)
                    {
                        if(object != null){
                            Intent personPage = new Intent(getActivity(), PersonPage.class);
                            startActivity(personPage);
                            Log.i("register","register is successful");
                        }

                    }
                });
                Log.i("Button reg is pressed","Button register is pressed");
                Log.i("Name ",editName.getText().toString());
                Log.i("Man? ",SelectSexPage.isMale() + "");
                Log.i("Here to ",Goals.getiamHereTo() + "");
                Log.i("Interest gender",PersonObject.InterestGender.ANY_GENDER+ "");
                Log.i("Email ",editEmail.getText().toString());
                Log.i("Pass",editPass.getText().toString());
                Log.i("Date", String.valueOf(Utils.getDateFromString1(editBDay.getText().toString())));
                Log.i("Button reg is pressed","Button register is pressed");
            }
        });
        editName = (EditText) rootView.findViewById(com.menemi.R.id.editNameField);
        editBDay = (EditText)rootView.findViewById(com.menemi.R.id.editBDayField);
        editEmail = (EditText) rootView.findViewById(com.menemi.R.id.editEmailField);
        editPass = (EditText)rootView.findViewById(com.menemi.R.id.editPassField);

        arrowBack = (ImageButton)rootView.findViewById(com.menemi.R.id.arrowBackButtonReg);
        arrowBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getFragmentManager().popBackStack();
                //firstActivity.removeFragment(getFragmentManager().beginTransaction(),Register.this);
            }
        });
        editBDay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DialogFragment dialogFragment = new DatePicker();
                dialogFragment.show(getFragmentManager(),"tag");
            }
        });

        return rootView;
    }


}
