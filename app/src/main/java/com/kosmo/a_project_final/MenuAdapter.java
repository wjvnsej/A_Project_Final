package com.kosmo.a_project_final;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuAdapter extends BaseAdapter {

    private Context context;
    private int[] resIds;
    private String[] titles;

    public MenuAdapter(Context context, int[] resIds, String[] titles) {
        this.context = context;
        this.resIds = resIds;
        this.titles = titles;
    }

    public MenuAdapter(Context context, int[] resIds) {
        this.context = context;
        this.resIds = resIds;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return titles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.menu_item, null);
        }

        ImageView menuImage = (ImageView)convertView.findViewById(R.id.menuImg);
        menuImage.setImageResource(resIds[position]);

        final TextView menuTitle = (TextView)convertView.findViewById(R.id.menuTitle);
        menuTitle.setText(titles[position]);

        return convertView;
    }
}


























