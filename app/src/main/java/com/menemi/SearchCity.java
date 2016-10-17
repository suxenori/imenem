package com.menemi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.menemi.dbfactory.DBHandler;
import com.menemi.models.PlaceModel;

import java.util.ArrayList;

/**
 * Created by tester03 on 09.08.2016.
 */
public class SearchCity extends AppCompatActivity
{
    private static Toolbar toolbar;

    public static Toolbar getToolbar()
    {
        return toolbar;
    }

    private ArrayList placeArrayList;
    private PlaceModel place;
    private ArrayList<String> placesNames = new ArrayList<>();
    private ListView placesListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<PlaceModel> preparedPlaces;
    private PlaceModel choicedPlace;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(com.menemi.R.layout.search_city_layout);
        toolbar = (Toolbar) findViewById(com.menemi.R.id.toolbar_with_checkBox);
        setSupportActionBar(toolbar);
        configureToolbar();
        placesListView = (ListView) findViewById(com.menemi.R.id.placesListView);
        final EditText editSearchField = (EditText) findViewById(com.menemi.R.id.searchField);
        editSearchField.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence string, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence string, int start, int before, int count)
            {
                if (string.length() == 0 && !placesNames.isEmpty() || string.length() < 3 && !placesNames.isEmpty())
                {
                    placesNames.clear();
                    placesListView.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable string)
            {
                int charQuantity = 3;
                if (string.length() >= charQuantity)
                {
                    startProgressBar();
                    placesNames.clear();
                    placesListView.setAdapter(adapter);
                    request(editSearchField.getText().toString());
                    stopProgressBar();
                }
            }
        });

        placesListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id)
            {

                choicedPlace = new PlaceModel(preparedPlaces.get(pos).getCityName(), preparedPlaces.get(pos).getPlaceId());
                DBHandler.getInstance().getUsersPlaceInfo(preparedPlaces.get(pos).getPlaceId(), new DBHandler.ResultListener()
                {
                    @Override
                    public void onFinish(Object object)
                    {
                        PlaceModel detailChoicedPlace = (PlaceModel) object;
                        choiseListener.changePlace(choicedPlace,detailChoicedPlace);

                    }
                });
                finish();


            }
        });

    }

    private void configureToolbar()
    {

        Toolbar toolbar = SearchCity.getToolbar();
        LinearLayout toolbarContainer = (LinearLayout) toolbar.findViewById(com.menemi.R.id.toolbarContainer);
        toolbarContainer.removeAllViews();
        toolbarContainer.addView(View.inflate(this, com.menemi.R.layout.searccity_toolbar, null));
        ImageView menuButton = (ImageView) toolbarContainer.findViewById(com.menemi.R.id.menuButton);
        menuButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        TextView title = (TextView) toolbarContainer.findViewById(com.menemi.R.id.screenTitle);
        title.setText(getString(com.menemi.R.string.search_text));
    }

    private void request(String value)
    {

        Log.d("city_name", "Method is called");
        DBHandler.getInstance().getPlacesList(value, new DBHandler.ResultListener()
        {
            @Override
            public void onFinish(Object object)
            {
                placeArrayList = (ArrayList) object;
                preparedPlaces = new ArrayList<>();
                // preparedPlaces.add(new PlaceModel("людей рядом","людей рядом"));
                Log.d("Response", placeArrayList.size() + "");
                for (int i = 0; i < placeArrayList.size(); i++)
                {
                    place = (PlaceModel) placeArrayList.get(i);
                    preparedPlaces.add(new PlaceModel(place.getCityName(), place.getPlaceId()));
                    placesNames.add(place.getCityName());
                }
                adapter = new ArrayAdapter<String>(getApplicationContext(), com.menemi.R.layout.custom_list_item, placesNames);
                placesListView.setAdapter(adapter);

            }
        });
    }

    private static ChoiceListener choiseListener = new ChoiceListener()
    {
        @Override
        public void changePlace(PlaceModel placeModel, PlaceModel detailPlaceModel)
        {
            Log.d("ChoiseListener", "ChoiseListener is called but not set");
        }
    };

    public static void setChoiseListener(ChoiceListener choiseListener)
    {
        SearchCity.choiseListener = choiseListener;
    }

    public interface ChoiceListener
    {
        void changePlace(PlaceModel shortPlaceModel, PlaceModel detailPlaceModel);
    }
    public void startProgressBar() {
        ProgressBar loading = (ProgressBar) findViewById(com.menemi.R.id.loadingPlaces);
        loading.setVisibility(View.VISIBLE);
    }

    public void stopProgressBar() {
        findViewById(com.menemi.R.id.loadingPlaces).setVisibility(View.GONE);
    }

}