package com.menemi.interests_classes;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.Interests;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tester03 on 02.08.2016.
 */
public class InterestsList extends Fragment
{
    private ArrayList<ItemSlideMenu> listSliding = new ArrayList<>();
    private ArrayList<Interests> personInterestsArray;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.interests_list_fragment,container,false);
        final LinearLayout interestsContainer = (LinearLayout)rootView.findViewById(R.id.interestsContainer);
        final EditText searchField = (EditText) rootView.findViewById(R.id.search_field);
        configureToolbar();
        if (listSliding.size() != 0){
            listSliding.clear();
        }
        for (int i = 0; i < DBHandler.getInstance().interestsGroupArray.size() ; i++)
        {
            listSliding.add(new ItemSlideMenu(DBHandler.getInstance().interestsGroupArray.get(i).getCategoryIcon()
                    ,DBHandler.getInstance().interestsGroupArray.get(i).getNameGroup()));
        }
        setList(interestsContainer,listSliding);
        searchField.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }
            Timer timer = new Timer();
            @Override
            public void onTextChanged(final CharSequence s, int i, int i1, int i2)
            {
                if(s.length() != 0){
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(new TimerTask()
                    {
                        @Override
                        public void run()
                        {
                            Log.d("result_fom_tf", searchField.getText().toString());

                            DBHandler.getInstance().findInterests(URLEncoder.encode(searchField.getText().toString()), DBHandler.getInstance().getUserId(), new DBHandler.ResultListener()
                            {
                                @Override
                                public void onFinish(Object object)
                                {
                                    ArrayList<Interests> interestsArray = (ArrayList) object;
                                    SearchResultDialog resultDialog = new SearchResultDialog();
                                    if (interestsArray.size() != 0){
                                        resultDialog.setInterestsArray(interestsArray);
                                        resultDialog.show(getFragmentManager(),"f");
                                    } else {
                                        resultDialog.setInterestsFromTextField(searchField.getText().toString());
                                        resultDialog.show(getFragmentManager(),"f");
                                    }
                                }
                            });
                        }
                    },500);
                }
            }
            @Override
            public void afterTextChanged(Editable editable)
            {
                if (editable.length() == 0){
                    timer.cancel();
                }
            }
        });
        return rootView;
    }
    private void setList(LinearLayout container, ArrayList<ItemSlideMenu> items){

    for (int i = 0; i < items.size(); i++) {
        container.addView(items.get(i).prepareItem());
        items.get(i).setId(i);
    }
}
    private void replaceFragment(int pos){
        ChoiseInterestsFragment choiseInterestsFragment = new ChoiseInterestsFragment();
        choiseInterestsFragment.setPersonInterestsArray(personInterestsArray);
        choiseInterestsFragment.setInterestGroup(DBHandler.getInstance().interestsGroupArray.get(pos));
        openFragment(choiseInterestsFragment);
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
        private Bitmap imgId;
        private String name;

        public void setId(int id)
        {
            this.id = id;
        }

        public ItemSlideMenu(Bitmap imgId, String name)
        {
            this.imgId = imgId;
            this.name = name;
        }

        private View prepareItem()
        {
            View view = View.inflate(getActivity(), R.layout.interests_item_list, null);
            ImageView img = (ImageView) view.findViewById(R.id.img_id);
            TextView textView = (TextView) view.findViewById(R.id.item_title);
            img.setImageBitmap(imgId);
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
    private void configureToolbar() {
        Toolbar toolbar = InterestContainer.getToolbar();
        LinearLayout toolbarContainer = (LinearLayout) toolbar.findViewById(R.id.toolbarContainer);
        toolbarContainer.removeAllViews();
        toolbarContainer.addView(View.inflate(getActivity(),R.layout.ab_buy_coins,null));

        ImageView menuButton = (ImageView) toolbarContainer.findViewById(R.id.menuButton);
        menuButton.setOnClickListener(view -> getFragmentManager().popBackStack());

        TextView title = (TextView) toolbarContainer.findViewById(R.id.screenTitle);
        title.setText(getString(R.string.set_interests));

        /*TextView nameAgeText = (TextView) toolbarContainer.findViewById(R.id.nameAgeText);
        nameAgeText.setText(personObject.getPersonName() +", " + personObject.getPersonAge());*/
    }
    public void setPersonInterestsArray(ArrayList<Interests> personInterestsArray)
    {
        this.personInterestsArray = personInterestsArray;
    }

}
