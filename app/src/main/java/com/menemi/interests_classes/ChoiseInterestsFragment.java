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
 * Created by tester03 on 02.08.2016.
 */
public class ChoiseInterestsFragment extends Fragment
{

    private CheckBox checkBox;
    private RowModel[] rowInterestModels;
    private ArrayList<Interests> checkedItemsList = new ArrayList<>();
    private InterestsGroup interestGroup;
    private ArrayList<Interests> interestsList;
    private ImageButton addButton;
    private ArrayList<Interests> personInterestsArray = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.choise_interests_layouts, container, false);
        configureToolbar();

        final ListView listInterests = (ListView) rootView.findViewById(R.id.listInterests);
        addButton.setClickable(false);
        addButton.setBackgroundResource(R.drawable.check_off);
        DBHandler.getInstance().getInterestsFromGroup(DBHandler.getInstance().getUserId(), interestGroup.getIndex(), new DBHandler.ResultListener()
        {
            @Override
            public void onFinish(Object object)
            {
                interestsList = (ArrayList) object;
                Log.i("interestsArray", interestsList.size() + " items");
                rowInterestModels = new RowModel[checkArray(interestsList, personInterestsArray).size()];
                for (int i = 0; i < checkArray(interestsList, personInterestsArray).size(); i++)
                {
                    rowInterestModels[i] = new RowModel(checkArray(interestsList,
                            personInterestsArray).get(i).getInterest(), checkArray(interestsList, personInterestsArray).get(i).getInterestId());
                }
                if (getActivity() != null){
                    CustomAdapter adapter = new CustomAdapter(getActivity(), rowInterestModels);
                    listInterests.setAdapter(adapter);
                }

            }
        });
        listInterests.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l)
            {
                checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
                if (checkBox.isChecked())
                {
                    checkBox.setChecked(false);
                    removeCheckItem(checkArray(interestsList, personInterestsArray).get(pos));
                } else
                {
                    checkBox.setChecked(true);
                    checkedItemsList.add(checkArray(interestsList, personInterestsArray).get(pos));
                }
                if (checkedItemsList.size() != 0)
                {
                    addButton.setClickable(true);
                    addButton.setBackgroundResource(R.drawable.check_on);

                } else
                {
                    addButton.setClickable(false);
                    addButton.setBackgroundResource(R.drawable.check_off);

                }
            }
        });
        return rootView;
    }

    private void removeCheckItem(Interests interest)
    {
        for (int i = 0; i < checkedItemsList.size(); i++)
        {
            if (checkedItemsList.get(i) == interest)
            {
                checkedItemsList.remove(i);
            }
        }
    }

    private ArrayList<Integer> getInterestsId(ArrayList<Interests> arrayList){
        ArrayList<Integer> array = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++)
        {
            array.add(arrayList.get(i).getInterestId());
        }

        return array;
    }

    private void configureToolbar()
    {
        Toolbar toolbar = InterestContainer.getToolbar();
        LinearLayout toolbarContainer = (LinearLayout) toolbar.findViewById(R.id.toolbarContainer);
        toolbarContainer.removeAllViews();
        toolbarContainer.addView(View.inflate(getActivity(), R.layout.choice_interest_toolbar, null));
        addButton = (ImageButton) toolbarContainer.findViewById(R.id.submButton);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("getProfileInterests",DBHandler.getInstance().getProfileInterests().size() + " - elements before marge");
                for (int i = 0; i < checkedItemsList.size(); i++)
                {
                    DBHandler.getInstance().getProfileInterests().add(checkedItemsList.get(i));
                    Log.d("getProfileInterests",DBHandler.getInstance().getProfileInterests().size() + " elements - after marge");
                }
                Toast.makeText(getActivity().getApplicationContext(), "Выбранные интересы успешно добавлены", Toast.LENGTH_SHORT).show();
                DBHandler.getInstance().addInterests(DBHandler.getInstance().getUserId(), getInterestsId(checkedItemsList), new DBHandler.ResultListener()
                {
                    @Override
                    public void onFinish(Object object)
                    {

                    }
                });
                getFragmentManager().popBackStack();
            }
        });

        ImageView menuButton = (ImageView) toolbarContainer.findViewById(R.id.menuButton);
        menuButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getFragmentManager().popBackStack();
            }
        });

        TextView title = (TextView) toolbarContainer.findViewById(R.id.screenTitle);
        title.setText(getString(R.string.set_interests));
    }

    public void setInterestGroup(InterestsGroup interestGroup)
    {
        this.interestGroup = interestGroup;
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


    public void setPersonInterestsArray(ArrayList<Interests> personInterestsArray)
    {
        this.personInterestsArray = personInterestsArray;
    }
}
