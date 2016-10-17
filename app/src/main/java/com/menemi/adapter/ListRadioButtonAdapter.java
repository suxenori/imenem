package com.menemi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.menemi.R;
import com.menemi.interests_classes.RowModel;

/**
 * Created by tester03 on 22.08.2016.
 */
public class ListRadioButtonAdapter extends ArrayAdapter
{
    RowModel[] rowModelItems = null;
    Context context;
    TextView name;
    RadioButton rb;

    public ListRadioButtonAdapter(Context context, RowModel[] resource) {
        super(context, R.layout.row_radio_button,resource);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.rowModelItems = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_radio_button, parent,false);
        name = (TextView) convertView.findViewById(R.id.textViewRadio);
        rb = (RadioButton) convertView.findViewById(R.id.radioButton2);

        if (convertView.isPressed())
        {
            rb.setChecked(true);
            Log.d("item_position",position + "");
        }
        name.setText(rowModelItems[position].getName());
        return convertView;
    }


}