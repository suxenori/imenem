package com.menemi;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.menemi.dbfactory.DBHandler;
import com.menemi.interests_classes.InterestsGroup;
import com.menemi.personobject.Interests;
import com.menemi.personobject.PersonObject;

import java.util.ArrayList;


public class LoginActivity extends ActionBarActivity {


    EditText setLoginPhoneOrEmail;
    EditText setLoginPassword;
    TextView wrongLogPassNotification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.menemi.R.layout.login_page);


         setLoginPhoneOrEmail = (EditText) findViewById(com.menemi.R.id.loginEmailPhoneNumberEditText);
         setLoginPassword = (EditText) findViewById(com.menemi.R.id.loginPasswordEditText);
         wrongLogPassNotification = (TextView) findViewById(R.id.wrongLogPassNotification);

        setLoginPhoneOrEmail.addTextChangedListener(new TypingListener());
        setLoginPassword.addTextChangedListener(new TypingListener());

        ImageButton arrowBack = (ImageButton) findViewById(com.menemi.R.id.arrowBackButtonInLoginPage);
        Button loginButton = (Button) findViewById(com.menemi.R.id.loginButton);

        arrowBack.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, FirstActivity.class);
            startActivity(i);
        });

        loginButton.setOnClickListener(v -> DBHandler.getInstance().isRESTAvailable((online)->{
            if((boolean)online == true){


        PersonObject personObject = new PersonObject(setLoginPhoneOrEmail.getText().toString().toLowerCase(), setLoginPassword.getText().toString());
        Log.i("PersonObject created", personObject + "");
        Log.i("PersonObject created", personObject.getPassword() + "");
        Log.i("Login data", setLoginPhoneOrEmail.getText().toString());
        Log.i("Login data", setLoginPassword.getText().toString());

if(!setLoginPhoneOrEmail.getText().toString().equals("")) {
    DBHandler.getInstance().authorise(personObject, object -> {
        PersonObject personObject1 = (PersonObject) object;
        Log.i("PersonObject created", personObject1 + "");
        if (personObject1 != null) {


            DBHandler.getInstance().saveLastId(DBHandler.getInstance().getIdOnLoginData(personObject1));

            Intent personPage = new Intent(LoginActivity.this, PersonPage.class);


            DBHandler.getInstance().getInterestProfile(DBHandler.getInstance().getUserId(),
                    DBHandler.getInstance().getUserId(), object12 -> {
                        ArrayList<Interests> profileInterests;
                        profileInterests = (ArrayList<Interests>) object12;
                        DBHandler.getInstance().setProfileInterests(profileInterests);
                    });

            DBHandler.getInstance().getInterestsGroup(DBHandler.getInstance().getUserId(), object1 -> {
                InterestsGroup interestGroup;
                ArrayList<InterestsGroup> interestsGroupArray;
                ArrayList groups;
                interestsGroupArray = new ArrayList<InterestsGroup>();
                groups = (ArrayList) object1;
                for (int i = 0; i < groups.size(); i++) {
                    interestGroup = (InterestsGroup) groups.get(i);
                    interestsGroupArray.add(interestGroup);
                    Log.i("groups", interestGroup.getNameGroup());
                }
                DBHandler.getInstance().setInterestsGroupArray(interestsGroupArray);
            });


            startActivity(personPage);

            finish();
        } else {
            setLoginPhoneOrEmail.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red_underline), PorterDuff.Mode.SRC_ATOP);
            setLoginPassword.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red_underline), PorterDuff.Mode.SRC_ATOP);
            wrongLogPassNotification.setText(R.string.wrong_password_or_email);
            wrongLogPassNotification.setVisibility(View.VISIBLE);

            //TODO SHOW ERROR
        }

    });
} else{
    setLoginPhoneOrEmail.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red_underline), PorterDuff.Mode.SRC_ATOP);
    setLoginPassword.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red_underline), PorterDuff.Mode.SRC_ATOP);
    wrongLogPassNotification.setText(R.string.wrong_password_or_email);
    wrongLogPassNotification.setVisibility(View.VISIBLE);
}
            } else{
                setLoginPhoneOrEmail.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red_underline), PorterDuff.Mode.SRC_ATOP);
                setLoginPassword.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red_underline), PorterDuff.Mode.SRC_ATOP);
                 wrongLogPassNotification.setText(R.string.no_internet_message);
                wrongLogPassNotification.setVisibility(View.VISIBLE);
            }
        })
        );
    }
class TypingListener implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        wrongLogPassNotification.setVisibility(View.INVISIBLE);
        setLoginPhoneOrEmail.getBackground().mutate().setColorFilter(getResources().getColor(R.color.orange_text), PorterDuff.Mode.SRC_ATOP);
        setLoginPassword.getBackground().mutate().setColorFilter(getResources().getColor(R.color.orange_text), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}


}

