package com.menemi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.menemi.R;
import com.menemi.models.ItemSlideMenu;

import java.util.List;
public class SlidigMenuAdapter extends BaseAdapter
{
    private Context context;
    private List<ItemSlideMenu> listItem;

    public SlidigMenuAdapter(Context context, List<ItemSlideMenu> listItem)
    {
        this.context = context;
        this.listItem = listItem;
    }

    @Override
    public int getCount()
    {
        return listItem.size();
    }

    @Override
    public Object getItem(int position)
    {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = View.inflate(context, R.layout.item_sliding_menu,null);
        ImageView img = (ImageView)view.findViewById(R.id.img_id);
        TextView textView = (TextView)view.findViewById(R.id.item_title);
        ItemSlideMenu itemSlideMenu = listItem.get(position);

        img.setImageResource(itemSlideMenu.getImgId());
        textView.setText(itemSlideMenu.getTitle());

        return view;
    }
}
