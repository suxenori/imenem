package com.menemi.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;


import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.menemi.PersonPage;
import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.DialogInfo;
import com.menemi.personobject.PersonObject;

/**
 * Created by irondev on 23.06.16.
 */
public class PersonDataFragment extends Fragment {
    private View rootView = null;
    private static final String TAG = "scroll_tag";
    private PersonObject personObject = null;
    private Purpose purpose = Purpose.LIKE;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.person_data_fragment, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
                parent.removeAllViews();
            }
        }
        DBHandler.getInstance().isRESTAvailable(object -> {
                if((boolean) object == true) {
                    if (purpose == Purpose.LIKE) {
                        generateNextRandomPerson(listener);
                    } else {
                        listener.onPrepare(personObject);
                    }
                } else {
                    if (purpose == Purpose.LIKE) {
                        LostInternetFragment lostInternetFragment = new LostInternetFragment();
                        lostInternetFragment.setOnRetryListener(()->{

                            generateNextRandomPerson(listener);
                            getFragmentManager().popBackStack();
                        });
                        getFragmentManager().beginTransaction().replace(com.menemi.R.id.content, lostInternetFragment).addToBackStack(null).commitAllowingStateLoss();
                    } else if(personObject == null){
                        LostInternetFragment lostInternetFragment = new LostInternetFragment();
                        lostInternetFragment.setOnRetryListener(()->{

                        });
                        getFragmentManager().beginTransaction().replace(com.menemi.R.id.content, lostInternetFragment).addToBackStack(null).commitAllowingStateLoss();
                    }
                }
            }
            );



        return rootView;
    }

    OnPersonPrepared listener = new OnPersonPrepared() {

        @Override
        public void onPrepare(final PersonObject preparedObject) {
            personObject = preparedObject;
try{
            if (getActivity() == null || getFragmentManager() == null || personObject == null) {
                return;
            }
            personObject.prepaparePictureUrls(()->{

                if (getActivity() == null || getFragmentManager() == null || !isVisible()) {
                    return;
                }
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            final EncountersFragment encountersFragment = new EncountersFragment();
            encountersFragment.setPurpose(purpose);
            encountersFragment.setPerson(preparedObject);
            fragmentTransaction.replace(R.id.fragment1, encountersFragment);
            Log.v("onClick", "FILTER");
            configureToolbar();
            if (getActivity() == null || getFragmentManager() == null || !isVisible()) {
                return;
            }

            final FragmentWithInfoAboutPerson fragmentWithInfoAboutPerson = new FragmentWithInfoAboutPerson();
            fragmentWithInfoAboutPerson.setPuropose(purpose);
            fragmentTransaction.replace(R.id.fragment2, fragmentWithInfoAboutPerson);
            fragmentWithInfoAboutPerson.setPersonObject(preparedObject);
            fragmentTransaction.commitAllowingStateLoss();

            LinearLayout disLikeContainer = (LinearLayout) rootView.findViewById(R.id.disLikeContainer);
            final ImageButton likeButton = (ImageButton) rootView.findViewById(R.id.buttonLike);
            final ImageButton dislikeButton = (ImageButton) rootView.findViewById(R.id.buttonDisLike);
            if (purpose == Purpose.MY_PROFILE) {
                disLikeContainer.removeView(likeButton);
                disLikeContainer.removeView(dislikeButton);
            } else if (purpose == Purpose.PROFILE) {

                    likeButton.setOnClickListener(new LikeClickListener(preparedObject));
                    dislikeButton.setImageResource(R.drawable.message);
                    dislikeButton.setOnClickListener(new OpenDialogListener(preparedObject));


            } else {
                likeButton.setOnClickListener(new LikeClickListener(preparedObject));
                dislikeButton.setOnClickListener(new DislikeClickListener(preparedObject));
            }
            });
}catch (IllegalArgumentException e){
    Log.e("Fragment error", "activity is already closed");
}
        }
    };

    private void configureToolbar() {
        Toolbar toolbar = PersonPage.getToolbar();
        LinearLayout toolbarContainer = (LinearLayout) toolbar.findViewById(R.id.toolbarContainer);
        toolbarContainer.removeAllViews();
        toolbarContainer.addView(View.inflate(getActivity(), R.layout.ab_person_data, null));
        ImageView menuButton = (ImageView) toolbarContainer.findViewById(R.id.menuButton);
        menuButton.setOnClickListener(PersonPage.getMenuListener());

        ImageView filterButton = (ImageView) toolbarContainer.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(PersonPage.getFilterButtonListener(getFragmentManager()));

        TextView nameAgeText = (TextView) toolbarContainer.findViewById(R.id.nameAgeText);
        if (nameAgeText != null) {
            if (personObject != null) {
                nameAgeText.setText(personObject.getPersonName() + ", " + personObject.getPersonAge());
            } else {
                nameAgeText.setText("...");
            }
        }
    }

    private void pressButton(Action action) {
        if (action == Action.BUTTON_LIKE) {

        }
        if (action == Action.BUTTON_DISLIKE) {
            //TODO: OPEN CHAT
        }

        if (purpose == Purpose.LIKE) {
            if (getActivity() == null || getFragmentManager() == null) {
                return;
            }
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(this);
            PersonDataFragment personDataFragment = new PersonDataFragment();
            personDataFragment.setPurpose(Purpose.LIKE);
            ft.replace(R.id.content, personDataFragment);
            ft.commitAllowingStateLoss();
        }
    }


    public void setPersonObject(PersonObject personObject) {
        this.personObject = personObject;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }


    private void generateNextRandomPerson(final OnPersonPrepared onPersonPrepared) {

        DBHandler.getInstance().getNextRandomProfile(new DBHandler.ResultListener() {
            @Override
            public void onFinish(Object object) {
                final PersonObject randomPerson = (PersonObject) object;
                if (randomPerson == null) {
                    onPersonPrepared.onPrepare(null);
                    return;
                }
                DBHandler.getInstance().getOtherProfile(randomPerson.getPersonId(), new DBHandler.ResultListener() {
                    @Override
                    public void onFinish(Object object) {
                        onPersonPrepared.onPrepare((PersonObject) object);
                       // Log.d("LOG SYKA", "blya " + ((PersonObject) object).getPhotoCount());
                    }
                });


            }
        });
    }

    public enum Purpose {
        LIKE,
        PROFILE,
        MY_PROFILE
    }

    class LikeClickListener implements View.OnClickListener {
        private PersonObject personObject = null;
        boolean isLiked = false;

        public LikeClickListener(PersonObject personObject) {
            this.personObject = personObject;
        }

        @Override
        public void onClick(View v) {
            isLiked = !isLiked;
            final ImageButton likeButton = (ImageButton) rootView.findViewById(R.id.buttonLike);
            likeButton.setImageResource(R.drawable.ic_voted_liked);
            DBHandler.getInstance().disLike(personObject.getPersonId(), isLiked, new DBHandler.ResultListener() {
                @Override
                public void onFinish(Object object) {
                    final ImageButton likeButton = (ImageButton) rootView.findViewById(R.id.buttonLike);
                    final ImageButton dislikeButton = (ImageButton) rootView.findViewById(R.id.buttonDisLike);
                    if (isLiked) {
                        likeButton.setImageResource(R.drawable.ic_vote_hot_white);
                    } else {
                        likeButton.setImageResource(R.drawable.ic_voted_liked);
                    }
                    pressButton(Action.BUTTON_LIKE);

                }
            });
        }
    }

    class DislikeClickListener implements View.OnClickListener {
        private PersonObject personObject = null;

        public DislikeClickListener(PersonObject personObject) {
            this.personObject = personObject;
        }

        @Override
        public void onClick(View view) {
            DBHandler.getInstance().disLike(personObject.getPersonId(), false, new DBHandler.ResultListener() {
                @Override
                public void onFinish(Object object) {
                    final ImageButton likeButton = (ImageButton) rootView.findViewById(R.id.buttonLike);
                    likeButton.setImageResource(R.drawable.ic_vote_hot_white);
                    pressButton(Action.BUTTON_DISLIKE);


                }
            });
        }
    }

    class OpenDialogListener implements View.OnClickListener {
        private PersonObject contactObject;

        public OpenDialogListener(PersonObject preparedObject) {
            this.contactObject = preparedObject;
        }

        @Override
        public void onClick(View view) {
            if(personObject.canChat()){
                final DialogInfo dialogInfo = new DialogInfo();
                dialogInfo.setProfileId(contactObject.getPersonId());
                dialogInfo.setContactName(contactObject.getPersonName());
                dialogInfo.setOnline(contactObject.isOnline());

                DBHandler.getInstance().getDialog(dialogInfo, new DBHandler.ResultListener() {
                    @Override
                    public void onFinish(Object object) {
                        ChatFragment chatFragment = new ChatFragment();
                        chatFragment.setDialogInfo(dialogInfo);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content, chatFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                });
            } else {

            }


        }
    }

    interface OnPersonPrepared {
        void onPrepare(PersonObject preparedObject);
    }

    enum Action {
        BUTTON_LIKE,
        BUTTON_DISLIKE
    }

}
