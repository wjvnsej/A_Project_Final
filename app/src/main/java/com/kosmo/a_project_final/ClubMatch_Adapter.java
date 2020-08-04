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

public class ClubMatch_Adapter extends BaseAdapter {

    String TAG = "iKOSMO";
    private Context context;
    private List<Map<String, Object>> list;
    private int a;

    public ClubMatch_Adapter(Context context, int a, List<Map<String, Object>> list) {
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
            convertView = inflater.inflate(R.layout.activity_club_match__adapter, null);
        }
        final TextView g_date = (TextView)convertView.findViewById(R.id.game_date);
        final TextView g_time = (TextView)convertView.findViewById(R.id.game_time);
        final TextView g_sname = (TextView)convertView.findViewById(R.id.stadium_name);
        final TextView c_name = (TextView)convertView.findViewById(R.id.game_team);

        Log.i(TAG,"BaseAdapter 에 들어온 값 : "+list);

        g_date.setText(list.get(position).get("g_date").toString());
        g_time.setText(list.get(position).get("g_time").toString());
        g_sname.setText(list.get(position).get("g_sname").toString());
        c_name.setText(list.get(position).get("c_name").toString());


        return convertView;
    }
}
