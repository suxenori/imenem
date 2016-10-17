package com.menemi.interests_classes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.menemi.R;

/**
 * Created by tester03 on 02.08.2016.
 */
public class CustomAdapter extends ArrayAdapter
{
    RowModel[] rowModelItems = null;
    Context context;
    TextView name;
    CheckBox cb;

    public CustomAdapter(Context context, RowModel[] resource) {
        super(context, R.layout.row_checkbox,resource);
        this.context = context;
        this.rowModelItems = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_checkbox, parent,false);
        name = (TextView) convertView.findViewById(R.id.textView1);
        cb = (CheckBox) convertView.findViewById(R.id.checkBox1);

        if (convertView.isPressed())
        {
            cb.setChecked(true);
            Log.d("item_position",position + "");
        }
        name.setText(rowModelItems[position].getName());
        return convertView;
    }


}

