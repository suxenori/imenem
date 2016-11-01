package com.menemi.social_network;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.dbfactory.Fields;
import com.menemi.social_network.instagram.InstagramApp;
import com.menemi.utils.Utils;
import com.vk.sdk.VKSdk;

import ru.ok.android.sdk.Odnoklassniki;


/**
 * Created by tester03 on 05.09.2016.
 */
public class LogOutSocialDialog extends DialogFragment
{
    private String social;
    private View rootView;
    private InstagramApp instagramApp;

    public void setInstagramApp(InstagramApp instagramApp)
    {
        this.instagramApp = instagramApp;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        rootView = View.inflate(getActivity(), R.layout.log_out_social_dialog, null);
        TextView name = (TextView) rootView.findViewById(R.id.name);
        builder.setView(rootView);
        String chars = ("Ваш профиль " + social);
        SpannableString str = new SpannableString(chars);
        str.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)), 0, chars.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setTitle(str);

        final ImageView image = (ImageView) rootView.findViewById(R.id.photo);
        if (social.equals(SocialNetworkHandler.getInstance().FB_SOCIAL))
        {
            SocialProfile socialProfile = DBHandler.getInstance().getSocialProfile(Fields.SOCIAL_NETWORKS.FACEBOOK);
            name.setText(socialProfile.getFirstName() + " " + socialProfile.getLastName());
            image.setImageBitmap(Utils.getCroppedBitmap(socialProfile.getImage()));

        } else if (social.equals(SocialNetworkHandler.getInstance().VK_SOCIAL))
        {
            SocialProfile socialProfile = DBHandler.getInstance().getSocialProfile(Fields.SOCIAL_NETWORKS.VKONTAKTE);
            name.setText(socialProfile.getFirstName() + " " + socialProfile.getLastName());
            image.setImageBitmap(Utils.getCroppedBitmap(socialProfile.getImage()));

        } else if (social.equals(SocialNetworkHandler.getInstance().OK_SOCIAL))
        {
            SocialProfile socialProfile = DBHandler.getInstance().getSocialProfile(Fields.SOCIAL_NETWORKS.ODNOKLASNIKI);
            name.setText(socialProfile.getFirstName() + " " + socialProfile.getLastName());
            image.setImageBitmap(Utils.getCroppedBitmap(socialProfile.getImage()));

        } else if (social.equals(SocialNetworkHandler.getInstance().INSTA_SOCIAL))
        {
            SocialProfile socialProfile = DBHandler.getInstance().getSocialProfile(Fields.SOCIAL_NETWORKS.INSTAGRAM);
            name.setText(instagramApp.getName());
            image.setImageBitmap(Utils.getCroppedBitmap(socialProfile.getImage()));
        }
        Button close = (Button) rootView.findViewById(R.id.cancelButton);
        close.setOnClickListener(view -> dismiss());
        Button unbindSocial = (Button) rootView.findViewById(R.id.unbindButton);
        unbindSocial.setOnClickListener(view -> {
            if (social.equals(SocialNetworkHandler.getInstance().FB_SOCIAL))
            {
                SocialNetworkHandler.getInstance().getPhotoUrlFb().clear();
                SocialNetworkHandler.getInstance().logOut();
                ImageView imageView = (ImageView) getActivity().findViewById(R.id.fbSrc);
                imageView.setImageResource(R.drawable.fb_off);
                TextView textView = (TextView) getActivity().findViewById(R.id.fbState);
                textView.setText("(привязать)");
                dismiss();
            } else if (social.equals(SocialNetworkHandler.getInstance().VK_SOCIAL))
            {
                SocialNetworkHandler.getInstance().getPhotoUrlVk().clear();
                VKSdk.logout();
                ImageView imageView = (ImageView) getActivity().findViewById(R.id.vkSrc);
                imageView.setImageResource(R.drawable.vk_off);
                dismiss();
            } else if (social.equals(SocialNetworkHandler.getInstance().OK_SOCIAL))
            {
                SocialNetworkHandler.getInstance().getPhotoUrlOk().clear();
                Odnoklassniki.getInstance().clearTokens();
                ImageView imageView = (ImageView) getActivity().findViewById(R.id.okImage);
                imageView.setImageResource(R.drawable.ok_off);
                TextView textView = (TextView) getActivity().findViewById(R.id.okState);
                textView.setText("(привязать)");
                dismiss();
            } else if (social.equals(SocialNetworkHandler.getInstance().INSTA_SOCIAL))
            {
                SocialNetworkHandler.getInstance().getPhotoUrlInsta().clear();
                ImageView imageView = (ImageView) getActivity().findViewById(R.id.instaImage);
                imageView.setImageResource(R.drawable.ic_verification_instagram_disabled);
                TextView textView = (TextView) getActivity().findViewById(R.id.instaState);
                textView.setText("(привязать)");
                instagramApp.resetAccessToken();
                dismiss();
            } else if (social.equals(SocialNetworkHandler.getInstance().G_SOCIAL))
            {
                SocialNetworkHandler.getInstance().getPhotoUrlG_plus().clear();
                ImageView imageView = (ImageView) getActivity().findViewById(R.id.g_Src);
                imageView.setImageResource(R.drawable.gplus_ic_off);
                TextView textView = (TextView) getActivity().findViewById(R.id.gState);
                textView.setText("(привязать)");

                dismiss();

            }
        });

        return builder.create();
    }

    public void setSocial(String social)
    {
        this.social = social;
    }

}
