package com.menemi.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.menemi.dbfactory.DBHandler;
import com.menemi.dbfactory.rest.PictureLoader;
import com.menemi.personobject.PersonObject;
import com.menemi.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ui-Developer on 01.07.2016.
 */
public class FriendsFragment extends Fragment {
    private View rootView = null;
    HashMap<Integer, String> friendsPhotosList = null;
    ArrayList<Integer> friends = null;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.friends_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }

        }



        DBHandler.getInstance().getMultiplePeoplePictures(Utils.PICTURE_QUALITY_THUMBNAIL, friends, (Object object) ->{
                friendsPhotosList = (HashMap<Integer, String>) object;
                Log.d("FriendsFragment", "friendsPhotosList " + friendsPhotosList);
                LinearLayout friendsList = (LinearLayout) rootView.findViewById(R.id.friendsScroll);

                if (friendsPhotosList != null) {
                    Log.d("FriendsFragment", "friendsPhotosList " + friendsPhotosList.size());
                    for (final Map.Entry<Integer, String> entry : friendsPhotosList.entrySet()) {
                        View item = inflater.inflate(R.layout.friend_image, friendsList, false);
                        ImageView image = (ImageView) item.findViewById(R.id.friendPhoto);


                        if (entry.getValue() != null) {
                            new PictureLoader(entry.getValue(), (Object bitmap)->{
                                if((Bitmap) bitmap != null) {
                                    Bitmap photo = Utils.scaleBitmapToMin((Bitmap) bitmap);
                                    photo = Utils.getCroppedBitmap(photo);
                                    image.setImageBitmap(photo);
                                } else {
                                    if(getActivity() == null){
                                        return;
                                    }
                                    Bitmap emptyPhoto = BitmapFactory.decodeResource(getActivity().getResources(),
                                            R.drawable.empty_photo);
                                    image.setImageBitmap(Utils.getCroppedBitmap(emptyPhoto));
                                }
                            });

                        }
                        else {
                            if(getActivity() != null) {
                                Bitmap emptyPhoto = BitmapFactory.decodeResource(getActivity().getResources(),
                                        R.drawable.empty_photo);
                                image.setImageBitmap(Utils.getCroppedBitmap(emptyPhoto));
                            }
                        }

                            friendsList.addView(item);
                            item.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DBHandler.getInstance().getOtherProfile(entry.getKey(), new DBHandler.ResultListener() {
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
                            });

                    }

                    TextView friendsCountText = (TextView) rootView.findViewById(R.id.friendsCountText);
                    String ending = "";

                    if (getActivity() != null) {
                        if (friendsPhotosList.size() == 1) {
                            ending = getString(R.string.friendsCountEnd1);
                        } else if (friendsPhotosList.size() < 5) {
                            ending = getString(R.string.friendsCountEnd2_3_4);
                        } else {
                            ending = getString(R.string.friendsCountEnd5_);
                        }
                        friendsCountText.setText(friendsPhotosList.size() + " " + ending);
                    }
                }

        });


        return rootView;
    }


    public void setFriends(ArrayList<Integer> friends) {
        this.friends = friends;
    }
}
