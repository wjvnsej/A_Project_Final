package com.kosmo.a_project_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class ClubSearch_Adapter extends BaseAdapter {

    String TAG = "iKOSMO";
    private Context context;
    private List<Map<String,Object>> list;
    private int a;

    public ClubSearch_Adapter(Context context, List<Map<String, Object>> list, int a) {
        this.context = context;
        this.list = list;
        this.a = a;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_club_search__adapter, null);
        }

        final TextView c_nameTxt = (TextView)convertView.findViewById(R.id.c_nameTxt);
        final TextView c_areaTxt = (TextView)convertView.findViewById(R.id.c_areaTxt);
        final TextView c_abilityTxt = (TextView)convertView.findViewById(R.id.c_abilityTxt);
        final TextView c_ageTxt = (TextView)convertView.findViewById(R.id.c_ageTxt);

        ImageView c_emb = (ImageView)convertView.findViewById(R.id.c_picImg);
        String img = "http://192.168.219.200:8282/project_final/resources/uploadsFile/"+list.get(position).get("c_emb").toString();
        Picasso.get().load(img).into(c_emb);

        c_nameTxt.setText(list.get(position).get("c_name").toString());
        c_areaTxt.setText(list.get(position).get("c_area").toString());
        c_abilityTxt.setText(list.get(position).get("c_ability").toString());
        c_ageTxt.setText(list.get(position).get("c_age").toString());

        return convertView;
    }

}
