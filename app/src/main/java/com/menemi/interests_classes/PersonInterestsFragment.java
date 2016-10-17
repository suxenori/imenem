package com.menemi.interests_classes;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.Interests;

import java.util.ArrayList;

/**
 * Created by tester03 on 02.08.2016.
 */
public class PersonInterestsFragment extends Fragment
{
    private ArrayList<ItemSlideMenu> listSliding = new ArrayList<>();
    private View rootView;
    private EditInterestsFragment editInterestsFragment = new EditInterestsFragment();
    public ArrayList<Interests> personInterestsArray  = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.person_interest, container, false);
        final LinearLayout interestsContainer = (LinearLayout) rootView.findViewById(R.id.interestsContainer);
        personInterestsArray = DBHandler.getInstance().getProfileInterests();
        configureToolbar();
        SearchResultDialog.setChoiseListener(new SearchResultDialog.ChoiceListener()
        {
            @Override
            public void addCustomInterest(final Interests interests)
            {
                DBHandler.getInstance().getProfileInterests().add(interests);
                Log.d("interstsId", DBHandler.getInstance().getProfileInterests().size() + " - size");
                ArrayList<Integer> customInterestIdArray = new ArrayList<Integer>();
                customInterestIdArray.add(interests.getInterestId());
                // add interests to person object
                DBHandler.getInstance().getMyProfile().setInterests(DBHandler.getInstance().getProfileInterests());
                //send interests to REST
                DBHandler.getInstance().addInterests(DBHandler.getInstance().getUserId(),customInterestIdArray, new DBHandler.ResultListener()
                {
                    @Override
                    public void onFinish(Object object)
                    {

                    }
                });
            }
        });
        if (listSliding.size() != 0){
           listSliding.clear();
        }
        listSliding.add(new ItemSlideMenu(BitmapFactory.decodeResource(getResources(),R.drawable.add_int), "Добавить интерес"));

        editInterestsFragment.setPersonInterestsArray(personInterestsArray);
        for (int i = 0; i < personInterestsArray.size(); i++)
        {
            listSliding.add(new ItemSlideMenu((DBHandler.getInstance()
                    .getInterestsGroupArray().get(personInterestsArray.get(i).getGroupId() - 1).getCategoryIcon()), personInterestsArray.get(i).getInterest()));

        }

        setList(interestsContainer,listSliding);

        return rootView;
    }
    private void setList(LinearLayout container, ArrayList<ItemSlideMenu> items){

        for (int i = 0; i < items.size(); i++) {
            container.addView(items.get(i).prepareItem());
            items.get(i).setId(i);
        }

    }
    private void replaceFragment(int pos){
        if (pos == 0){
            InterestsList interestsList = new InterestsList();
            interestsList.setPersonInterestsArray(personInterestsArray);
            openFragment(interestsList);

        }

    }
    private void openFragment(Fragment fragment)
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.interest_empty, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }
    private class ItemSlideMenu implements View.OnClickListener
    {
        int id = 0;
        private String name;
        private Bitmap icon;

        public void setId(int id)
        {
            this.id = id;
        }

        public ItemSlideMenu(Bitmap bitmap, String name)
        {
            this.icon = bitmap;
            this.name = name;
        }
        private View prepareItem()
        {
            View view = View.inflate(getActivity(), R.layout.interests_item_list, null);
            ImageView img = (ImageView) view.findViewById(R.id.img_id);
            TextView textView = (TextView) view.findViewById(R.id.item_title);
            img.setImageBitmap(icon);
            textView.setText(name);
            view.setOnClickListener(this);
            return view;
        }


        @Override
        public void onClick(View view)
        {
            replaceFragment(id);

        }
    }

    private static ChangeInterestListener choiseListener = new ChangeInterestListener()
    {
        @Override
        public void changeInterests(ArrayList<Interests> arrayList)
        {
            Log.d("ChoiseListener", "ChoiseListener is called but not set");
        }
    };

    public static void setChoiceListener(ChangeInterestListener changeInterestListener)
    {
        PersonInterestsFragment.choiseListener = changeInterestListener;
    }

    public interface ChangeInterestListener
    {
        void changeInterests(ArrayList<Interests> arrayList);
    }
    private void configureToolbar() {

        Toolbar toolbar = InterestContainer.getToolbar();
        LinearLayout toolbarContainer = (LinearLayout) toolbar.findViewById(R.id.toolbarContainer);
        toolbarContainer.removeAllViews();
        toolbarContainer.addView(View.inflate(getActivity(),R.layout.ab_in_add_interests,null));
        ImageView menuButton = (ImageView) toolbarContainer.findViewById(R.id.menuButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                getFragmentManager().popBackStack();
            }
        });
        ImageButton editButton = (ImageButton)toolbarContainer.findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openFragment(editInterestsFragment);

            }
        });
        TextView title = (TextView) toolbarContainer.findViewById(R.id.screenTitle);
        title.setText(getString(R.string.interests_conf));
    }

    @Override
    public void onDetach()
    {
        choiseListener.changeInterests(personInterestsArray);
        super.onDetach();
    }
}
