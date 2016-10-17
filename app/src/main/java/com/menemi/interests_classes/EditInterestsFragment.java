package com.menemi.interests_classes;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.menemi.R;
import com.menemi.dbfactory.DBHandler;
import com.menemi.personobject.Interests;

import java.util.ArrayList;

/**
 * Created by tester03 on 04.08.2016.
 */
public class EditInterestsFragment extends Fragment
{
    private RowModel[] rowInterestModels;
    private ArrayList<Interests> personInterestsArray;
    private Button removeInterestsButton;
    private ArrayList<Interests> checkedItemsList = new ArrayList<>();
    private TextView title;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.edit_interests_fragment,container,false);
        final ListView personInterestsList = (ListView) rootView.findViewById(R.id.editListInterests);
        removeInterestsButton = (Button)rootView.findViewById(R.id.deleteInterestsButton);
        removeInterestsButton.setBackgroundResource(android.R.drawable.btn_default);
        configureToolbar();
        rowInterestModels = new RowModel[personInterestsArray.size()];

        for (int i = 0; i < personInterestsArray.size() ; i++)
        {
            rowInterestModels[i] = new RowModel(personInterestsArray.get(i).getInterest(),personInterestsArray.get(i).getInterestId());
        }
        final CustomAdapter adapter = new CustomAdapter(getActivity().getApplicationContext(), rowInterestModels);
        personInterestsList.setAdapter(adapter);
        if (!checkedItemsList.isEmpty()){
            checkedItemsList.clear();
        }
        personInterestsList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                    CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkBox1);
                if (checkBox.isChecked()){
                    checkBox.setChecked(false);
                    view.setBackgroundColor(getResources().getColor(R.color.activity_back));
                    removeCheckItem(personInterestsArray.get(position));
                } else {
                    checkBox.setChecked(true);
                    view.setBackgroundColor(getResources().getColor(R.color.blue));
                    checkedItemsList.add(personInterestsArray.get(position));
                }
                if (checkedItemsList.size() != 0){

                    removeInterestsButton.setBackgroundColor(getResources().getColor(R.color.red));
                    removeInterestsButton.setClickable(true);
                } else {
                    removeInterestsButton.setBackgroundResource(android.R.drawable.btn_default);
                    removeInterestsButton.setClickable(false);
                }
                choicedInterestCounter(checkedItemsList);
            }
        });

        removeInterestsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


                DBHandler.getInstance().deleteInterest(DBHandler.getInstance().getUserId(), getInterestsId(checkedItemsList), new DBHandler.ResultListener()
                {
                    @Override
                    public void onFinish(Object object)
                    {
                        DBHandler.getInstance().setProfileInterests(checkArray(DBHandler.getInstance().getProfileInterests(),checkedItemsList));
                        Toast.makeText(getActivity().getApplicationContext(),"Выбранные интересы успешно удалены",Toast.LENGTH_SHORT).show();
                        getFragmentManager().popBackStack();

                    }
                });


            }
        });
        removeInterestsButton.setClickable(false);
        return rootView;
    }
    private void removeCheckItem(Interests interests){
    for (int i = 0; i < checkedItemsList.size(); i++)
    {
        if(checkedItemsList.get(i) == interests){
            checkedItemsList.remove(i);
        }
      }
    }

    private ArrayList<Integer> getInterestsId(ArrayList<Interests> checkedItemsList){
        ArrayList<Integer> selectedInterests = new ArrayList<>();
        for (int i = 0; i < checkedItemsList.size(); i++)
        {
            selectedInterests.add(checkedItemsList.get(i).getInterestId());
        }
        return selectedInterests;
    }
    private void configureToolbar() {
        Toolbar toolbar = InterestContainer.getToolbar();
        LinearLayout toolbarContainer = (LinearLayout) toolbar.findViewById(R.id.toolbarContainer);
        toolbarContainer.removeAllViews();
        toolbarContainer.addView(View.inflate(getActivity(),R.layout.ab_in_add_interests,null));
        ImageView menuButton = (ImageView) toolbarContainer.findViewById(R.id.menuButton);
        title = (TextView) toolbarContainer.findViewById(R.id.screenTitle);
        title.setText(getString(R.string.interests_conf));
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        ImageButton editButton = (ImageButton)toolbarContainer.findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
               getFragmentManager().popBackStack();
            }
        });
    }
    public void choicedInterestCounter(ArrayList arrayList){
        if (arrayList.size() == 0){
            title.setText(getString(R.string.interests_conf));
        } else if (checkedItemsList.size() == 1){

            title.setText(arrayList.size() + getResources().getString(R.string.one_choiced));

        } else {

            title.setText(arrayList.size() + getResources().getString(R.string.two_choiced));
        }
        Log.d("checkedItemsList",arrayList.size() + "");
    }
    public void setPersonInterestsArray(ArrayList<Interests> personInterestsArray)
    {
        this.personInterestsArray = personInterestsArray;
    }

    public ArrayList<Interests> checkArray(ArrayList<Interests> checkedArrayList, ArrayList<Interests> interests)
    {
        Log.d("result", checkedArrayList.size() + " - размер");
        Log.d("result", interests.size() + " - размер");

        for (int i = 0; i < checkedArrayList.size(); i++)
        {
            for (int j = 0; j < interests.size(); j++)
            {
                if (checkedArrayList.size() == 0)
                {
                    break;
                }
                for (int c = 0; c < checkedArrayList.size(); c++)
                {
                    if (checkedArrayList.get(c).getInterestId() == interests.get(j).getInterestId())
                    {
                        checkedArrayList.remove(c);
                    }
                }
            }
        }

        return checkedArrayList;
    }


}
