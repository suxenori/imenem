package com.menemi;

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

public class Register extends Fragment {

    private EditText editName;
    private EditText editBDay;
    private EditText editEmail;
    private Button registerButton;
    private ImageButton arrowBack;
    private EditText editPass;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(com.menemi.R.layout.register_page, container, false);
        registerButton = (Button) rootView.findViewById(com.menemi.R.id.button_register);
        editName = (EditText) rootView.findViewById(com.menemi.R.id.editNameField);
        editBDay = (EditText) rootView.findViewById(com.menemi.R.id.editBDayField);
        editEmail = (EditText) rootView.findViewById(com.menemi.R.id.editEmailField);
        editPass = (EditText) rootView.findViewById(com.menemi.R.id.editPassField);

        arrowBack = (ImageButton) rootView.findViewById(com.menemi.R.id.arrowBackButtonReg);
        editEmail.addTextChangedListener(new TextChangeListener(rootView.findViewById(R.id.wrongLogPassNotification)));
        editName.addTextChangedListener(new TextChangeListener(rootView.findViewById(R.id.wrongName)));
        editBDay.setKeyListener(null);//should not be edited by typing
        editPass.addTextChangedListener(new TextChangeListener(rootView.findViewById(R.id.wrongPassword)));

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAllFieldsValid()) {
                    PersonObject personObject = new PersonObject(SelectSexPage.isMale(), Goals.getiamHereTo()
                            , PersonObject.InterestGender.ANY_GENDER,
                            editEmail.getText().toString(), editName.getText().toString(), Utils.getDateFromString(editBDay.getText().toString()), editPass.getText().toString());
                    DBHandler.getInstance().register(personObject, new DBHandler.ResultListener() {
                        @Override
                        public void onFinish(Object object) {
                            if (object != null) {
                                Intent personPage = new Intent(getActivity(), PersonPage.class);
                                startActivity(personPage);
                                Log.i("register", "register is successful");
                            } else {
                                TextView wrongLogPassNotification = (TextView) rootView.findViewById(R.id.wrongLogPassNotification);
                                wrongLogPassNotification.setText(R.string.wrong_password_or_email);
                                wrongLogPassNotification.setVisibility(View.VISIBLE);
                                //editEmail.setHintTextColor(getResources().getColor(R.color.red_text));
                            }

                        }
                    });

                }
            }
        });

        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                //firstActivity.removeFragment(getFragmentManager().beginTransaction(),Register.this);
            }
        });
        editBDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker dialogFragment = new DatePicker();
                dialogFragment.setOkListener((Date date) -> {
                    editBDay.setText(Utils.getStringFromDate(date));
                    if(editBDay.getText().toString().length() > 6){ // TODO CHANGE TO REGEX
                        TextView wrongNotification = (TextView) rootView.findViewById(R.id.wrongBDay);
                        wrongNotification .setVisibility(View.INVISIBLE);

                    }
                });
                dialogFragment.show(getFragmentManager(), "tag");

            }
        });

        return rootView;
    }

    private boolean checkAllFieldsValid() {
        boolean isAllCorrect = true;
        if (!Utils.isEmailValid(editEmail.getText().toString())) {
            TextView wrongLogPassNotification = (TextView) rootView.findViewById(R.id.wrongLogPassNotification);
            wrongLogPassNotification.setVisibility(View.VISIBLE);
            isAllCorrect = false;
        }
        if(Utils.isNameValid(editName.getText().toString())){ // TODO CHANGE TO REGEX
            TextView wrongNotification = (TextView) rootView.findViewById(R.id.wrongName);
            wrongNotification .setVisibility(View.VISIBLE);
            isAllCorrect = false;
        }
        if(editPass.getText().toString().length() < 6){ // TODO CHANGE TO REGEX
            TextView wrongNotification = (TextView) rootView.findViewById(R.id.wrongPassword);
            wrongNotification .setVisibility(View.VISIBLE);
            isAllCorrect = false;
        }
        if(editBDay.getText().toString().length() < 6){ // TODO CHANGE TO REGEX
            TextView wrongNotification = (TextView) rootView.findViewById(R.id.wrongBDay);
            wrongNotification .setVisibility(View.VISIBLE);
            isAllCorrect = false;
        }

        return isAllCorrect;
    }

    class TextChangeListener implements TextWatcher {
        View notificationView;

        public TextChangeListener(View view) {
            this.notificationView = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            notificationView.setVisibility(View.INVISIBLE);

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}

