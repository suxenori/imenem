package com.menemi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
public class ConnectTwitterPage extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(com.menemi.R.layout.connect_twitter_page);
    }
    public void onBackPressed(){
       openPersonPage();
    }
    public void openPersonPage(){
        Intent intent = new Intent(ConnectTwitterPage.this,PersonPage.class);
        startActivity(intent);
    }
}
