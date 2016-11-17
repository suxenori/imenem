package com.menemi.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.menemi.PersonPage;
import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.DialogInfo;
import com.menemi.personobject.PersonObject;
import com.menemi.utils.Utils;

/**
 * Created by irondev on 23.06.16.
 */
public class PersonDataFragment extends Fragment {
    private View rootView = null;
    private static final String TAG = "scroll_tag";
    private PersonObject personObject = null;
    private PersonObject personPrevious = null;
    private Purpose purpose = Purpose.LIKE;

    public void setPersonPrevious(PersonObject personPrevious) {
        this.personPrevious = personPrevious;
    }

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
        if(purpose == PersonDataFragment.Purpose.PROFILE && personObject.getPersonId() == DBHandler.getInstance().getUserId()){
          purpose = PersonDataFragment.Purpose.MY_PROFILE;
        }

        DBHandler.getInstance().isRESTAvailable(object -> {
                    if ((boolean) object == true) {

                        if (purpose == Purpose.LIKE) {
                            if(personPrevious != null){
                                ImageView buttonShowPreviousUser = (ImageView ) rootView.findViewById(R.id.buttonShowPreviousUser);
                                buttonShowPreviousUser.setImageResource(R.drawable.profile_back);
                                buttonShowPreviousUser.setOnClickListener(new OpenPreviousPerson(personPrevious));

                            }
                            if(personObject != null){
                                listener.onPrepare(personObject);
                            } else {
                                generateNextRandomPerson(listener);
                            }
                        } else {
                            listener.onPrepare(personObject);
                        }
                    } else {
                        if (purpose == Purpose.LIKE) {
                            LostInternetFragment lostInternetFragment = new LostInternetFragment();
                            lostInternetFragment.setOnRetryListener(() -> {

                                generateNextRandomPerson(listener);
                                getFragmentManager().popBackStack();
                            });
                            getFragmentManager().beginTransaction().replace(com.menemi.R.id.content, lostInternetFragment).addToBackStack(null).commitAllowingStateLoss();
                        } else {
                            LostInternetFragment lostInternetFragment = new LostInternetFragment();
                            lostInternetFragment.setOnRetryListener(() -> {
                                if (purpose == Purpose.MY_PROFILE) {
                                    DBHandler.getInstance().authorise(personObject, (Object obj) -> {
                                        listener.onPrepare((PersonObject) obj);
                                    });

                                } else {
                                    DBHandler.getInstance().getOtherProfile(personObject.getPersonId(), (Object obj) -> {
                                        listener.onPrepare((PersonObject) obj);
                                    });
                                }
                                if (getFragmentManager() != null) {
                                    getFragmentManager().popBackStack();
                                }
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
            try {
                if (getActivity() == null || getFragmentManager() == null || personObject == null) {
                    return;
                }
                personObject.prepaparePictureUrls(() -> {

                    if (getActivity() == null || getFragmentManager() == null || !isVisible()) {
                        return;
                    }
                        TextView locationText = (TextView) rootView.findViewById(R.id.locationText);
                    locationText.setText(personObject.getPersonCurrLocation());
                    if (purpose == Purpose.MY_PROFILE) {
                        Button addAvatarButton = (Button) rootView.findViewById(R.id.addAvatarButton);
                        addAvatarButton.setBackgroundResource(R.drawable.add_avatar);
                        addAvatarButton.setOnClickListener(new AddAvatarClickListener());
                    } else {

                            String units;
                            double distance;
                            if(Utils.getUnits() == Utils.UNITS.IMPERIAL){
                                distance = personObject.getDistance();
                                units = getString(R.string.units_miles);
                            } else {
                                units = getString(R.string.units_killometers);
                                distance = personObject.getDistance() * 1.60934d;
                            }
                        ImageView mapPlacIcon = (ImageView) rootView.findViewById(R.id.mapPlacIcon);
                        mapPlacIcon.setImageResource(R.drawable.map_place);
                        TextView range = (TextView) rootView.findViewById(R.id.range);
                            range.setText(Utils.prepareShortUnts(distance) + " " + units);

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

                        configureLikePicture(likeButton);
                        likeButton.setOnClickListener(new LikeClickListener(preparedObject));
                        dislikeButton.setImageResource(R.drawable.message);
                        dislikeButton.setOnClickListener(new OpenDialogListener(preparedObject));


                    } else {
                        configureLikePicture(likeButton);
                        dislikeButton.setImageResource(R.drawable.button_dislike);
                        likeButton.setOnClickListener(new LikeClickListener(preparedObject));
                        dislikeButton.setOnClickListener(new DislikeClickListener(preparedObject));
                    }
                });
            } catch (IllegalArgumentException e) {
                Log.e("Fragment error", "activity is already closed");
            }
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

        }
    };

    private void configureLikePicture(ImageButton likeButton) {
        if (personObject.getLikeStatus() == PersonObject.LikeStatus.none) {
            likeButton.setImageResource(R.drawable.button_like);
        } else if (personObject.getLikeStatus() == PersonObject.LikeStatus.liked_me) {
            likeButton.setImageResource(R.drawable.button_like_mutual);
        } else if (personObject.getLikeStatus() == PersonObject.LikeStatus.like_him) {
            likeButton.setImageResource(R.drawable.button_like_unclick);
        }
        if (personObject.getLikeStatus() == PersonObject.LikeStatus.mutual_like) {
            likeButton.setImageResource(R.drawable.button_like_mutual_unclick);
        }

    }


    private void configureToolbar() {
        Toolbar toolbar = PersonPage.getToolbar();
        LinearLayout toolbarContainer = (LinearLayout) toolbar.findViewById(R.id.toolbarContainer);
        toolbarContainer.removeAllViews();
        toolbarContainer.addView(View.inflate(getActivity(), R.layout.ab_person_data, null));
        ImageView menuButton = (ImageView) toolbarContainer.findViewById(R.id.menuButton);
        menuButton.setOnClickListener(PersonPage.getMenuListener());

        ImageView filterButton = (ImageView) toolbarContainer.findViewById(R.id.filterButton);
        FilterFragment.FilterType filterType;
        if(purpose == Purpose.LIKE){
            filterType = FilterFragment.FilterType.FILTER_FROM_ENCOUNTERS;
        } else
        {
            filterType = FilterFragment.FilterType.FILTER_FROM_NEAR;
        }

        filterButton.setOnClickListener(PersonPage.getFilterButtonListener(getFragmentManager(), filterType));

        TextView nameAgeText = (TextView) toolbarContainer.findViewById(R.id.nameAgeText);
        if (nameAgeText != null) {
            if (personObject != null) {
                nameAgeText.setText(personObject.getPersonName() + ", " + personObject.getPersonAge());
            } else {
                nameAgeText.setText("...");
            }
        }
    }

    private void getNextPerson() {

        if (purpose == Purpose.LIKE) {
            if (getActivity() == null || getFragmentManager() == null) {
                return;
            }


            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(this);
            PersonDataFragment personDataFragment = new PersonDataFragment();
            personDataFragment.setPurpose(Purpose.LIKE);
            personDataFragment.setPersonPrevious(personObject);
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


        public LikeClickListener(PersonObject personObject) {
            this.personObject = personObject;
        }

        @Override
        public void onClick(View v) {

            final ImageButton likeButton = (ImageButton) rootView.findViewById(R.id.buttonLike);
            likeButton.setImageResource(R.drawable.like_presed);
            boolean isLiked;
            if (personObject.getLikeStatus() == PersonObject.LikeStatus.none || personObject.getLikeStatus() == PersonObject.LikeStatus.liked_me) {
                isLiked = true;
            } else {
                isLiked = false;
            }
            if (personObject.getLikeStatus() == PersonObject.LikeStatus.none) {
                personObject.setLikeStatus(PersonObject.LikeStatus.like_him);
            } else if (personObject.getLikeStatus() == PersonObject.LikeStatus.like_him) {
                personObject.setLikeStatus(PersonObject.LikeStatus.none);
            } else if (personObject.getLikeStatus() == PersonObject.LikeStatus.liked_me) {
                personObject.setLikeStatus(PersonObject.LikeStatus.mutual_like);
                configureLikePicture(likeButton);
                MutualLikeFragment mutualLikeFragment = new MutualLikeFragment();
                mutualLikeFragment.setLikedPerson(personObject);
                mutualLikeFragment.setOnCancel(()->{if (purpose == Purpose.LIKE) {getNextPerson();}});
                getFragmentManager().beginTransaction().replace(R.id.content, mutualLikeFragment).addToBackStack(null).commitAllowingStateLoss();

                DBHandler.getInstance().disLike(personObject.getPersonId(), isLiked, (Object object)-> {});
                return;
            } else if (personObject.getLikeStatus() == PersonObject.LikeStatus.mutual_like) {
                personObject.setLikeStatus(PersonObject.LikeStatus.liked_me);
            }
            configureLikePicture(likeButton);
            DBHandler.getInstance().disLike(personObject.getPersonId(), isLiked, new DBHandler.ResultListener() {
                @Override
                public void onFinish(Object object) {
                    if (purpose == Purpose.LIKE) {
                        getNextPerson();
                    }
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
            final ImageButton likeButton = (ImageButton) rootView.findViewById(R.id.buttonLike);
            if (personObject.getLikeStatus() == PersonObject.LikeStatus.like_him) {
                personObject.setLikeStatus(PersonObject.LikeStatus.none);
            } else if (personObject.getLikeStatus() == PersonObject.LikeStatus.mutual_like) {
                personObject.setLikeStatus(PersonObject.LikeStatus.liked_me);
            }
            configureLikePicture(likeButton);
            DBHandler.getInstance().disLike(personObject.getPersonId(), false, new DBHandler.ResultListener() {
                @Override
                public void onFinish(Object object) {
                    if (purpose == Purpose.LIKE)
                        getNextPerson();
                }
            });
        }
    }
    class OpenPreviousPerson implements View.OnClickListener {
        private PersonObject personObject = null;


        public OpenPreviousPerson(PersonObject personObject) {
            this.personObject = personObject;
        }

        @Override
        public void onClick(View v) {
            if (getActivity() == null || getFragmentManager() == null) {
                return;
            }


            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(PersonDataFragment.this);
            PersonDataFragment personDataFragment = new PersonDataFragment();
            personDataFragment.setPurpose(Purpose.LIKE);
            personDataFragment.setPersonObject(personObject);
            ft.replace(R.id.content, personDataFragment);
            ft.commitAllowingStateLoss();

        }
    }

    class OpenDialogListener implements View.OnClickListener {
        private PersonObject contactObject;

        public OpenDialogListener(PersonObject preparedObject) {
            this.contactObject = preparedObject;
        }

        @Override
        public void onClick(View view) {
            if (personObject.canChat()) {
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

    private class AddAvatarClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (getActivity() == null || getFragmentManager() == null) {
                return;
            }
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            PublicPhotoListFragment publicPhotoListFragment = new PublicPhotoListFragment();
            publicPhotoListFragment.setPurpose(PublicPhotoListFragment.Purpose.CHOOSE_AVATAR);
            ft.addToBackStack(null);
            ft.replace(R.id.content, publicPhotoListFragment);
            ft.commitAllowingStateLoss();
        }
    }
}
