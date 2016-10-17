package com.menemi.fragments;

import android.app.Dialog;
import android.app.Fragment;
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
import android.widget.Button;
import com.menemi.R;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Ui-Developer on 15.08.2016.
 */
public class AttachPhotoDialogFragment extends android.app.DialogFragment {
    private View dialogView;
    private Button uploadFromCamera;
    private Button uploadFromGallery;
    public static final int SELECTED_PICTURES_FROM_GIZMO = 3;
    public static final int SELECTED_PICTURES_FROM_CAMERA = 4;
    public static String PICTURE_PATH = "";
    private Fragment parent = null;

    public void setParent(Fragment parent) {
        this.parent = parent;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.add_photo_dialog_fragment, null);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.full_screen_dialog);
        builder.setView(dialogView);
        uploadFromGallery = (Button) dialogView.findViewById(R.id.uploadFromGallery);
        uploadFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getFromGallery = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                parent.startActivityForResult(getFromGallery, SELECTED_PICTURES_FROM_GIZMO);
                dismiss();
            }
        });
        uploadFromCamera = (Button) dialogView.findViewById(R.id.uploadFromCamera);
        uploadFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dispatchTakePictureIntent();




            }
        });

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
                parent.startActivityForResult(getFromCamera, SELECTED_PICTURES_FROM_CAMERA);
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
        PICTURE_PATH = "" + image.getAbsolutePath();
        return image;
    }


}

