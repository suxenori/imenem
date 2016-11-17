package com.menemi.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.dbfactory.Fields;
import com.menemi.personobject.NotificationSettings;

/**
 * Created by tester03 on 20.06.2016.
 */
public class NotificationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.notifications));


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        NotificationSettings notifications = DBHandler.getInstance().getNotifications();
        prepareData(notifications);

    }

    private void prepareData( NotificationSettings notifications){
        ImageView pushMessages = (ImageView) findViewById(R.id.pushMessages);
        if (notifications.getMessages(NotificationSettings.SETTINGTYPE.PUSH)) {
            pushMessages.setImageResource(R.drawable.push_not);
        } else {
            pushMessages.setImageResource(R.drawable.push_not_off);
        }

        pushMessages.setOnClickListener((v)->{
            notifications.revertMessages(NotificationSettings.SETTINGTYPE.PUSH);
            prepareData(notifications);
            DBHandler.getInstance().setNotifications(Fields.MESSAGES, notifications.getMessages(), (isSucced)->{
                if(!(boolean)isSucced){
                    notifications.revertMessages(NotificationSettings.SETTINGTYPE.PUSH);
                    prepareData(notifications);
                }
            });
        });

        ImageView emailMessages = (ImageView) findViewById(R.id.emailMessages);
        if (notifications.getMessages(NotificationSettings.SETTINGTYPE.EMAIL)) {
            emailMessages.setImageResource(R.drawable.email_not);
        } else {
            emailMessages.setImageResource(R.drawable.email_not_off);
        }
        emailMessages.setOnClickListener((v)->{
            notifications.revertMessages(NotificationSettings.SETTINGTYPE.EMAIL);
            prepareData(notifications);
            DBHandler.getInstance().setNotifications(Fields.MESSAGES, notifications.getMessages(), (isSucced)->{
                if(!(boolean)isSucced){
                    notifications.revertMessages(NotificationSettings.SETTINGTYPE.EMAIL);
                    prepareData(notifications);
                }
            });
        });





        ImageView pushVisitors = (ImageView) findViewById(R.id.pushVisitors);
        if (notifications.getVisitors(NotificationSettings.SETTINGTYPE.PUSH)) {
            pushVisitors.setImageResource(R.drawable.push_not);
        } else {
            pushVisitors.setImageResource(R.drawable.push_not_off);
        }
        pushVisitors.setOnClickListener((v)->{
            notifications.revertVisitors(NotificationSettings.SETTINGTYPE.PUSH);
            prepareData(notifications);
            DBHandler.getInstance().setNotifications(Fields.VISITORS, notifications.getVisitors(), (isSucced)->{
                if(!(boolean)isSucced){
                    notifications.revertVisitors(NotificationSettings.SETTINGTYPE.PUSH);
                    prepareData(notifications);
                }
            });
        });






        ImageView emailVisitors = (ImageView) findViewById(R.id.emailVisitors);
        if (notifications.getVisitors(NotificationSettings.SETTINGTYPE.EMAIL)) {
            emailVisitors.setImageResource(R.drawable.email_not);
        } else {
            emailVisitors.setImageResource(R.drawable.email_not_off);
        }
        emailVisitors.setOnClickListener((v)->{
            notifications.revertVisitors(NotificationSettings.SETTINGTYPE.EMAIL);
            prepareData(notifications);
            DBHandler.getInstance().setNotifications(Fields.VISITORS, notifications.getVisitors(),  (isSucced)->{
                if(!(boolean)isSucced){
                    notifications.revertVisitors(NotificationSettings.SETTINGTYPE.EMAIL);
                    prepareData(notifications);
                }
            });
        });



        ImageView pushbumped_into_n = (ImageView) findViewById(R.id.pushbumped_into_n);
        if (notifications.getNearby(NotificationSettings.SETTINGTYPE.PUSH)) {
            pushbumped_into_n.setImageResource(R.drawable.push_not);
        } else {
            pushbumped_into_n.setImageResource(R.drawable.push_not_off);
        }
        pushbumped_into_n.setOnClickListener((v)->{
            notifications.revertNearby(NotificationSettings.SETTINGTYPE.PUSH);
            prepareData(notifications);
            DBHandler.getInstance().setNotifications(Fields.NEARBY, notifications.getNearby(),  (isSucced)->{
                if(!(boolean)isSucced){
                    notifications.revertNearby(NotificationSettings.SETTINGTYPE.PUSH);
                    prepareData(notifications);
                }
            });
        });





        ImageView emailbumped_into_n = (ImageView) findViewById(R.id.emailbumped_into_n);
        if (notifications.getNearby(NotificationSettings.SETTINGTYPE.EMAIL)) {
            emailbumped_into_n.setImageResource(R.drawable.email_not);
        } else {
            emailbumped_into_n.setImageResource(R.drawable.email_not_off);
        }
        emailbumped_into_n.setOnClickListener((v)->{
            notifications.revertNearby(NotificationSettings.SETTINGTYPE.EMAIL);
            prepareData(notifications);
            DBHandler.getInstance().setNotifications(Fields.NEARBY, notifications.getNearby(),  (isSucced)->{
                if(!(boolean)isSucced){
                    notifications.revertNearby(NotificationSettings.SETTINGTYPE.EMAIL);
                    prepareData(notifications);
                }
            });

        });




        ImageView pushfavorited_you = (ImageView) findViewById(R.id.pushfavorited_you);
        if (notifications.getFavorites(NotificationSettings.SETTINGTYPE.PUSH)) {
            pushfavorited_you.setImageResource(R.drawable.push_not);
        } else {
            pushfavorited_you.setImageResource(R.drawable.push_not_off);
        }
        pushfavorited_you.setOnClickListener((v)->{
            notifications.revertFavorites(NotificationSettings.SETTINGTYPE.PUSH);
            prepareData(notifications);
            DBHandler.getInstance().setNotifications(Fields.FAVORITES, notifications.getFavorites(),  (isSucced)->{
                if(!(boolean)isSucced){
                    notifications.revertFavorites(NotificationSettings.SETTINGTYPE.PUSH);
                    prepareData(notifications);
                }
            });

        });



        ImageView emailfavorited_you = (ImageView) findViewById(R.id.emailfavorited_you);
        if (notifications.getFavorites(NotificationSettings.SETTINGTYPE.EMAIL)) {
            emailfavorited_you.setImageResource(R.drawable.email_not);
        } else {
            emailfavorited_you.setImageResource(R.drawable.email_not_off);
        }

        emailfavorited_you.setOnClickListener((v)->{
            notifications.revertFavorites(NotificationSettings.SETTINGTYPE.EMAIL);
            prepareData(notifications);
            DBHandler.getInstance().setNotifications(Fields.FAVORITES, notifications.getFavorites(),  (isSucced)->{
                if(!(boolean)isSucced){
                    notifications.revertFavorites(NotificationSettings.SETTINGTYPE.EMAIL);
                    prepareData(notifications);
                }
            });

        });



        ImageView pushliked_you = (ImageView) findViewById(R.id.pushliked_you);
        if (notifications.getTheir_likes(NotificationSettings.SETTINGTYPE.PUSH)) {
            pushliked_you.setImageResource(R.drawable.push_not);
        } else {
            pushliked_you.setImageResource(R.drawable.push_not_off);
        }

        pushliked_you.setOnClickListener((v)->{
            notifications.revertTheir_likes(NotificationSettings.SETTINGTYPE.PUSH);
            prepareData(notifications);
            DBHandler.getInstance().setNotifications(Fields.THEIR_LIKES, notifications.getTheir_likes(),  (isSucced)->{
                if(!(boolean)isSucced){
                    notifications.revertTheir_likes(NotificationSettings.SETTINGTYPE.PUSH);
                    prepareData(notifications);
                }
            });

        });


        ImageView emailliked_you = (ImageView) findViewById(R.id.emailliked_you);
        if (notifications.getTheir_likes(NotificationSettings.SETTINGTYPE.EMAIL)) {
            emailliked_you.setImageResource(R.drawable.email_not);
        } else {
            emailliked_you.setImageResource(R.drawable.email_not_off);
        }

        emailliked_you.setOnClickListener((v)->{
            notifications.revertTheir_likes(NotificationSettings.SETTINGTYPE.EMAIL);
            prepareData(notifications);
            DBHandler.getInstance().setNotifications(Fields.THEIR_LIKES, notifications.getTheir_likes(),  (isSucced)->{
                if(!(boolean)isSucced){
                    notifications.revertTheir_likes(NotificationSettings.SETTINGTYPE.EMAIL);
                    prepareData(notifications);
                }
            });

        });



        ImageView pushmatches = (ImageView) findViewById(R.id.pushmatches);
        if (notifications.getMutual_likes(NotificationSettings.SETTINGTYPE.PUSH)) {
            pushmatches.setImageResource(R.drawable.push_not);
        } else {
            pushmatches.setImageResource(R.drawable.push_not_off);
        }
        pushmatches.setOnClickListener((v)->{
            notifications.revertMutual_likes(NotificationSettings.SETTINGTYPE.PUSH);
            prepareData(notifications);
            DBHandler.getInstance().setNotifications(Fields.MUT_LIKES, notifications.getMutual_likes(),  (isSucced)->{
                if(!(boolean)isSucced){
                    notifications.revertMutual_likes(NotificationSettings.SETTINGTYPE.PUSH);
                    prepareData(notifications);
                }
            });

        });


        ImageView emailmatches = (ImageView) findViewById(R.id.emailmatches);
        if (notifications.getMutual_likes(NotificationSettings.SETTINGTYPE.EMAIL)) {
            emailmatches.setImageResource(R.drawable.email_not);
        } else {
            emailmatches.setImageResource(R.drawable.email_not_off);
        }
        emailmatches.setOnClickListener((v)->{
            notifications.revertMutual_likes(NotificationSettings.SETTINGTYPE.EMAIL);
            prepareData(notifications);
            DBHandler.getInstance().setNotifications(Fields.MUT_LIKES, notifications.getMutual_likes(),  (isSucced)->{
                if(!(boolean)isSucced){
                    notifications.revertMutual_likes(NotificationSettings.SETTINGTYPE.EMAIL);
                    prepareData(notifications);
                }
            });

        });



        ImageView pushgifts = (ImageView) findViewById(R.id.pushgifts);
        if (notifications.getGifts(NotificationSettings.SETTINGTYPE.PUSH)) {
            pushgifts.setImageResource(R.drawable.push_not);
        } else {
            pushgifts.setImageResource(R.drawable.push_not_off);
        }
        pushgifts.setOnClickListener((v)->{
            notifications.revertGifts(NotificationSettings.SETTINGTYPE.PUSH);
            prepareData(notifications);
            DBHandler.getInstance().setNotifications(Fields.GIFTS, notifications.getGifts(),  (isSucced)->{
                if(!(boolean)isSucced){
                    notifications.revertGifts(NotificationSettings.SETTINGTYPE.PUSH);
                    prepareData(notifications);
                }
            });

        });


        ImageView emailgifts = (ImageView) findViewById(R.id.emailgifts);
        if (notifications.getGifts(NotificationSettings.SETTINGTYPE.EMAIL)) {
            emailgifts.setImageResource(R.drawable.email_not);
        } else {
            emailgifts.setImageResource(R.drawable.email_not_off);
        }
        emailgifts.setOnClickListener((v)->{
            notifications.revertGifts(NotificationSettings.SETTINGTYPE.EMAIL);
            prepareData(notifications);
            DBHandler.getInstance().setNotifications(Fields.GIFTS, notifications.getGifts(),  (isSucced)->{
                if(!(boolean)isSucced){
                    notifications.revertGifts(NotificationSettings.SETTINGTYPE.EMAIL);
                    prepareData(notifications);
                }
            });

        });


        ImageView pushnews = (ImageView) findViewById(R.id.pushnews);
        if (notifications.getOther(NotificationSettings.SETTINGTYPE.PUSH)) {
            pushnews.setImageResource(R.drawable.push_not);
        } else {
            pushnews.setImageResource(R.drawable.push_not_off);
        }
        pushnews.setOnClickListener((v)->{
            notifications.revertOther(NotificationSettings.SETTINGTYPE.PUSH);
            prepareData(notifications);
            DBHandler.getInstance().setNotifications(Fields.OTHER, notifications.getOther(),  (isSucced)->{
                if(!(boolean)isSucced){
                    notifications.revertOther(NotificationSettings.SETTINGTYPE.PUSH);
                    prepareData(notifications);
                }
            });

        });


        ImageView emailnews = (ImageView) findViewById(R.id.emailnews);
        if (notifications.getOther(NotificationSettings.SETTINGTYPE.EMAIL)) {
            emailnews.setImageResource(R.drawable.email_not);
        } else {
            emailnews.setImageResource(R.drawable.email_not_off);
        }
        emailnews.setOnClickListener((v)->{
            notifications.revertOther(NotificationSettings.SETTINGTYPE.EMAIL);
            prepareData(notifications);
            DBHandler.getInstance().setNotifications(Fields.OTHER, notifications.getOther(),  (isSucced)->{
                if(!(boolean)isSucced){
                    notifications.revertOther(NotificationSettings.SETTINGTYPE.EMAIL);
                    prepareData(notifications);
                }
            });

        });

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
