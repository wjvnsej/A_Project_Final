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

public class ClubMemberPoint_Adapter extends BaseAdapter {

    String TAG = "iKOSMO";
    private Context context;
    private List<Map<String,Object>> list;
    private int a;

    public ClubMemberPoint_Adapter(Context context, List<Map<String, Object>> list, int a) {
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
            convertView = inflater.inflate(R.layout.activity_club_member_point__adapter, null);
        }

        final TextView no = (TextView)convertView.findViewById(R.id.no);
        final TextView m_nameTxt = (TextView)convertView.findViewById(R.id.m_nameTxt);
        final TextView appearance = (TextView)convertView.findViewById(R.id.appearance);
        final TextView point = (TextView)convertView.findViewById(R.id.point);

        ImageView m_picTxt = (ImageView)convertView.findViewById(R.id.m_picTxt);
        String img = "http://192.168.219.200:8282/project_final/resources/uploadsFile/"+list.get(position).get("m_pic").toString();
        Picasso.get().load(img).into(m_picTxt);

        no.setText(list.get(position).get("rnum").toString());
        m_nameTxt.setText(list.get(position).get("m_name").toString());
        appearance.setText(list.get(position).get("appearance").toString());
        point.setText(list.get(position).get("point").toString());

        Log.i(TAG,"position"+position);
        return convertView;
    }
}
