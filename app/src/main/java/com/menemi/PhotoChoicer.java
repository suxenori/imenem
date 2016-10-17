/*
package com.menemi;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.menemi.customviews.ObservableScrollView;
import com.menemi.fragments.SocialRowPhotoFragment;
import com.menemi.social_network.SocialNetworkHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

*/
/**
 * Created by tester03 on 25.08.2016.
 *//*

public class PhotoChoicer extends AppCompatActivity
{
    private Toolbar toolbar;
    private int fragmentId = 0;
    private int viewWithImage = 3;
    private int count;
    int a  = 0;
    private boolean isFbSocial = false;
    private boolean isVkSocial = false;
    private boolean isOkSocial = false;
    private boolean isInstaSocial = false;
    FragmentManager fm = getFragmentManager();
    ObservableScrollView scrollView;
    private ArrayList<Bitmap> list = new ArrayList<Bitmap>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(com.menemi.R.layout.photo_choiser_layout);
        toolbar = (Toolbar) findViewById(com.menemi.R.id.toolbar_with_checkBox);
        scrollView = (ObservableScrollView) findViewById(com.menemi.R.id.scrollView);
        setSupportActionBar(toolbar);
        configureToolbar();
        if (getIntent().getStringExtra(SocialNetworkHandler.getInstance().TARGET_SOCIAL_CONST).
                equals(SocialNetworkHandler.getInstance().FB_SOCIAL)){
            isOkSocial = false;
            isInstaSocial = false;
            isVkSocial = false;
            isFbSocial = true;
            Log.i("Photo_Count",count + " - user photo count from Fb");
        } else if (getIntent().getStringExtra(SocialNetworkHandler.getInstance().TARGET_SOCIAL_CONST)
                .equals(SocialNetworkHandler.getInstance().VK_SOCIAL)){
            isFbSocial = false;
            isVkSocial = true;
            isOkSocial = false;
            isInstaSocial = false;
            Log.i("Photo_Count",count + " - user photo count from Vk");
        } else if (getIntent().getStringExtra(SocialNetworkHandler.getInstance().TARGET_SOCIAL_CONST)
                .equals(SocialNetworkHandler.getInstance().OK_SOCIAL)){
            isFbSocial = false;
            isVkSocial = false;
            isOkSocial = true;
            isInstaSocial = false;

            Log.i("Photo_Count",count + " - user photo count from Ok");
        } else if (getIntent().getStringExtra(SocialNetworkHandler.getInstance().TARGET_SOCIAL_CONST)
                .equals(SocialNetworkHandler.getInstance().INSTA_SOCIAL)){
            isFbSocial = false;
            isVkSocial = false;
            isOkSocial = false;
            isInstaSocial = true;
            count = SocialNetworkHandler.getInstance().getPhotoCountInsta();
            Log.i("Photo_Count",count + " - user photo count from Insta");
        }
        if (count % 2 != 0){
            count++;
            Log.i("Photo_Count",count + " - user photo count from Fb + 1, if count % 2 != 0");
        }
        SocialRowPhotoFragment socialRowPhotoFragment;
        for (int i = 0; i < count / 2; i++)
        {
            Log.i("counter", i + "");
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            socialRowPhotoFragment = new SocialRowPhotoFragment();
            ft.add(com.menemi.R.id.vertical, socialRowPhotoFragment, i + "");
            ft.commitAllowingStateLoss();
        }
        Log.i("Fragment_count",count / 2 + " - fragment count with 2 image");
        SocialNetworkHandler.setChangeArray(new SocialNetworkHandler.ChangePhotoArray()
        {
            @Override
            public void changeArray(ArrayList<Bitmap> photoArray)
            {
                Log.i("photoArray", "photoArray is changed");
                a++;
                list.add(photoArray.get(photoArray.size() - 1));
                Log.d("list", list.size() + "");
                if (list.size() % 2 == 0)
                {

                    if(picturesToLoad[0] != -1){
                        editView(list.get(0), list.get(1), (SocialRowPhotoFragment) fm.findFragmentByTag(picturesToLoad[0]/2 + ""));
                        picturesToLoad[0] = -1;
                        list.clear();
                        return;
                    } else if (picturesToLoad[1] != -1)
                        {
                            editView(list.get(0), list.get(1), (SocialRowPhotoFragment) fm.findFragmentByTag(picturesToLoad[1]/2 + ""));
                            picturesToLoad[1] = -1;
                            list.clear();
                            return;
                        } else if (picturesToLoad[2] != -1)
                    {
                        editView(list.get(0), list.get(1), (SocialRowPhotoFragment) fm.findFragmentByTag(picturesToLoad[2]/2 + ""));
                        picturesToLoad[2] = -1;
                        list.clear();
                        return;
                    }

                    editView(list.get(0), list.get(1), (SocialRowPhotoFragment) fm.findFragmentByTag(fragmentId + ""));
                    fragmentId++;
                    list.clear();
                } else if (a == count - 1){
                    editView(list.get(0), null, (SocialRowPhotoFragment) fm.findFragmentByTag(fragmentId + ""));
                    fragmentId++;
                    list.clear();
                }
            }


        });
        scrollView.setOnTouchListener(new View.OnTouchListener()
        {

            public boolean onTouch(View v, MotionEvent event)
            {

                if (event.getAction() == MotionEvent.ACTION_UP)
                {

                    scrollView.startScrollerTask();
                }

                return false;
            }
        });
        scrollView.setOnScrollStoppedListener(new ObservableScrollView.OnScrollStoppedListener()
        {
            @Override
            public void onScrollStopped()
            {
                Log.d("count", " i am stopped");
                PictureData pictureData = new PictureData(picturesToLoad);
                Log.d("count", picturesToLoad[0] + " " + picturesToLoad[1] + " " + picturesToLoad[2] + " ");
                Log.d("count", " pictureData.isNeedRequest() " + pictureData.isNeedRequest());
                Log.d("count", "pictureData.getOffset() " + pictureData.getOffset());
                Log.d("count", "pictureData.getCount() " + pictureData.getCount());

                if (pictureData.isNeedRequest)
                    if (isFbSocial){
                        {
                            SocialNetworkHandler.getInstance().getPhotoTest(getApplicationContext(), AccessToken.getCurrentAccessToken(), pictureData.getOffset(), pictureData.getCount());
                            if (pictureData.isDoubleRequest()) {
                                SocialNetworkHandler.getInstance().getPhotoTest(getApplicationContext(), AccessToken.getCurrentAccessToken(), " " + picturesToLoad[3], "2");

                            }
                        }
                    } else if (isVkSocial){
                        SocialNetworkHandler.getInstance().getUserPhotoFromVk(pictureData.getOffset(),pictureData.getCount());
                        if (pictureData.isDoubleRequest()) {
                            SocialNetworkHandler.getInstance().getUserPhotoFromVk(" " + picturesToLoad[3], "2");
                        }
                    } else if (isOkSocial){
                        Log.d("listlist", SocialNetworkHandler.getInstance().getPhotoUrlOk().size() + " - total count Url");
                        ArrayList urlList = SocialNetworkHandler.getInstance().getPhotoUrlOk();
                        List result;
                        int count =  (pictureData.getOffsetInt() + pictureData.getCountInt()) - 1;
                        Log.d("urlListSize", urlList.size() + " - size of main array");
                        Log.d("listlist", count +  " - summ of count and offset");
                        result = urlList.subList(pictureData.getOffsetInt(), count);
                        for (int i = 0; i < result.size(); i++)

                        {
                            SocialNetworkHandler.getInstance().dowloadTask(SocialNetworkHandler.getInstance().getPhotoUrlOk().get(i));
                        }
                        Log.d("subList", result.size() + " - result after sort");

                        if (pictureData.isDoubleRequest()){

                        }

                    }



            }
        });
        scrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener()
        {

            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy)
            {
                Log.i("count", y + " - new coor");
                Log.i("count", oldy + " - old coor");
                // viewWithImage;  // <-- количество элементов c фото
                Log.i("count", viewWithImage + " viewWithImage in firs time");
                picturesToLoad = new int[]{-1, -1, -1};
                int offset = ((y + 1536) / 512) - 3;
                Log.i("count", offset + " - offset");

                for (int i = offset; i < offset + 3; i++)
                {
                    SocialRowPhotoFragment socialRowPhotoFragment = (SocialRowPhotoFragment) getFragmentManager().findFragmentByTag("" + i);
                    Log.i("socialRowPhotoFragment", i + " - tag for social row fragment");
                    if (offset != -1){
                        if (!socialRowPhotoFragment.isPicturesSet())
                        {
                            picturesToLoad[i - offset] = i * 2;
                        }
                    }

                }

            }

        });
    }

    int[] picturesToLoad = {-1, -1, -1};

    class PictureData
    {
        private int offset;
        private int count;
        private boolean isDoubleRequest;
        private boolean isNeedRequest = true;

        public PictureData(int[] picturesToLoad)
        {
            if (picturesToLoad[0] != -1)
            {
                offset = picturesToLoad[0];
                count += 2;
                if (picturesToLoad[1] != -1)
                {
                    count += 2;
                } else
                {
                    isDoubleRequest = true;
                    return;
                }

                if (picturesToLoad[2] != -1)
                {
                    count += 2;
                }
            } else
            {
                if (picturesToLoad[1] != -1)
                {
                    offset = picturesToLoad[1];
                    count += 2;
                } else
                {
                    if (picturesToLoad[2] != -1)
                    {
                        offset = picturesToLoad[2];
                        count += 2;
                        return;
                    }
                    isNeedRequest = false;
                    return;
                }

                if (picturesToLoad[2] != -1)
                {
                    count += 2;
                }
            }
        }

        public String getOffset()
        {
            return "" + offset;
        }

        public int getCountInt()
        {
            return count;
        }

        public int getOffsetInt()
        {
            return offset;
        }

        public String getCount()
        {
            return "" + count;
        }

        public boolean isDoubleRequest()
        {
            return isDoubleRequest;
        }

        public boolean isNeedRequest()
        {
            return isNeedRequest;
        }
    }


    private void configureToolbar()
    {
        LinearLayout toolbarContainer = (LinearLayout) toolbar.findViewById(com.menemi.R.id.toolbarContainer);
        toolbarContainer.removeAllViews();
        toolbarContainer.addView(View.inflate(this, com.menemi.R.layout.ab_in_phot_choiser, null));
        ImageView menuButton = (ImageView) toolbarContainer.findViewById(com.menemi.R.id.menuButton);
        TextView title = (TextView) toolbarContainer.findViewById(com.menemi.R.id.screenTitle);
        title.setText(getString(com.menemi.R.string.interests_conf));
        menuButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        ImageButton editButton = (ImageButton) toolbarContainer.findViewById(com.menemi.R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getFragmentManager().popBackStack();
            }
        });
    }

    public SocialRowPhotoFragment editView(Bitmap left, Bitmap right, SocialRowPhotoFragment socialRowPhotoFragment)
    {
        Log.d("add_photo", "method left is called");
        socialRowPhotoFragment.setPictureLeft(left);
        Log.d("add_photo", "method right is called");
        socialRowPhotoFragment.setPictureRight(right);

        return socialRowPhotoFragment;
    }
    public<T> List<T> safeSubList(List<T> list, int fromIndex, int toIndex) {
        int size = list.size();
        if (fromIndex >= size || toIndex <= 0 || fromIndex >= toIndex) {
            return Collections.emptyList();
        }

        fromIndex = Math.max(0, fromIndex);
        toIndex = Math.min(size, toIndex);

        return list.subList(fromIndex, toIndex);
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        count = 0;
    }
}

*/
