package com.menemi.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.facebook.FacebookSdk;
import com.menemi.PersonPage;
import com.menemi.R;
import com.menemi.social_network.SocialNetworkHandler;
import com.menemi.social_network.instagram.InstagramApp;
import com.menemi.social_network.social_profile_photo_handler.SocialGridView;
import com.vk.sdk.VKSdk;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ru.ok.android.sdk.Odnoklassniki;
import ru.ok.android.sdk.OkListener;

/**
 * Created by tester03 on 11.07.2016.
 */
public class UploadPhotoDialogFragments extends android.app.DialogFragment
{
    private View dialogView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        FacebookSdk.sdkInitialize(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.upload_photo_dialog_fragment, null);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.full_screen_dialog);
        builder.setView(dialogView);
        ImageButton uploadFromGallery = (ImageButton) dialogView.findViewById(R.id.uploadFromGallery);
        uploadFromGallery.setOnClickListener(v -> {
            Intent getFromGallery = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            getActivity().startActivityForResult(getFromGallery, PersonPage.SELECTED_PICTURES_FROM_GIZMO);
            dismiss();
        });
        LinearLayout uploadFromCamera = (LinearLayout)dialogView.findViewById(R.id.uploadFromCamera);
        uploadFromCamera.setOnClickListener(v -> dispatchTakePictureIntent());
        ImageButton uploadFromFb = (ImageButton)dialogView.findViewById(R.id.uploadFromFb);
        uploadFromFb.setOnClickListener(view -> {
            if (SocialNetworkHandler.getInstance().isAuthFb())
            {
                Intent i = new Intent(getActivity(), SocialGridView.class);
                i.putExtra(SocialNetworkHandler.getInstance().TARGET_SOCIAL_CONST,SocialNetworkHandler.getInstance().FB_SOCIAL);
                startActivity(i);
            } else {

                SocialNetworkHandler.getInstance().authFb(getActivity());
            }
        });
        ImageButton uploadFromVk = (ImageButton)dialogView.findViewById(R.id.uploadFromVk);
        uploadFromVk.setOnClickListener(view -> {
            if (VKSdk.isLoggedIn()){
                Intent i = new Intent(getActivity(), SocialGridView.class);
                i.putExtra(SocialNetworkHandler.getInstance().TARGET_SOCIAL_CONST,SocialNetworkHandler.getInstance().VK_SOCIAL);
                startActivity(i);
            } else {
                SocialNetworkHandler.getInstance().authVk(getActivity());
            }
        });

        ImageButton uploadFromInsta = (ImageButton) dialogView.findViewById(R.id.uploadFromInsta);
        final InstagramApp instaObj = new InstagramApp(getActivity(), SocialNetworkHandler.CLIENT_ID,
                SocialNetworkHandler.CLIENT_SECRET, SocialNetworkHandler.CALLBACK_URL);
        uploadFromInsta.setOnClickListener(view -> {

            Intent i = new Intent(getActivity(), SocialGridView.class);
            i.putExtra(SocialNetworkHandler.getInstance().TARGET_SOCIAL_CONST,SocialNetworkHandler.getInstance().INSTA_SOCIAL);
            startActivity(i);
            if (instaObj.isLogged()){
            } else {
                instaObj.authorize();
                instaObj.setListener(new InstagramApp.OAuthAuthenticationListener()
                {
                    @Override
                    public void onSuccess()
                    {

                    }

                    @Override
                    public void onFail(String error)
                    {

                    }
                });
            }
        });

        ImageButton uploadFromG = (ImageButton)dialogView.findViewById(R.id.uploadFromGoogle);
        uploadFromG.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), SocialGridView.class);
            i.putExtra(SocialNetworkHandler.getInstance().TARGET_SOCIAL_CONST,SocialNetworkHandler.getInstance().G_SOCIAL);
            startActivity(i);

        });

        ImageButton uploadFromOk = (ImageButton)dialogView.findViewById(R.id.uploadFromOk);
        uploadFromOk.setOnClickListener(view -> Odnoklassniki.getInstance().checkValidTokens(new OkListener()
        {
            @Override
            public void onSuccess(JSONObject json)
            {
                Intent i = new Intent(getActivity(), SocialGridView.class);
                i.putExtra(SocialNetworkHandler.getInstance().TARGET_SOCIAL_CONST,SocialNetworkHandler.getInstance().OK_SOCIAL);
                startActivity(i);
            }

            @Override
            public void onError(String error)
            {
                SocialNetworkHandler.getInstance().okAuth(getActivity());
            }
        }));
        return builder.create();
    }
    private void dispatchTakePictureIntent() {
        Intent getFromCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (getFromCamera.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.android.fileprovider",
                        photoFile);

                List<ResolveInfo> resInfoList = getActivity().getApplicationContext().getPackageManager().queryIntentActivities(getFromCamera, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    getActivity().getApplicationContext().grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                getFromCamera.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                getActivity().startActivityForResult(getFromCamera, PersonPage.SELECTED_PICTURES_FROM_CAMERA);
                dismiss();
            }
        }
    }
    private File createImageFile() throws IOException {

        // Create an image file name
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                "HUI",  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        PersonPage.GOVNOCODE = "" + image.getAbsolutePath();
        return image;
    }



}