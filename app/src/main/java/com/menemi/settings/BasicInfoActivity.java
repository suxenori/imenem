package com.menemi.settings;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.menemi.DatePicker;
import com.menemi.PersonPage;
import com.menemi.R;
import com.menemi.customviews.OCDialog;
import com.menemi.dbfactory.DBHandler;
import com.menemi.utils.Utils;

import java.sql.Date;

/**
 * Created by Ui-Developer on 11.10.2016.
 */

public class BasicInfoActivity extends AppCompatActivity {
    boolean isMaleSelected;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_top);
        TextView title = (TextView)toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        title.setText(getString(R.string.basic_info));
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }



        TextView name = (TextView) findViewById(R.id.name);
        name.setText(DBHandler.getInstance().getMyProfile().getPersonName());

        LinearLayout basicInfoButton = (LinearLayout)findViewById(R.id.basicInfoButton);
        basicInfoButton.setOnClickListener(new NameChangeListener(name));

        TextView birthday = (TextView) findViewById(R.id.birthday);
        birthday.setText(Utils.getStringFromDate(DBHandler.getInstance().getMyProfile().getBirthday()));

        LinearLayout birthdayButton = (LinearLayout) findViewById(R.id.birthdayButton);
        birthdayButton.setOnClickListener(new AgeChangeListener(DBHandler.getInstance().getMyProfile().getBirthday()));

        isMaleSelected = DBHandler.getInstance().getMyProfile().isMale();
        LinearLayout maleButton = (LinearLayout) findViewById(R.id.maleButton);
        maleButton.setOnClickListener(new CheckedChangeListener());

        LinearLayout femaleButton = (LinearLayout) findViewById(R.id.femaleButton);
        femaleButton.setOnClickListener(new CheckedChangeListener());


        if (isMaleSelected)
        {
            setChecked((ImageView)findViewById(R.id.maleRadioButton), (ImageView)findViewById(R.id.femaleRadioButton));
        } else{
            setChecked((ImageView)findViewById(R.id.femaleRadioButton), (ImageView)findViewById(R.id.maleRadioButton));
        }

    }


    private void setChecked(ImageView switchedOn, ImageView switchedOff){
        switchedOn.setImageResource(R.drawable.all_on);
        switchedOff.setImageResource(R.drawable.all_of);
    }
    class CheckedChangeListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.maleButton && !isMaleSelected){

                    new OCDialog(BasicInfoActivity.this, getString(R.string.change_gender), getString(R.string.gender_notification), OCDialog.NO_ICON,() -> {
                        ProgressDialog progressDialog =  Utils.startLodingProgress(BasicInfoActivity.this, getString(R.string.change_age),new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {}});
                            DBHandler.getInstance().setMyGender(true,  (isSucceed)->{
                                if(!(boolean)isSucceed){
                                } else{
                                    isMaleSelected = true;
                                    setChecked((ImageView)findViewById(R.id.maleRadioButton), (ImageView)findViewById(R.id.femaleRadioButton));
                                }
                                progressDialog.dismiss();
                            });


                        //TODO change gender on server
                    }, () -> {});

            } else if(v.getId() == R.id.femaleButton && isMaleSelected){

              new OCDialog(BasicInfoActivity.this, getString(R.string.change_gender), getString(R.string.gender_notification), OCDialog.NO_ICON,() -> {
                  ProgressDialog progressDialog =  Utils.startLodingProgress(BasicInfoActivity.this, getString(R.string.change_age),new DialogInterface.OnDismissListener() {
                      @Override
                      public void onDismiss(DialogInterface dialog) {}});
                  DBHandler.getInstance().setMyGender(true,  (isSucceed)->{
                      if(!(boolean)isSucceed){
                      } else{
                          isMaleSelected = false;
                          setChecked((ImageView)findViewById(R.id.femaleRadioButton), (ImageView)findViewById(R.id.maleRadioButton));
                      }
                      progressDialog.dismiss();
                  });

                }, () -> {});

            }
        }
    }

    class AgeChangeListener implements View.OnClickListener{
        Date date;

        public AgeChangeListener(Date date) {
            this.date = date;
        }

        @Override
        public void onClick(View v) {
            new OCDialog(BasicInfoActivity.this, getString(R.string.change_age), getString(R.string.age_notification), OCDialog.NO_ICON,() -> {

                DatePicker dialogFragment = new DatePicker();
                dialogFragment.setDate(date);
                dialogFragment.show(getFragmentManager(),"tag");
                dialogFragment.setTitle(getString(R.string.change_age));
                dialogFragment.setOkListener((Date date) ->{
                    ProgressDialog progressDialog =  Utils.startLodingProgress(BasicInfoActivity.this, getString(R.string.change_age),new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {

                        }
                    });
                    this.date = date;
                 DBHandler.getInstance().setBirthday(date, (isSucceed)->{
                     if(!(boolean)isSucceed){
                     } else{
                         TextView birthday = (TextView) findViewById(R.id.birthday);
                         birthday.setText(Utils.getStringFromDate(DBHandler.getInstance().getMyProfile().getBirthday()));
                     }
                     progressDialog.dismiss();
                 });




                });

                //TODO change birthday on server
            }, () -> {});

        }
    }
    class NameChangeListener implements View.OnClickListener{
        private TextView name;

        public NameChangeListener(TextView name) {
            this.name = name;
        }

        @Override
        public void onClick(View v) {
            new OCDialog(BasicInfoActivity.this, getString(R.string.change_name), name.getText().toString(), OCDialog.NO_ICON,(String data) -> {

                ProgressDialog progressDialog =  Utils.startLodingProgress(BasicInfoActivity.this,  getString(R.string.change_name), new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                });
                DBHandler.getInstance().setName(data, isSucceed -> {
                    if(!(boolean)isSucceed){

                    } else{
                        DBHandler.getInstance().getMyProfile().setPersonName(data);
                        name.setText(data);
                        PersonPage.prepareNavigationalHeader();
                    }
                    progressDialog.dismiss();
                });
              //TODO change name on server
            }, () -> {});

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

}
