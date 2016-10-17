package com.menemi.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.DialogInfo;
import com.menemi.personobject.PersonObject;
import com.menemi.personobject.PhotoSetting;
import com.menemi.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Ui-Developer on 11.08.2016.
 */
public class DialogListItem extends Fragment {
    private View rootView = null;
    private DialogInfo dialogInfo = null;

    public void setDialogInfo(DialogInfo dialogInfo) {
        this.dialogInfo = dialogInfo;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.dialog_list_items, null);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
                parent.removeAllViews();

            }
        }
        int ownerId = DBHandler.getInstance().getUserId();


        final LinearLayout messageContainer = (LinearLayout)rootView.findViewById(R.id.messageContainer);
        DBHandler.getInstance().getPhotos(ownerId, dialogInfo.getProfileId(), 0, 1, Utils.PICTURE_QUALITY_THUMBNAIL, new DBHandler.ResultListener() {
            @Override
            public void onFinish(Object object) {
                ArrayList<PhotoSetting> pictures = (ArrayList<PhotoSetting>) object;
                ImageView photo = (ImageView) rootView.findViewById(R.id.photo);
                Bitmap contactAvatar;
                if(pictures != null && pictures.size() != 0){
                    contactAvatar = Utils.getCroppedBitmap(pictures.get(0).getPhoto());

                } else {
                    contactAvatar = Utils.getCroppedBitmap(Utils.getBitmapFromResource(getActivity(), R.drawable.empty_photo));
                }
                dialogInfo.setConatactAvatar(contactAvatar);
                photo.setImageBitmap(contactAvatar);
                photo.setOnClickListener(new PictureClickListener(dialogInfo.getProfileId()));
                messageContainer.setOnClickListener(new MessageClickListener(dialogInfo, contactAvatar));
            }
        });

        TextView name = (TextView) rootView.findViewById(R.id.name);
        name.setText(dialogInfo.getContactName());
        TextView time = (TextView) rootView.findViewById(R.id.time);
        time.setText(Utils.getMessageTimeForChat(dialogInfo.getLastMessageDate()));
        TextView message = (TextView) rootView.findViewById(R.id.message);
        message.setText(dialogInfo.getLastMessage());
        TextView newMessagesCount = (TextView) rootView.findViewById(R.id.newMessagesCount);
        newMessagesCount.setText("" + dialogInfo.getNewMessagesCount());



        return rootView;
    }
    class PictureClickListener implements View.OnClickListener{
        private int personId;

        public PictureClickListener(int personId) {
            this.personId = personId;
        }

        @Override
        public void onClick(View view) {
            DBHandler.getInstance().getOtherProfile(personId, new DBHandler.ResultListener() {
                @Override
                public void onFinish(Object object) {

                    if (getActivity() == null || getFragmentManager() == null) {
                        return;
                    } else {
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        PersonDataFragment personDataFragment = new PersonDataFragment();
                        personDataFragment.setPurpose(PersonDataFragment.Purpose.PROFILE);
                        personDataFragment.setPersonObject((PersonObject) object);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.replace(getActivity().findViewById(R.id.content).getId(), personDataFragment);
                        fragmentTransaction.commitAllowingStateLoss();
                    }

                }
            });

        }

    }
    class MessageClickListener implements View.OnClickListener{
        private DialogInfo dialogInfo;

        private Bitmap contactAvatar;

        public MessageClickListener(DialogInfo dialogInfo, Bitmap contactAvatar) {
            this.dialogInfo = dialogInfo;

            this.contactAvatar = contactAvatar;
        }

        @Override
        public void onClick(View view) {

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

            ChatFragment chatFragment = new ChatFragment();
            chatFragment.setDialogInfo(dialogInfo);
            fragmentTransaction.replace(R.id.content, chatFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }
}
