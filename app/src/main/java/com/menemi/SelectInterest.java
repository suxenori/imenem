package com.menemi;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
public class SelectInterest  extends ActionBarActivity
{
    Button selectWomanInterest;
    Button selectManInterest;
    Button selectWomanAndManInterest;
    Intent goGoalsBack;
    Intent goRegisterPage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(com.menemi.R.layout.select_interest);
        selectWomanInterest  = (Button)findViewById(com.menemi.R.id.womanInterestButton);
        selectManInterest = (Button)findViewById(com.menemi.R.id.manInterestButton);
        selectWomanAndManInterest = (Button)findViewById(com.menemi.R.id.manWomanInterestButton);
        selectWomanInterest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                goToRegisterPage();
            }
        });
        selectManInterest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                goToRegisterPage();
            }
        });
        selectWomanAndManInterest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                goToRegisterPage();
            }
        });

    }

    public void goToGoalsBack()
    {
        goGoalsBack = new Intent(this,Goals.class);
        startActivity(goGoalsBack);
    }
    public void goToRegisterPage()
    {
        goRegisterPage = new Intent(this,Register.class);
        startActivity(goRegisterPage);
    }


}
