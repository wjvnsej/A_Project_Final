package com.kosmo.a_project_final;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ClubList_Adapter extends BaseAdapter {

    String TAG = "iKOSMO";
    private Context context;
    private List<Map<String, Object>> list;
    private int a;

    public ClubList_Adapter(Context context, int a, List<Map<String, Object>> list) {
        this.context = context;
        this.a = a;
        this.list = list;
    }

    @Override
    public int getCount() { return list.size(); }

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
            convertView = inflater.inflate(R.layout.activity_club_list, null);
        }

        final TextView clubName = (TextView)convertView.findViewById(R.id.club_name);
        final ImageView clubEmb = (ImageView)convertView.findViewById(R.id.club_emb);
        final ListView club_list = (ListView)convertView.findViewById(R.id.club_list);

        Log.i(TAG,"BaseAdapter 에 들어온 값 : "+list);
        Log.i(TAG,"BaseAdapter 에서 출력할 값 : "+list.get(position).get("c_name").toString());

        clubName.setText(list.get(position).get("c_name").toString());

        //이미지 보내기
        String img = "http://192.168.219.200:8282/project_final/resources/uploadsFile/"+list.get(position).get("c_emb").toString();
        Picasso.get().load(img).into(clubEmb);

        return convertView;
    }
}
