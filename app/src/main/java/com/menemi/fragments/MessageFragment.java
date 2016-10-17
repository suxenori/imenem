package com.menemi.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.menemi.R;
import com.menemi.personobject.DialogMessage;
import com.menemi.utils.Utils;

/**
 * Created by Ui-Developer on 12.08.2016.
 */
public class MessageFragment extends Fragment {
    private View rootView = null;
    boolean isFirst = true;
    private DialogMessage message;
    private Bitmap cropedAvatar;

    public void setMessage(DialogMessage message) {
        this.message = message;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public void setStatus(STATUS status){

        ImageView messageStatus = (ImageView) rootView.findViewById(R.id.messageStatus);
        if(status == STATUS.SEND) {
            messageStatus.setImageResource(R.drawable.unread_msg);
        } else if(status == STATUS.READ){
            messageStatus.setImageResource(R.drawable.read_msg);
        }
    }
public enum STATUS{
    SEND,
    READ
}


    public void setCropedAvatar(Bitmap cropedAvatar) {
        this.cropedAvatar = cropedAvatar;
    }
    public boolean compareRND(int[] rnd){
        return message.comparRND(rnd);
    }
    public String getRND(){
        return "RND1 " + message.getRnd1() + " RND2 " + message.getRnd2();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            if (message.isOwner()) {
                rootView = inflater.inflate(R.layout.message_me_fragment, container, false);

            } else {
                rootView = inflater.inflate(R.layout.message_to_fragment, container, false);
            }

        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }

        }
        Log.d("MessageFragment", message.toString());
        LinearLayout messageContainer = (LinearLayout) rootView.findViewById(R.id.messageContainer);
        if (!isFirst && message.isOwner()) {
            messageContainer.setBackgroundResource(R.drawable.message_me_2_background);
        } else if (!isFirst && !message.isOwner()) {
            messageContainer.setBackgroundResource(R.drawable.message_to_2_background);
        }

        final TextView text = (TextView) rootView.findViewById(R.id.text);
        if (message.getPicture() == null) {
            text.setText(message.getMessageBody());
        } else {
            ImageView picture = (ImageView) rootView.findViewById(R.id.picture);
            picture.setImageBitmap(message.getPicture());

            messageContainer.removeView(text);
        }

        TextView time = (TextView) rootView.findViewById(R.id.time);
        time.setText(Utils.getMessageTimeForChat(message.getDate()));

        if (isFirst) {
            Log.d("AVATAR", "" + cropedAvatar);
            ImageView photo = (ImageView) rootView.findViewById(R.id.photo);
            photo.setImageBitmap(cropedAvatar);

        }

        return rootView;
    }

}
