package com.menemi;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.menemi.customviews.OCDialog;
import com.menemi.dbfactory.DBHandler;
import com.menemi.dbfactory.Fields;
import com.menemi.dbfactory.SQLiteEngine;
import com.menemi.dbfactory.rest.PictureLoader;
import com.menemi.fragments.PhotoSettingsFragment;
import com.menemi.fragments.PublicPhotoListFragment;
import com.menemi.fragments.UploadPhotoDialogFragments;
import com.menemi.fragments.VerificationFragment;
import com.menemi.personobject.PhotoSetting;
import com.menemi.social_network.SocialNetworkHandler;
import com.menemi.social_network.SocialProfile;
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

import static com.menemi.fragments.VerificationFragment.acct;

/**
 * Created by Ui-Developer on 08.07.2016.
 */
public class PictureSlideActivity extends Activity {
    static ArrayList<PhotoSetting> photosUrls = new ArrayList<>();
    public static final int SELECTED_PICTURES_FROM_GIZMO = 1;
    public static final int SELECTED_PICTURES_FROM_CAMERA = 2;

    public static void setPhotosUrls(ArrayList<PhotoSetting> photosUrls) {
        PictureSlideActivity.photosUrls = photosUrls;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_slide);

        int pageNumber = 0;
        int totalPages = 0;
        boolean isMyProfile = false;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                pageNumber = extras.getInt("page");
                totalPages = extras.getInt("totalPages");
                isMyProfile = extras.getBoolean("MY_profile", false);

            }
        }
        authCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(authCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Log.d("test_fb", "Success");
                Log.d("fbProfileId", loginResult + "");
             /*   Log.d("",data + "");
                Log.d("",data + "");*/
                ImageView imageView = (ImageView) findViewById(R.id.fbSrc);
                imageView.setImageResource(R.drawable.fb_icon);
                TextView textView = (TextView) findViewById(R.id.fbState);
                textView.setText(R.string.confirmed);
                // SocialNetworkHandler.getInstance().setCurrentFbUserToSQLite();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PhotoSwipeFragment photoSwipeFragment = new PhotoSwipeFragment();
        photoSwipeFragment.setUrlsArray(photosUrls);
        photoSwipeFragment.setFullScreen(true);
        photoSwipeFragment.setPageNumber(pageNumber);


        fragmentTransaction.replace(com.menemi.R.id.fragmentContainer, photoSwipeFragment);
        fragmentTransaction.commitAllowingStateLoss();

        LinearLayout photoMenu = (LinearLayout) findViewById(R.id.photoMenu);
        if (!isMyProfile) {
            photoMenu.removeAllViews();
            photoMenu.setBackgroundResource(R.color.no_color);
        } else {
            photoMenu.setOnClickListener(new View.OnClickListener() {
                boolean isOpened = false;

                @Override
                public void onClick(View v) {
                    LinearLayout menuContainer = (LinearLayout) findViewById(R.id.menuContainer);
                    if (!isOpened) {
                        isOpened = true;


                        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        View menu = inflater.inflate(R.layout.photo_menu_fragment, null);
                        LinearLayout avatar = (LinearLayout) menu.findViewById(R.id.avatar);
                        avatar.setOnClickListener(new AddAvatarClickListener());


                        LinearLayout addPhoto = (LinearLayout) menu.findViewById(R.id.addPhoto);
                        addPhoto.setOnClickListener(new UploadPhotoListener());
                        menuContainer.addView(menu);

                        LinearLayout delete = (LinearLayout) findViewById(R.id.delete);
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new OCDialog(PictureSlideActivity.this, getString(R.string.delete_photo_title), getString(R.string.delete_photo_message), R.drawable.delete_icon, new OCDialog.OKListener() {
                                    @Override
                                    public void onOKpressed() {

                                        DBHandler.getInstance().deletePhoto(photoSwipeFragment.getCurrentItem(), DBHandler.getInstance().getUserId(), new DBHandler.ResultListener() {
                                            @Override
                                            public void onFinish(Object object) {
                                                finish();
                                            }
                                        });

                                    }
                                },
                                        new OCDialog.CANCELListener() {
                                            @Override
                                            public void onCANCELpressed() {

                                            }
                                        });

                            }
                        });
                    } else {
                        isOpened = false;
                        menuContainer.removeAllViews();
                    }
                }
            });
        }
    }

    private class AddAvatarClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (getFragmentManager() == null) {
                return;
            }
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            PublicPhotoListFragment publicPhotoListFragment = new PublicPhotoListFragment();
            publicPhotoListFragment.setPurpose(PublicPhotoListFragment.Purpose.CHOOSE_AVATAR);
            ft.addToBackStack(null);
            ft.replace(R.id.content, publicPhotoListFragment);
            ft.commitAllowingStateLoss();
        }
    }

    private class UploadPhotoListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (getFragmentManager() == null) {
                return;
            }
            UploadPhotoDialogFragments dialogFragment = new UploadPhotoDialogFragments();
            dialogFragment.show(getFragmentManager(), "Dialog Fragment");
        }
    }

    private CallbackManager authCallbackManager;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        authCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 666) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //handleSignInResult(result);

            if (result != null) {

                handleSignInResult(result);
            }

        }
        if (SocialNetworkHandler.getInstance().isAuthFb()) {


            SocialNetworkHandler.getInstance().getProfileAlbumId(getApplicationContext(), AccessToken.getCurrentAccessToken());
        }
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                ImageView imageView = (ImageView) findViewById(R.id.vkSrc);
                imageView.setImageResource(R.drawable.vk_icon);
                SocialNetworkHandler.getInstance().getUserPhotoFromVk(String.valueOf(0), String.valueOf(200));
                if (DBHandler.getInstance().isEmtyTable(SQLiteEngine.TABLE_SOCIAL_VK)) {
                    Log.d("check_table", String.valueOf(DBHandler.getInstance().isEmtyTable(SQLiteEngine.TABLE_SOCIAL_VK)));
                    SocialNetworkHandler.getInstance().getCurrentVkUser();
                }


            }

            @Override
            public void onError(VKError error) {
                // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
            }
        })) ;
        if (Odnoklassniki.getInstance().isActivityRequestOAuth(requestCode)) {
            Odnoklassniki.getInstance().onAuthActivityResult(requestCode, resultCode, data, new OkListener() {
                @Override
                public void onSuccess(JSONObject json) {
                    SocialNetworkHandler.getInstance().getCurrentOkUser();
                    SocialNetworkHandler.getInstance().getCurrentOkUserPhoto(null);
                    ImageView imageView = (ImageView) findViewById(R.id.okImage);
                    imageView.setImageResource(R.drawable.ok_logo);
                    TextView textView = (TextView) findViewById(R.id.okState);
                    textView.setText(getString(R.string.verified));
                }

                @Override
                public void onError(String error) {

                }
            });
        }
        if (requestCode != RESULT_CANCELED) {

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
                finishProgressDialog();
                Log.d("onActivityResult", "camera" + data);

            } else if (requestCode == SELECTED_PICTURES_FROM_CAMERA && resultCode == RESULT_OK) {

                Uri selectedImage = null;
                Log.d("onActivityResult", "GOVNOCODE " + PersonPage.GOVNOCODE);
                File image = new File(PersonPage.GOVNOCODE);
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
                finishProgressDialog();
                Log.d("onActivityResult", "camera" + data);
            }
        }


    }

    public static void finishProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        acct = result.getSignInAccount();

        // Signed in successfully, show authenticated UI.
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            if (acct != null) {
                SocialProfile socialProfile = new SocialProfile();
                socialProfile.setId(acct.getId());
                socialProfile.setFullName(acct.getDisplayName());
                socialProfile.setLastName(acct.getFamilyName());
                socialProfile.setFirstName(acct.getGivenName());
                if (acct.getPhotoUrl() == null) {
                    socialProfile.setImage(Utils.getBitmapFromResource(this, R.drawable.no_photo));

                    DBHandler.getInstance().saveSocialProfile(socialProfile, Fields.SOCIAL_NETWORKS.GOOGLE_PLUS, new DBHandler.ResultListener() {
                        @Override
                        public void onFinish(Object object) {


                        }
                    });

                } else {
                    new PictureLoader(acct.getPhotoUrl().toString(), picture -> {
                        socialProfile.setImage(picture);

                        DBHandler.getInstance().saveSocialProfile(socialProfile, Fields.SOCIAL_NETWORKS.GOOGLE_PLUS, new DBHandler.ResultListener() {
                            @Override
                            public void onFinish(Object object) {

                            }
                        });

                    });
                }
                SocialNetworkHandler.getInstance().getImageG_plus(acct.getId());
                ImageView imageView = (ImageView) findViewById(R.id.g_Src);
                imageView.setImageResource(R.drawable.gplus_ic);
                TextView textView = (TextView) findViewById(R.id.gState);
                textView.setText(getString(R.string.verified));
                VerificationFragment.isLogInG = true;
            }


        }

    }

    private void openFragment(Fragment fragment) {
        if (getFragmentManager() == null) {
            return;
        }
        startProgressDialog(this);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(com.menemi.R.id.content, fragment);
        transaction.addToBackStack(transaction.getClass().getName());
        transaction.commitAllowingStateLoss();

    }

    private static ProgressDialog progressDialog;

    public static void startProgressDialog(Context ctx) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = Utils.startLodingProgress(ctx, null, null);
    }
}
