package com.menemi.interests_classes;

import android.app.DialogFragment;
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
import com.menemi.personobject.Interests;

import java.util.ArrayList;

/**
 * Created by tester03 on 16.08.2016.
 */
public class SearchResultDialog extends DialogFragment
{
    private ArrayList<Interests> interestsArray;
    private ArrayList<ItemSlideMenu> listSliding = new ArrayList<>();
    private String interestsFromTextField;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.search_result_dialog, container, false);
        final LinearLayout interestsContainer = (LinearLayout) rootView.findViewById(R.id.interestsContainer);
        if (interestsArray != null){
            for (int i = 0; i < interestsArray.size(); i++)
            {
                listSliding.add(new ItemSlideMenu((DBHandler.getInstance()
                        .getInterestsGroupArray().get(interestsArray.get(i).getGroupId() - 1).getCategoryIcon()),interestsArray.get(i).getInterest(),(DBHandler.getInstance()
                        .getInterestsGroupArray().get(interestsArray.get(i).getGroupId() - 1).getNameGroup())));
            }
        } else {
            listSliding.add(new ItemSlideMenu(BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),
                    R.drawable.add_int),interestsFromTextField,"как новый интерес"));
        }

        setList(interestsContainer,listSliding);
        return rootView;
    }
    private void setList(LinearLayout container, ArrayList<ItemSlideMenu> items)
    {
        for (int i = 0; i < items.size(); i++)
        {
            container.addView(items.get(i).prepareItem());
            items.get(i).setId(i);
        }
    }

    public void setInterestsArray(ArrayList<Interests> interestsArray)
    {
        this.interestsArray = interestsArray;
    }

    public void setInterestsFromTextField(String interestsFromTextField)
    {
        this.interestsFromTextField = interestsFromTextField;
    }

    private class ItemSlideMenu implements View.OnClickListener
    {
        int id = 0;
        private Bitmap imgId;
        private String name;
        private String category;

        public void setId(int id)
        {
            this.id = id;
        }
        public int getId()
        {
            return id;
        }

        public Bitmap getImgId()
        {
            return imgId;
        }

        public String getName()
        {
            return name;
        }

        public ItemSlideMenu(Bitmap imgId, String name, String category)
        {
            this.imgId = imgId;
            this.name = name;
            this.category = category;
        }

        private View prepareItem()
        {
            View view = View.inflate(getActivity(), R.layout.search_result_item_list, null);
            ImageView img = (ImageView) view.findViewById(R.id.img_id);
            TextView textView = (TextView) view.findViewById(R.id.item_title);
            TextView categoryTextView = (TextView) view.findViewById(R.id.item_group);
            img.setImageBitmap(imgId);
            textView.setText(name);
            categoryTextView.setText(category);
            view.setOnClickListener(this);
            return view;
        }

        @Override
        public void onClick(View view)
        {
            view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Interests interests;
                    if (interestsArray != null){

                        interests = (Interests) interestsArray.get(id);
                        choiseListener.addCustomInterest(interests);
                        dismiss();
                        Log.d("interests", interests.getInterest());
                    } else {
                        interests = new Interests(interestsFromTextField,2,11);
                        Log.d("interests", interests.getInterest());
                        choiseListener.addCustomInterest(interests);
                        dismiss();
                    }


                }
            });
        }
    }
    private static ChoiceListener choiseListener = new ChoiceListener()
    {
        @Override
        public void addCustomInterest(Interests interests)
        {
            Log.d("ChoiseListener", "ChoiseListener is called but not set");
        }
    };

    public static void setChoiseListener(ChoiceListener choiseListener)
    {
        SearchResultDialog.choiseListener = choiseListener;
    }

    public interface ChoiceListener
    {
        void addCustomInterest(Interests interests);
    }

}
