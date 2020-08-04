package com.kosmo.a_project_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class ClubMember_Adapter extends BaseAdapter {
    String TAG = "iKOSMO";
    private Context context;
    private List<Map<String,Object>> list;
    private int a;

    public ClubMember_Adapter(Context context, int a, List<Map<String, Object>> list) {
        this.context = context;
        this.a = a;
        this.list = list;
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
            convertView = inflater.inflate(R.layout.activity_club_member__adapter, null);
        }
        final TextView clubMember_name = (TextView)convertView.findViewById(R.id.clubMember_name);
        final TextView clubMember_phone = (TextView)convertView.findViewById(R.id.clubMember_phone);
        final TextView clubMember_grade = (TextView)convertView.findViewById(R.id.clubMember_grade);

        Log.i(TAG,"BaseAdapter 에 들어온 값 : "+list);

        clubMember_name.setText(list.get(position).get("m_name").toString());
        clubMember_phone.setText(list.get(position).get("m_phone").toString());
        clubMember_grade.setText(list.get(position).get("cm_grade").toString());


        return convertView;
    }
}
