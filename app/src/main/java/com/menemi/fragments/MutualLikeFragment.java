package com.menemi.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.DialogInfo;
import com.menemi.personobject.PersonObject;
import com.menemi.utils.Utils;

/**
 * Created by Ui-Developer on 04.11.2016.
 */

public class MutualLikeFragment extends Fragment {

    private View rootView = null;
    PersonObject likedPerson = null;

    public void setLikedPerson(PersonObject likedPerson) {
        this.likedPerson = likedPerson;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.mutual_like_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        Button startChat = (Button) rootView.findViewById(R.id.startChat);
        Button cancel = (Button) rootView.findViewById(R.id.cancel);
        startChat.setOnClickListener(new OpenChatFragment());
        cancel.setOnClickListener(new CancelButtonListener());
        TextView text = (TextView)rootView.findViewById(R.id.text);
        text.setText(getString(R.string.mutual_text, likedPerson.getPersonName()));
        ImageView myAvatar = (ImageView) rootView.findViewById(R.id.myAvatar);
        ImageView likedAvatar = (ImageView) rootView.findViewById(R.id.likedAvatar);
        DBHandler.getInstance().getAvatar(DBHandler.getInstance().getUserId(), (image)->{
            if(image != null) {
                myAvatar.setImageBitmap(Utils.getCroppedBitmap((Bitmap) image));
            } else {
                if(getActivity()!= null) {
                    myAvatar.setImageBitmap(Utils.getCroppedBitmap(Utils.getBitmapFromResource(getActivity(), R.drawable.no_photo)));
                }
            }
        });
        DBHandler.getInstance().getAvatar(likedPerson.getPersonId(), (image)->{
            if(image != null) {
                likedAvatar.setImageBitmap(Utils.getCroppedBitmap((Bitmap) image));
            } else {
                if(getActivity()!= null) {
                    likedAvatar.setImageBitmap(Utils.getCroppedBitmap(Utils.getBitmapFromResource(getActivity(), R.drawable.no_photo)));
                }
            }
        });

        return rootView;
    }
    class OpenChatFragment implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            final DialogInfo dialogInfo = new DialogInfo(likedPerson);
            ChatFragment chatFragment = new ChatFragment();
            chatFragment.setDialogInfo(dialogInfo);
            getFragmentManager().beginTransaction().replace(R.id.content, chatFragment).addToBackStack(null).commitAllowingStateLoss();
        }
    }
    class CancelButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            getFragmentManager().popBackStack();
            onCancel.onCancel();
        }
    }

    public void setOnCancel(OnCancelListener onCancel) {
        this.onCancel = onCancel;
    }

    private OnCancelListener onCancel;
    interface OnCancelListener{
        void onCancel();
    }
}
