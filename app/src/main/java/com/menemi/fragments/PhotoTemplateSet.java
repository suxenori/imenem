package com.menemi.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.menemi.R;
import com.menemi.personobject.PhotoTemplate;
import com.menemi.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Ui-Developer on 09.09.2016.
 */
public class PhotoTemplateSet  extends Fragment {
    private View rootView = null;
    private ArrayList<PhotoTemplate> photoTemplates;
    private ImageView[] images = new ImageView[4];
    private ImageView[] checks = new ImageView[4];
    private int start;

    public void setPhotoTemplates(ArrayList<PhotoTemplate> photoTemplates,int start) {
        this.photoTemplates = photoTemplates;
        this.start = start;
    }

    public void setOnArray(int[] ids){
        for (int i = 0; i < ids.length; i++) {
            for (int j = start; j < photoTemplates.size() && j < start + 4; j++) {
                if(photoTemplates.get(j).getTemplateID() == ids[i]){
                    photoTemplates.get(j).setOn(true);
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.photo_template_set, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }

        images[0] = (ImageView) rootView.findViewById(R.id.template0);
        images[1] = (ImageView) rootView.findViewById(R.id.template1);
        images[2] = (ImageView) rootView.findViewById(R.id.template2);
        images[3] = (ImageView) rootView.findViewById(R.id.template3);

        checks[0] = (ImageView) rootView.findViewById(R.id.templateBorder0);
        checks[1] = (ImageView) rootView.findViewById(R.id.templateBorder1);
        checks[2] = (ImageView) rootView.findViewById(R.id.templateBorder2);
        checks[3] = (ImageView) rootView.findViewById(R.id.templateBorder3);

        for (int i = start, j = 0; i < photoTemplates.size() && j < images.length; i++, j++) {
            images[j].setImageBitmap(photoTemplates.get(i).getTemplatePicture());
            checks[j].setOnClickListener(new OnTemplateClickListener(checks[j], photoTemplates.get(i)));
        }
        return rootView;
    }
    class OnTemplateClickListener implements View.OnClickListener{
        ImageView imageView;
        boolean isChecked = false;
        PhotoTemplate template;

        public OnTemplateClickListener(ImageView imageView, PhotoTemplate template) {
            this.imageView = imageView;
            this.template = template;
            if(template.isOn()){
                isChecked = true;
                imageView.setImageBitmap(Utils.getBitmapFromResource(getActivity(), R.drawable.template_border));
            }
        }

        @Override
        public void onClick(View view) {
            if(isChecked){
                isChecked = false;
                imageView.setImageBitmap(null);
                template.setOn(false);
            } else {
                isChecked = true;
                imageView.setImageBitmap(Utils.getBitmapFromResource(getActivity(), R.drawable.template_border));
                template.setOn(true);
            }

        }
    }
}
