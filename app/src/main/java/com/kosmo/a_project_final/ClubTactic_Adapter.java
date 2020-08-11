package com.kosmo.a_project_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ClubTactic_Adapter extends BaseAdapter {

    String TAG = "iKOSMO";
    private Context context;
    private List<Map<String, Object>> list;
    private int a;
    private String c_name;

    public ClubTactic_Adapter(Context context, int a, List<Map<String, Object>> list, String c_name) {
        this.context = context;
        this.list = list;
        this.a = a;
        this.c_name = c_name;
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
            convertView = inflater.inflate(R.layout.activity_club_tactic__adapter, null);
        }
        final TextView g_date = (TextView)convertView.findViewById(R.id.game_date);
        final TextView g_time = (TextView)convertView.findViewById(R.id.game_time);
        final TextView g_sname = (TextView)convertView.findViewById(R.id.stadium_name);
        final Button g_tactic = (Button)convertView.findViewById(R.id.game_tactic);
        final Button g_QR = (Button)convertView.findViewById(R.id.game_QR);

        Log.i(TAG,"BaseAdapter 에 들어온 값 : "+list);

        g_date.setText(list.get(position).get("g_date").toString());
        g_time.setText(list.get(position).get("g_time").toString());
        g_sname.setText(list.get(position).get("g_sname").toString());

        final String g_idx = list.get(position).get("g_idx").toString();
        final String yourC_name = list.get(position).get("c_name").toString();
        g_tactic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Tactic_WebView.class);
                intent.putExtra("url", "http://192.168.219.200:8282/project_final/android/clubTacticBoard.do");
                intent.putExtra("g_idx", g_idx);
                intent.putExtra("myC_name", c_name);
                intent.putExtra("yourC_name", yourC_name);
                context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
            }
        });

        final String g_qrcheck = list.get(position).get("g_qrcheck").toString();
        final String g_num = list.get(position).get("g_num").toString();

        g_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Game_QR_Create.class);

                intent.putExtra("g_idx", g_idx);
                intent.putExtra("g_qrcheck", g_qrcheck);
                intent.putExtra("g_num",g_num);
                context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
            }
        });

        return convertView;
    }
}
