package com.menemi;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.PersonObject;
import com.menemi.utils.Utils;

import java.sql.Date;

public class Register extends Fragment
{

    private EditText editName;
    private EditText editBDay;
    private EditText editEmail;
    private Button registerButton;
    private ImageButton arrowBack;
    private EditText editPass;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        rootView = inflater.inflate(com.menemi.R.layout.register_page,container,false);
        registerButton = (Button)rootView.findViewById(com.menemi.R.id.button_register);
        editName = (EditText) rootView.findViewById(com.menemi.R.id.editNameField);
        editBDay = (EditText)rootView.findViewById(com.menemi.R.id.editBDayField);
        editEmail = (EditText) rootView.findViewById(com.menemi.R.id.editEmailField);
        editPass = (EditText)rootView.findViewById(com.menemi.R.id.editPassField);

        arrowBack = (ImageButton)rootView.findViewById(com.menemi.R.id.arrowBackButtonReg);
        editEmail.addTextChangedListener(new EmailChangeListener());
        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (Utils.isEmailValid(editEmail.getText().toString())) {
                    PersonObject personObject = new PersonObject(SelectSexPage.isMale(), Goals.getiamHereTo()
                            , PersonObject.InterestGender.ANY_GENDER,
                            editEmail.getText().toString(), editName.getText().toString(), Utils.getDateFromString(editBDay.getText().toString()), editPass.getText().toString());
                    DBHandler.getInstance().register(personObject, new DBHandler.ResultListener()

                    {
                        @Override
                        public void onFinish(Object object) {
                            if (object != null) {
                                Intent personPage = new Intent(getActivity(), PersonPage.class);
                                startActivity(personPage);
                                Log.i("register", "register is successful");
                            } else {
                                TextView wrongLogPassNotification = (TextView)rootView.findViewById(R.id.wrongLogPassNotification);
                                wrongLogPassNotification.setText(R.string.wrong_password_or_email);
                                wrongLogPassNotification.setVisibility(View.VISIBLE);
                                //editEmail.setHintTextColor(getResources().getColor(R.color.red_text));
                            }

                        }
                    });
                    Log.i("Button reg is pressed", "Button register is pressed");
                    Log.i("Name ", editName.getText().toString());
                    Log.i("Man? ", SelectSexPage.isMale() + "");
                    Log.i("Here to ", Goals.getiamHereTo() + "");
                    Log.i("Interest gender", PersonObject.InterestGender.ANY_GENDER + "");
                    Log.i("Email ", editEmail.getText().toString());
                    Log.i("Pass", editPass.getText().toString());
                    Log.i("Date", String.valueOf(Utils.getDateFromString1(editBDay.getText().toString())));
                    Log.i("Button reg is pressed", "Button register is pressed");
                } else{

                    TextView wrongLogPassNotification = (TextView)rootView.findViewById(R.id.wrongLogPassNotification);
                    wrongLogPassNotification.setText(R.string.wrong_email);
                    wrongLogPassNotification.setVisibility(View.VISIBLE);
                }
            }
        });

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
                DatePicker dialogFragment = new DatePicker();
                dialogFragment.setOkListener((Date date) ->{
                    editBDay.setText(Utils.getStringFromDate(date));
                });
                dialogFragment.show(getFragmentManager(),"tag");
            }
        });

        return rootView;
    }

class EmailChangeListener implements TextWatcher{

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        TextView wrongLogPassNotification = (TextView)rootView.findViewById(R.id.wrongLogPassNotification);
        wrongLogPassNotification.setVisibility(View.INVISIBLE);

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
}
