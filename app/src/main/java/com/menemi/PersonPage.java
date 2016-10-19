package com.menemi;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.menemi.dbfactory.DBHandler;
import com.menemi.dbfactory.InternetConnectionListener;
import com.menemi.dbfactory.MyFirebaseMessagingService;
import com.menemi.dbfactory.SQLiteEngine;
import com.menemi.dbfactory.rest.Loader;
import com.menemi.fragments.BuyCoinsFragment;
import com.menemi.fragments.BuyVipFragment;
import com.menemi.fragments.DialogsList;
import com.menemi.fragments.FilterFragment;
import com.menemi.fragments.GiftInfoDialogFragment;
import com.menemi.fragments.IConnectionInformerFragment;
import com.menemi.fragments.PersonDataFragment;
import com.menemi.fragments.PhotoSettingsFragment;
import com.menemi.fragments.ShowPeopleCompositeFragment;
import com.menemi.personobject.PersonObject;
import com.menemi.personobject.PersonalGift;
import com.menemi.personobject.PhotoSetting;
import com.menemi.settings.SettingsActivity;
import com.menemi.social_network.SocialNetworkHandler;
import com.menemi.utils.Utils;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ru.ok.android.sdk.Odnoklassniki;
import ru.ok.android.sdk.OkListener;


public class PersonPage extends AppCompatActivity {

    private static final int ENCOUNTERS = 0;
    private static final int MY_PROFILE = -1;
    private static final int NEAR = 1;
    private static final int MESSAGES = 2;
    private static final int FAVORITES = 3;
    private static final int VISITORS = 4;
    private static final int MY_LIKES = 5;
    private static final int INVITE_FRIENDS = 6;
    private static final int SETTINGS = 7;
    public static boolean isFilterVisible = false;
    private ArrayList<ItemSlideMenu> listSliding = new ArrayList<>();

    private static ScrollView listViewSliding;
    private static DrawerLayout drawerLayout;
    private static Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    public static final int SELECTED_PICTURES_FROM_GIZMO = 1;
    public static final int SELECTED_PICTURES_FROM_CAMERA = 2;
    private long back_pressed;
    private PersonObject personObject = null;

    private ImageView ownerAvatar = null;
    private ImageView ownerCircleAvatar = null;
    private TextView ownerName = null;
    private TextView balanceValue = null;
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

         if(intent.getStringExtra(MyFirebaseMessagingService.ACTION) != null && intent.getStringExtra(MyFirebaseMessagingService.ACTION).equals(MyFirebaseMessagingService.ACTION_GIFT)) {
             GiftInfoDialogFragment giftInfoDialogFragment = new GiftInfoDialogFragment();
             giftInfoDialogFragment.setGift((PersonalGift) intent.getSerializableExtra(MyFirebaseMessagingService.DATA));

             giftInfoDialogFragment.show(getFragmentManager(), "Dialog Fragment");
         }

        }
    };

    public static Toolbar getToolbar() {
        return toolbar;
    }
    public static OnMenuButtonListener getMenuListener(){
        return new OnMenuButtonListener();
    }

    public static View.OnClickListener getFilterButtonListener(FragmentManager fragmentManager) {
        return new OnFilterClickListener(fragmentManager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.menemi.R.layout.activity_person_page);

        DBHandler.setUP(this);
       DBHandler.getInstance().sendFirebaseTokentToServer();
        toolbar = (Toolbar) findViewById(com.menemi.R.id.toolbar_with_checkBox);
        //FirebaseMessaging.getInstance().subscribeToTopic("news");

        // [END subscribe_topics]

        // Log and toast
        registerReceiver(myReceiver, new IntentFilter(MyFirebaseMessagingService.INTENT_FILTER));
        Log.d("TAG", "subscribed");
        Toast.makeText(PersonPage.this, "subscribed", Toast.LENGTH_SHORT).show();
        DBHandler.getInstance().lunchStreams();
        View header = getLayoutInflater().inflate(com.menemi.R.layout.navigation_header_layout, listViewSliding, false);
        View footer = getLayoutInflater().inflate(com.menemi.R.layout.vip_users, listViewSliding, false);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(com.menemi.R.id.drawer);
        listViewSliding = (ScrollView) findViewById(com.menemi.R.id.sliding_menu);

        LinearLayout balance = (LinearLayout) header.findViewById(com.menemi.R.id.balance);
        balance.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                BuyCoinsFragment buyCoinsFragment = new BuyCoinsFragment();
                openFragment(buyCoinsFragment);

            }
        });

        final ImageView vipStatus = (ImageView) header.findViewById(com.menemi.R.id.vipStatus);

        ownerAvatar = (ImageView) header.findViewById(com.menemi.R.id.ownerAvatar);
        ownerCircleAvatar = (ImageView) header.findViewById(com.menemi.R.id.circleAvatar);
        ownerName = (TextView) header.findViewById(com.menemi.R.id.ownerName);
        balanceValue = (TextView) header.findViewById(com.menemi.R.id.balanceValue);

        final int userId = DBHandler.getInstance().getUserId();
                personObject = DBHandler.getInstance().getMyProfile();
                if( personObject.isPersonVIP()) {
                    vipStatus.setImageResource(com.menemi.R.drawable.vip_on);
                    vipStatus.setOnClickListener(new OnVipClickListener(true));
                } else {
                    vipStatus.setImageResource(com.menemi.R.drawable.vip_off);
                    vipStatus.setOnClickListener(new OnVipClickListener(false));
                }
                OnFilterClickListener.setOwner(personObject);
                replaceFragment(ENCOUNTERS);


        DBHandler.getInstance().subscribeToRest(new InternetConnectionListener() {
            @Override
            public void internetON() {
                DBHandler.getInstance().getAvatar(userId, Utils.PICTURE_QUALITY_THUMBNAIL, object -> {
                    final Bitmap bitmap = (Bitmap) object;
                    if (bitmap != null) {
                        final Bitmap avatar = prepareAvatar(bitmap);
                        DBHandler.getInstance().getMyProfile(object1 -> ((PersonObject) object1).setPersonAvatar(avatar));
                    Log.d("Size", ownerAvatar.getWidth() + "");
                    Log.d("Size", ownerAvatar.getHeight() + "");
                    }});
            }

            @Override
            public void internetOFF() {

            }
        });

        prepareAvatar(Utils.getBitmapFromResource(this, com.menemi.R.drawable.empty_photo));
        //Add component
        listSliding.add(new ItemSlideMenu(com.menemi.R.drawable.meet, com.menemi.R.string.encounters));
        listSliding.add(new ItemSlideMenu(com.menemi.R.drawable.nearby, com.menemi.R.string.people_nearby));
        listSliding.add(new ItemSlideMenu(com.menemi.R.drawable.messages, com.menemi.R.string.messages));
        listSliding.add(new ItemSlideMenu(com.menemi.R.drawable.favorite, com.menemi.R.string.favorites));
        listSliding.add(new ItemSlideMenu(com.menemi.R.drawable.guests, com.menemi.R.string.visitors));
        listSliding.add(new ItemSlideMenu(com.menemi.R.drawable.like, com.menemi.R.string.liked_you));
        listSliding.add(new ItemSlideMenu(com.menemi.R.drawable.invite_fr, com.menemi.R.string.invite_friends));
        listSliding.add(new ItemSlideMenu(com.menemi.R.drawable.setting, com.menemi.R.string.settings));
        LinearLayout container = (LinearLayout) listViewSliding.findViewById(com.menemi.R.id.sliding_menu_container);
        container.addView(header);
        setMenu(container, listSliding);
        container.addView(footer);
        setTitle("");
        drawerLayout.closeDrawer(listViewSliding);


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, com.menemi.R.string.drawer_opened, com.menemi.R.string.drawer_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(drawerView.getWindowToken(), 0);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        DBHandler.getInstance().subscribeToRest(new InternetConnectionListener() {
            @Override
            public void internetON() {
                showNoInternetMessage(getFragmentManager());
            }

            @Override
            public void internetOFF() {
                hideNoInternetMessage(getFragmentManager());
            }
        } );
        if(getIntent() != null && getIntent().getExtras() != null){
            Bundle oldExtras = getIntent().getExtras();
            Intent content = new Intent();
            content.putExtra(MyFirebaseMessagingService.ACTION, oldExtras.getString(MyFirebaseMessagingService.ACTION));
            content.putExtra(MyFirebaseMessagingService.DATA, Loader.parceGift(oldExtras));
            myReceiver.onReceive(this,content);
        }


        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            TextView versionTV = (TextView)findViewById(R.id.version);
            versionTV.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private Bitmap prepareAvatar(Bitmap bitmap) {
        final Bitmap avatar = Utils.getCroppedBitmap(bitmap);
        ownerAvatar.setColorFilter(getResources().getColor(R.color.avatarFilter), PorterDuff.Mode.MULTIPLY);
        ownerCircleAvatar.setImageBitmap(avatar);
        ownerAvatar.setImageBitmap(Utils.megaBlur(getApplicationContext(), bitmap));


        ownerAvatar.setOnClickListener(new OnMyProfileOpenListener());
        ownerCircleAvatar.setOnClickListener(new OnMyProfileOpenListener());
        ownerName.setText(DBHandler.getInstance().getMyProfile().getPersonName());
        balanceValue.setText(""+DBHandler.getInstance().getMyProfile().getPersonCredits());

        return avatar;
    }

    private static IConnectionInformerFragment connectionInformerFragment = new IConnectionInformerFragment();
    public static void showNoInternetMessage(FragmentManager fm){
        if(fm == null){
            return;
        }
        if(connectionInformerFragment == null){
           connectionInformerFragment = new IConnectionInformerFragment();
       } else{
         return;
        }

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(com.menemi.R.id.messageFragmentPlaceHolder, connectionInformerFragment);
        transaction.commitAllowingStateLoss();
    }
    public static void hideNoInternetMessage(FragmentManager fm){
        if(fm == null){
            return;
        }
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.remove( connectionInformerFragment);
        transaction.commitAllowingStateLoss();
        connectionInformerFragment = null;

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
    private void setMenu(LinearLayout container, ArrayList<ItemSlideMenu> items){

        for (int i = 0; i < items.size(); i++) {
            container.addView(items.get(i).prepareItem());
            items.get(i).setId(i);
        }

    }
    private void openFragment(Fragment fragment){
        if(getFragmentManager() == null) {
            return;
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(com.menemi.R.id.content, fragment);
        transaction.addToBackStack(transaction.getClass().getName());
        transaction.commitAllowingStateLoss();
        drawerLayout.closeDrawer(listViewSliding);
    }
    private void closeFragment(Fragment fragment){
        if( getFragmentManager() == null) {
            return;
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.remove(fragment);
        transaction.commitAllowingStateLoss();
    }



    private void replaceFragment(int pos) {
        if(pos == ENCOUNTERS){
            PersonDataFragment personDataFragment = new PersonDataFragment();
            personDataFragment.setPurpose(PersonDataFragment.Purpose.LIKE);
            openFragment(personDataFragment);

        } else if( pos == NEAR){
            ShowPeopleCompositeFragment showPeopleCompositeFragment = new ShowPeopleCompositeFragment();
            showPeopleCompositeFragment.setPurpose(ShowPeopleCompositeFragment.Purpose.NEAR);
            openFragment(showPeopleCompositeFragment);
        } else if( pos == MESSAGES){
            DialogsList dialogsList = new DialogsList();
            openFragment(dialogsList);
        } else if( pos == FAVORITES){
            ShowPeopleCompositeFragment showPeopleCompositeFragment = new ShowPeopleCompositeFragment();
            showPeopleCompositeFragment.setPurpose(ShowPeopleCompositeFragment.Purpose.FAVORITES);
            openFragment(showPeopleCompositeFragment);
        } else if( pos == VISITORS){
            ShowPeopleCompositeFragment showPeopleCompositeFragment = new ShowPeopleCompositeFragment();
            showPeopleCompositeFragment.setPurpose(ShowPeopleCompositeFragment.Purpose.VISITORS);
            openFragment(showPeopleCompositeFragment);
        } else if( pos == MY_LIKES){
            ShowPeopleCompositeFragment showPeopleCompositeFragment = new ShowPeopleCompositeFragment();
            showPeopleCompositeFragment.setPurpose(ShowPeopleCompositeFragment.Purpose.LIKES);
            openFragment(showPeopleCompositeFragment);
        } else if(pos == INVITE_FRIENDS){

        } else if( pos == SETTINGS) {
            Intent settingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(settingsActivity);
        } else if(pos == MY_PROFILE){

            PersonDataFragment personDataFragment1 = new PersonDataFragment();
            personDataFragment1.setPurpose(PersonDataFragment.Purpose.MY_PROFILE);
            personDataFragment1.setPersonObject(personObject);
            openFragment(personDataFragment1);

        }
        OnFilterClickListener.hideFilter();

    }

    private class ItemSlideMenu implements View.OnClickListener{
        int id = 0;
        private int imgId;
        private int titleId;

        public void setId(int id) {
            this.id = id;
        }

        public ItemSlideMenu(int imgId, int titleId) {
            this.imgId = imgId;
            this.titleId = titleId;
        }

        private View prepareItem() {
            View view = View.inflate(PersonPage.this, com.menemi.R.layout.item_sliding_menu, null);
            ImageView img = (ImageView) view.findViewById(com.menemi.R.id.img_id);
            TextView textView = (TextView) view.findViewById(com.menemi.R.id.item_title);
            img.setImageResource(imgId);
            textView.setText(titleId);
            view.setOnClickListener(this);
            return view;
        }

        @Override
        public void onClick(View view) {
            replaceFragment(id);
            drawerLayout.closeDrawer(listViewSliding);
        }
    }

    public static String GOVNOCODE = "";
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 22222) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
           /* if (result != null){
                //SocialNetworkHandler.getInstance().getImageG_plus();
            }
*/
        }

        CallbackManager.Factory.create().onActivityResult(requestCode, resultCode, data);

        if (SocialNetworkHandler.getInstance().isAuthFb()){
            ImageView imageView = (ImageView)findViewById(R.id.fbSrc);
            imageView.setImageResource(R.drawable.fb_icon);
            TextView textView = (TextView)findViewById(R.id.fbState);
            textView.setText("(подтвержден)");

            SocialNetworkHandler.getInstance().getProfileAlbumId(getApplicationContext(),AccessToken.getCurrentAccessToken());
        }
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                ImageView imageView = (ImageView)findViewById(R.id.vkSrc);
                imageView.setImageResource(R.drawable.vk_icon);
                SocialNetworkHandler.getInstance().getUserPhotoFromVk(String.valueOf(0), String.valueOf(200));
                if (DBHandler.getInstance().isEmtyTable(SQLiteEngine.TABLE_SOCIAL_VK)){
                    Log.d("check_table", String.valueOf(DBHandler.getInstance().isEmtyTable(SQLiteEngine.TABLE_SOCIAL_VK )));
                    SocialNetworkHandler.getInstance().getCurrentVkUser();
                }



            }
            @Override
            public void onError(VKError error) {
                // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
            }
        }));
        if (Odnoklassniki.getInstance().isActivityRequestOAuth(requestCode)) {
            Odnoklassniki.getInstance().onAuthActivityResult(requestCode, resultCode, data, new OkListener()
            {
                @Override
                public void onSuccess(JSONObject json)
                {
                    SocialNetworkHandler.getInstance().getCurrentOkUser();
                    SocialNetworkHandler.getInstance().getCurrentOkUserPhoto(null);
                    ImageView imageView = (ImageView)findViewById(R.id.okImage);
                    imageView.setImageResource(R.drawable.ok_logo);
                    TextView textView = (TextView)findViewById(R.id.okState);
                    textView.setText("(подтвержден)");
                }

                @Override
                public void onError(String error)
                {

                }
            });
        }
        if(requestCode != RESULT_CANCELED) {

        Bitmap bitmap = null;
        if (requestCode == SELECTED_PICTURES_FROM_GIZMO && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = null;
            selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            bitmap = BitmapFactory.decodeFile(picturePath);
            Log.d("Bitmap from SD", "method is called");

            cursor.close();
            PhotoSettingsFragment photoSettingsFragment = new PhotoSettingsFragment();
            photoSettingsFragment.setPhotoSetting(new PhotoSetting(bitmap));
            openFragment(photoSettingsFragment);
            Log.d("onActivityResult", "camera" + data);

        } else if (requestCode == SELECTED_PICTURES_FROM_CAMERA && resultCode == RESULT_OK) {

            Uri selectedImage = null;
            Log.d("onActivityResult", "GOVNOCODE " + GOVNOCODE);
            File image = new File(GOVNOCODE);
            Log.d("onActivityResult", "" + image.exists());
            selectedImage = Uri.fromFile(image);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            PhotoSettingsFragment photoSettingsFragment = new PhotoSettingsFragment();
            photoSettingsFragment.setPhotoSetting(new PhotoSetting(bitmap));
            openFragment(photoSettingsFragment);
            Log.d("onActivityResult", "camera" + data);
        }
        }


    }
    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()){
            finish();
        }
        else {
            Toast.makeText(getBaseContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();

            if(getFragmentManager().getBackStackEntryCount() > 1) {
                getFragmentManager().popBackStack();
            }
        }
    }


    class OnMyProfileOpenListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            replaceFragment(MY_PROFILE);
            drawerLayout.closeDrawer(listViewSliding);
        }
    }
    static class OnMenuButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            drawerLayout.openDrawer(listViewSliding);
        }
    }
    static class OnFilterClickListener implements View.OnClickListener {
        FilterFragment fragment;
        private static PersonObject owner;
        FragmentManager fm;


        public static void setOwner(PersonObject owner)
        {
            OnFilterClickListener.owner = owner;
        }


        public OnFilterClickListener(FragmentManager fm ){
            isFilterVisible = false;
            this.fm = fm;
            Log.v("onClick", "FILTER");

        }
        public static void hideFilter(){
            if ( isFilterVisible) {

            }
        }
        @Override
        public void onClick(View view) {


            Log.v("onClick", "onClick");
            if ( isFilterVisible) {
                isFilterVisible = false;
                fm.beginTransaction().remove(fragment).commitAllowingStateLoss();

            } else {
                isFilterVisible = true;
                fragment = new FilterFragment();
                fragment.setPersonObject(owner);
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(com.menemi.R.id.messageFragmentPlaceHolder, fragment);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();
            }
        }
    }

class OnVipClickListener implements View.OnClickListener{
    boolean isPersonVip;

    public OnVipClickListener(boolean isPersonVip) {
        this.isPersonVip = isPersonVip;
    }

    @Override
    public void onClick(View v) {
        if(!isPersonVip){
            openFragment(new BuyVipFragment());
        }
    }
}
    @Override
    protected void onResume() {
        if(DBHandler.getInstance().loadLastId() == -1){
            Log.d("LOGGED", "OUT");
            finish();

        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(myReceiver);
        super.onDestroy();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String s = acct.getId();
            String s1 = acct.getDisplayName();
            /*Toast.makeText(this,acct.getFamilyName() + "",Toast.LENGTH_SHORT).show();*/

        }
    }

}


